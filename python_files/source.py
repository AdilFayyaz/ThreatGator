class Source:
    def __init__(self):
        self._dataFormat = ""
        self._name = ""
        self._type = ""
        self._url = ""

    def __init__(self, df, name, sourcetype, url):
        self._dataFormat = df
        self._name = name
        self._type = sourcetype
        self._url = url

    def get_name(self):
        return self._name

    def get_data_format(self):
        return self._dataFormat

    def get_type(self):
        return self._type

    def get_url(self):
        return self._url

    def set_name(self, name):
        self._name=name

    def set_data_format(self, df):
        self._dataFormat=df

    def set_url(self, url):
        self._url=url

    def set_type(self, sourcetype):
        self._type=sourcetype