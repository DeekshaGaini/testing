package mrpack.mappers;

import java.io.IOException;
import java.util.StringTokenizer;

import main.Statistics;
import main.Util;
import main.datastructures.InstanceWritable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class InitMapper extends
		Mapper<LongWritable, Text, IntWritable, InstanceWritable> {

	private final String clusterCountKey = "clusterCount";
	private int clusterCount;
	//private int prob = 2;

	@Override
	protected void setup(Context context) throws IOException,
			InterruptedException {

		// get cluster count from console
		Configuration conf = context.getConfiguration();
		clusterCount = conf.getInt(clusterCountKey, 3);
	}

	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {

		// new key and value for map
		InstanceWritable mapValue = new InstanceWritable();
		IntWritable mapKey = new IntWritable();

		// set attribute values
		StringTokenizer st = new StringTokenizer(value.toString(), ",");
		while (st.hasMoreTokens()) {
			mapValue.getAttribureValues().add(
					new DoubleWritable(Double.parseDouble(st.nextToken())));
		}

		// set belong probabilities
		for (int i = 0; i < clusterCount; i++) {
			mapValue.getBelongProbabilities().add(new DoubleWritable(0.5/(double)(clusterCount-1)));
		}

		// mod
		/*mapValue.getBelongProbabilities().set(prob++ % clusterCount,
				new DoubleWritable(0.6));*/
		
		
		// random
		mapValue.getBelongProbabilities().set(Util.randInt(0, clusterCount),
				new DoubleWritable(0.5));

		// get max value as key
		mapKey.set(Statistics.maxValue(mapValue.getBelongProbabilities()));

		// send to reduce
		context.write(mapKey, mapValue);
	}
}
