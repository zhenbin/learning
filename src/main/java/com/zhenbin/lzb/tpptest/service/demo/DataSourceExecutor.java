package com.zhenbin.lzb.tpptest.service.demo;

import com.taobao.igraph.client.model.*;
import com.taobao.recommendplatform.protocol.datasource.igraph.Updater;
import com.taobao.recommendplatform.protocol.exception.RecommendPlatformException;
import com.taobao.recommendplatform.protocol.service.IGraphService;
import com.taobao.recommendplatform.protocol.service.ServiceFactory;

import java.io.UnsupportedEncodingException;

/**
 * Created by zhenbin.lzb on 2016/7/12.
 */
public class DataSourceExecutor {
    public static void main(String[] args) throws RecommendPlatformException, UnsupportedEncodingException {
        IGraphService iGraphService = ServiceFactory.getIGraphService();
        // �򵥵�kv��ѯ
        AtomicQuery query = new AtomicQuery("TPP_lingxia_igraph_con4", new KeyList("4")); // TPP_lingxia_igraph_con4Ϊͨ��TPP������iGraph��������Ч������Ķ��壺
        // AtomicQuery query = new TppDsAtomicQuery("lingxia_igraph_con4", new KeyList("4")); // �����iGraph����������Դ������ʹ�����ֶ���AtomicQuery�ķ�ʽ��ֻ��Ҫ֪������Դ���ƣ�������Ķ�Ӧ��iGraph����
        query.setRange(0, 50); // ���÷��ص������������������ã�����෵��10����¼

        QueryResult queryResult = iGraphService.search(query);
        SingleQueryResult singleQueryResult = queryResult.getSingleQueryResult(); //����query��Ӧ�������
        for (MatchRecord matchRecord : singleQueryResult.getMatchRecords()) { // ѭ������ÿһ�в�ѯ���
            System.out.println(matchRecord.getFieldValue("weight", "UTF-8")); // �����ֶζ��ǲ���String���ͣ���ֵ����Ҫ������ת�������ں��֣���Ҫָ������
        }

        /**
         * �����updater���Դ�һ��pkey��Ҳ���Դ�pkey��skey��field���ǳ���key֮��������ֶΡ�
         */
        Updater updater1 = new Updater("4", "0").addField("params", "test0"); // �ڹ��캯����ָ��pkey��skey��ȷ��һ����¼��Ȼ����ÿһ��field��ֵ
        iGraphService.update("lingxia_igraph_noadd", updater1);
    }
}
