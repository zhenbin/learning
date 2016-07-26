import datingSite
import knn

# intX = [0.44832535, 0.39805139, 0.56233353]
# print knn.classify0(intX, matrix, vector, 3)

datingSite.datingClassTest()

# draw picture
import matplotlib.pyplot as plt
from numpy import array

matrix, vector = datingSite.file2matrix('data/datingTestSet2.txt')
matrix, _, _ = knn.autoNorm(matrix)

fig = plt.figure()
ax = fig.add_subplot(111)
ax.scatter(matrix[:, 0], matrix[:, 1], 15.0 * array(vector), 15.0 * array(vector))
plt.show()