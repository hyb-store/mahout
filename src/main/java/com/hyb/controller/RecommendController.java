package com.hyb.controller;

import com.hyb.pojo.Item;
import com.hyb.service.RecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RecommendController {

    @Autowired
    private RecommendService recommendService;

    /**
     * 基于用户的推荐
     *
     * @param userId 用户id
     * @param num    推荐数量
     * @return
     */
    @RequestMapping("recommendByUser")
    public List<Item> getRecommendItemsByUser(Long userId, int num) {
        List<Item> items = recommendService.getRecommendItemsByUser(userId, num);
        return items;
    }

    /**
     * 基于内容的推荐
     *
     * @param userId 用户id
     * @param itemId 商品id
     * @param num    推荐数量
     * @return
     */
    @RequestMapping("recommendByItem")
    public List<Item> getRecommendItemsByItem(Long userId, Long itemId, int num) {
        List<Item> items = recommendService.getRecommendItemsByItem(userId, itemId, num);
        return items;
    }
}