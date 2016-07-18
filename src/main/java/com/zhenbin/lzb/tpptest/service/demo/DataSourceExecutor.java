package com.zhenbin.lzb.tpptest.service.demo;

import com.taobao.igraph.client.model.*;
import com.taobao.recommendplatform.protocol.datasource.igraph.Updater;
import com.taobao.recommendplatform.protocol.exception.RecommendPlatformException;
import com.taobao.recommendplatform.protocol.service.IGraphService;
import com.taobao.recommendplatform.protocol.service.ServiceFactory;

import java.io.UnsupportedEncodingException;

/**
 * Created by zhenbin.lzb on 2016/7/12.
 */
public class DataSourceExecutor {
    public static void main(String[] args) throws RecommendPlatformException, UnsupportedEncodingException {
        IGraphService iGraphService = ServiceFactory.getIGraphService();
        // 简单的kv查询
        AtomicQuery query = new AtomicQuery("TPP_lingxia_igraph_con4", new KeyList("4")); // TPP_lingxia_igraph_con4为通过TPP创建的iGraph表名，等效于下面的定义：
        // AtomicQuery query = new TppDsAtomicQuery("lingxia_igraph_con4", new KeyList("4")); // 如果是iGraph创建的数据源，建议使用这种定义AtomicQuery的方式，只需要知道数据源名称，无需关心对应的iGraph表名
        query.setRange(0, 50); // 设置返回的最大结果数，如果不设置，则最多返回10条记录

        QueryResult queryResult = iGraphService.search(query);
        SingleQueryResult singleQueryResult = queryResult.getSingleQueryResult(); //单条query对应单条结果
        for (MatchRecord matchRecord : singleQueryResult.getMatchRecords()) { // 循环处理每一行查询结果
            System.out.println(matchRecord.getFieldValue("weight", "UTF-8")); // 返回字段都是采用String类型，数值型需要做类型转换；对于汉字，需要指定编码
        }

        /**
         * 这里的updater可以传一个pkey，也可以传pkey和skey。field就是除了key之外的其它字段。
         */
        Updater updater1 = new Updater("4", "0").addField("params", "test0"); // 在构造函数中指定pkey、skey以确定一条记录，然后定义每一个field的值
        iGraphService.update("lingxia_igraph_noadd", updater1);
    }
}
