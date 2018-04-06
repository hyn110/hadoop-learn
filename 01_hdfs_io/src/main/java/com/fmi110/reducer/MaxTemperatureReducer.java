package com.fmi110.reducer;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @author fmi110
 * @Description: 获取最大的天气值
 * @Date 2018/4/6 11:39
 */
@Slf4j
public class MaxTemperatureReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {
        log.info("-------------- reduce ------------");
        log.info("key = {}",key.toString());

        int maxValue = Integer.MIN_VALUE;

        for (IntWritable v : values) {
            log.info("max = {},v = {}",maxValue,v.get());
            maxValue = Math.max(maxValue, v.get());
        }
        // 输出最后的结果
        context.write(key,new IntWritable(maxValue));
    }
}
