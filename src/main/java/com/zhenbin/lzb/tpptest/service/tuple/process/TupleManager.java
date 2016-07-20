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
        tupleProcessors.add(new TupleGenerator());  //����������¼
        tupleProcessors.add(new TupleReplace());    //�滻��¼��Ĳ�������
    }

    public List<String> process(List<String> originList, Map<String, String> customParams) {
        for (TupleProcessor tupleProcessor : tupleProcessors) {
            originList = tupleProcessor.process(originList, customParams);
        }
        return originList;
    }

    /**
     * ��classpath�µĲ�������·�������������Ϳ���������ݵ����ɺͲ��롣
     */
    public void insertData(String testDataClassPath) {
        JSONObject jsonObject = JsonObjectUtil.testData(testDataClassPath);
        String customParamStr = jsonObject.getString(ConstData.TEST_DATA_CUSTOM_PARAM);
        //��ȡ�Զ������
        Map<String, String> customMap = JSON.parseObject(customParamStr, new TypeReference<Map<String, String>>() {
        });

        JSONArray datasources = jsonObject.getJSONArray(ConstData.TEST_DATA_DATA_SOURCE);
        //��ÿ������Դ��������
        for (Object datasource : datasources) {
            String sourceName = ((JSONObject) datasource).getString(ConstData.TEST_DATA_SOURCE_NAME);
            //�������ݵ��������ͣ����û�����õĻ�Ĭ��Ϊigraph
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
            //��������igraph�ճ�����
            for (String tuple : tuples) {
                DataSourceService.insert(sourceName, tuple, sourceType);
                System.out.println("insert: " + tuple);
            }
        }
    }

}
