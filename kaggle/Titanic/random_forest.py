import pandas
from sklearn.ensemble import RandomForestClassifier

from kaggle.kaggle_parent import kaggle_parent


class rf(kaggle_parent):
    def test_parameters(self):
        pass

    def cross_train(self):
        data_x = pandas.read_csv("data/train.csv")

        data_y = data_x['Survived']
        data_x.drop("Survived", axis=1, inplace=True)

        # handle data_x

        # passengerid, name, ticket
        data_x.drop(['PassengerId', 'Name', 'Ticket'], axis=1, inplace=True)

        # embarked
        data_x['Embarked'] = data_x['Embarked'].fillna("S")
        embarked_dummies = pandas.get_dummies(data_x['Embarked'])
        data_x = data_x.join(embarked_dummies)
        data_x.drop('Embarked')

        # fare
        data_x['Fare'].fillna(data_x['Fare'].median(), inplace=True)

        rf = RandomForestClassifier(n_estimators=400, n_jobs=-1, max_features="sqrt")
        return rf, train_x, train_y


rf().train()
