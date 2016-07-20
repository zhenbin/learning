package com.zhenbin.lzb.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhenbin.lzb.tpptest.common.ConstData;

/**
 * Created by zhenbin.lzb on 2016/7/15.
 */
public class JsonObjectUtil {

    /**
     * 返回这样的一个jsonobject
     * {
     "sourceName": "lingxia_test_now",
     "pkey": "id",
     "value": "scene_owner,scene_name,debug_layers,gmt_create"
     }
     * @param sourceName
     * @return
     */
    public static JSONObject IgraphMeta(String sourceName) {
        String jsonStr = ReadFileUtil.readAsClassPath(ConstData.IGRAPH_CLASS_PATH, ConstData.IGRAPH_CHARSET);
        JSONObject igraphConfig = JSON.parseObject(jsonStr);
        JSONArray dataSource = igraphConfig.getJSONArray(ConstData.IGRAPH_DATA_SOURCE);
        for (Object config : dataSource) {
            JSONObject configJson = (JSONObject) config;
            if (sourceName.equals(configJson.getString(ConstData.IGRAPH_SOURCE_NAME))) {
                return configJson;
            }
        }
        System.out.println("ERROR : 找不到对应的数据源配置");
        return null;
    }

    /**
     * 返回这样的一个jsonobject
     * {
     "customParam": {
        "solutionId": "41897",
     },
     "dataSource": [
         {
         "sourceName": "crmx_item_advanced_tag_score",
         "tuples": [
             "24322432|#{item[0-1]} reserve_price&1:20[0-1]0.0"
             ]
         }
        ]
     }
     * @param testDataClassPath
     * @return
     */
    public static JSONObject testData(String testDataClassPath) {
        String jsonStr = ReadFileUtil.readAsClassPath(testDataClassPath, ConstData.TEST_DATA_CHARSET);
        return JSON.parseObject(jsonStr);
    }
}
