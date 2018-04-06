package com.fmi110.mapper;

import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @author fmi110
 * @Description: 输入的行数据包含了年份和天气信息, 通过map提取出来
 * @Date 2018/4/6 11:13
 */
@Slf4j
public class MaxTemperatureMapper
        extends Mapper<LongWritable, Text, Text, IntWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        log.info("----------------- map ---------------");

        long   keyL = key.get();
        String line = value.toString();
        String year = line.substring(15, 19);
        log.info("输入 key = {},year = {}", keyL, year);

        int airTemperature;
        if (line.charAt(87) == '+') { // parseInt doesn't like leading plus signs
            airTemperature = Integer.parseInt(line.substring(88, 92));
        } else {
            airTemperature = Integer.parseInt(line.substring(87, 92));
        }
        String quality = line.substring(92, 93);
        if (quality.matches("[01459]")) {
            context.write(new Text(year), new IntWritable(airTemperature));
        }

    }
}
