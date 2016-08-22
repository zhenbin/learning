# -*- coding: UTF-8 -*-
from sklearn.ensemble import RandomForestClassifier
import pandas

from kaggle.kaggle_parent import kaggle_parent


class random_forest(kaggle_parent):
    def cross_train(self):
        train_data = pandas.read_csv("data/train_small.csv")
        train_x_small = train_data.values[0:, 1:]
        train_y_small = train_data.values[0:, 0]

        '''
        RandomForestClassifier:
        n_estimators        : 树的棵数（默认10棵）
        n_jobs              : 并行任务数，如果为-1则设置为cpu的核数（默认为1）
        max_features        : 挑选的特征数目，默认是"auto"（全选），也可以设置成"log2"、"sqrt"
        criterion           : 不确定性，有"gini"和"entropy"（默认gini）

        max_depth           : 树的最大深度(默认最大深度）
        min_samples_split   : 结点需要切分的最少样本数（默认为2）
        min_samples_leaf    : 结点切分后，最大的子结点最少拥有的样本数（默认为1）
        max_leaf_nodes      : 最大叶子结点个数

        '''
        rf = RandomForestClassifier(n_estimators=400, n_jobs=-1, max_features="sqrt")
        return rf, train_x_small, train_y_small

    def test_parameters(self):
        pass
        rf = RandomForestClassifier(n_estimators=400, n_jobs=-1, max_features="sqrt")
        # score: 0.96714
        # ('Training Time used:', 1.15419215024101, 'min')
        # ('Test Time used:', 0.05699266602587727, 'min')
        train_data = pandas.read_csv("data/train.csv")
        train_x = train_data.values[0:, 1:]
        train_y = train_data.values[0:, 0]
        test_x = pandas.read_csv("data/test.csv")
        test_x = test_x.values[0:, 0:]

        return rf, train_x, train_y, test_x, "data/result.rf.csv"


random_forest().test()
