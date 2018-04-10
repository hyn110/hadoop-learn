import com.fmi110.mapper.MaxTemperatureMapper;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.junit.Test;

import java.io.IOException;


/**
 * MRUnit 的 mapper 测试
 * @author fmi110
 * @Date 2018/4/10 22:45
 */
public class MapperTest {
    @Test
    public void testMapper() throws IOException {
        Text value = new Text("0043012650999991949032418004+62300+010750FM-12+048599999V0202701N00461220001CN0500001N9+00781+99999999999") ;
        new MapDriver<LongWritable,Text,Text,IntWritable>()
                .withMapper(new MaxTemperatureMapper())
                .withInput(new LongWritable(0),value)
                .withOutput(new Text("1949"),new IntWritable(78))
                .runTest();
    }
}
