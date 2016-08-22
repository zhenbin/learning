import pandas
from sklearn.ensemble import RandomForestClassifier
import numpy

from kaggle.kaggle_parent import kaggle_parent


class rf(kaggle_parent):
    def test_parameters(self):
        pass

    def cross_train(self):
        train_data = pandas.read_csv("data/train.csv")
        train_x = numpy.array(train_data.values[0:, 2:])
        train_y = numpy.array(train_data.values[0:, 1])
        rf = RandomForestClassifier(n_estimators=400, n_jobs=-1, max_features="sqrt")
        return rf, train_x, train_y

rf().train()
