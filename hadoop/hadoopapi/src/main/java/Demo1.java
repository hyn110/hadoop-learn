
import com.fmi110.bean.IntPair;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * @author fmi110
 * @Date 2018/6/4 21:42
 */
@Slf4j
public class Demo1 {

    static class Mapper1 extends Mapper<LongWritable, Text, IntPair, IntWritable> {
        /**
         * Called once for each key/value pair in the input split. Most applications
         * should override this, but the default is the identity function.
         *
         * @param key
         * @param value
         * @param context
         */
        @Override
        protected void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            String[] split = value.toString()
                                  .split(" ");
            IntPair intPair = new IntPair(new IntWritable(Integer.valueOf(split[0])
                                                                 .intValue()), new IntWritable(Integer.valueOf(split[1])
                                                                                                      .intValue()));
            System.out.println(intPair+" ====== "+intPair.getSecond());
            context.write(intPair,intPair.getSecond());
        }
    }

    static class Reduce1 extends Reducer<IntPair, IntWritable, IntWritable, IntWritable> {

        @Override
        protected void reduce(IntPair key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            System.out.println("reduce .... ");
            String a = "";
            while (values.iterator()
                         .hasNext()) {
                a = a + values.iterator().next()+",";
            }
            System.out.println(a);
            context.write(key.getFirst(), key.getSecond());
        }
    }

    /**
     * 按照第一个数进行分区
     */
    static class FirstPartition extends Partitioner<IntPair, NullWritable> {

        @Override
        public int getPartition(IntPair intPair, NullWritable nullWritable, int numPartitions) {
            System.out.println("numPartitions = "+numPartitions);
            return Math.abs(intPair.getFirst()
                                   .hashCode() * 127) % numPartitions;
        }
    }

    /**
     * 第一列升序 , 第二列降序
     */
    static class KeyComparator extends WritableComparator {
        protected KeyComparator() {
            super(IntPair.class, true);
        }

        @Override
        public int compare(WritableComparable a, WritableComparable b) {
            IntPair p1 = (IntPair) a;
            IntPair p2 = (IntPair) b;

            int comp = p1.getFirst()
                         .get() - p2.getFirst()
                                    .get();
            if (comp == 0) {
                return -(p1.getSecond()
                           .get() - p2.getSecond()
                                      .get());
            }
            return comp;
        }
    }

    static class GroupCompartor extends WritableComparator {
        protected GroupCompartor() {
            super(IntPair.class, true);
        }

        @Override
        public int compare(WritableComparable a, WritableComparable b) {
            IntPair p1 = (IntPair) a;
            IntPair p2 = (IntPair) b;

            return -(p1.getSecond()
                       .get() - p2.getSecond()
                                  .get());
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {


        if (args.length < 2) {
            log.error("必须指定输入文件和输出路径...");
            System.exit(-1);
        }
        //1 创建配置文件 conf
        //2 创建 job , 指定输入文件类型
        //3 设置 mapper , 并设置k-v类型
        //4 设置 reducer , 并设置k-v类型
        //5 设置应用的输入
        //6 设置应用的输出
        //7 运行

        org.apache.hadoop.conf.Configuration conf = new org.apache.hadoop.conf.Configuration();
        conf.set("fs.defaultFS", "file:///");

        FileSystem fs = FileSystem.get(conf);
        fs.delete(new Path(args[1]), true);

        Job job = Job.getInstance(conf);
        job.setInputFormatClass(TextInputFormat.class);
        job.setJobName("Demo Job");
        job.setJarByClass(Demo1.class);

        job.setMapperClass(Mapper1.class);
        job.setMapOutputKeyClass(IntPair.class);
        job.setMapOutputValueClass(IntWritable.class);

//        job.setCombinerClass(MaxTemperatureReducer.class);
        job.setReducerClass(Reduce1.class);
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(IntWritable.class);

        job.setPartitionerClass(FirstPartition.class);
        job.setSortComparatorClass(KeyComparator.class);
        job.setGroupingComparatorClass(GroupCompartor.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : -1);
    }
}
