# 基于mahout的推荐系统

### 1.mahout介绍

Mahout 是 Apache Software Foundation（ASF）旗下的一个开源的分布式机器学习算法的算法库，使用了Taste来提高协同过滤算法的实现，它是一个基于Java实现的**可扩展的，高效的**推荐引擎，提供一些可扩展的机器学习领域经典算法的实现。

同时mahout也是一个强大的数据挖掘工具，旨在帮助开发人员更加方便快捷地创建智能应用程序。Taste不仅仅只适用于Java应用程序，它可以作为内部服务器的一个组件以HTTP和Web Service的形式向外界提供推荐的逻辑。Mahout另外一个大特点是具有基于hadoop的实现，可以把算法运行在hadoop之上，运用MapReduce模式大大提高算法处理能力。

Mahout包含了许多算法实现：

- 聚类算法：K-means（K均值算法）、Canopy（Canopy聚类）、Hierarchical（层次聚类）等。
- 分类算法：Logistic Regression（逻辑回归）、Bayesian（贝叶斯）、SVM（支持向量机）、Perceptron（感知器算法）等。
- 回归算法：Locally Weighted Linear Regression（局部加权线性回归）等。
- 计算相似性算法：Non-distributed recommenders（Non-distributed recommenders）、Non-distributed recommenders（欧几里得距离算法）、CosineMeasureSimilarity（余弦计算）、SpearmanCorrelationSimilarity（斯皮尔曼相关系数）
- 推荐算法：GenericUserBasedRecommender（基于用户的推荐）、GenericItemBasedRecommender（基于内容的推荐）、SlopeOneRecommender（SlopeOne算法）、SVDRecommender （SVD算法）、TreeClusteringRecommender（树形聚类的推荐算法）。

### 2.Mahout是用来干嘛的？

- 推荐引擎：服务商或网站会根据你过去的行为为你推荐书籍、电影或文章。
- 聚类：Google news使用聚类技术通过标题把新闻文章进行分组，从而按照逻辑线索来显示新闻，而并非给出所有新闻的原始列表。
- 分类：雅虎邮箱基于用户以前对正常邮件和垃圾邮件的报告，以及电子邮件自身的特征，来判别到来的消息是否是垃圾邮件。

 ### 3. 协同过滤实现过程

#### **3.1 收集用户偏好数据**

​     要从用户的行为和偏好中发现规律，并基于此给予推荐，如何收集用户的偏好信息成为系统推荐效果最基础的决定因素。用户有很多方式向系统提供自己的偏好信息，而且不同的应用也可能大不相同，通用的用户行为有评分，投票，转发，保存，书签，标记，评论，点击流，页面停留时间，是否购买等。以上行为常见的有两种组合方式：方式1：根据查询或者购买分组，给用户显示时，提示查看过该商品的用户还查看过什么什么商品，购买过该商品的用户还购买过什么什么商品。方式2：根据不同的行为加权，比如购买了该商品权重高，查看权重低。

偏好数据的来源一般有：

- 数据库或者缓存：可以收集到用户的订单信息，购物车信息，关注，评论，收藏等信息，能直观的反应出用户的喜好数据。
- 日志信息：通过flume组件收集用户从打开网站，浏览过什么商品，页面停留多久这些可以收集到用户点击流数据。
- 第三方数据平台：与第三方平台合作拿到用户在其他平台的偏好数据。

#### **3.2数据减噪与归一处理**

​     减噪的目的是去除误操作，归一的目的是通过加权操作，比如刚才的行为划分1234类，权重也是从0.1到0.4不等，这样就可以把各个行为数据统一在一定范围中，使得最终的喜好值更加精确。

#### **3.3 算出相似的物品或者用户**

​     常用的算法基于用户UserCF和基于内容ItemCF的推荐

- UserCF：推荐和此用户相似的用户行为过的商品给该用户，比如，张三买过苹果、香蕉、橘子、梨等物品，李四只买过香蕉，王五买过香蕉、橘子、梨、火龙果。张三和李四相似度是1，张三和王五相似度是3。则基于UserCF则会给张三推荐相似度高的用户王五，产生行为过的商品火龙果给张三。

- ItemCF：推荐和此用户历史上行为过商品的相似商品推荐给该用户，比如张三以前买过笔记本电脑，则基于ItemCF则会把和笔记本电脑相似度高的其他商品推荐给张三。

​     两者区别：UserCF本质是基于用户对物品的偏好找到相邻的用户，然后将邻居用户喜欢的推荐给当前用户,适用于用户较少的情况，如果用户比较多计算量太大，侧重于跟当前用户相似的其他用户的行为，ItemCF本质基于用户对物品的偏好找到相似的物品，然后依据用户的历史偏好。推荐相似的物品给他，适用于商品较少的情况，更偏向于用户的历史行为。

#### **3.4 将相似商品推荐给用户**

​     算法推荐出来id以后，在根据id查询出商品其他信息展示给用户。

### 4.Mahout协同过滤算法

Mahout使用了Taste来提高协同过滤算法的实现，它是一个基于Java实现的可扩展的，高效的推荐引擎。Taste既实现了最基本的基于用户的和基于内容的推荐算法，同时也提供了扩展接口，使用户可以方便的定义和实现自己的推荐算法。同时，Taste不仅仅只适用于Java应用程序，它可以作为内部服务器的一个组件以HTTP和Web Service的形式向外界提供推荐的逻辑。Taste的设计使它能满足企业对推荐引擎在性能、灵活性和可扩展性等方面的要求。

Taste主要包括以下几个接口：

- DataModel 是用户喜好信息的抽象接口，它的具体实现支持从任意类型的数据源抽取用户喜好信息。Taste 默认提供 JDBCDataModel 和 FileDataModel，分别支持从数据库和文件中读取用户的喜好信息。
- UserSimilarity 和 ItemSimilarity 。UserSimilarity 用于定义两个用户间的相似度，它是基于协同过滤的推荐引擎的核心部分，可以用来计算用户的“邻居”，这里我们将与当前用户口味相似的用户称为他的邻居。ItemSimilarity 类似的，计算Item之间的相似度。
- UserNeighborhood 用于基于用户相似度的推荐方法中，推荐的内容是基于找到与当前用户喜好相似的邻居用户的方式产生的。UserNeighborhood 定义了确定邻居用户的方法，具体实现一般是基于 UserSimilarity 计算得到的。
- Recommender 是推荐引擎的抽象接口，Taste 中的核心组件。程序中，为它提供一个 DataModel，它可以计算出对不同用户的推荐内容。实际应用中，主要使用它的实现类 GenericUserBasedRecommender 或者 GenericItemBasedRecommender，分别实现基于用户相似度的推荐引擎或者基于内容的推荐引擎。
- RecommenderEvaluator ：评分器。
- RecommenderIRStatsEvaluator ：搜集推荐性能相关的指标，包括准确率、召回率等等。







参考

> [基于mahout的推荐系统 上](https://blog.csdn.net/qq_40208605/article/details/105838555?spm=1001.2014.3001.5502)
>
> [基于mahout的推荐系统 下](https://blog.csdn.net/qq_40208605/article/details/105840101)
>
> [Mahout介绍和简单应用](https://www.cnblogs.com/ahu-lichang/p/7073836.html)