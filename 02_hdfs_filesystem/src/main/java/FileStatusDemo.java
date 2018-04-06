import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;

/**
 * 获取文件元信息和路径信息
 * @author fmi110
 * @Date 2018/4/6 21:43
 */
public class FileStatusDemo {
    public static void main(String[] args) throws IOException {
        // args = {"hdfs://m01:8020/user", "hdfs://m01:8020/user/fmi110"}
        String uri = args[0];
        FileSystem fs = FileSystem.get(URI.create(uri),new Configuration());

        Path[] paths = new Path[args.length];
        for(int i=0;i<paths.length;i++) {
            paths[i] = new Path(args[i]);
        }
        // 获取一组路径的 fileStauts
        FileStatus[] status = fs.listStatus(paths);
        // FileUtil 将元信息转换为路径信息
        Path[] filePaths = FileUtil.stat2Paths(status);
        Arrays.stream(filePaths).forEach(System.out::println);
    }
}
