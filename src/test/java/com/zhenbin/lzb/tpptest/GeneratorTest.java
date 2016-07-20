package com.zhenbin.lzb.tpptest;

import com.zhenbin.lzb.tpptest.service.tuple.process.TupleGenerator;
import com.zhenbin.lzb.tpptest.service.tuple.process.TupleManager;
import javafx.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Created by zhenbin.lzb on 2016/7/15.
 */
public class GeneratorTest {
    @Test
    public void intTest() {
        String input = "24322432|{item[0-6]} reserve_price&1:20[9-15]0.0";

        TupleGenerator tupleGenerator = new TupleGenerator();
        Assert.assertEquals(tupleGenerator.generateInts(input).size(), 7);
    }
    @Test
    public void int1Test() {
        String input = "24322432|{item2} reserve_price&1:20110.0";

        TupleGenerator tupleGenerator = new TupleGenerator();
        List<String> result = tupleGenerator.generateInts(input);
        Assert.assertEquals(result.size(), 1);
        Assert.assertEquals(result.get(0), input);
    }

    @Test
    public void tupleManagerTest() {
        TupleManager tupleManager = new TupleManager();
        tupleManager.insertData("test_data.json");
    }
    @Test
    public void tupleManagse11rTest() {
        TupleManager tupleManager = new TupleManager();
        tupleManager.insertData("test_data1.json");
    }

}
