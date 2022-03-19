package com.hyb.config;

import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataModelConfig {

    @Bean
    public DataModel getMySQLDataModel(DataSource dataSource) {
        //参数1：mysql数据源信息，参数2：表名，参数3：用户列字段，参数4：商品列字段，参数5：偏好值字段，参数6：时间戳
        //userId表示用户ID itemId表示为itemID  preference表示用户对这个item的喜好程度
        JDBCDataModel dataModel=new MySQLJDBCDataModel(dataSource,"user_pianhao_data1","uid","pid","val", "time");
        return dataModel;
    }
}
