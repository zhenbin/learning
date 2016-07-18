package com.zhenbin.lzb.tpptest.service;

import com.taobao.igraph.client.core.IGraphClient;
import com.taobao.igraph.client.core.IGraphClientBuilder;

/**
 * Created by zhenbin.lzb on 2016/7/18.
 */
public class IgraphClientHelper {
    private static IGraphClient client;

    static {
        // daily的配置
        // 具体配置见http://site.alibaba.net/ups_dev/igraph_document/igraph_client/java/java_client_init.html
        init("test", "ups.daily.proxy.taobao.org", "ups.daily.swift.tbsite.net:8080");
    }

    public static IGraphClient getClient() {
        return client;
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
        client = builder.build(src, searchDomain, updateDomain);
    }

}
