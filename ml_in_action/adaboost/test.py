from numpy import mat

import adaboost


def demo1():
    test_data = mat([[0, 0], [1, 0], [2, 0], [3, 1], [4, 0], [5, 1], [6, 0], [7, 0], [8, 0]])
    labels = mat([1, 1, 1, -1, 1, -1, -1, -1, -1]).T

    adad = adaboost.ada(test_data, labels)
    adad.train_ada()


def demo2():
    test_data = mat([0, 1, 2, 3, 4, 5, 6, 7, 8, 9]).T
    labels = mat([1, 1, 1, -1, -1, -1, 1, 1, 1, -1]).T

    adad = adaboost.ada(test_data, labels)
    adad.train_ada()


demo2()
