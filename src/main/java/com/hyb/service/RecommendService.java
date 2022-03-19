package com.hyb.service;

import com.hyb.pojo.Item;

import java.util.List;

public interface RecommendService {
    //基于用户的商品推荐
    List<Item> getRecommendItemsByUser(Long userId, int howMany);
    //基于内容的商品推荐
    List<Item> getRecommendItemsByItem(Long userId, Long itemId, int howMany);
}
