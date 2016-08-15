from numpy import ones, mat, multiply

class stump:
    def __init__(self, thresh=0.0, left_value=0.0, right_value=0.0, position=0):
        self.thresh = thresh
        self.left_value = left_value
        self.right_value = right_value
        self.position = position  # [0, position) belongs to left


class boosting_reg_tree:
    def __init__(self, train_data, train_result):
        self.stumps = []
        self.train_data = train_data
        self.train_result = train_result
        self.current_result = train_result.copy()

    def regression_all(self, test_data):
        regression_values = []
        for sample in test_data:
            regression_value = 0.0
            for stump in self.stumps:
                if float(sample[0]) <= stump.thresh:
                    regression_value += stump.left_value
                else:
                    regression_value += stump.right_value
            regression_values.append(regression_value)
        return regression_values

    def loss_value(self):
        regression_values = mat(self.regression_all(self.train_data)).T
        loss_values = multiply(self.train_result - regression_values, self.train_result - regression_values)
        # print "train result: ", self.train_result.T
        print "regression result: ", regression_values.T
        return loss_values.sum()

    def train(self):
        max_count = 10
        for i in range(max_count):
            new_stump = self.train_stump()
            print "\n------- %d ----------\n" % i
            print "threshold: %f, c1: %f, c2: %f, pos: %d" \
                  % (new_stump.thresh, new_stump.left_value, new_stump.right_value, new_stump.position)
            self.stumps.append(new_stump)
            self.current_result[:new_stump.position, 0] -= new_stump.left_value
            self.current_result[new_stump.position:, 0] -= new_stump.right_value
            loss_value = self.loss_value()
            print "residual: ", self.current_result.T
            print "loss_value: %f" % loss_value
            if loss_value < 0.2:
                break

    def train_stump(self):
        # N ^ 2
        sample_count = self.current_result.shape[0]
        min_loss = 100000000000000.0
        min_pos = 0
        best_stump = stump()
        for i in range(sample_count):
            if i == 0:
                continue
            c1 = float(self.current_result[:i, 0].sum()) / i
            loss1 = self.current_result[:i, 0] - c1
            loss1 = multiply(loss1, loss1)
            loss1 = loss1.sum()

            c2 = float(self.current_result[i:, 0].sum()) / (sample_count - i)
            loss2 = self.current_result[i:, 0] - c2
            loss2 = multiply(loss2, loss2)
            loss2 = loss2.sum()
            if loss1 + loss2 < min_loss:
                min_loss = loss1 + loss2
                best_stump.position = i
                best_stump.left_value = c1
                best_stump.right_value = c2
                best_stump.thresh = float(self.train_data[i, 0] + self.train_data[i - 1, 0]) / 2
        return best_stump
