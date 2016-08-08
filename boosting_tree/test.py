from numpy import mat

import boosting_regression_tree


def demo1():
    test_data = mat([1, 2, 3, 4, 5, 6, 7, 8, 9, 10]).T
    test_y = mat([5.56, 5.70, 5.91, 6.40, 6.80, 7.05, 8.90, 8.70, 9.00, 9.05]).T

    boosting_tree = boosting_regression_tree.boosting_reg_tree(test_data, test_y)
    boosting_tree.train()


def demo2():
    demo1()
    # test_data = mat([0, 1, 2, 3, 4, 5, 6, 7, 8, 9]).T
    # labels = mat([1, 1, 1, -1, -1, -1, 1, 1, 1, -1]).T
    #
    # adad = adaboost.ada(test_data, labels)
    # adad.train_ada()

demo1()
