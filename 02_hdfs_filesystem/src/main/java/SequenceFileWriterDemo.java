import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;

import java.io.IOException;
import java.net.URI;


/**
 * 顺序文件的写操作
 *
 * @author fmi110
 * @Date 2018/4/8 22:28
 */
public class SequenceFileWriterDemo {
    public static final String[] DATA = {
            "窗前明月光",
            "疑是地上霜",
            "举头望明月",
            "低头思故乡"
    };

    public static void main(String[] args) throws IOException {
        String outPath = "00TT/squencefile.seq";

        Configuration conf = new Configuration();
        FileSystem    fs   = FileSystem.get(URI.create(outPath), conf);
        Path          path = new Path(outPath);

        IntWritable key   = new IntWritable(); // 顺序文件的key
        Text        value = new Text();        // 顺序文件的value

        SequenceFile.Writer writer = null;
        try {
            writer = SequenceFile.createWriter(fs, conf, path, key.getClass(), value.getClass());
            for (int i = 0; i < 100; i++) {
                key.set(i);
                value.set(DATA[i % DATA.length]);
                System.out.printf("[%s]\t%s\t%s\n", writer.getLength(), key.get(), value.toString());

                writer.append(key, value); // 内容写入顺序文件
            }
        } finally {
            IOUtils.closeStream(writer);
        }

    }
}
