import pandas

path = "kaggle/Digit Recognizer/data/test"
small_count = 10000

dataset = pandas.read_csv("%s.csv" % path)
dataset_small = dataset.values[0:small_count, 0:]

dataset_small_t = pandas.DataFrame(dataset_small, columns=dataset.columns)
dataset_small_t.to_csv("%s_small.csv" % path, index=False, columns=dataset.columns)
