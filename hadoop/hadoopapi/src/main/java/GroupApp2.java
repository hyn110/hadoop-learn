/**
sort1	1
sort2	3
sort2	77
sort2	54
sort1	2
sort6	22
sort6	221
sort6	20
 * 要求使用现在的TwoIntWritable，第一列相同的时候，输出第二列的最大值
 *
 */

//--------------------------------------------------

/**
 * 输出结果是
 实际效果：
sort1   1,2
sort2   3,54,77
sort6   20,22,221

 */
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class GroupApp2 {
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, GroupApp2.class.getSimpleName());
		job.setJarByClass(GroupApp2.class);

		FileSystem fs = FileSystem.get(conf);
		fs.delete(new Path(args[1]), true);

		//数据来自哪里
		FileInputFormat.setInputPaths(job, args[0]);
		
		//使用我的mapper
		job.setMapperClass(SortMapper.class);
		//指明<k2,v2>类型
		job.setMapOutputKeyClass(TwoText.class);
		job.setMapOutputValueClass(Text.class);
		
		//设置分组
//		job.setGroupingComparatorClass(MyGroup.class);
		job.setSortComparatorClass(MyGroup.class);
		
		//使用我的reducer
		job.setReducerClass(SortReducer.class);
		//指明<k3,v3>类型
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		//数据写到哪里
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		job.waitForCompletion(true);
	}
	
	public static class SortMapper extends Mapper<LongWritable, Text, TwoText, Text>{
		
		TwoText k2 = new TwoText();
		Text v2 = new Text();
		
		@Override
		protected void map(
				LongWritable key,
				Text value,
				Context context)
				throws IOException, InterruptedException {
			String line = value.toString();
			String[] splited = line.split(" ");
			
			k2.set(splited[0], splited[1]);
			v2.set(splited[1]);
			System.out.println("map输出的值是<"+k2+"\t"+v2+">");
			context.write(k2, v2);
		}
	}
	
	public static class SortReducer extends Reducer<TwoText, Text, Text, Text>{
		
		Text k3 = new Text();
		Text v3 = new Text();
		StringBuilder sb = new StringBuilder();
		
		@Override
		protected void reduce(
				TwoText k2,
				Iterable<Text> v2s,
				Context context)
				throws IOException, InterruptedException {
			
			sb.delete(0, sb.length());
			
			for (Text v2 : v2s) {
				
				sb.append(v2).append(",");
			}
			k3.set(k2.t1);
			sb.deleteCharAt(sb.length()-1);
			v3.set(sb.toString());
			
			System.out.println("reduce输出的值是<"+k3+"\t"+v3+">");
			
			context.write(k3, v3);
		}
		
	}
	
	public static class TwoText implements WritableComparable<TwoText>{

		String t1;
		String t2;
		
		

		public String getT1() {
			return t1;
		}

		public String getT2() {
			return t2;
		}

		public void set(String t1, String t2) {
			this.t1 = t1;
			this.t2 = t2;
		}

		@Override
		public void write(DataOutput out) throws IOException {
			out.writeUTF(t1);
			out.writeUTF(t2);
		}

		@Override
		public void readFields(DataInput in) throws IOException {
			this.t1 = in.readUTF();
			this.t2 = in.readUTF();
		}

		@Override
		public int compareTo(TwoText o) {
			String thatT1 = o.t1;
			String thatT2 = o.t2;
			
			int result = this.t1.compareTo(thatT1);
			if (result==0) {
				result = this.t2.compareTo(thatT2);
			}
			return result;
		}

		@Override
		public String toString() {
			return t1 + "," + t2;
		}
	}
	
	public static class MyGroup extends WritableComparator{
		
		
		//如果这里不写构造函数的话，就会报错
		//java.lang.NullPointerException
		
		public MyGroup(){
			super(TwoText.class,true);
		}
		
		@Override
		public int compare(WritableComparable a, WritableComparable b) {
			TwoText twoInta = (TwoText) a;
			TwoText twoIntb = (TwoText) b;
			
			
			//千万注意这里采用get方法，不让t1的值为空
			String thatT1 = twoInta.getT1();
			String thatT2 = twoIntb.getT1();
			
			return thatT1.compareTo(thatT2);
		}
	}
}
