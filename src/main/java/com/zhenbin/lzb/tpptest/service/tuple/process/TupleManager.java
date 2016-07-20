package com.zhenbin.lzb.tpptest.service.tuple.process;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.zhenbin.lzb.tpptest.common.ConstData;
import com.zhenbin.lzb.tpptest.service.tuple.source.DataSourceService;
import com.zhenbin.lzb.util.JsonObjectUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhenbin.lzb on 2016/7/18.
 */
public class TupleManager {
    private List<TupleProcessor> tupleProcessors;

    public TupleManager() {
        tupleProcessors = new ArrayList<TupleProcessor>();
        tupleProcessors.add(new TupleGenerator());  //批量产生记录
        tupleProcessors.add(new TupleReplace());    //替换记录里的参数变量
    }

    public List<String> process(List<String> originList, Map<String, String> customParams) {
        for (TupleProcessor tupleProcessor : tupleProcessors) {
            originList = tupleProcessor.process(originList, customParams);
        }
        return originList;
    }

    /**
     * 将classpath下的测试数据路径传给它，它就可以完成数据的生成和插入。
     */
    public void insertData(String testDataClassPath) {
        JSONObject jsonObject = JsonObjectUtil.testData(testDataClassPath);
        String customParamStr = jsonObject.getString(ConstData.TEST_DATA_CUSTOM_PARAM);
        //获取自定义参数
        Map<String, String> customMap = JSON.parseObject(customParamStr, new TypeReference<Map<String, String>>() {
        });

        JSONArray datasources = jsonObject.getJSONArray(ConstData.TEST_DATA_DATA_SOURCE);
        //对每个数据源生成数据
        for (Object datasource : datasources) {
            String sourceName = ((JSONObject) datasource).getString(ConstData.TEST_DATA_SOURCE_NAME);
            //测试数据的数据类型，如果没有设置的话默认为igraph
            String sourceType = ((JSONObject) datasource).getString(ConstData.TEST_DATA_SOURCE_TYPE);
            if (null == sourceType) {
                sourceType = ConstData.DATA_SOURCE_IGRAPH;
            }
            String listStr = ((JSONObject) datasource).getString(ConstData.TEST_DATA_TUPLES);
            List<String> keysAndfields = JSON.parseObject(listStr, new TypeReference<List<String>>() {
            });
            System.out.println("==========================");
            System.out.println("| " + sourceName);
            System.out.println("==========================");
            List<String> tuples = process(keysAndfields, customMap);
            //逐条插入igraph日常环境
            for (String tuple : tuples) {
                DataSourceService.insert(sourceName, tuple, sourceType);
                System.out.println("insert: " + tuple);
            }
        }
    }

}
