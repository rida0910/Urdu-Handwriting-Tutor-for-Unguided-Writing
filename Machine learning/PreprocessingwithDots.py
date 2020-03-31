# -*- coding: utf-8 -*-
"""
Created on Wed Feb 12 22:35:14 2020

@author: HP
"""


from __future__ import absolute_import, division, print_function, unicode_literals
import pandas as pd
import numpy as np
import scipy.signal
import matplotlib.pyplot as plt
import csv

from sklearn.preprocessing import normalize


with open('PreprocessedTestData3.csv', 'w', newline='') as file:
        writer = csv.writer(file)
        writer.writerow(["x1", "x2", "x3", "x4", "x5", "x6", "x7", "x8", "x9",
                        "x10", "x11", "x12", "x13", "x14", "x15", "x16", "x17", 
                        "x18", "x19", "x20", "x21", "x22", "x23", "x24", "x25", 
                        "x26", "x27", "x28", "x29",
                        "x30", "y1", "y2", "y3", "y4", "y5", "y6", 
                        "y7", "y8", "y9", "y10", "y11", "y12", "y13", "y14", 
                        "y15", "y16", "y17", "y18", "y19", "y20", "y21", "y22", "y23", "y24", 
                        "y25", "y26", "y27", "y28", "y29", "y30", "dots", "Character"])


#df = pd.read_excel("D:\\ML\\TestData.xls")
df = pd.read_csv("D:\\ML\\StrokeData.csv")
df.head()
df.dtypes

arrx = df['XCoord']
arry = df['YCoord']
arrt = df['TimeElapsed']

k = 0
while (k < len(arrx)):
    testx = arrx[k].split(",")
    test1x = pd.to_numeric(testx, errors='coerce')
    testy = arry[k].split(",")
    test1y = pd.to_numeric(testy, errors='coerce')
    testt = arrt[k].split(",")
    test1t = pd.to_numeric(testt, errors='coerce')

    # excluding the last empty string
    test1x = test1x[:-1]
    test1y = test1y[:-1]
    test1t = test1t[:-1]

    #checking the time when dot was drawn
    dotstime = []
    dotsindices = []
    dot = test1t[0]
    i = 0
    while(i < len(test1t) - 1):
        
        if test1t[i] >= test1t[i+1]:
            dot = test1t[i+1]
            dotstime.append(dot)
            dotsindices.append(i+1)
        i = i + 1
    # getting the index of first dot            
    if len(dotsindices) != 0:
        dotIndex = dotsindices[0]
        #Removing dot from character
        test1x_Without_dot = test1x[:dotIndex]
        test1y_Without_dot = test1y[:dotIndex]
        test1x_dot = test1x[dotIndex:]
        test1y_dot = test1y[dotIndex:]
        Yx_dot = test1x_dot
        Yy_dot = test1y_dot
        StoredCharacter = df["StoredCHaracter"][k]
        if StoredCharacter == 5 or StoredCharacter == 12 or StoredCharacter == 15 or StoredCharacter == 28:
            Yx_dot = scipy.signal.resample(test1x_dot, 10)
            Yy_dot = scipy.signal.resample(test1y_dot, 10) 
        elif len(dotsindices) <= 3 and len(dotsindices) != 1:
            Yx_dot = scipy.signal.resample(test1x_dot, len(dotsindices))
            Yy_dot = scipy.signal.resample(test1y_dot, len(dotsindices))
        else:
            Yx_dot = scipy.signal.resample(test1x_dot, 3)
            Yy_dot = scipy.signal.resample(test1y_dot, 3)
        
        Yx = scipy.signal.resample(test1x_Without_dot, 30-len(Yx_dot))
        Yy = scipy.signal.resample(test1y_Without_dot, 30-len(Yy_dot))

        
        smoothX = scipy.signal.savgol_filter(Yx, 17, 3)
        smoothY = scipy.signal.savgol_filter(Yy, 17, 3)
    
        Final_X_Coord = np.concatenate((smoothX, Yx_dot))
        Final_Y_Coord = np.concatenate((smoothY, Yy_dot))
        
    else:
        #downsampling to 30 points
        Yx = scipy.signal.resample(test1x, 30)
        Yy = scipy.signal.resample(test1y, 30)
    
    
        smoothX = scipy.signal.savgol_filter(Yx, 17, 3)
        smoothY = scipy.signal.savgol_filter(Yy, 17, 3)
    
        Final_X_Coord = smoothX
        Final_Y_Coord = smoothY
    
    Normalized_X = normalize([Final_X_Coord]).reshape(len(Final_X_Coord))
    Normalized_Y = normalize([Final_Y_Coord]).reshape(len(Final_Y_Coord) )
    
    
    CombinedXY = np.concatenate((Normalized_X[:30], Normalized_Y[:30]))
    with open('PreprocessedTestData3.csv', 'a', newline='') as file:
        writer = csv.writer(file)
        
        writer.writerow([CombinedXY[0], CombinedXY[1], CombinedXY[2], CombinedXY[3], 
                         CombinedXY[4], CombinedXY[5], CombinedXY[6], CombinedXY[7],
                         CombinedXY[8], CombinedXY[9], CombinedXY[10], CombinedXY[11],
                         CombinedXY[12], CombinedXY[13], CombinedXY[14], CombinedXY[15],
                         CombinedXY[16], CombinedXY[17], CombinedXY[18], CombinedXY[19],
                         CombinedXY[20], CombinedXY[21], CombinedXY[22], CombinedXY[23],
                         CombinedXY[24], CombinedXY[25], CombinedXY[26], CombinedXY[27],
                         CombinedXY[28], CombinedXY[29], CombinedXY[30], CombinedXY[31],
                         CombinedXY[32], CombinedXY[33], CombinedXY[34], CombinedXY[35],
                         CombinedXY[36], CombinedXY[37], CombinedXY[38], CombinedXY[39], 
                         CombinedXY[40], CombinedXY[41], CombinedXY[42], CombinedXY[43], 
                         CombinedXY[44], CombinedXY[45], CombinedXY[46], CombinedXY[47],
                         CombinedXY[48], CombinedXY[49], CombinedXY[50], CombinedXY[51],
                         CombinedXY[52], CombinedXY[53], CombinedXY[54], CombinedXY[55],
                         CombinedXY[56], CombinedXY[57], CombinedXY[58], CombinedXY[59], len(dotsindices), df["StoredCHaracter"][k]])
    
    k = k + 1