# -*- coding: utf-8 -*-
"""
Created on Wed Apr 29 16:08:36 2020

@author: RIDA SAJID
"""

from __future__ import absolute_import, division, print_function, unicode_literals
import pandas as pd
import numpy as np
import scipy.signal
import matplotlib.pyplot as plt
import math
from sklearn.preprocessing import normalize
#import random
import csv

def getCharacter(i):
    char = ""
    if i == 1:
        char = "Alif"
    elif i == 2:
        char = "Bay"
    elif i == 3:
        char = "Pay"
    elif i == 4:
        char = "Tay"
    elif i == 5:
        char = "TTay"
    elif i == 6:
        char = "Say"
    elif i == 7:
        char = "Jeem"
    elif i == 8:
        char = "Chay"
    elif i == 9:
        char = "Hay"
    elif i == 10:
        char = "Khay"
    elif i == 11:
        char = "Daal"
    elif i == 12:
        char = "DDal"
    elif i == 13:
        char = "Zaal"
    elif i == 14:
        char = "Ray"
    elif i == 15:
        char = "RRay"
    elif i == 16:
        char = "Zay"
    elif i == 17:
        char = "SSay"
    elif i == 18:
        char = "Seen"
    elif i == 19:
        char = "Sheen"
    elif i == 20:
        char = "Suuad"
    elif i == 21:
        char = "Zuaad"
    elif i == 22:
        char = "Toayein"
    elif i == 23:
        char = "Zoayein"
    elif i == 24:
        char = "Aayein"
    elif i == 25:
        char = "Ghayein"
    elif i == 26:
        char = "Fay"
    elif i == 27:
        char = "Qaaf"
    elif i == 28:
        char = "kaaf"
    elif i == 29:
        char = "Gaaf"
    elif i == 30:
        char = "Laam"
    elif i == 31:
        char = "Meem"
    elif i == 32:
        char = "Noon"
    elif i == 33:
        char = "Wao"
    elif i == 34:
        char = "Choti hay"
    elif i == 35:
        char = "Hamza"
    elif i == 36:
        char = "Choti yay"
    elif i == 37:
        char = "Barri yay"
    return char

# def normalizeList(X):
#     Normalized_X = []
#     for i in range(0, len(X)):
#         Normalized_X.append((((0.9 - 0.1)*(X[i] - min(X)))/(max(X) - min(X))) + 0.1)
#     return Normalized_X


def normalize(X, factor):
    Normalized_X = []
    for i in range(0, len(X)):
        Normalized_X.append(X[i]/factor)
    return Normalized_X

# def normalizeInRangeZerotoOne(X):
#     Normalized_X = []
#     for i in range(0, len(X)):
#         Normalized_X.append((X[i] - min(X))/(max(X) - min(X)))
#     return Normalized_X

def FindAvgSpacing(X, Y):
    sum = 0
    for i in range(0, len(X) - 1):
        sum = sum + (distance(X[i], X[i+1], Y[i], Y[i+1]))
    avg = sum//(len(X)-1)
    return avg


