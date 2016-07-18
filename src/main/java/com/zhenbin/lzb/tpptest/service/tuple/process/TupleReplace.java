package com.zhenbin.lzb.tpptest.service.tuple.process;

import com.zhenbin.lzb.tpptest.model.ConstData;
import com.zhenbin.lzb.tpptest.model.data.Cids;
import com.zhenbin.lzb.tpptest.model.data.ItemIds;
import com.zhenbin.lzb.tpptest.model.data.SellerIds;
import com.zhenbin.lzb.tpptest.model.data.UserIds;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhenbin.lzb on 2016/7/15.
 * <p>
 * 用来作变量的替换。
 * 例如"24322432|#{item3} hot&1:0.9;#{commonTag}"，会把#{item3}替换成ItemIds里的itemId3对应的值，
 * 把#{commonTag}替换成配置文件里自定义的commonTag参数。
 */
public class TupleReplace implements TupleProcessor {
    private static final String REGEX = "#\\{\\w+}";//匹配#{name}这样的字符串
    private static final String REPLACE = "###";

    public List<String> process(List<String> originList, Map<String, String> customParams) {
        System.out.println("-------开始替换参数--------");
        List<String> result = new ArrayList<String>();
        for (String originTuple : originList) {
            result.add(replaceParams(originTuple, customParams));
        }
        return result;
    }

    //匹配#{username}这样的东西，提取出里面的username字符串
    private String replaceParams(String originTuple, Map<String, String> customParams) {
        List<String> paramNames = new ArrayList<String>();

        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(originTuple);

        //将参数名提取出来
        while (m.find()) {
            paramNames.add(originTuple.substring(m.start() + 2, m.end() - 1));
        }

        //没有匹配的，就只返回原来的字符串
        if (paramNames.size() == 0) {
//            System.out.println("generate: " + originTuple);
            return originTuple;
        }
        //将所有参数替换一下
        String resultTuple = m.replaceAll(REPLACE);
        for (String paramName : paramNames) {
            resultTuple = resultTuple.replaceFirst(REPLACE, getParamValue(paramName, customParams));
        }
        System.out.println("replace: " + resultTuple);
        return resultTuple;
    }

    private String getParamValue(String paramName, Map<String, String> customParams) {
        //如果在自定义参数里，直接返回值
        if (customParams.containsKey(paramName)) {
            return customParams.get(paramName);
        }

        //如果是商品、卖家等，从data包里获取定义好的参数
        String dataValue = dataParam(paramName);
        if (null != dataValue) {
            return dataValue;
        }

        //以上都不是，报错
        System.out.println("ERROR: #{" + paramName + "} 找不到匹配的值");
        return "#####ERROR#####";
    }

    private String dataParam(String paramName) {
        if (0 == paramName.indexOf(ConstData.ITEM)) {
            String indexStr = paramName.substring(ConstData.ITEM.length());
            int index = Integer.valueOf(indexStr);
            return ItemIds.getItemId(index);
        }
        if (0 == paramName.indexOf(ConstData.SELLER)) {
            String indexStr = paramName.substring(ConstData.SELLER.length());
            int index = Integer.valueOf(indexStr);
            return SellerIds.getSellerId(index);
        }
        if (0 == paramName.indexOf(ConstData.USER)) {
            String indexStr = paramName.substring(ConstData.USER.length());
            int index = Integer.valueOf(indexStr);
            return UserIds.getUserId(index);
        }
        if (0 == paramName.indexOf(ConstData.CATEGORY)) {
            String indexStr = paramName.substring(ConstData.CATEGORY.length());
            int index = Integer.valueOf(indexStr);
            return Cids.getCid(index);
        }
        return null;
    }
}
