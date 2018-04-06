import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * 写入文件,并带进度提示
 * @author fmi110
 * @Date 2018/4/6 21:23
 */
public class FileCopyWithProgress {
    public static void main(String[] args) throws IOException, InterruptedException {
        String localFile = args[0];
        String dst = args[1];

        InputStream in = new BufferedInputStream(new FileInputStream(localFile));
        FileSystem  fs = FileSystem.get(URI.create(dst), new Configuration(),"fmi110");
        // fs.create() 会自动创建父级目录!!!
        FSDataOutputStream out = fs.create(new Path(dst), new Progressable() {
            @Override
            public void progress() {
                System.out.print(".");
            }
        });
        IOUtils.copyBytes(in,out,4096,true);
    }
}
