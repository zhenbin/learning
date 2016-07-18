package com.zhenbin.lzb.tpptest.service.tuple.process;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhenbin.lzb on 2016/7/15.
 * <p>
 * 用来生成批量的语句。
 * 例如"24322432|#{item[0-1]} reserve_price&1:20[1-2]0.0",
 * 会生成：
 * "24322432|#{item0} reserve_price&1:2010.0"
 * "24322432|#{item1} reserve_price&1:2020.0"
 */
public class TupleGenerator implements TupleProcessor {
    private static final String REGEX = "\\[\\d+-\\d+]";//匹配[0-23]这样的字符串
    private static final String REPLACE = "###";

    /**
     * 解析带有[0-23]的字符串数组，具体解析见generateInts(String originTuple);
     *
     * @param originList
     * @return
     */
    public List<String> process(List<String> originList, Map<String, String> customParams) {
        System.out.println("-------开始指产生数据--------");
        List<String> result = new ArrayList<String>();
        for (String originTuple : originList) {
            result.addAll(generateInts(originTuple));
        }
        return result;
    }

    /**
     * 解析[0-23]这样的记录，会生成24条记录，该位置对应的值为0，1，……23。
     * 如果有多个这样的区间，则不同区间内的值个数必须相同，否则返回null。
     *
     * @param originTuple
     * @return
     */
    public List<String> generateInts(String originTuple) {
        List<Integer> indexStart = new ArrayList<Integer>();
        List<Integer> indexEnd = new ArrayList<Integer>();

        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(originTuple);

        //将所有区间提取出来
        while (m.find()) {
            addStartAndEnd(indexStart, indexEnd, originTuple.substring(m.start(), m.end()));
        }

        List<String> result = new ArrayList<String>();
        //没有匹配的，就只返回原来的字符串
        if (indexStart.size() == 0) {
            System.out.println("generator: " + originTuple);
            result.add(originTuple);
            return result;
        }

        if (!checkInterval(indexStart, indexEnd)) {
            System.out.println(originTuple + " 区间大小不一致！");
            return null;
        }

        //将所有区间的值替换一下
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

    //检查区间是不是一样大
    private boolean checkInterval(List<Integer> indexStart, List<Integer> indexEnd) {
        int interval = indexEnd.get(0) - indexStart.get(0);
        for (int i = 1; i < indexStart.size(); i++) {
            if (interval != indexEnd.get(i) - indexStart.get(i)) {
                return false;
            }
        }
        return true;
    }

    //将[32-5432]解析出32 和 5432
    private void addStartAndEnd(List<Integer> indexStart, List<Integer> indexEnd, String input) {
        int splitPosition = input.indexOf('-');
        indexStart.add(Integer.valueOf(input.substring(1, splitPosition)));
        indexEnd.add(Integer.valueOf(input.substring(splitPosition + 1, input.length() - 1)));
    }

}
