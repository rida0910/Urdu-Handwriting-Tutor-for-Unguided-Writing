# -*- coding: utf-8 -*-
"""
Created on Wed Apr  1 21:19:03 2020

@author: tuba9
"""


# importing libraries  
import numpy as nm  
import matplotlib.pyplot as mtp  
import pandas as pd  
  
#importing datasets  
data_set= pd.read_csv('C:\\Users\\tuba9\\Desktop\Machine learning\\PreprocessedTrainingData.csv')  
  
#Extracting Independent and dependent Variable  
x= data_set.iloc[:, 0:61].values  
y= data_set.iloc[:, 61].values  
  
# Splitting the dataset into training and test set.  
from sklearn.model_selection import train_test_split  
x_train, x_test, y_train, y_test= train_test_split(x, y, test_size= 0.25, random_state=0)  
  
#feature Scaling  
from sklearn.preprocessing import StandardScaler    
st_x= StandardScaler()    
x_train= st_x.fit_transform(x_train)    
x_test= st_x.transform(x_test)    

#Fitting Decision Tree classifier to the training set  
from sklearn.ensemble import RandomForestClassifier  
classifier= RandomForestClassifier(n_estimators= 10, criterion="entropy")  
classifier.fit(x_train, y_train) 


#Predicting the test set result  
y_pred= classifier.predict(x_test) 

#Creating the Confusion matrix  
from sklearn.metrics import confusion_matrix  
cm= confusion_matrix(y_test, y_pred)  

'''

import pickle 


rf = RandomForestClassifier()
rf.fit(x, y)

with open('path/to/file', 'wb') as f:
    Pickle.dump(rf, f)
    
    
'''
import pickle
# Fit the model on training set
model = RandomForestClassifier()
model.fit(x_train, y_train)
# save the model to disk
filename = 'finalized_model.sav'
pickle.dump(model, open(filename, 'wb'))
 
# some time later...
''' 
datasett = pd.read_csv('C:\\Users\\tuba9\\Desktop\Machine learning\\preprocessedTestData4.csv')

X_val = datasett.iloc[:, 0:61].values
y_val = datasett.iloc[:, 61].values

# load the model from disk
loaded_model = pickle.load(open(filename, 'rb'))
result = loaded_model.score(X_val, y_val)
print(result)

'''

loaded_model = pickle.load(open(filename, 'rb'))
result = loaded_model.score(x_test, y_test)
print(result)