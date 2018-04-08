import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionCodecFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * 根据输入文件名的拓展名推断codec 并解压文件,输出到控制台
 *
 * @author fmi110
 * @Date 2018/4/8 20:52
 */
@Slf4j
public class FileDecompressor {
    public static void main(String[] args) throws IOException {
        String        uri  = args[0];
        Configuration conf = new Configuration();
        FileSystem    fs   = FileSystem.get(URI.create(uri), conf);

        Path                    inputPath = new Path(uri);
        CompressionCodecFactory factory   = new CompressionCodecFactory(conf);
        CompressionCodec        codec     = factory.getCodec(inputPath);

        if (null == codec) {
            log.error("输入的文件后缀名有错...path = {}", uri);
            System.exit(-1);
        }

        // 获取输出文件名
        String outputUri = CompressionCodecFactory.removeSuffix(uri, codec.getDefaultExtension());

        InputStream  in  = null;
//        OutputStream out = null;
        try {
            in = codec.createInputStream(fs.open(inputPath));
//            out = fs.create(new Path(outputUri));
            IOUtils.copyBytes(in,System.out,4096,false);
        }finally {
            IOUtils.closeStream(in);
//            IOUtils.closeStream(out);
        }
    }
}
