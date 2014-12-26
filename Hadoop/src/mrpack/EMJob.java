package mrpack;

import java.io.IOException;

import main.Util;
import main.datastructures.InstanceWritable;
import mrpack.mappers.EMMapper;
import mrpack.mappers.InitMapper;
import mrpack.reducers.EMReducer;
import mrpack.reducers.InitReducer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class EMJob extends Configured implements Tool {

	static final double CONST_CONVERGENCE = 1e-7;
	static final String clusterCountKey = "clusterCount";
	static final String iterationKey = "em.iteration";
	static final String outputPath = "/user/output/iteration_";
	static final String dataPath = "/user/output/cdata_";
	static final Path path = new Path("/user/output/llh.seq");

	static String inputPath = "";
	static double loglikelihood_old = 0, loglikelihood;
	static boolean flag = true;
	static int iteration = 0;
	static int max_iterations;
	static int clusterCount;
	static Configuration conf = new Configuration();
	static FileSystem fs;

	public static void main(String[] args) throws Exception {

		// run program
		long start = System.currentTimeMillis();
		int res = ToolRunner.run(new Configuration(), new EMJob(), args);
		long end = System.currentTimeMillis();

		// print time and iterations
		System.out.println(Util.getTime(end - start));
		System.out.println("ITERATIONs : " + iteration);
		System.exit(res);
	}

	@Override
	public int run(String[] args) throws Exception {

		// get arguments
		max_iterations = Integer.parseInt(args[2]);
		clusterCount = Integer.parseInt(args[1]);
		inputPath = args[0];

		// get configuration
		Configuration conf = this.getConf();
		conf.setInt(clusterCountKey, clusterCount);
		Job job = new Job(conf, "Expectation Maximization :" + iteration);
		job.setJarByClass(EMJob.class);

		// Input path
		Path in = new Path(inputPath);
		FileInputFormat.addInputPath(job, in);
		job.setInputFormatClass(TextInputFormat.class);

		// Output path
		Path out = new Path(outputPath + iteration);
		FileOutputFormat.setOutputPath(job, out);
		job.setOutputFormatClass(TextOutputFormat.class);

		// Setup MapReduce job
		// Do not specify the number of Reducer
		job.setMapperClass(InitMapper.class);
		job.setReducerClass(InitReducer.class);

		// Specify key / value
		job.setOutputKeyClass(IntWritable.class);
		job.setOutputValueClass(InstanceWritable.class);
		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(InstanceWritable.class);

		// wait for job to be completed
		job.waitForCompletion(true);

		// ================================================================================================================
		// EM Iterations
		// =================================================================================================================

		do {
			System.out.println("CURRENT ITERATION : " + ++iteration);
			// new configuration
			conf = new Configuration();
			conf.setInt(clusterCountKey, clusterCount);
			conf.setInt(iterationKey, iteration);

			job = new Job(conf, "Expectation Maximization : " + iteration);

			// set mr classes
			job.setMapperClass(EMMapper.class);
			job.setReducerClass(EMReducer.class);
			job.setJarByClass(EMJob.class);

			// Input path
			FileInputFormat.addInputPath(job, in);
			job.setInputFormatClass(TextInputFormat.class);

			// Output path
			out = new Path(outputPath + iteration);
			FileOutputFormat.setOutputPath(job, out);
			job.setOutputFormatClass(TextOutputFormat.class);

			// Specify key / value
			job.setOutputKeyClass(IntWritable.class);
			job.setOutputValueClass(InstanceWritable.class);
			job.setMapOutputKeyClass(IntWritable.class);
			job.setMapOutputValueClass(InstanceWritable.class);

			// wait for job to be completed
			job.waitForCompletion(true);

		} while (convergenceTest(iteration) && iteration < max_iterations);

		// return when done
		return 0;
	}

	public boolean convergenceTest(int iteration) throws IOException {

		// read lllh file and compute termination
		fs = FileSystem.get(conf);
		Path data = new Path(dataPath + (iteration - 1) + ".seq");

		SequenceFile.Reader reader = new SequenceFile.Reader(fs, path, conf);
		IntWritable key = new IntWritable();
		DoubleWritable value = new DoubleWritable();

		int index = 0;
		loglikelihood = 0;
		while (reader.next(key, value)) {
			loglikelihood += value.get();
			index++;
		}
		loglikelihood /= index;

		reader.close();

		fs.delete(path, true);
		if (fs.exists(data))
			fs.delete(data, true);

		System.out.println("NEW LOG LIKELIHOOD : " + loglikelihood + "\n"
				+ "OLD LOG LIKELIHOOD : " + loglikelihood_old);
		if (Math.abs(loglikelihood - loglikelihood_old) > CONST_CONVERGENCE) {
			flag = true;
			loglikelihood_old = loglikelihood;
		} else {
			flag = false;
		}
		return flag;
	}
}
