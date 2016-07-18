package com.zhenbin.lzb.tpptest.model.data;

import com.google.common.collect.Lists;

import java.util.List;

public class UserIds {
    public static final String user_id_key = "2011615130";//nick:tdkxf2010
    public static final String user_id1 = "2021492655";//nick:nandie001_seller
    public static final String user_id2 = "2040781823";//nick:xlj006
    public static final String user_id3 = "2050520231";//nick:relationrecommendtest09
    public static final String user_id4 = "2020593162";//nick:mercury_webxtest11
    public static final String user_id5 = "2020799494";//nick:nandie002
    public static final String user_id6 = "181567966";//nick:gushe00
    public static final String user_id7 = "179698311";//nick:mytaobao_minyi10
    public static final String user_id8 = "179374584";//nick:tbtestuser33
    public static final String user_id9 = "179284375";//nick:抽风啊04
    public static final String user_id10 = "3605511145";//nick:test_guesslike_04
    public static final String user_id11 = "3605511144";//nick=test_guesslike_03
    public static final String user_id12 = "3605511146";//nick:test_guesslike_05
    public static final String user_id13 = "3613746957";//nick:test_guesslike_06
    public static final String user_id14 = "179284421";//nick:抽风啊10
    public static final String user_id16 = "179284420";//nick:抽风啊09
    public static final String user_id15 = "3594192703";//nick:snsdongtaiguangchang001
    public static final String user_id17 = "3596530353";//nick:licaibaoxian001
    public static final String user_id18 = "3596530354";//nick:licaibaoxian002
    //FIXME::test_guesslike**和 test_rt_guesslike**用于实时推荐相关的，涉及多张表的数据清理，请调试完及时还原数据
    public static final String user_id19 = "3605140059";//nick=test_guesslike_01
    public static final String user_id20 = "3605510711";//nick=test_guesslike_02
    public static final String user_id21 = "3614209585";//nick:test_guesslike_10
    public static final String user_id22 = "3614206334";//nick:test_guesslike_08
    public static final String user_id23 = "3635990766";//nick=test_rt_guesslike_01
    public static final List<String> uids;

    static {
        uids = Lists.newArrayList();
        uids.add(user_id1);
        uids.add(user_id2);
        uids.add(user_id3);
        uids.add(user_id4);
        uids.add(user_id5);
        uids.add(user_id6);
        uids.add(user_id7);
        uids.add(user_id8);
        uids.add(user_id9);
        uids.add(user_id10);
        uids.add(user_id11);
        uids.add(user_id12);
        uids.add(user_id13);
        uids.add(user_id14);
        uids.add(user_id15);
        uids.add(user_id16);
        uids.add(user_id17);
        uids.add(user_id18);
    }

    //返回user_id{index}
    public static String getUserId(int index) {
        return uids.get(index - 1);
    }

    //从上到下获取count个uids
    public static List<String> getuids(int count) {
        if (count > 17) {
            throw new Error("获取的uesrId个数不能大于17个，如果需要更多，请自行往本方法里添加更多！");
        }
        return uids.subList(0, count);
    }

    //获取第个startIndex到endIndex个uids
    public static List<String> getuids(int startIndex, int endIndex) {
        if (endIndex > 17) {
            throw new Error("获取的uesrId个数不能大于17个，如果需要更多，请自行往本方法里添加更多！");
        }
        return uids.subList(startIndex, endIndex);
    }

}
