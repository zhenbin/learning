import id3
import treePlotter

fr = open('data/lenses.txt')
data_set = [inst.strip().split('\t') for inst in fr.readlines()]
feature_names = ['age', 'prescript', 'astigmatic', 'tear rate']
lenses_tree = id3.create_tree(data_set, feature_names)
print lenses_tree
treePlotter.createPlot(lenses_tree)

