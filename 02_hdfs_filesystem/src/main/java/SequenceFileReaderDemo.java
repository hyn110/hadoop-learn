import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.util.ReflectionUtils;

import java.io.IOException;
import java.net.URI;


/**
 * 顺序文件的读操作
 *
 * @author fmi110
 * @Date 2018/4/8 22:28
 */
public class SequenceFileReaderDemo {


    public static void main(String[] args) throws IOException {
        String inPath = "00TT/squencefile.seq";

        Configuration conf = new Configuration();
        FileSystem    fs   = FileSystem.get(URI.create(inPath), conf);
        Path          path = new Path(inPath);

        SequenceFile.Reader reader = null;
        try {
            reader = new SequenceFile.Reader(fs, path, conf);
            Writable key   = (Writable) ReflectionUtils.newInstance(reader.getKeyClass(), conf);
            Writable value = (Writable) ReflectionUtils.newInstance(reader.getValueClass(), conf);

            long position = reader.getPosition();
            while (reader.next(key, value)) {
                String syncSeen = reader.syncSeen() ? "*" : ""; // 是否是同步点

                System.out.printf("[%s%s]\t%s\t%s\n", position, syncSeen, key, value);

                position = reader.getPosition(); // 获取下一次起始的位置
            }
        } finally {
            IOUtils.closeStream(reader);
        }

    }
}
