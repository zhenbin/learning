package com.zhenbin.lzb.tpptest.service.tuple.process;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhenbin.lzb on 2016/7/15.
 * <p>
 * ����������������䡣
 * ����"24322432|#{item[0-1]} reserve_price&1:20[1-2]0.0",
 * �����ɣ�
 * "24322432|#{item0} reserve_price&1:2010.0"
 * "24322432|#{item1} reserve_price&1:2020.0"
 */
public class TupleGenerator implements TupleProcessor {
    private static final String REGEX = "\\[\\d+-\\d+]";//ƥ��[0-23]�������ַ���
    private static final String REPLACE = "###";

    /**
     * ��������[0-23]���ַ������飬���������generateInts(String originTuple);
     *
     * @param originList
     * @return
     */
    public List<String> process(List<String> originList, Map<String, String> customParams) {
        System.out.println("-------��ʼָ��������--------");
        List<String> result = new ArrayList<String>();
        for (String originTuple : originList) {
            result.addAll(generateInts(originTuple));
        }
        return result;
    }

    /**
     * ����[0-23]�����ļ�¼��������24����¼����λ�ö�Ӧ��ֵΪ0��1������23��
     * ����ж�����������䣬��ͬ�����ڵ�ֵ����������ͬ�����򷵻�null��
     *
     * @param originTuple
     * @return
     */
    public List<String> generateInts(String originTuple) {
        List<Integer> indexStart = new ArrayList<Integer>();
        List<Integer> indexEnd = new ArrayList<Integer>();

        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(originTuple);

        //������������ȡ����
        while (m.find()) {
            addStartAndEnd(indexStart, indexEnd, originTuple.substring(m.start(), m.end()));
        }

        List<String> result = new ArrayList<String>();
        //û��ƥ��ģ���ֻ����ԭ�����ַ���
        if (indexStart.size() == 0) {
            System.out.println("generator: " + originTuple);
            result.add(originTuple);
            return result;
        }

        if (!checkInterval(indexStart, indexEnd)) {
            System.out.println(originTuple + " �����С��һ�£�");
            return null;
        }

        //�����������ֵ�滻һ��
        originTuple = m.replaceAll(REPLACE);
        int interval = indexEnd.get(0) - indexStart.get(0);
        int regexNum = indexStart.size();
        for (int i = 0; i <= interval; i++) {
            String resultTuple = originTuple;
            for (int j = 0; j < regexNum; j++) {
                resultTuple = resultTuple.replaceFirst(REPLACE, String.valueOf(indexStart.get(j) + i));
            }
            System.out.println("generator: " + resultTuple);
            result.add(resultTuple);
        }
        return result;
    }

    //��������ǲ���һ����
    private boolean checkInterval(List<Integer> indexStart, List<Integer> indexEnd) {
        int interval = indexEnd.get(0) - indexStart.get(0);
        for (int i = 1; i < indexStart.size(); i++) {
            if (interval != indexEnd.get(i) - indexStart.get(i)) {
                return false;
            }
        }
        return true;
    }

    //��[32-5432]������32 �� 5432
    private void addStartAndEnd(List<Integer> indexStart, List<Integer> indexEnd, String input) {
        int splitPosition = input.indexOf('-');
        indexStart.add(Integer.valueOf(input.substring(1, splitPosition)));
        indexEnd.add(Integer.valueOf(input.substring(splitPosition + 1, input.length() - 1)));
    }

}
