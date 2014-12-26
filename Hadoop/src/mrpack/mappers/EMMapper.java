package mrpack.mappers;

import java.io.IOException;
import java.util.StringTokenizer;

import main.Statistics;
import main.datastructures.ArrayListWritable;
import main.datastructures.ClusterDataWritable;
import main.datastructures.InstanceWritable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class EMMapper extends
		Mapper<LongWritable, Text, IntWritable, InstanceWritable> {

	private static final String dataPath = "/user/output/cdata_";
	private static final String fileExtension = ".seq";
	private static final String iterationKey = "em.iteration";

	// configuration
	private static FileSystem fs;
	private static Configuration conf = new Configuration();
	private static Configuration c;

	private static ArrayListWritable<ClusterDataWritable> cData = new ArrayListWritable<>();

	@Override
	protected void setup(Context context) throws IOException,
			InterruptedException {

		c = context.getConfiguration();
		fs = FileSystem.get(conf);

		if (cData.size() == 0) {
			Path path = new Path(dataPath + (c.getInt(iterationKey, 0) - 1)
					+ fileExtension);
			SequenceFile.Reader reader = new SequenceFile.Reader(fs, path, conf);

			IntWritable tempKey = new IntWritable();
			ArrayListWritable<ClusterDataWritable> tempValue = new ArrayListWritable<>();

			while (reader.next(tempKey, tempValue)) {
				cData.addAll(tempValue);
			}

			reader.close();
		}
	}

	@Override
	protected void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {

		int attributeCount;

		// new key and value for map
		InstanceWritable mapValue = new InstanceWritable();
		IntWritable mapKey = new IntWritable();

		// set attribute values
		StringTokenizer st = new StringTokenizer(value.toString(), ",");
		while (st.hasMoreTokens()) {
			mapValue.getAttribureValues().add(
					new DoubleWritable(Double.parseDouble(st.nextToken())));
		}
		attributeCount = mapValue.getAttribureValues().size();

		// update belong probabilities
		for (int i = 0; i < cData.size(); i++) {
			mapValue.getBelongProbabilities().add(
					Statistics.getPDFValue(attributeCount, cData.get(i)
							.getClusterMeans(), cData.get(i)
							.getClusterVariance(), mapValue
							.getAttribureValues()));
		}

		// get max value as key
		mapKey.set(Statistics.maxValue(mapValue.getBelongProbabilities()));

		// send to reduce
		context.write(mapKey, mapValue);
	}
}
