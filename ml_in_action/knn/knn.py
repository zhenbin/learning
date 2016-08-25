# -*- coding: utf-8 -*-

__author__ = 'zhenbin.lzb'
from numpy import *


def createDateSet():
    group = array([[1.0, 1.1], [1.0, 1.0], [0, 0], [0, 0.1]])
    labels = ['A', 'A', 'B', 'B']
    return group, labels


# knn algorithm
def classify0(intX, dataSet, labels, k):
    m = dataSet.shape[0]
    diffMatrix = dataSet - tile(intX, (m, 1))
    sqDiffMatrix = diffMatrix ** 2  # square each element
    sqDistances = sqDiffMatrix.sum(axis=1)
    distances = sqDistances ** 0.5
    sortedDistIndices = distances.argsort()

    classCount = {}  # dictionary
    for i in range(k):
        voteLabel = labels[sortedDistIndices[i]]
        classCount[voteLabel] = classCount.get(voteLabel, 0) + 1  # if no keys names 'voteLabel', return 0

    # .iteritems() get iterator; .itemgetter(1) means sorted by the second element.
    import operator
    sortedClassCount = sorted(classCount.iteritems(), key=operator.itemgetter(1), reverse=True)
    return sortedClassCount[0][0]


# normalizing numeric values
def autoNorm(dataSet):
    minVals = dataSet.min(0)  # create a (1, n) numpy array, each column contains the min value of dataSet's column.
    maxVals = dataSet.max(0)
    ranges = maxVals - minVals
    normDataSet = zeros(shape(dataSet))  # shap(dataSet) returns dataSet's shape: (m, n)
    m = dataSet.shape[0]  # get m of (m, n), that is, the number of lines
    normDataSet = dataSet - tile(minVals, (m, 1))  # tile(): create a numpy array, see 'tile' for details.
    normDataSet = normDataSet / tile(ranges, (m, 1))
    return normDataSet, ranges, minVals


def testAlgorithm(testMatrix, testVector, trainingMatrix, trainingVector, k):
    m = testMatrix.shape[0]
    errorCount = 0.0
    for i in range(m):
        result = classify0(testMatrix[i, :], trainingMatrix, trainingVector, k)

        if (result != testVector[i]):
            errorCount += 1.0
            print "%d: the classsifier came back with: %d, but the real answer is: %d" % (i, result, testVector[i])
    print "the total error rate is: %f" % (errorCount / m)
    print "the number of test set is %d, the number of error count is %d" % (m, errorCount)
