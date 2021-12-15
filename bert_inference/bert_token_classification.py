import os
import torch
from transformers import BertTokenizerFast, BertConfig, BertForTokenClassification

class BertTokenClassification:
    def __init__(self):
        self.model = BertForTokenClassification.from_pretrained('./Model-1')
        self.tokenizer = BertTokenizerFast.from_pretrained('bert-base-uncased')
        self.MAX_LEN = 128
        self.ids_to_labels = {0: 'O',
                            1: 'B-ID',
                            2: 'I-ID',
                            3: 'B-V',
                            4: 'I-V',
                            5: 'B-M',
                            6: 'I-M',
                            7: 'B-AP',
                            8: 'I-AP',
                            9: 'B-C',
                            10: 'I-C',
                            11: 'B-TA',
                            12: 'I-TA',
                            13: 'B-L',
                            14: 'B-INF',
                            15: 'I-INF',
                            16: 'B-IND',
                            17: 'I-IND',
                            18: float('nan'),
                            19: 'B-T',
                            20: 'I-T',
                            21: 'I-L'
                        }

    def getInference(self,sentence):
        inputs = self.tokenizer(sentence.split(),
                    # is_pretokenized=True, 
                    is_split_into_words=True,
                    return_offsets_mapping=True, 
                    padding='max_length', 
                    truncation=True, 
                    max_length=self.MAX_LEN,
                    return_tensors="pt")
        ids = inputs["input_ids"].to("cpu")
        mask = inputs["attention_mask"].to("cpu")
        outputs = self.model(ids, attention_mask=mask)
        logits = outputs[0]
        active_logits = logits.view(-1, self.model.num_labels) # shape (batch_size * seq_len, num_labels)
        flattened_predictions = torch.argmax(active_logits, axis=1) # shape (batch_size*seq_len,) - predictions at the token level
        tokens = self.tokenizer.convert_ids_to_tokens(ids.squeeze().tolist())
        token_predictions = [self.ids_to_labels[i] for i in flattened_predictions.cpu().numpy()]
        wp_preds = list(zip(tokens, token_predictions)) # list of tuples. Each tuple = (wordpiece, prediction)
        prediction = []
        for token_pred, mapping in zip(wp_preds, inputs["offset_mapping"].squeeze().tolist()):
            #only predictions on first word pieces are important
            if mapping[0] == 0 and mapping[1] != 0:
                prediction.append(token_pred[1])
            else:
                continue

        return sentence.split(), prediction

