import requests
from bs4 import BeautifulSoup
import re
from nltk.tokenize import sent_tokenize
import string

class SecureListScrapper:
    def __init__(self, link):
        self.link = link
    
    # Get link of all the report on a single page
    def getAllLinks(self, filename):
        fileContent = None
        # Get a list of already stored files
        with open(filename) as f:
            fileContent = f.readlines()
        # Remove \n from the end of the links
        for i,f in enumerate(fileContent):
            fileContent[i] = fileContent[i].split("\n")[0]

        articles = []
        # Crawl all the links in 5 pages
        for i in range(1):
            page = requests.get(self.link + "/page/"+str(i+1))
            soup = BeautifulSoup(page.content, 'html.parser')
            for a in (soup.find_all('a',href=True,class_='c-card__figure')):
                # FOR TESTING, REMOVE IN PRODUCTION
                if len(articles)>3:
                    break
                # Link should not already have been fetched
                if (a['href'] not in articles and a['href'] not in fileContent):
                    articles.append(a['href'])

        return articles

    # Write the links to a text file
    def writeLinksToFiles(self, filename, articles):
        with open(filename, "a") as myfile:
            for a in articles:
                myfile.write(a+"\n")

    # Get a list of all reports data
    def getReports(self, articles):
        reports = []
        for a in articles:
            reports.extend(self.getIndividualReport(a))
        return reports

    # Get an individual reports data
    def getIndividualReport(self,article):
        page = requests.get(article)
        soup = BeautifulSoup(page.content, 'html.parser')
        content = soup.find_all('div', class_ = "c-wysiwyg")
        single_paragraph = [] # para single list
        for p in content:
            single_paragraph.append(p.get_text())
        single_paragraph = single_paragraph[0]
        paragraphs = single_paragraph.split("\n")
        text = ""
        final_paragraphs = []
        for index, para in enumerate(paragraphs):
            if para == '':
                paragraphs.pop(index)
        for index, para in enumerate(paragraphs):
            if (len(para.split()) > 512):
                text += ' '
                text += para[0:511]
                text.strip
                final_paragraphs.append(text)
                text = ""
                text += para[511:len(para)-1]
                text.strip
                final_paragraphs.append(text)
            else:
                # Input size to the Inference model limits length of sentences
                if (len(para.split()) + len(text.split()) < 512):
                    text += ' '
                    text += para
                else:
                    text = text[1:]
                    text.strip
                    # text= text.translate(str.maketrans('', '', string.punctuation))
                    final_paragraphs.append(text)
                    text = ""
            
        # If multiple paragraphs are not formed
        if len(final_paragraphs) == 0:
            text = text[1:]
            text = text[:len(text)-1]
            text.strip
            # text= text.translate(str.maketrans('', '', string.punctuation))
            final_paragraphs.append(text)

        # punctuations = '''!()-[]{};:'"\\<>/?@#$%^&*_~“”’'''

        # # Clean the final paragraphs
        # for i,para in enumerate(final_paragraphs):
        #     use_string = ""
        #     for char in final_paragraphs[i]:
        #         if char not in punctuations:
        #             use_string += char
        #     final_paragraphs[i] = use_string

        
        return final_paragraphs
