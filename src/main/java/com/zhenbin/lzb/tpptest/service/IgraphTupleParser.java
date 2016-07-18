package com.zhenbin.lzb.tpptest.service;

import com.alibaba.fastjson.JSONObject;
import com.zhenbin.lzb.tpptest.model.ConstData;
import com.zhenbin.lzb.tpptest.model.IgraphTuple;
import com.zhenbin.lzb.util.JsonObjectUtil;
import javafx.util.Pair;

import java.util.*;

/**
 * Created by zhenbin.lzb on 2016/7/15.
 */
public class IgraphTupleParser {
    /**
     * 将"34 lam male"这种记录转成igraphTuple类型
     *
     * @param tuple
     * @return
     */
    public IgraphTuple parseIgraphTuple(String sourceName, String tuple) {
        List<String> keyAndFields = new ArrayList<String>(Arrays.asList(tuple.split(" ")));
        IgraphTuple igraphTuple = parseIgraphTuple(sourceName);

        //set pkeyValue
        String pkeyKey = igraphTuple.getPkey().getKey();
        igraphTuple.setPkey(new Pair<String, String>(pkeyKey, keyAndFields.get(0)));

        //set skeyValue
        String skeyKey = igraphTuple.getSkey().getKey();
        int keyNums = 1;
        if (null != skeyKey) {
            keyNums = 2;
            igraphTuple.setSkey(new Pair<String, String>(skeyKey, keyAndFields.get(1)));
        }

        //set fields
        List<Pair<String, String>> fields = igraphTuple.getFields();
        for (int i = 0; i < fields.size(); i++) {
            String key = fields.get(i).getKey();
            fields.set(i, new Pair<String, String>(key, keyAndFields.get(i + keyNums)));
        }

        return igraphTuple;
    }

    //返回sourceName对应的元数据
    public IgraphTuple parseIgraphTuple(String sourceName) {
        JSONObject igraphMeta = JsonObjectUtil.IgraphMeta(sourceName);

        IgraphTuple igraphTuple = new IgraphTuple();
        //set pkeyKey
        String pkeyName = igraphMeta.getString(ConstData.IGRAPH_PKEY);
        igraphTuple.setPkey(new Pair<String, String>(pkeyName, null));

        //set skey
        String skeyName = igraphMeta.getString(ConstData.IGRAPH_SKEY);
        igraphTuple.setSkey(new Pair<String, String>(skeyName, null));

        //set fields
        String fieldsMetaStr = igraphMeta.getString(ConstData.IGRAPH_VALUE);
        List<String> fieldsMeta = new ArrayList<String>(Arrays.asList(fieldsMetaStr.split(",")));
        List<Pair<String, String>> fields = new ArrayList<Pair<String, String>>();
        for (int i = 0; i < fieldsMeta.size(); i++) {
            fields.add(new Pair<String, String>(fieldsMeta.get(i), null));
        }

        return igraphTuple;
    }
}
