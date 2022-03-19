package com.hyb.service.impl;

import com.hyb.mapper.ItemMapper;
import com.hyb.pojo.Item;
import com.hyb.service.RecommendService;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 曼哈顿相似度
 *      UserSimilarity similarity = new org.apache.mahout.cf.taste.impl.similarity.CityBlockSimilarity(model);
 * 欧几里德相似度
 *      UserSimilarity similarity = new org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity(model);
 * 对数似然相似度
 *      UserSimilarity similarity = new org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity(model);
 * 斯皮尔曼相似度
 *      UserSimilarity similarity = new org.apache.mahout.cf.taste.impl.similarity.SpearmanCorrelationSimilarity(model);
 * Tanimoto 相似度
 *      UserSimilarity similarity = new org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity(model)
 * Cosine相似度
 *      UserSimilarity similarity = new org.apache.mahout.cf.taste.impl.similarity.UncenteredCosineSimilarity();
 */
@Service
public class RecommendServiceImpl implements RecommendService {
    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private DataModel dataModel;

    /**
     *
     * @param userId 当前用户id
     * @param howMany 推荐多少个
     * @return
     */
    @Override
    public List<Item> getRecommendItemsByUser(Long userId, int howMany) {
        List<Item> list = null;
        try {
            //计算相似度，相似度算法有很多种，采用基于皮尔逊相关性的相似度
            //相似度算法有很多种，欧几里得、皮尔逊等等
            UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
            //计算最近邻域，邻居有两种算法，基于固定数量的邻居和基于相似度的邻居，这里使用基于固定数量的邻居
            UserNeighborhood userNeighborhood = new NearestNUserNeighborhood(100, similarity, dataModel);
            //构建推荐器，基于用户的协同过滤推荐
            //协同过滤推荐有两种，分别是基于用户的和基于物品的，这里使用基于用户的协同过滤推荐
            Recommender recommender = new GenericUserBasedRecommender(dataModel, userNeighborhood, similarity);
            long start = System.currentTimeMillis();
            //推荐商品
            List<RecommendedItem> recommendedItemList = recommender.recommend(userId, howMany);
            List<Long> itemIds = new ArrayList<Long>();
            for (RecommendedItem recommendedItem : recommendedItemList) {
                System.out.println(recommendedItem);
                itemIds.add(recommendedItem.getItemID());
            }
            System.out.println("推荐出来的商品id集合"+itemIds);

            //根据商品id查询商品
            if(itemIds!=null &&itemIds.size()>0) {
                list = itemMapper.findAllByIds(itemIds);
            }else{
                list = new ArrayList<>();
            }
            System.out.println("推荐数量:"+list.size() +"耗时："+(System.currentTimeMillis()-start));
        } catch (TasteException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Item> getRecommendItemsByItem(Long userId, Long itemId, int howMany) {
        List<Item> list = null;
        try {
            //计算相似度，相似度算法有很多种，采用基于皮尔逊相关性的相似度
            ItemSimilarity itemSimilarity = new PearsonCorrelationSimilarity(dataModel);
            //4)构建推荐器，使用基于物品的协同过滤推荐
            GenericItemBasedRecommender recommender = new GenericItemBasedRecommender(dataModel, itemSimilarity);
            long start = System.currentTimeMillis();
            // 物品推荐相似度，计算两个物品同时出现的次数，次数越多任务的相似度越高。
            List<RecommendedItem> recommendedItemList = recommender.recommendedBecause(userId, itemId, howMany);
            //打印推荐的结果
            List<Long> itemIds = new ArrayList<Long>();
            for (RecommendedItem recommendedItem : recommendedItemList) {
                System.out.println(recommendedItem);
                itemIds.add(recommendedItem.getItemID());
            }
            System.out.println("推荐出来的商品id集合"+itemIds);

            //根据商品id查询商品
            if(itemIds!=null &&itemIds.size()>0) {
                list = itemMapper.findAllByIds(itemIds);
            }else{
                list = new ArrayList<>();
            }
            System.out.println("推荐数量:"+list.size() +"耗时："+(System.currentTimeMillis()-start));
        } catch (TasteException e) {
            e.printStackTrace();
        }
        return list;

    }
}