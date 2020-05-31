# -*- coding: utf-8 -*-
"""
Created on Sun May 31 17:13:09 2020

@author: RIDA SAJID
"""

from __future__ import absolute_import, division, print_function, unicode_literals
import pandas as pd
import matplotlib.pyplot as plt

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

df = pd.read_csv("D:\\ML\\MergedData.csv")

arrx = df['XCoord']
arry = df['YCoord']
arrt = df['TimeElapsed']
arrCanvasSize = df['CanvasSize']

k = 0
while (k < 37):
    testx = arrx[k].split(",")
    test1x = pd.to_numeric(testx, errors='coerce')
    testy = arry[k].split(",")
    test1y = pd.to_numeric(testy, errors='coerce')
    testt = arrt[k].split(",")
    test1t = pd.to_numeric(testt, errors='coerce')
    cvsize = arrCanvasSize[k].replace('(', '').replace(')', '')
    cvsize = eval(cvsize)
    StoredCharacter = df["StoredCHaracter"][k]
    
    # excluding the last empty string
    test1x = test1x[:-1]
    test1y = test1y[:-1]
    test1t = test1t[:-1]
    
    fig = plt.figure()
    plt.ylim(-cvsize[1], 0)
    plt.xlim(0, cvsize[0])
    plt.plot(test1x, -test1y, 'bo')
    fig.suptitle('Character: ' + getCharacter(StoredCharacter) + ' (' + str(StoredCharacter) + ')' + '   Row number: ' + str(k+2), fontsize=10)
    
    k = k + 1