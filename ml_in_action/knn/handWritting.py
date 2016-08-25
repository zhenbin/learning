__author__ = 'zhenbin.lzb'

from os import listdir
from numpy import *
import knn


def fileToVector(filename):
    fr = open(filename)
    returnVector = zeros((1, 1024))
    for i in range(32):
        line = fr.readline()
        for j in range(32):
            returnVector[0, 32 * i + j] = int(line[j])
    return returnVector


def filesToMatrix(dirname):
    fileList = listdir(dirname)
    m = len(fileList)
    matrix = zeros((m, 1024))
    vector = []

    for i in range(m):
        filename = fileList[i]
        target = filename.split('.')[0].split('_')[0]
        vector.append(int(target))
        matrix[i, :] = fileToVector("%s/%s" % (dirname, filename))
    return matrix, vector


def handWrittingTest():
    # don't normalize matrix, because it's just a 0-1 matrix, some position may be always zero!
    trainingMatrix, trainingVector = filesToMatrix("data/digits/trainingDigits")
    testMatrix, testVector = filesToMatrix("data/digits/testDigits")
    knn.testAlgorithm(testMatrix, testVector, trainingMatrix, trainingVector, 3)

handWrittingTest()