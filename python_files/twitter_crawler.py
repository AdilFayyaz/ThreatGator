from source import *
import datetime
import time
from bs4 import BeautifulSoup
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from webdriver_manager.chrome import ChromeDriverManager


class TwitterCrawler(Source):
    
    def __init__(self):
        self._SCROLL_PAUSE_TIME = 3
        self._MONTHS = {"Jan": 1, "Feb": 2, "Mar": 3, "Apr": 4, "May": 5, "Jun": 6,
                        "Jul": 7, "Aug": 8, "Sep": 9, "Oct": 10, "Nov": 11, "Dec": 12}
        Source.__init__(self, "string", "Twitter", "Social Media", "www.twitter.com")
        self.numberOfPostsChecked = 0

    def checkTimeLimit(self, time_html, dateString):
        self.numberOfPostsChecked += 1
        # If split point is possible it means date is not in hours
        splitDate = time_html.split()
        dateMaxSplit = dateString.split()
        dateMax = datetime.datetime(datetime.datetime.now().year, self._MONTHS[dateMaxSplit[0]], int(dateMaxSplit[1]))

        # If split len > 1 means split date has a month and a day
        if len(splitDate) > 1:
            dateOfTweet = datetime.datetime(datetime.datetime.now().year, self._MONTHS[splitDate[0]], int(splitDate[1]))
            # If max date found till traversal
            if dateOfTweet <= dateMax:
                return True
            else:
                # return false if max date is not exceeded
                return False
        # return False because search only went back in hours
        return False

    # Fetch the HTML Page Source from the driver
    def crawlTill(self, _driver, url, dateString):
        _driver.get(url)
        time_taken = 0
        while True:
            # Scroll till a certain date
            try:
                postTime = _driver.find_elements(By.XPATH, '//time[@datetime]')
                stop = 0
                for t in postTime:
                    time_html = t.get_property('innerHTML')
                    
                    stopSearch = self.checkTimeLimit(time_html, dateString)
                    if self.numberOfPostsChecked >= 10:
                        stopSearch = 1
                    if stopSearch:
                        stop = 1
                        break
                if stop:
                    break

            except:
                x = 1
            
            _driver.execute_script("window.scrollTo(0, document.body.scrollHeight)")
            time.sleep(self._SCROLL_PAUSE_TIME) 
            time_taken += 1
            # This can be changed too
            if time_taken >= 6:
                break
            
        htmlSource = _driver.page_source

        return htmlSource

    def scrapeTwitter(self, _driver, url, username, all_tweets, dateString):
        # Global variable
        global soup

        # crawl Twitter till the date specified
        soup = BeautifulSoup(self.crawlTill(_driver, url, dateString), "html.parser")
        tweets = _driver.find_elements(By.CSS_SELECTOR, "[data-testid=\"tweet\"]")

        # print(tweets)
        twt_set = []
        tweetTexts = []
        for t in tweets:
            if t not in twt_set:
                twt_set.append(t)
                html_response = t.get_property('innerHTML')
                html_response = BeautifulSoup(html_response, "html.parser")
                date = html_response.find('time')['datetime']
                line_count = float('inf')

                # Iterate over the html response
                for i, text in enumerate(html_response.strings):
                    text = text.strip()
                    if text == '·':
                        line_count = i
                    # After two lines tweet is written
                    if i == line_count + 2:
                        tweetTexts.append(text)

                for line in reversed(tweetTexts):
                    if not line or line[0].isdigit():
                        del tweetTexts[-1]
                    else:
                        break
        all_tweets[username] = tweetTexts
        _driver.quit()
        return

    def init(self, username, date, isHandle):
        s = Service(ChromeDriverManager().install())
        if isHandle:
            driver = webdriver.Chrome(service=s)
            all_tweets = {}
            self.scrapeTwitter(driver, 'https://www.twitter.com/' + username, username, all_tweets, dateString=date)
            print(all_tweets)
            return all_tweets
        else:
            driver2 = webdriver.Chrome(service=s)
            hashTagTweets = {}
            searchTag = username
            urlHashTags = "https://twitter.com/search?q=%23"
            self.scrapeTwitter(driver2, urlHashTags + searchTag, searchTag, hashTagTweets, dateString=date)
            print(hashTagTweets)
            driver2.quit()
            return hashTagTweets