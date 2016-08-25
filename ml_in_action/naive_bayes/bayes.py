from numpy import *


def load_data_set():
    posting_list = [['my', 'dog', 'has', 'flea', 'problems', 'help', 'please'],
                    ['maybe', 'not', 'take', 'him', 'to', 'dog', 'park', 'stupid'],
                    ['my', 'dalmation', 'is', 'so', 'cute', 'I', 'love', 'him'],
                    ['stop', 'posting', 'stupid', 'worthless', 'garbage'],
                    ['mr', 'licks', 'ate', 'my', 'steak', 'how', 'to', 'stop', 'him'],
                    ['quit', 'buying', 'worthless', 'dog', 'food', 'stupid']]
    class_vec = [0, 1, 0, 1, 0, 1]  # 1 is abusive, 0 not
    return posting_list, class_vec


def create_vocab_list(data_set):
    vocab_set = set([])
    for document in data_set:
        vocab_set |= set(document)
    return list(vocab_set)


def set_of_words_to_vector(vocab_list, input_set):
    return_vector = [0] * len(vocab_list)
    for token in input_set:
        if token in vocab_list:
            return_vector[vocab_list.index(token)] = 1
        else:
            print "the word: %s is not in my vocabulary!" % token
    return return_vector


def train_naive_bayes(trained_matrix, train_category):
    n = len(trained_matrix[0])
    p1_vector = ones(n)  # [1.0] * n, in case 0 value
    p0_vector = ones(n)  # [1.0] * n
    num1 = 2  # must be 2!! add [1,...,1] and [0,...,0]. then there will not be all 1s or 0s.
    num0 = 2
    for i in range(len(trained_matrix)):
        if 1 == train_category[i]:
            p1_vector += trained_matrix[i]
            num1 += 1
        else:
            p0_vector += trained_matrix[i]
            num0 += 1
    p1_vector = p1_vector / num1
    p0_vector = p0_vector / num0
    return p1_vector, p0_vector, float(num1) / (num1 + num0)


def classify(vect_to_classify, p1_vector, p0_vector, p1):
    p1_value = log(p1)
    p0_value = log(1 - p1)
    for i in range(len(vect_to_classify)):
        if 1 == vect_to_classify[i]:
            p1_value += log(p1_vector[i])
            p0_value += log(p0_vector[i])
        else:
            p1_value += log(1 - p1_vector[i])
            p0_value += log(1 - p0_vector[i])
    if p1_value > p0_value:
        return 1
    return 0


def testing_nb():
    list_of_posts, list_classes = load_data_set()
    my_vocab_list = create_vocab_list(list_of_posts)
    train_mat = []
    for postinDoc in list_of_posts:
        train_mat.append(set_of_words_to_vector(my_vocab_list, postinDoc))
    p0_v, p1_v, p1 = train_naive_bayes(array(train_mat), array(list_classes))
    test_entry = ['love', 'my', 'dalmation']
    this_doc = array(set_of_words_to_vector(my_vocab_list, test_entry))
    print test_entry, 'classified as: ', classify(this_doc, p0_v, p1_v, p1)
    test_entry = ['stupid', 'garbage']
    this_doc = array(set_of_words_to_vector(my_vocab_list, test_entry))
    print test_entry, 'classified as: ', classify(this_doc, p0_v, p1_v, p1)


def textParse(bigString):  # input is big string, #output is word list
    import re
    listOfTokens = re.split(r'\W*', bigString)
    return [tok.lower() for tok in listOfTokens if len(tok) > 2]


