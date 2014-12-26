package mrpack.reducers;

import java.io.IOException;
import java.util.ArrayList;

import main.Statistics;
import main.datastructures.ArrayListWritable;
import main.datastructures.ClusterDataWritable;
import main.datastructures.InstanceWritable;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.SequenceFile.Writer;
import org.apache.hadoop.mapreduce.Reducer;

public class InitReducer extends
		Reducer<IntWritable, InstanceWritable, IntWritable, InstanceWritable> {

	private final String dataPath = "/user/output/cdata_";
	private final String fileExtension = ".seq";
	private final String iterationKey = "em.iteration";

	// configuration
	private Configuration c;
	private Configuration conf = new Configuration();
	private FileSystem fs;

	private ArrayListWritable<ClusterDataWritable> cData = new ArrayListWritable<>();

	@Override
	protected void setup(Context context) throws IOException,
			InterruptedException {
		c = context.getConfiguration();
	}

	@Override
	protected void reduce(IntWritable key, Iterable<InstanceWritable> values,
			Context context) throws IOException, InterruptedException {

		ArrayList<InstanceWritable> instances = new ArrayList<>();
		ClusterDataWritable data = new ClusterDataWritable();

		for (InstanceWritable t : values) {
			instances.add(new InstanceWritable(t));
		}

		data.getClusterMeans().addAll(Statistics.mean(key.get(), instances));
		data.getClusterVariance().addAll(
				Statistics.variance(key.get(), instances,
						data.getClusterMeans()));

		cData.add(data);
		System.out.println(instances.size() + "\t" + data.toString());

		// set filesystem and paths
		Path path = new Path(dataPath + c.getInt(iterationKey, 0)
				+ fileExtension);
		fs = FileSystem.get(conf);

		// write mean and variance
		SequenceFile.Writer writer = new Writer(fs, conf, path,
				IntWritable.class, ArrayListWritable.class);
		writer.append(key, cData);

		// close writers
		writer.close();
	}
}
