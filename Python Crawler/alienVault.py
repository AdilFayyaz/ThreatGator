# pip install OTXv2
from source import *
from OTXv2 import OTXv2


class AlienVault(Source):
    def __init__(self):
        self._APIKey = "a60ad0b0d7215cc5dc0fc978ee37109b1c5cc6d218a8d9969cf46c488540cc70"
        self._otx = OTXv2(self._APIKey)
        Source.__init__(self, "json", "AlienVault", "Threat Exchange Platform", "otx.alienvault.com")

    def get_pulse_by_keyword(self, keyword):
        pulses = self._otx.search_pulses(keyword)
        return pulses

    def get_indicators_by_pulse(self, pulseid):
        indicators = self._otx.get_pulse_indicators(pulseid)
        for indicator in indicators:
            print(indicator["indicator"] + "Indicator Type: " + indicator["type"])
