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
 * �������������滻��
 * ����"24322432|#{item3} hot&1:0.9;#{commonTag}"�����#{item3}�滻��ItemIds���itemId3��Ӧ��ֵ��
 * ��#{commonTag}�滻�������ļ����Զ����commonTag������
 */
public class TupleReplace implements TupleProcessor {
    private static final String REGEX = "#\\{\\w+}";//ƥ��#{name}�������ַ���
    private static final String REPLACE = "###";

    public List<String> process(List<String> originList, Map<String, String> customParams) {
        System.out.println("-------��ʼ�滻����--------");
        List<String> result = new ArrayList<String>();
        for (String originTuple : originList) {
            result.add(replaceParams(originTuple, customParams));
        }
        return result;
    }

    //ƥ��#{username}�����Ķ�������ȡ�������username�ַ���
    private String replaceParams(String originTuple, Map<String, String> customParams) {
        List<String> paramNames = new ArrayList<String>();

        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(originTuple);

        //����������ȡ����
        while (m.find()) {
            paramNames.add(originTuple.substring(m.start() + 2, m.end() - 1));
        }

        //û��ƥ��ģ���ֻ����ԭ�����ַ���
        if (paramNames.size() == 0) {
//            System.out.println("generate: " + originTuple);
            return originTuple;
        }
        //�����в����滻һ��
        String resultTuple = m.replaceAll(REPLACE);
        for (String paramName : paramNames) {
            resultTuple = resultTuple.replaceFirst(REPLACE, getParamValue(paramName, customParams));
        }
        System.out.println("replace: " + resultTuple);
        return resultTuple;
    }

    private String getParamValue(String paramName, Map<String, String> customParams) {
        //������Զ�������ֱ�ӷ���ֵ
        if (customParams.containsKey(paramName)) {
            return customParams.get(paramName);
        }

        //�������Ʒ�����ҵȣ���data�����ȡ����õĲ���
        String dataValue = dataParam(paramName);
        if (null != dataValue) {
            return dataValue;
        }

        //���϶����ǣ�����
        System.out.println("ERROR: #{" + paramName + "} �Ҳ���ƥ���ֵ");
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
