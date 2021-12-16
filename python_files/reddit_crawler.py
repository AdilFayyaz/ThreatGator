import requests
import json
import pandas as pd
from source import *


class RedditCrawler(Source):
    def __init__(self):
        Source.__init__(self, "json", "Reddit", "Social Media", "www.reddit.com")
        self._CLIENT_ID = 'vjBH9ASH7P-uyQ7gJmn-VA'
        self._TOKEN = 'oRsf8Xl7HRkPNZAqFFlb2KN1yP7Xsg'
        self._USERNAME = 'threatgator'
        self._PASSWORD = 'fyp16azh'
        self.headers = None

    def authenticate(self):
        auth = requests.auth.HTTPBasicAuth(self._CLIENT_ID, self._TOKEN)

        # passing login method
        data = {
            'grant_type': 'password',
            'username': self._USERNAME,
            'password': self._PASSWORD}

        headers = {'User-Agent': 'FYPScript/0.0.1'}  # giving reddit a brief description of app

        res = requests.post('https://www.reddit.com/api/v1/access_token',
                            auth=auth, data=data, headers=headers)

        # convert response to JSON and pull access_token value
        TOK = res.json()['access_token']

        # add authorization to our headers dictionary
        headers = {**headers, **{'Authorization': f"bearer {TOK}"}}

        # while the token is valid (~2 hours) we just add headers=headers to our requests
        requests.get('https://oauth.reddit.com/api/v1/me', headers=headers)

        self.headers = headers

        return headers

    def fetch_most_popular_data(self, subreddit, headers, params, num):
        data = pd.DataFrame()
        response = []

        for i in range(num):
            res = requests.get("https://oauth.reddit.com/r/" + subreddit + "/hot",
                               headers=headers, params=params)

            # print(json.dumps(json.loads(res.text), indent=4, sort_keys=True ))

            for post in res.json()['data']['children']:
                data = data.append({
                    'subreddit': post['data']['subreddit'],
                    'id': post['data']['id'],
                    'fullname': post['kind'] + "_" + post['data']['id'],
                    'title': post['data']['title'],
                    'selftext': post['data']['selftext'],
                    'upvote_ratio': post['data']['upvote_ratio'],
                    'ups': post['data']['ups'],
                    'downs': post['data']['downs'],
                    'score': post['data']['score'],
                    'kind': post['kind']
                }, ignore_index=True)

            latest = data.iloc[len(data) - 1]
            params['after'] = latest['fullname']
            response.append(res)

        print(data)
        return response, data

    def fetch_most_recent_data(self, subreddit, headers, params, num):
        data = pd.DataFrame()
        response = []

        for i in range(num):
            res = requests.get("https://oauth.reddit.com/r/" + subreddit + "/new",
                               headers=headers, params=params)

            # print(json.dumps(json.loads(res.text), indent=4, sort_keys=True ))

            for post in res.json()['data']['children']:
                data = data.append({
                    'subreddit': post['data']['subreddit'],
                    'id': post['data']['id'],
                    'fullname': post['kind'] + "_" + post['data']['id'],
                    'title': post['data']['title'],
                    'selftext': post['data']['selftext'],
                    'upvote_ratio': post['data']['upvote_ratio'],
                    'ups': post['data']['ups'],
                    'downs': post['data']['downs'],
                    'score': post['data']['score'],
                    'kind': post['kind']
                }, ignore_index=True)

            latest = data.iloc[len(data) - 1]
            params['after'] = latest['fullname']
            response.append(res)

        print(data)
        return response, data

    def fetch_comments(self, subreddit, articleid, headers, params, num):
        data = pd.DataFrame()

        # for i in range(num):
        req = requests.get("https://oauth.reddit.com/r/" + subreddit + "/comments/" + articleid,
                           headers=headers, params=params)

        # print(json.dumps(json.loads(req.text), indent=4, sort_keys=True))

        for post in req.json()[1]['data']['children']:
            data = data.append({
                'id': post['data']['id'],
                'author': post['data']['author'],
                'author fullname': post['data']['author_fullname'],
                'is_blocked': post['data']['author_is_blocked'],
                'is edited': post['data']['edited'],
                'body': post['data']['body'],
                # 'upvote_ratio': post['data']['upvote_ratio'],
                'ups': post['data']['ups'],
                'downs': post['data']['downs'],
                'score': post['data']['score'],
                'kind': post['kind']
            }, ignore_index=True)

        # latest = data.iloc[len(data) - 1]
        # params['after'] = latest['fullname']
        #
        print(data)
        return req, data

    def fetch_comments_by_user(self, username, headers, params, num):
        data = pd.DataFrame()
        response = []
        for i in range(num):
            res = requests.get("https://oauth.reddit.com/user/" + username + "/comments",
                               headers=headers, params=params)

            # print(json.dumps(json.loads(res.text), indent=4, sort_keys=True))

            for post in res.json()['data']['children']:
                data = data.append({
                    'id': post['data']['id'],
                    'author': post['data']['author'],
                    'author fullname': post['data']['author_fullname'],
                    'is_blocked': post['data']['author_is_blocked'],
                    'is edited': post['data']['edited'],
                    'body': post['data']['body'],
                    'fullname': post['kind'] + "_" + post['data']['id'],
                    'ups': post['data']['ups'],
                    'downs': post['data']['downs'],
                    'score': post['data']['score'],
                    'kind': post['kind']
                }, ignore_index=True)

            latest = data.iloc[len(data) - 1]
            params['after'] = latest['fullname']
            response.append(res)

        print(data)
        return response, data

    def fetch_submitted_by_user(self, username, headers, params, num):
        data = pd.DataFrame()
        response = []

        for i in range(num):
            res = requests.get("https://oauth.reddit.com/user/" + username + "/submitted",
                               headers=headers, params=params)

            for post in res.json()['data']['children']:
                data = data.append({
                    'subreddit': post['data']['subreddit'],
                    'id': post['data']['id'],
                    'fullname': post['kind'] + "_" + post['data']['id'],
                    'title': post['data']['title'],
                    'selftext': post['data']['selftext'],
                    'upvote_ratio': post['data']['upvote_ratio'],
                    'ups': post['data']['ups'],
                    'downs': post['data']['downs'],
                    'score': post['data']['score'],
                    'kind': post['kind']
                }, ignore_index=True)

            latest = data.iloc[len(data) - 1]
            params['after'] = latest['fullname']
            response.append(res)

        print(data)
        return response, data

# Press the green button in the gutter to run the script.
