import org.apache.hadoop.fs.FsUrlStreamHandlerFactory;
import org.apache.hadoop.io.IOUtils;

import java.io.InputStream;
import java.net.URL;

/**
 * @author fmi110
 * @Date 2018/4/6 20:02
 */
public class URLCat {
    static {
        // 必须设置,否则URL无法识别 hdfs 协议!!!每个jvm只能调用一次该方法,
        // 所以这个方式读取文件有局限!!!
        URL.setURLStreamHandlerFactory(new FsUrlStreamHandlerFactory());
    }
    public static void main(String[] args) throws Exception {
        InputStream in = null;
      try{
          // 连接linux服务器时
          // in = new URL("hdfs://m01:8020/user/fmi110/1.txt").openStream();
          // 读取windows本地文件时  args[0] = "file://C:/input/sample.txt"
          in = new URL(args[0]).openStream();
          IOUtils.copyBytes(in,System.out,1024*4,false);
      }finally {
        IOUtils.closeStream(in);
      }
    }
}
