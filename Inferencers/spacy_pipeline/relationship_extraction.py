# needed installations
# pip install -U spacy
# pip install -U spacy[transformers]
# pip install spacy-transformers
# pip install -U spacy-nightly --pre
import spacy
import random
import typer
from pathlib import Path
import spacy
from spacy.tokens import DocBin, Doc
from spacy.training.example import Example
from rel_pipe import make_relation_extractor, score_relations
from rel_model import create_relation_model, create_classification_layer, create_instances, create_tensors
class RelationExtracter:
    def __init__(self):
        self.nlp=spacy.load("./roberta_spacy_model/model-best")
        self.nlp2 = spacy.load("./relation_extraction_model/model-best")
        self.nlp2.add_pipe('sentencizer') # made this change - Adil (to load the pipeline multiple times)

    def getMax(self, x):
        return max(x, key=x.get)

    def isAcceptableRelation(self, rel_dict, x):
        if rel_dict[x]>=0.5:
            return True
        else:
            return False

    def getInference(self, sentence):
        entities=[]
        relationships=[]
        for doc in self.nlp.pipe(sentence, disable=["tagger"]):
            print(f"spans: {[(e.start, e.text, e.label_) for e in doc.ents]}")
            entities.append([(e.start, e.text, e.label_) for e in doc.ents])
        
        # print("*****Relationships*****")
        for name, proc in self.nlp2.pipeline:
            doc = proc(doc) # Here, we split the paragraph into sentences and apply the relation extraction for each pair of entities found in each sentence.
        printed=[]
        for value, rel_dict in doc._.rel.items():
                for sent in doc.sents:
                    for e in sent.ents:
                        for b in sent.ents:
                            if e.start == value[0] and b.start == value[1]:
                                # max(stats, key=stats.get)
                                for relation in rel_dict:
                                    if (e.text, b.text) not in printed and (b.text, e.text) not in printed:
                                        # print(f" entities: {e.text, b.text} --> predicted relation: {self.getMax(rel_dict)}")
                                        printed.append((e.text, b.text)) 
                                        rel={'entities':[e.text, b.text], 'predicted_relations':self.getMax(rel_dict) }
                                        print(rel)
                                        print(rel_dict[self.getMax(rel_dict)])
                                        
                                        if self.isAcceptableRelation(rel_dict, self.getMax(rel_dict)):
                                            relationships.append(rel)
        return {'entity tags': entities, 'relationships': relationships}
                                               
