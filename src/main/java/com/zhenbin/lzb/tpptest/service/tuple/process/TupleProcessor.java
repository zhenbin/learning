package com.zhenbin.lzb.tpptest.service.tuple.process;

import java.util.List;
import java.util.Map;

/**
 * Created by zhenbin.lzb on 2016/7/18.
 */
public interface TupleProcessor {
    List<String> process(List<String> originList, Map<String, String> customParams);
}
