import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

/**
 * 实现mapper 端的 join 操作
 *
 * @author fmi110
 * @Date 2018/6/5 20:04
 */
public class MappJoinDemo {

    public static void main(String[] args)
            throws IOException, URISyntaxException, ClassNotFoundException, InterruptedException {
        if (args.length < 2) {
            System.err.printf("输出参数错误....需要两个输入");
            System.exit(-1);
        }

        Configuration conf = new Configuration();

        FileSystem.get(conf)
                  .delete(new Path(args[1]), true);

        Job job = Job.getInstance(conf);
        job.setJobName("Mapper Join");
        job.setJarByClass(MappJoinDemo.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setMapperClass(JoinMapper.class);
        job.setMapOutputKeyClass(NullWritable.class);
        job.setMapOutputValueClass(Text.class);



//        job.setReducerClass(. class);
//        job.setOutputKeyClass(.class);
//        job.setOutputValueClass(.class);

        job.addCacheFile(new URI("file:/c:/00/product.txt"));

        job.setNumReduceTasks(0);
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        System.exit(job.waitForCompletion(true) ? 0 : -1);

    }

    static class JoinMapper extends Mapper<LongWritable, Text, NullWritable, Text> {

        HashMap<String, String> products = new HashMap<>();

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            /**
             * 从工作目录下读取商品文件文件
             */

            URI[] files = context.getCacheFiles();
            for (URI file : files) {
                System.out.println(file.getPath()+"==========="+file.getRawPath());
            }

//            File file = new File(" product.txt");
//            System.out.println(String.format("mapper.setup..输入文件路径 : %s ",file.getAbsolutePath()));
            BufferedReader br   = new BufferedReader(new InputStreamReader(new FileInputStream(files[0].getPath())));
            String         line = "";
            while (StringUtils.isNotEmpty(line = br.readLine())) {
                String[] split = line.split(",");
                products.put(split[0], line.substring(split[0].length()+1));
                System.out.println(String.format("%s --- %s", split[0], line.substring(split[0].length()+1)));
            }

        }


        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = parseOrder(value.toString());
            context.write(NullWritable.get(),new Text(line));
        }

        private String parseOrder(String order) {
            System.out.println(String.format("map ... order --- %s", order));
            String[] split = order.split(",");
            String   pid   = split[1];
            return order.concat(",")
                        .concat(products.get(pid));
        }
    }

}
