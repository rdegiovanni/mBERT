#!/usr/bin/python

import sys

from transformers import RobertaConfig, RobertaTokenizer, RobertaForMaskedLM, pipeline

model = RobertaForMaskedLM.from_pretrained("microsoft/codebert-base-mlm")
tokenizer = RobertaTokenizer.from_pretrained("microsoft/codebert-base-mlm")

model.save_pretrained('pre-trained/codebert-base-mlm/')
tokenizer.save_pretrained('pre-trained/codebert-base-mlm/')