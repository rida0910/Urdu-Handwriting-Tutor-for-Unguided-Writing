


# load and evaluate a saved model
from keras.models import load_model
import pandas as pd
from keras.utils import to_categorical
 
# load model
model = load_model('ANN.h5')
# load dataset
datasett = pd.read_csv("D:\\ML\\PreprocessedTestData3.csv")

X_val = datasett.iloc[:, 0:61].values
y_val = datasett.iloc[:, 61].values

y_val = to_categorical(y_val)

from sklearn.preprocessing import StandardScaler
sc = StandardScaler()
X_val = sc.fit_transform(X_val)

# evaluate the model
# =============================================================================
# score = model.evaluate(X_val, y_val, verbose=0)
# print("%s: %.2f%%" % (model.metrics_names[1], score[1]*100))
# =============================================================================



y_pred = model.predict_proba(X_val)
print(y_pred)
# =============================================================================
# y = (y_pred > 0.5)
# from sklearn.metrics import confusion_matrix
# cm = confusion_matrix(y_pred.argmax(axis=1), y_pred.argmax(axis=1))
# import matplotlib.pyplot as plt
# plt.matshow(cm)
# =============================================================================

#print(max(max(y_pred)))
# =============================================================================
# import numpy as np
# acc = np.sum(cm.diagonal()) / np.sum(cm)
# print('Overall accuracy: {} %'.format(acc*100))
# =============================================================================


ynew = model.predict_classes(X_val)
# show the inputs and predicted outputs
#print("X=%s, Predicted=%s" % (X_val[0], ynew[0]))

for i in range(len(X_val)):
	print("Predicted=%s" % (ynew[i]))