package com.zhenbin.lzb.tpptest;

import com.taobao.igraph.client.core.IGraphClient;
import com.taobao.igraph.client.core.IGraphClientBuilder;
import com.taobao.igraph.client.model.AtomicQuery;
import com.taobao.igraph.client.model.KeyList;
import com.taobao.igraph.client.model.MatchRecord;
import com.taobao.igraph.client.model.QueryResult;
import com.zhenbin.lzb.tpptest.service.tuple.source.igraph.IgraphClientService;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhenbin.lzb on 2016/7/20.
 */
public class IgraphClientUtilTest {
    @Test
    public void test() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("itemids", "323,332");
        IgraphClientService.update("TPP_qnzf_rec_s2c2i_kv", "3", null, map);
        System.out.println("fds");
    }
}
