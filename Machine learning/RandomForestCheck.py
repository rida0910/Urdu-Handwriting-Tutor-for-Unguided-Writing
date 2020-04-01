# -*- coding: utf-8 -*-
"""
Created on Wed Apr  1 19:48:42 2020

@author: tuba9
"""



import numpy as nm  
import matplotlib.pyplot as mtp  
import pandas as pd  
import pickle
from sklearn.ensemble import RandomForestClassifier  
rf = RandomForestClassifier()

datasett = pd.read_csv('C:\\Users\\tuba9\\Desktop\Machine learning\\preprocessedTestData4.csv')

X_val = datasett.iloc[:, 0:61].values
Y_val = datasett.iloc[:, 61].values
'''
# load the model from disk
loaded_model = pickle.load(open(filename, 'rb'))
result = loaded_model.score(X_val, X_val)
print(result)
'''

with open('finalized_model.sav', 'rb') as f:
    rf = pickle.load(f)


y_preds = rf.predict(X_val)
#print(y_preds)

ynew = rf.predict(X_val)

for i in range(len(X_val)):
	print("Predicted=%s" % (ynew[i]))
   
















