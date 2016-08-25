# -*- coding: utf-8 -*-

__author__ = 'zhenbin.lzb'
from numpy import *
import knn

# load file to matrix
def file2matrix(filename):
    fr = open(filename)
    arrayOLines = fr.readlines()
    numberOfLines = len(arrayOLines)

    # create numpy matrix
    returnMatrix = zeros((numberOfLines, 3))
    classLabelVector = []
    index = 0
    for line in arrayOLines:
        line = line.strip()  # Return a copy of the string with leading and trailing whitespace removed.
        listFromLine = line.split('\t')

        # pick the first 3 features
        returnMatrix[index, :] = listFromLine[0:3]
        index += 1

        # pick the last element as target variable
        classLabelVector.append(int(listFromLine[-1]))
    return returnMatrix, classLabelVector


# test dating class
def datingClassTest():
    datingMatrix, datingVector = file2matrix("data/datingTestSet2.txt")
    datingMatrix, _, _ = knn.autoNorm(datingMatrix)

    m = datingMatrix.shape[0]
    startIndex = int(0.1 * m)

    knn.testAlgorithm(datingMatrix[0:startIndex, :], datingVector[0:startIndex],
                      datingMatrix[startIndex:m, :], datingVector[startIndex:m], 3)


datingClassTest()