import com.fmi110.mapper.MaxTemperatureMapper;
import com.fmi110.reducer.MaxTemperatureReducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


/**
 * @author fmi110
 * @Date 2018/4/8 21:31
 */
@Slf4j
public class MaxTemperatureWithCompression {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            log.error("输出参数少于2...");
            System.exit(-1);
        }

        Configuration conf = new Configuration() ;
        Job           job = Job.getInstance(conf);
        job.setJarByClass(MaxTemperatureWithCompression.class);

        FileInputFormat.addInputPath(job,new Path(args[0]));
        FileOutputFormat.setOutputPath(job,new Path(args[1]));

        // 设置输出使用的压缩算法
        FileOutputFormat.setOutputCompressorClass(job, GzipCodec.class);

        job.setMapperClass(MaxTemperatureMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setReducerClass(MaxTemperatureReducer.class);
        job.setCombinerClass(MaxTemperatureReducer.class);

        System.exit(job.waitForCompletion(true)?0:1);
    }
}
