package com.zhenbin.lzb.tpptest.service.tuple.source.igraph;

import com.taobao.igraph.client.core.IGraphClient;
import com.taobao.igraph.client.core.IGraphClientBuilder;
import com.taobao.igraph.client.model.AtomicQuery;
import com.taobao.igraph.client.model.KeyList;
import com.taobao.igraph.client.model.QueryResult;

import java.util.Map;

/**
 * Created by zhenbin.lzb on 2016/7/18.
 */
public class IgraphClientService {
    private static IGraphClient iGraphClient;

    static {
        // daily的配置
        // 具体配置见http://site.alibaba.net/ups_dev/igraph_document/igraph_client/java/java_client_init.html
        init("test", "ups.daily.proxy.taobao.org", "ups.daily.swift.tbsite.net:8080");
    }

    public static IGraphClient getIGraphClient() {
        return iGraphClient;
    }

    //igraph插入
    public static void update(String table, IgraphTuple igraphTuple) {
        update(table, igraphTuple.getPkey(), igraphTuple.getSkey(), igraphTuple.getFields());
    }

    public static void update(String table, String pkey, String skey, Map<String, String> valueMap) {
        if (null == skey) {
            iGraphClient.update(table, pkey, valueMap);
        } else {
            iGraphClient.update(table, pkey, skey, valueMap);
        }
    }

    //igraph查询
    public static QueryResult readIgraph(String tableName,int returnNum, String pkey, String... skeys) throws Exception {
        KeyList keyList;
        if (null == skeys || skeys.length == 0) {
            keyList = new KeyList(pkey);
        } else {
            keyList = new KeyList(pkey, skeys);
        }
        AtomicQuery query = new AtomicQuery(tableName, keyList);
        query.setRange(0, returnNum);
        QueryResult result = iGraphClient.search(query);
        return result;
    }

    //igraph删除
    public void doDeleteJob(String tableName, String deleteKey) throws Exception {
        iGraphClient.delete(tableName, deleteKey);
//        System.out.println("key:" + deleteKey + "对应数据已删除");
    }

    private static void init(String src, String searchDomain, String updateDomain) {
        IGraphClientBuilder builder = IGraphClientBuilder.create();
        builder.setSocketTimeout(3000);
        builder.setConnectTimeout(3000);
        builder.setConnectionRequestTimeout(3000);
        builder.setSearchCluster("DEFAULT");
        builder.setUpdateCluster("DEFAULT");
        builder.setMaxConnTotal(1024);
        builder.setMaxConnPerRoute(200);
        iGraphClient = builder.build(src, searchDomain, updateDomain);
    }

}
