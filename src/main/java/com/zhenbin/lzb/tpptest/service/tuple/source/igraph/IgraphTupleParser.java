package com.zhenbin.lzb.tpptest.service.tuple.source.igraph;

import com.alibaba.fastjson.JSONObject;
import com.zhenbin.lzb.tpptest.common.ConstData;
import com.zhenbin.lzb.util.JsonObjectUtil;

import java.util.*;

/**
 * Created by zhenbin.lzb on 2016/7/15.
 */
public class IgraphTupleParser {
    /**
     * 将"34 lam male"这种记录转成igraphTuple类型
     * @param tuple
     * @return
     */
    public static IgraphTuple parseIgraphTuple(String table, String tuple) {
        List<String> keyAndFields = new ArrayList<String>(Arrays.asList(tuple.split(" ")));
        JSONObject igraphMeta = JsonObjectUtil.IgraphMeta(table);

        IgraphTuple igraphTuple = new IgraphTuple();
        //set pkey
        String pkeyName = igraphMeta.getString(ConstData.IGRAPH_PKEY);
        igraphTuple.setPkeyName(pkeyName);
        igraphTuple.setPkey(keyAndFields.get(0));

        //set skey
        String skeyName = igraphMeta.getString(ConstData.IGRAPH_SKEY);
        int keyNums = 1;
        if (null != skeyName) {
            keyNums = 2;
            igraphTuple.setSkeyName(skeyName);
            igraphTuple.setSkey(keyAndFields.get(1));
        }

        //set fields
        String fieldsMetaStr = igraphMeta.getString(ConstData.IGRAPH_VALUE);
        List<String> fieldsMeta = new ArrayList<String>(Arrays.asList(fieldsMetaStr.split(",")));
        Map<String, String> fields = new HashMap<String, String>();
        for (int i = 0; i < fieldsMeta.size(); i++) {
            fields.put(fieldsMeta.get(i), keyAndFields.get(i + keyNums));
        }
        igraphTuple.setFields(fields);

        return igraphTuple;
    }
}
