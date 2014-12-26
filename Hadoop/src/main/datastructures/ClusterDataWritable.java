package main.datastructures;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.text.DecimalFormat;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Writable;

public class ClusterDataWritable implements Writable {

	ArrayListWritable<DoubleWritable> clusterMeans = new ArrayListWritable<>();
	ArrayListWritable<DoubleWritable> clusterVariance = new ArrayListWritable<>();

	public ClusterDataWritable() {
	}

	public ClusterDataWritable(ClusterDataWritable c) {
		this.clusterMeans.clear();
		this.clusterVariance.clear();
		this.clusterMeans.addAll(c.getClusterMeans());
		this.clusterVariance.addAll(c.getClusterVariance());
	}

	public ArrayListWritable<DoubleWritable> getClusterMeans() {
		return clusterMeans;
	}

	public ArrayListWritable<DoubleWritable> getClusterVariance() {
		return clusterVariance;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.clusterMeans.clear();
		this.clusterVariance.clear();

		clusterMeans.readFields(in);
		clusterVariance.readFields(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		clusterMeans.write(out);
		clusterVariance.write(out);
	}

	@Override
	public String toString() {
		int i;
		StringBuilder sb = new StringBuilder();
		DecimalFormat df = new DecimalFormat("#.###");

		sb.append(" CLUSTER MEANS : [ ");
		for (i = 0; i < clusterMeans.size() - 1; i++) {
			sb.append(df.format(getClusterMeans().get(i).get()) + ", ");
		}
		sb.append(df.format(getClusterMeans().get(i).get()));
		sb.append(" ] ");
		return sb.toString();
	}
}
