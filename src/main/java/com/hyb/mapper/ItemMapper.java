package com.hyb.mapper;


import com.hyb.pojo.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ItemMapper {
    public List<Item> findAllByIds(@Param("Ids") List<Long> Ids);
}
