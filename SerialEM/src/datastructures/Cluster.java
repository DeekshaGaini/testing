package datastructures;

import java.util.ArrayList;

public class Cluster {

	int number;
	int instanceCount;
	double clusterProbability;
	ArrayList<Double> attributeMeans;
	ArrayList<Double> variance;

	public int getInstanceCount() {
		return instanceCount;
	}

	public void setInstanceCount(int instanceCount) {
		this.instanceCount = instanceCount;
	}

	public Cluster(int number) {
		this.number = number;
	}

	public int getNumber() {
		return number;
	}
	
	public double getClusterProbability() {
		return clusterProbability;
	}

	public void setClusterProbability(double clusterProbability) {
		this.clusterProbability = clusterProbability;
	}


	public ArrayList<Double> getvariance() {
		return variance;
	}

	public void setVariance(ArrayList<Double> variance) {
		this.variance = variance;
	}

	public ArrayList<Double> getAttributeMeans() {
		return attributeMeans;
	}

	public void setAttributeMeans(ArrayList<Double> attributeMeans) {
		this.attributeMeans = attributeMeans;
	}

	public ArrayList<String> printValues() {
		ArrayList<String> values = new ArrayList<String>();

		// print attribute values
		values.add("CLUSTER : " + getNumber());
		
		values.add("INSTANCE COUNT : " + getInstanceCount());

		values.add("ATTRIBUTE MEANS");
		String temp = "";
		for (Double d : attributeMeans) {
			temp = temp + " " + d.toString();
		}
		values.add(temp);

		values.add("VARIANCE");
		temp = "";
		for (Double d : variance) {
			temp = temp + " " + d.toString();
		}
		values.add(temp);

		return values;
	}
}
