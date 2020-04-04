# -*- coding: utf-8 -*-
"""
Created on Tue Feb 11 11:07:03 2020

@author: HP
"""


# -*- coding: utf-8 -*-
"""
Created on Fri Dec 14 10:27:49 2018

@author: Rida Sajid
"""

import pandas as pd
import numpy as np
import matplotlib.pyplot as plt


dataset = pd.read_csv("D:\\ML\\PreprocessedTrainingData.csv")
dataset.head()
dataset.info()
dataset.describe()
dataset.columns



X = dataset.iloc[:, 0:61].values
y = dataset.iloc[:, 61].values


from keras.utils import to_categorical
y = to_categorical(y)

from sklearn.model_selection import train_test_split
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size = 0.25, random_state = 0)

# =============================================================================
# from sklearn.preprocessing import StandardScaler
# sc = StandardScaler()
# X_train = sc.fit_transform(X_train)
# X_test = sc.fit_transform(X_test)
# =============================================================================

import keras
from keras.models import Sequential
from keras.layers import Dense

classifier = Sequential()
classifier.add(Dense(output_dim = 61, init = 'uniform', activation = 'relu', input_dim = 61))
classifier.add(Dense(output_dim = 122, init = 'uniform', activation = 'relu'))
classifier.add(Dense(output_dim = 122, init = 'uniform', activation = 'relu'))
#classifier.add(Dense(output_dim = 122, init = 'uniform', activation = 'relu'))
classifier.add(Dense(output_dim = 38, init = 'uniform', activation='softmax'))
classifier.compile(optimizer = 'adam', loss='categorical_crossentropy', metrics = ['accuracy'])
classifier.fit(X_train, y_train, batch_size = 10, nb_epoch = 100)


y_pred = classifier.predict(X_test)
y = (y_pred > 0.5)

from sklearn.metrics import confusion_matrix
cm = confusion_matrix(y_test.argmax(axis=1), y_pred.argmax(axis=1))
plt.matshow(cm)
cm

from sklearn import metrics
# Model Accuracy, how often is the classifier correct?
print("Accuracy:",metrics.accuracy_score(y_test, y_pred.round()))

print("Precision:",metrics.precision_score(y_test, y_pred.round(), average='micro'))

print("Recall:",metrics.recall_score(y_test, y_pred.round(), average='micro'))



from tensorflow.compat.v1 import lite
keras_file = "ANN.h5"
keras.models.save_model(classifier, keras_file)
converter = lite.TFLiteConverter.from_keras_model_file(keras_file)
tflite_model = converter.convert()
open("ANN.tflite", "wb").write(tflite_model)





# load and evaluate a saved model
from keras.models import load_model
import pandas as pd
from keras.utils import to_categorical
 
# load model
model = load_model('ANN.h5')
# load dataset
datasett = pd.read_csv("D:\\ML\\PreprocessedTestData.csv")

X_val = datasett.iloc[:, 0:61].values
y_val = datasett.iloc[:, 61].values

y_val = to_categorical(y_val)


#X_val = sc.fit_transform(X_val)

# evaluate the model
score = model.evaluate(X_val, y_val, verbose=0)
print("%s: %.2f%%" % (model.metrics_names[1], score[1]*100))