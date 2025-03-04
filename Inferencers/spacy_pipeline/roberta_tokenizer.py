# needed installations
# pip install -U spacy
# pip install -U spacy[transformers]
# pip install spacy-transformers
# pip install -U spacy-nightly --pre
import spacy
class SpacyRobertaTokenizer:
    def __init__(self):
        self.nlp=spacy.load("./roberta_spacy_model/model-best")


    def getInference(self, sentence):

    # text = ["Lazarus Group is a threat group that has been attributed to the North Korean government ."
    # ]
        output=[]
        for doc in self.nlp.pipe(sentence, disable=["tagger", "parser"]):
            print([(ent.text, ent.label_) for ent in doc.ents])
            output.append([(ent.text, ent.label_) for ent in doc.ents])
        return output