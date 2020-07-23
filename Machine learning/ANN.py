# -*- coding: utf-8 -*-
"""
Created on Tue Feb 11 11:07:03 2020

@author: Rida Sajid
"""


import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from imblearn.over_sampling import SMOTE


dataset = pd.read_csv("D:\\ML\\PreprocessedTestDataFinal.csv")
dataset.head()
dataset.info()
dataset.describe()
dataset.columns
dataset = dataset.replace([np.inf, -np.inf], np.nan)
dataset = dataset.dropna()



X = dataset.iloc[:, 0:101].values
y = dataset.iloc[:, 101].values

smote = SMOTE('minority')
X, y = smote.fit_sample(X, y)


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


from keras.models import Sequential
from keras.layers import Dense

classifier = Sequential()
classifier.add(Dense(output_dim = 222, init = 'uniform', activation = 'tanh', input_dim = 101))
classifier.add(Dense(output_dim = 222, init = 'uniform', activation = 'tanh'))
classifier.add(Dense(output_dim = 38, init = 'uniform', activation='softmax'))
classifier.compile(optimizer = 'adam', loss='categorical_crossentropy', metrics = ['accuracy'])
classifier.fit(X_train, y_train, batch_size = 100, nb_epoch = 200)


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

import keras
keras_file = "ANN.h5"
keras.models.save_model(classifier, keras_file)