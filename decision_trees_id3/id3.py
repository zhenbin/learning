from math import log
import operator


def calc_shannon_entropy(labels):
    labels_length = len(labels)

    # hash labels
    dict_count = {}
    for element in labels:
        dict_count[element] = dict_count.get(element, 0) + 1

    # calculate entropy
    shannon_entropy = 0.0
    for key in dict_count:
        p = float(dict_count[key]) / labels_length
        shannon_entropy -= p * log(p, 2)

    return shannon_entropy


def calc_data_set_entropy(data_set):
    labels = [element[-1] for element in data_set]
    return calc_shannon_entropy(labels)


def split_data_set(data_set, axis, feature_value):
    return_data_set = []
    for element in data_set:
        if element[axis] == feature_value:
            return_element = element[:axis]
            return_element.extend(element[axis + 1:])
            return_data_set.append(return_element)
    return return_data_set


def choose_best_feature_to_split(data_set):
    feature_num = len(data_set[0]) - 1
    best_feature = -1
    best_info_gain = 0.0
    base_entropy = calc_data_set_entropy(data_set)

    for i in range(feature_num):  # iterate over all the features
        # create a list of all the examples of the feature
        feature_list = [element[i] for element in data_set]
        feature_set = set(feature_list)  # a set of unique values

        # calculate info gain
        total_entropy = 0.0
        for feature_value in feature_set:
            sub_data_set = split_data_set(data_set, i, feature_value)
            prob = float(len(sub_data_set)) / len(data_set)
            total_entropy += prob * calc_data_set_entropy(sub_data_set)
        info_gain = base_entropy - total_entropy

        # update best info gain
        if info_gain > best_info_gain:
            best_info_gain = info_gain
            best_feature = i
    return best_feature


def majority_label(labels):
    dict_labels = {}
    for label in labels:
        dict_labels[label] = dict_labels.get(label, 0) + 1
    sorted_dict_labels = sorted(dict_labels.iteritems(),
                                key=operator.itemgetter(1), reverse=True)
    return sorted_dict_labels[0][0]


def create_tree(data_set, feature_names):
    labels = [element[-1] for element in data_set]

    if len(data_set[0]) == 1:  # stop splitting when all the classes are equal
        return majority_label(labels)
    # stop splitting when there are no more features in dataSet
    if labels.count(labels[0]) == len(labels):
        return labels[0]

    best_feature = choose_best_feature_to_split(data_set)
    best_feature_name = feature_names[best_feature]
    tree = {best_feature_name: {}}
    sub_feature_names = feature_names[:]  # copy all of feature names
    del (sub_feature_names[best_feature])

    feature_values = set([element[best_feature] for element in data_set])
    for feature_value in feature_values:
        sub_data_set = split_data_set(data_set, best_feature, feature_value)
        tree[best_feature_name][feature_value] = create_tree(sub_data_set, sub_feature_names)

    return tree


def classify(tree, feature_names, test_vec):
    if type(tree).__name__ != 'dict':
        return tree

    feature_name = tree.keys()[0]
    sub_trees = tree[feature_name]
    feature_index = feature_names.index(feature_name)
    return classify(sub_trees[test_vec[feature_index]], feature_names, test_vec)


def store_object(object_instance, filename):
    import pickle
    fw = open(filename, 'w')
    pickle.dump(object_instance, fw)
    fw.close()


def grab_object(filename):
    import pickle
    fw = open(filename, 'r')
    return pickle.load(fw)


# test code
def create_data_set():
    data_set = [[1, 1, 'yes'],
                [1, 1, 'yes'],
                [1, 0, 'no'],
                [0, 1, 'no'],
                [0, 1, 'no']]
    feature_names = ['no surfacing', 'flippers']
    return data_set, feature_names


test_data_set, test_feature_names = create_data_set()
test_tree = create_tree(test_data_set, test_feature_names)
print test_tree
print classify(test_tree, test_feature_names, [1, 0])
print classify(test_tree, test_feature_names, [1, 1])

store_object(test_tree, 'data/store_object.txt')
new_tree = grab_object('data/store_object.txt')
print new_tree
print new_tree['no surfacing']
