from numpy import mat, ones, dot, log, array, multiply

__author__ = 'zhenbin.lzb'


class stump:
    def __init__(self):
        self.dim = 0
        self.thresh = 0.0
        self.ineq = "lt"
        self.weight = 0.0


class ada:
    def __init__(self, test_data, class_labels):
        self.stumps = []
        self.test_data = test_data
        self.test_data_weight = ones((test_data.shape[0], 1)) / test_data.shape[0]
        self.class_labels = class_labels

    def classify_all(self, test_matrix, test_labels):
        err_num = 0.0
        predict = test_labels.copy()
        for i in range(test_matrix.shape[0]):
            if self.classify(array(test_matrix)[i]) != test_labels[i]:
                err_num += 1.0
                predict[i] = -predict[i]
        print "f(x) predict: ", predict.T
        return err_num / test_matrix.shape[0]

    def classify(self, test_data):
        tot_sum = 0.0
        for stump in self.stumps:
            sign = 1.0 if stump.ineq == 'lt' else -1.0
            sign = sign if test_data[stump.dim] >= stump.thresh else -sign
            tot_sum += float(sign) * stump.weight
        return 1 if tot_sum > 0 else -1

    def train_ada(self):
        for i in range(5):
            print " \n******* %d *******\n" % (i)
            stump, err_rate, predict = self.train_stump()
            print "stump: dimension = %d, threshold = %f" % (stump.dim, stump.thresh)
            print "stump error rate: ", err_rate
            print "stump predict: ", predict.T
            err_odds = (1 - err_rate) / max(err_rate, 0.0000000001)
            stump.weight = 0.5 * log(err_odds)
            print "stump weight: ", stump.weight
            self.test_data_weight[self.class_labels == predict] /= err_odds
            tot_weight = self.test_data_weight.sum()
            self.test_data_weight /= tot_weight
            print "D%d: " % (i), self.test_data_weight.T
            self.stumps.append(stump)
            err = self.classify_all(self.test_data, self.class_labels)
            print "G%d error rate: %f" % (i, err)
            if err < 0.05:
                break

    def train_stump(self):
        best_stump = stump()
        min_err = 2.0
        min_predict = []
        steps = 10
        for i in range(self.test_data.shape[1]):
            thresh_left = self.test_data[:, i].min()
            thresh_right = self.test_data[:, i].max()
            for j in range(steps):
                current_stump = stump()
                current_stump.thresh = thresh_left + float(thresh_right - thresh_left) * j / steps
                # print current_stump.thresh
                current_stump.dim = i
                for ineq in ['lt', 'gt']:
                    current_stump.ineq = ineq
                    predict = self.predict_labels(current_stump)
                    error_position = ones((self.test_data.shape[0], 1))
                    error_position[predict == self.class_labels] = 0
                    error_rate = float(dot(self.test_data_weight.T, error_position))

                    if error_rate < min_err:
                        min_err = error_rate
                        min_predict = predict
                        best_stump.dim = current_stump.dim
                        best_stump.thresh = current_stump.thresh
                        best_stump.ineq = ineq
        return best_stump, min_err, min_predict

    def predict_labels(self, current_stump):
        predict = ones((self.test_data.shape[0], 1))
        if current_stump.ineq == 'lt':
            predict[self.test_data[:, current_stump.dim] < current_stump.thresh] = -1.0
        else:
            predict[self.test_data[:, current_stump.dim] >= current_stump.thresh] = -1.0
        return predict
