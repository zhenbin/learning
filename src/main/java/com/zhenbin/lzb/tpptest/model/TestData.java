package com.zhenbin.lzb.tpptest.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zhenbin.lzb.util.ReadFileUtil;

import java.io.IOException;

/**
 * Created by zhenbin.lzb on 2016/7/12.
 */
public class TestData {
    /**
     * �����Զ���Ĳ�������solutionId, sellerId��
     */
    private JSONObject customParam;
    /**
     * ����ԴҪ����ļ�¼
     */
    private JSONArray dataSource;

    /**
     * �ټ���testCases�ȶ���
     */
    //private JsonArray testCases;

    private static final String CHARSET = "GBK";
    private static final String CUSTOM_PARAM = "customParam";
    private static final String DATA_SOURCE = "dataSource";
    private static final String DATA_SOURCE_SOURCE_NAME = "sourceName";
    private static final String DATA_SOURCE_TUPLES = "tuples";

    //���������ļ��ڹ���������·������"src\\test\\java\\..."
    public static TestData getTestDataFromFilePath(String configPath) throws IOException {
        String jsonStr = ReadFileUtil.readAsFilePath(configPath, CHARSET);
        return getTestDataFromJsonString(jsonStr);
    }

    private static TestData getTestDataFromJsonString(String jsonStr) {
        JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        TestData testData = new TestData();
        testData.customParam = jsonObject.getJSONObject(CUSTOM_PARAM);
        testData.dataSource = jsonObject.getJSONArray(DATA_SOURCE);
        return testData;
    }


    /**
     * ������getter and setter
     */
    public JSONObject getCustomParam() {
        return customParam;
    }

    public void setCustomParam(JSONObject customParam) {
        this.customParam = customParam;
    }

    public JSONArray getDataSource() {
        return dataSource;
    }

    public void setDataSource(JSONArray dataSource) {
        this.dataSource = dataSource;
    }
}
