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
        n_estimators    : 树的棵数
        n_jobs          : 并行任务数，如果为-1则设置为cpu的核数
        max_features    : 挑选的特征数目，默认是"auto"（全选），也可以设置成"log2"、"sqrt"
        criterion       : 不确定性，有"gini"和"entropy"
        '''
        rf = RandomForestClassifier(n_estimators=400, n_jobs=-1)
        return rf, train_x_small, train_y_small

    def test_parameters(self):
        pass
        rf = RandomForestClassifier(n_estimators=400, n_jobs=-1)
        # score: 0.96671
        # ('Training Time used:', 1.1883150467398427, 'min')
        # ('Test Time used:', 0.07169732430278468, 'min')
        train_data = pandas.read_csv("data/train.csv")
        train_x = train_data.values[0:, 1:]
        train_y = train_data.values[0:, 0]
        test_x = pandas.read_csv("data/test.csv")
        test_x = test_x.values[0:, 0:]

        return rf, train_x, train_y, test_x, "data/result.rf.csv"

random_forest().test()
