# -*- coding: UTF-8 -*-
'''
加载数据
'''
import pandas
from sklearn.neighbors import KNeighborsClassifier

from kaggle.kaggle_parent import kaggle_parent


class KNN(kaggle_parent):
    def cross_train(self):
        train_data = pandas.read_csv("data/train_small.csv")
        train_x_small = train_data.values[0:, 1:]
        train_y_small = train_data.values[0:, 0]
        '''
        KNN

        n_neighbors : 就是KNN里的K了,代表找K近邻;
        weights     : 指k个近邻每个的权重,'uniform'就是大家权重一样,'distance'就是越近权重越大;
        algorithm   : 就是查找近邻时用的算法,有auto/kd_tree/ball_tree;
        metric      : 度量距离的方法，有minkowski和lp两种;lp是minkowski的开p次方;lp详见《统计学习方法》
        p           : 距离的p值,在minkowski时,p=1是曼哈顿,p=2是平方差(在lp时是欧氏);
        '''
        knn = KNeighborsClassifier(
            n_neighbors=3,
            weights='uniform',
            algorithm='kd_tree',
            p=2,
            metric='minkowski'
        )
        return knn, train_x_small, train_y_small

    def test_parameters(self):
        knn = KNeighborsClassifier(
            n_neighbors=3,
            weights='uniform',
            algorithm='kd_tree',
            p=2,
            metric='minkowski'
        )
        # score: 0.96857
        # ('Test Time used:', 大概半个小时)
        train_data = pandas.read_csv("data/train.csv")
        train_x = train_data.values[0:, 1:]
        train_y = train_data.values[0:, 0]
        test_x = pandas.read_csv("data/test.csv")
        test_x = test_x.values[0:, 0:]

        return knn, train_x, train_y, test_x, "data/result.knn.csv"


KNN().test()
