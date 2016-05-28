package com.zhenbin.lzb.mapred;

import com.aliyun.odps.OdpsException;
import com.aliyun.odps.data.Record;
import com.aliyun.odps.data.TableInfo;
import com.aliyun.odps.mapred.JobClient;
import com.aliyun.odps.mapred.MapperBase;
import com.aliyun.odps.mapred.ReducerBase;
import com.aliyun.odps.mapred.conf.JobConf;
import com.aliyun.odps.mapred.utils.InputUtils;
import com.aliyun.odps.mapred.utils.OutputUtils;
import com.aliyun.odps.mapred.utils.SchemaUtils;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by zhenbin.lzb on 2016/5/26.
 */
public class WordCount {

    /**
     * MapperBase: 用户自定义的Map函数需要继承自此类。
     * 处理输入表的记录对象，加工处理成键值对集合输出到Reduce阶段，或者不经过 Reduce阶段直接输出结果记录到结果表。
     * 不经过Reduce阶段而 直接输出计算结果的作业，也可称之为Map-Only作业。
     */
    public static class WordCountMapper extends MapperBase {
        //Record类的对象表示ODPS表中一条记录
        private Record word;
        private Record one;

        //在Map阶段开始时，map方法之前调用。
        @Override
        //TaskContext 是MapperBase及ReducerBase多个成员函数的输入参数之一。 含有任务运行的上下文信息。
        public void setup(TaskContext context) throws IOException {
            //生成用来记录key的记录，只有一个word:string字段，在main函数里设置的
            word = context.createMapOutputKeyRecord();
            //生成用来记录value的记录，只有一个count:bigint字段，在main函数里设置的
            one = context.createMapOutputValueRecord();
            one.set(new Object[]{1L});
            System.out.println("TaskID:" + context.getTaskID().toString());
        }

        //map方法，处理输入表的记录record。
        @Override
        public void map(long recordNum, Record record, TaskContext context) throws IOException {
            for (int i = 0; i < record.getColumnCount(); i++) {
                word.set(new Object[] {record.get(i).toString()});
                //Map时写记录到中间结果
                context.write(word, one);
            }
        }
    }
    /*
     * ReducerBase: 用户自定义的Reduce函数需要继承自此类。对与一个键(Key) 关联的一组数值集(Values)进行归约计算。
     */
    public static class WordCountCombiner extends ReducerBase {
        private Record count;

        @Override
        public void setup(TaskContext context) throws IOException {
            count = context.createMapOutputValueRecord();
        }

        @Override
        public void reduce(Record key, Iterator<Record> values, TaskContext context) throws IOException {
            long c = 0;
            while (values.hasNext()) {
                Record val = values.next();
                c += (Long) val.get(0);
            }
            count.set(0, c);
            //Map时写记录到中间结果，这里仍是在Map阶段
            context.write(key, count);
        }
    }



    public static class WordCountReducer extends ReducerBase {
        private Record result;

        @Override
        public void setup(TaskContext context) throws IOException {
            //创建默认输出表的记录对象
            result = context.createOutputRecord();
        }

        @Override
        public void reduce(Record key, Iterator<Record> values, TaskContext context) throws IOException {
            long count = 0;
            while (values.hasNext()) {
                Record val = values.next();
                count += (Long) val.get(0);
            }
            result.set(0, key.get(0));
            result.set(1, count);
            //写记录到默认输出，用于Reduce端写出数据
            context.write(result);
        }
    }

    public static void main(String[] args) throws OdpsException {
        if (args.length != 2) {
            System.err.println("Usage: WordCount <in_table> <out_table>");
            System.exit(2);
        }

        //jobConf: 描述一个MapReduce任务的配置，通常在main函数中定义JobConf对象，然后通过JobClient提交作业给ODPS服务。
        JobConf jobConf = new JobConf();

        jobConf.setMapperClass(WordCountMapper.class);
        //设置作业的combiner。在Map端运行，作用类似于单个Map 对本地的相同Key值做Reduce
        jobConf.setCombinerClass(WordCountCombiner.class);
        jobConf.setReducerClass(WordCountReducer.class);

        //设置Mapper输出到Reducer的Key属性
        jobConf.setMapOutputKeySchema(SchemaUtils.fromString("word:string"));
        //设置Mapper输出到Reducer的Value属性
        jobConf.setMapOutputValueSchema(SchemaUtils.fromString("count:bigint"));

        //添加表table到任务输入，可以被调用多次 ，新加入的表以append方式添加到输入队列中。
        InputUtils.addTable(TableInfo.builder().tableName(args[0]).build(), jobConf);
        //添加表table到任务输出，可以被调用多次 ，新加入的表以append方式添加到输出队列中。
        OutputUtils.addTable(TableInfo.builder().tableName(args[1]).build(), jobConf);

        //JobClient: 用于提交和管理作业，提交方式包括阻塞(同步)方式及非阻塞 (异步)方式。runJob为同步
        JobClient.runJob(jobConf);

    }
}
