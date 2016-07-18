package com.zhenbin.lzb.tpptest.model;

import javafx.util.Pair;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by zhenbin.lzb on 2016/7/15.
 */
@Data
public class IgraphTuple {
    private Pair<String, String> pkey;
    private Pair<String, String> skey;
    private List<Pair<String, String>> fields;
}