def resample(X, Y, count):
    if len(X) == 1 or len(X) == count:
        return X, Y
    if len(X) < count:
        resampled_X = list(X)
        resampled_Y = list(Y)
        for i in range(len(resampled_X), count):
            resampled_X.append(X[len(X) - 1])
            resampled_Y.append(Y[len(Y) - 1])
        return resampled_X, resampled_Y
    
    t = 5
    resampled_X = []
    resampled_Y = []
    resampled_X.append(X[0])
    resampled_Y.append(Y[0])
    
    
    j = ((len(X))//count) + 1
    i = j     
    
   
    while len(resampled_X) < count - 2 and i + j < len(X) - 1:
        if distance(X[i], X[i+j], Y[i], Y[i+j]) >= t:
            resampled_X.append(X[i])
            resampled_Y.append(Y[i])
        i = i + j
    
    resampled_X.append(X[len(X) - 1])
    resampled_Y.append(Y[len(Y) - 1])
    if len(resampled_X) < count:
        for i in range(len(resampled_X), count):
            resampled_X.append(X[len(X) - 1])
            resampled_Y.append(Y[len(Y) - 1])
    return resampled_X, resampled_Y

def Smooth(X, Y):
    if (len(X) < 5):
        return X, Y
    else:
        smoothX = []
        smoothY = []
        smoothX.append(X[0])
        smoothX.append(X[1])
        smoothY.append(Y[0])
        smoothY.append(Y[1])
        
        for i in range(2, len(X) - 2):
            xm = (X[i-2] + X[i-1] + X[i] + X[i+1] + X[i+2])/5
            ym = (Y[i-2] + Y[i-1] + Y[i] + Y[i+1] + Y[i+2])/5
            smoothX.append(xm)
            smoothY.append(ym)
        smoothX.append(X[len(X) - 2])
        smoothY.append(Y[len(Y) - 2])
        smoothX.append(X[len(X) - 1])
        smoothY.append(Y[len(Y) - 1])
        return smoothX, smoothY
            
    

def distance(x1, x2, y1, y2):
    x = x1 - x2
    y = y1 - y2
    dist = math.sqrt(pow(x, 2) + pow(y, 2))
    return dist




with open('PreprocessedTestDataFinal.csv', 'w', newline='') as file:
        writer = csv.writer(file)
        writer.writerow(["x1", "x2", "x3", "x4", "x5", "x6", "x7", "x8", "x9",
                        "x10", "x11", "x12", "x13", "x14", "x15", "x16", "x17", 
                        "x18", "x19", "x20", "x21", "x22", "x23", "x24", "x25", 
                        "x26", "x27", "x28", "x29", "x30",
                        "x31", "x32", "x33", "x34", "x35", "x36", "x37", "x38",
                        "x39", "x40", "x41", "x42", "x43", "x44", "x45", "x46", 
                        "x47", "x48", "x49", "x50",
                        "y1", "y2", "y3", "y4", "y5", "y6", 
                        "y7", "y8", "y9", "y10", "y11", "y12", "y13", "y14", 
                        "y15", "y16", "y17", "y18", "y19", "y20", "y21", "y22", "y23", "y24", 
                        "y25", "y26", "y27", "y28", "y29", "y30",
                        "y31", "y32", "y33", "y34", "y35", "y36", "y37", "y38",
                        "y39", "y40", "y41", "y42", "y43", "y44", "y45", "y46", 
                        "y47", "y48", "y49", "y50",
                        "dots", "Character"])



df = pd.read_excel("D:\\ML\\TrainingData.xls")
#df = pd.read_csv("D:\\ML\\StrokeData.csv")
#df = pd.read_excel("D:\\ML\\ValidationData.xls")
df.head()
df.dtypes

arrx = df['XCoord']
arry = df['YCoord']
arrt = df['TimeElapsed']
arrCanvasSize = df['CanvasSize']

k = 0
#while (k < 1000):
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
    StoredCharacter = df["StoredCHaracter"][k]
            # getting the index of first dot            
    if len(dotsindices) != 0 and StoredCharacter != 1 and StoredCharacter != 9 and StoredCharacter != 11 and StoredCharacter != 14 and StoredCharacter != 18 and StoredCharacter != 20 and StoredCharacter != 22 and StoredCharacter != 24 and StoredCharacter != 30 and StoredCharacter != 31 and StoredCharacter != 33 and StoredCharacter != 34 and StoredCharacter != 35 and StoredCharacter != 36 and StoredCharacter != 37:
        dotIndex = dotsindices[0]
        #Removing dot from character
        test1x_Without_dot = test1x[:dotIndex]
        test1y_Without_dot = test1y[:dotIndex]
        test1x_dot = test1x[dotIndex:]
        test1y_dot = test1y[dotIndex:]
        #Yx_dot = test1x_dot
        #Yy_dot = test1y_dot
        
        
        if len(test1x_dot) > len(test1x_Without_dot) and StoredCharacter != 5 and StoredCharacter != 12 and StoredCharacter != 15 and StoredCharacter != 28 and StoredCharacter != 29:
            tempX = test1x_dot
            tempY = test1y_dot
            
            test1x_dot = test1x_Without_dot
            test1y_dot = test1y_Without_dot
            
            test1x_Without_dot = tempX
            test1y_Without_dot = tempY
        
        Yx_dot = []
        Yy_dot = []
        
        if StoredCharacter == 5 or StoredCharacter == 12 or StoredCharacter == 15 or StoredCharacter == 28 or StoredCharacter == 29:
            Yx_dot, Yy_dot = resample(test1x_dot, test1y_dot, 20)
            #Yy_dot = resample(test1y_dot, 10) 
        elif len(dotsindices) <= 3 and len(dotsindices) != 1:
            #Yx_dot, Yy_dot = resample(test1x_dot, test1y_dot, len(dotsindices))
            for i in range(0, len(dotsindices)):
                Yx_dot.append(test1x[dotsindices[i]])
                Yy_dot.append(test1y[dotsindices[i]])
            #Yy_dot = resample(test1y_dot, len(dotsindices))
        else:
            Yx_dot, Yy_dot = resample(test1x_dot, test1y_dot, 3)
            #Yy_dot = resample(test1y_dot, 3)
        
        Yx, Yy = resample(test1x_Without_dot, test1y_Without_dot, 50-len(Yx_dot))
        #Yy = resample(test1y_Without_dot, 30-len(Yy_dot))
        
        smoothX, smoothY = Smooth(Yx, Yy)
        #smoothY = scipy.signal.savgol_filter(Yy, 17, 3)
    
        Final_X_Coord = np.concatenate((smoothX[:50-len(Yx_dot)], Yx_dot))
        Final_Y_Coord = np.concatenate((smoothY[:50-len(Yx_dot)], Yy_dot))
        
    else:
        #downsampling to 20 points
        Yx, Yy = resample(test1x, test1y, 50)
        #Yy = resample(test1y, 30)
    
        smoothX, smoothY = Smooth(Yx, Yy)
        #smoothX = scipy.signal.savgol_filter(Yx, 17, 3)
        #smoothY = scipy.signal.savgol_filter(Yy, 17, 3)
    
        Final_X_Coord = smoothX
        Final_Y_Coord = smoothY
    
    
    cvsize = arrCanvasSize[k].replace('(', '').replace(')', '')
    cvsize = eval(cvsize)
    Normalized_X = np.array(normalize(Final_X_Coord, cvsize[0]))
    Normalized_Y = np.array(normalize(Final_Y_Coord, cvsize[1]))
    #Normalized_X = normalize([Final_X_Coord]).reshape(len(Final_X_Coord))
    #Normalized_Y = normalize([Final_Y_Coord]).reshape(len(Final_Y_Coord) )
    
    # fig = plt.figure()
    # plt.ylim(-1083, 0)
    # plt.xlim(0, 768)
    # plt.plot(test1x, -test1y, 'bo')
    # #fig.suptitle("Original Sample")
    # fig.suptitle("Original Sample: " + getCharacter(df["StoredCHaracter"][k]) + ' ' + str(df["StoredCHaracter"][k]) + ' ' + str(k+2))
    # fig = plt.figure()
    # plt.ylim(-1, 0)
    # plt.xlim(0, 1)
    # plt.plot(Normalized_X, -Normalized_Y, 'bo')
    # fig.suptitle("Preprocessed Sample " + getCharacter(df["StoredCHaracter"][k]) + ' ' + str(df["StoredCHaracter"][k]) + ' ' + str(k+2))
        
    #plt.plot(Final_X_Coord, -Final_Y_Coord, 'bo')
    CombinedXY = np.concatenate((Normalized_X[:50], Normalized_Y[:50]))
    
    if len(CombinedXY) == 100:
        with open('PreprocessedTestDataFinal.csv', 'a', newline='') as file:
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
                              CombinedXY[56], CombinedXY[57], CombinedXY[58], CombinedXY[59], 
                                CombinedXY[60], CombinedXY[61], CombinedXY[62], CombinedXY[63],
                                CombinedXY[64], CombinedXY[65], CombinedXY[66], CombinedXY[67],
                                CombinedXY[68], CombinedXY[69], CombinedXY[70], CombinedXY[71],
                                CombinedXY[72], CombinedXY[73], CombinedXY[74], CombinedXY[75], 
                                CombinedXY[76], CombinedXY[77], CombinedXY[78], CombinedXY[79], 
                                CombinedXY[80], CombinedXY[81], CombinedXY[82], CombinedXY[83], 
                                CombinedXY[84], CombinedXY[85], CombinedXY[86], CombinedXY[87], 
                                CombinedXY[88], CombinedXY[89], CombinedXY[90], CombinedXY[91], 
                                CombinedXY[92], CombinedXY[93], CombinedXY[94], CombinedXY[95], 
                                CombinedXY[96], CombinedXY[97], CombinedXY[98], CombinedXY[99], 
                              len(dotsindices), df["StoredCHaracter"][k]])
        
    k = k + 1


