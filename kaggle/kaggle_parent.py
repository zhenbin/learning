# -*- coding: UTF-8 -*-
import time
from sklearn.cross_validation import cross_val_score
import numpy
import pandas


class kaggle_parent:
    def train(self):
        print("start training.")
        start = time.clock()
        estimator, x, y = self.cross_train()
        '''
        交叉验证
        使用cross_val_score做交叉验证,其中入参有:
        estimator   : 就是模型了,这里的模型实现了fit接口;
        cv          : 这里是一个数字时，用的是kfold,也就是用的S折交叉验证；
        '''

        score = cross_val_score(estimator, x, y, cv=3)
        print(score.mean())
        elapsed = (time.clock() - start)
        print("Time used: %ds" % (int(elapsed)))

    def test(self):
        print("start testing.")
        start = time.clock()
        estimator, x_train, y_train, x_test, path = self.test_parameters()
        estimator.fit(x_train, y_train)

        elapsed = (time.clock() - start)
        start = time.clock()
        print("Training Time used:", float(elapsed / 60), "min")

        result = estimator.predict(x_test)
        result = numpy.c_[range(1, len(result) + 1), result.astype(int)]
        df_result = pandas.DataFrame(result, columns=['ImageId', 'Label'])
        df_result.to_csv(path, index=False)
        # end time
        elapsed = (time.clock() - start)
        print("Test Time used:", float(elapsed / 60), "min")

    def cross_train(self):
        raise NotImplementedError

    def test_parameters(self):
        raise NotImplementedError
