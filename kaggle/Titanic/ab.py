# -*- coding: UTF-8 -*-
# Imports

# pandas
import matplotlib.pyplot as plt
import pandas as pd
import seaborn as sn
sn.set_style('whitegrid')

# load数据
titanic_df = pd.read_csv("data/train.csv")
test_df = pd.read_csv("data/test.csv")

# print titanic_df.head()

'''
打印表格的信息，可看到
有多少列
有多少空字段
各列数据类型
'''
# titanic_df.info()
# print "-------------------------"
# test_df.info()

#舍弃没有用的列
titanic_df = titanic_df.drop(['PassengerId', 'Name',  'Ticket'], axis=1)
test_df = test_df.drop(['Name', 'Ticket'], axis=1)
# titanic_df.info()




# only in titanic_df, fill the two missing values with the most occurred value, which is "S".
titanic_df["Embarked"] = titanic_df["Embarked"].fillna("S")

# plot
sn.factorplot('Embarked','Survived', data=titanic_df,size=4,aspect=3)

fig, (axis1,axis2,axis3) = plt.subplots(1,3,figsize=(15,5))

# sns.factorplot('Embarked',data=titanic_df,kind='count',order=['S','C','Q'],ax=axis1)
# sns.factorplot('Survived',hue="Embarked",data=titanic_df,kind='count',order=[1,0],ax=axis2)
sn.countplot(x='Embarked', data=titanic_df, ax=axis1)
sn.countplot(x='Survived', hue="Embarked", data=titanic_df, order=[1,0], ax=axis2)

# group by embarked, and get the mean for survived passengers for each value in Embarked
embark_perc = titanic_df[["Embarked", "Survived"]].groupby(['Embarked'],as_index=False).mean()
sn.barplot(x='Embarked', y='Survived', data=embark_perc,order=['S','C','Q'],ax=axis3)

# Either to consider Embarked column in predictions,
# and remove "S" dummy variable,
# and leave "C" & "Q", since they seem to have a good rate for Survival.

# OR, don't create dummy variables for Embarked column, just drop it,
# because logically, Embarked doesn't seem to be useful in prediction.

embark_dummies_titanic  = pd.get_dummies(titanic_df['Embarked'])
embark_dummies_titanic.drop(['S'], axis=1, inplace=True)

embark_dummies_test  = pd.get_dummies(test_df['Embarked'])
embark_dummies_test.drop(['S'], axis=1, inplace=True)

titanic_df = titanic_df.join(embark_dummies_titanic)
test_df    = test_df.join(embark_dummies_test)

titanic_df.drop(['Embarked'], axis=1,inplace=True)
test_df.drop(['Embarked'], axis=1,inplace=True)
