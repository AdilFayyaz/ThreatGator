from source import *
import requests
import json


class DiscordCrawler(Source):
    def __init__(self):
        self._headers = {
            # 'authorization':'NzY2NzIzOTMxODI5Njk4NjEw.YWvw_Q.O4EJUyzRb_47Ghu-rySWT9sphLw'
            'authorization': 'NzY2NzIzOTMxODI5Njk4NjEw.YXD0QQ.OxRGwYJDB1UYN2u1wpoSOAhlfqI'
        }
        Source.__init__(self, "json", "Discord", "Social Media", "www.discord.com")

    def get_discord_messages(self, channel_id):
        r=requests.get(f'https://discord.com/api/v9/channels/{channel_id}/messages?limit=100',headers=self._headers)
        jsonV = r.json()
        ret = []
        returnDic = {}
        for i,val in enumerate(jsonV):
            ret.append(val["content"])
            # print(i," ",type(val),"\n")
        returnDic["content"] = ret
        return returnDic
