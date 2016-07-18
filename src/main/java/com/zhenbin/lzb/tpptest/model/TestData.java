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
     * 保存自定义的参数，如solutionId, sellerId等
     */
    private JSONObject customParam;
    /**
     * 数据源要插入的记录
     */
    private JSONArray dataSource;

    /**
     * 再加上testCases等东西
     */
    //private JsonArray testCases;

    private static final String CHARSET = "GBK";
    private static final String CUSTOM_PARAM = "customParam";
    private static final String DATA_SOURCE = "dataSource";
    private static final String DATA_SOURCE_SOURCE_NAME = "sourceName";
    private static final String DATA_SOURCE_TUPLES = "tuples";

    //传入配置文件在工程里的相对路径，如"src\\test\\java\\..."
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
     * 以下是getter and setter
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
