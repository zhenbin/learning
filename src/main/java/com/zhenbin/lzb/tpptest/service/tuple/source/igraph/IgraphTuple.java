package com.zhenbin.lzb.tpptest.service.tuple.source.igraph;

import lombok.Data;

import java.util.Map;

/**
 * Created by zhenbin.lzb on 2016/7/15.
 */
@Data
public class IgraphTuple {
    private String pkeyName;
    private String pkey;
    private String skeyName;
    private String skey;
    private Map<String, String> fields;
}
