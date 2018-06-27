import com.fmi110.reducer.MaxTemperatureReducer;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

/**
 * @author fmi110
 * @Date 2018/4/14 9:13
 */
public class ReducerTest {
    @Test
    public void testReducer() throws IOException {
        new ReduceDriver<Text, IntWritable,Text,IntWritable>()
                .withReducer(new MaxTemperatureReducer())
                .withInput(new Text("1988"), Arrays.asList(new IntWritable(10),new IntWritable(5)))
                .withOutput(new Text("1988"),new IntWritable(10))
                .runTest();
    }
}