def spamTest():
    docList = [];
    classList = [];
    fullText = []
    for i in range(1, 26):
        wordList = textParse(open('data/spam/%d.txt' % i).read())
        docList.append(wordList)
        fullText.extend(wordList)
        classList.append(1)
        wordList = textParse(open('data/ham/%d.txt' % i).read())
        docList.append(wordList)
        fullText.extend(wordList)
        classList.append(0)
    vocabList = create_vocab_list(docList)  # create vocabulary
    trainingSet = range(50);
    testSet = []  # create test set
    for i in range(10):
        randIndex = int(random.uniform(0, len(trainingSet)))
        testSet.append(trainingSet[randIndex])
        del (trainingSet[randIndex])
    trainMat = [];
    trainClasses = []
    for docIndex in trainingSet:  # train the classifier (get probs) trainNB0
        trainMat.append(set_of_words_to_vector(vocabList, docList[docIndex]))
        trainClasses.append(classList[docIndex])
    p0V, p1V, pSpam = train_naive_bayes(array(trainMat), array(trainClasses))
    errorCount = 0
    for docIndex in testSet:  # classify the remaining items
        wordVector = set_of_words_to_vector(vocabList, docList[docIndex])
        if classify(array(wordVector), p0V, p1V, pSpam) != classList[docIndex]:
            errorCount += 1
            print "classification error", docList[docIndex]
    print 'the error rate is: ', float(errorCount) / len(testSet)
    # return vocabList,fullText


def calcMostFreq(vocabList, fullText):
    import operator
    freqDict = {}
    for token in vocabList:
        freqDict[token] = fullText.count(token)
    sortedFreq = sorted(freqDict.iteritems(), key=operator.itemgetter(1), reverse=True)
    return sortedFreq[:30]


# def localWords(feed1, feed0):
#     import feedparser
#     docList = [];
#     classList = [];
#     fullText = []
#     minLen = min(len(feed1['entries']), len(feed0['entries']))
#     for i in range(minLen):
#         wordList = textParse(feed1['entries'][i]['summary'])
#         docList.append(wordList)
#         fullText.extend(wordList)
#         classList.append(1)  # NY is class 1
#         wordList = textParse(feed0['entries'][i]['summary'])
#         docList.append(wordList)
#         fullText.extend(wordList)
#         classList.append(0)
#     vocabList = createVocabList(docList)  # create vocabulary
#     top30Words = calcMostFreq(vocabList, fullText)  # remove top 30 words
#     for pairW in top30Words:
#         if pairW[0] in vocabList: vocabList.remove(pairW[0])
#     trainingSet = range(2 * minLen);
#     testSet = []  # create test set
#     for i in range(20):
#         randIndex = int(random.uniform(0, len(trainingSet)))
#         testSet.append(trainingSet[randIndex])
#         del (trainingSet[randIndex])
#     trainMat = [];
#     trainClasses = []
#     for docIndex in trainingSet:  # train the classifier (get probs) trainNB0
#         trainMat.append(bagOfWords2VecMN(vocabList, docList[docIndex]))
#         trainClasses.append(classList[docIndex])
#     p0V, p1V, pSpam = trainNB0(array(trainMat), array(trainClasses))
#     errorCount = 0
#     for docIndex in testSet:  # classify the remaining items
#         wordVector = bagOfWords2VecMN(vocabList, docList[docIndex])
#         if classifyNB(array(wordVector), p0V, p1V, pSpam) != classList[docIndex]:
#             errorCount += 1
#     print 'the error rate is: ', float(errorCount) / len(testSet)
#     return vocabList, p0V, p1V
#
#
# def getTopWords(ny, sf):
#     import operator
#     vocabList, p0V, p1V = localWords(ny, sf)
#     topNY = [];
#     topSF = []
#     for i in range(len(p0V)):
#         if p0V[i] > -6.0: topSF.append((vocabList[i], p0V[i]))
#         if p1V[i] > -6.0: topNY.append((vocabList[i], p1V[i]))
#     sortedSF = sorted(topSF, key=lambda pair: pair[1], reverse=True)
#     print "SF**SF**SF**SF**SF**SF**SF**SF**SF**SF**SF**SF**SF**SF**SF**SF**"
#     for item in sortedSF:
#         print item[0]
#     sortedNY = sorted(topNY, key=lambda pair: pair[1], reverse=True)
#     print "NY**NY**NY**NY**NY**NY**NY**NY**NY**NY**NY**NY**NY**NY**NY**NY**"
#     for item in sortedNY:
#         print item[0]
for i in range(100):
    spamTest()
# testing_nb()
# matrix = [[1, 0, 0, 1], [0, 1, 1, 0], [1, 0, 1, 0]]
# category = [1, 0, 1]
# p1_vector, p0_vector, p1 = train_naive_bayes(matrix, category)
# print p1_vector, p0_vector, p1
