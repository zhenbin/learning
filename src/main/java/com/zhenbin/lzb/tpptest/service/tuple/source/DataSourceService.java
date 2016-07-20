package com.zhenbin.lzb.tpptest.service.tuple.source;

import com.zhenbin.lzb.tpptest.common.ConstData;
import com.zhenbin.lzb.tpptest.service.tuple.source.igraph.IgraphClientService;
import com.zhenbin.lzb.tpptest.service.tuple.source.igraph.IgraphTuple;
import com.zhenbin.lzb.tpptest.service.tuple.source.igraph.IgraphTupleParser;

/**
 * Created by zhenbin.lzb on 2016/7/20.
 */
public class DataSourceService {
    public static void insert(String table, String tuple, String sourceType) {
        if (ConstData.DATA_SOURCE_IGRAPH.equals(sourceType)) {
            insertIgraphData(table, tuple);
        }
        //��������Դ���͵Ĵ������������
    }

    private static void insertIgraphData(String table, String tuple) {
        IgraphTuple igraphTuple = IgraphTupleParser.parseIgraphTuple(table, tuple);
        IgraphClientService.update(table, igraphTuple);
    }
}
