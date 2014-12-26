package datastructures;

import java.util.ArrayList;

public class Instance {

	int clusterNumber;
	ArrayList<Double> attributeValues;
	ArrayList<Double> belongProbability;

	public ArrayList<Double> getbelongProbability() {
		return belongProbability;
	}

	public void setbelongProbability(ArrayList<Double> belongProbability) {
		this.belongProbability = belongProbability;
	}

	public ArrayList<Double> getAttributeValues() {
		return attributeValues;
	}

	public void setAttributeValues(ArrayList<Double> attributeValues) {
		this.attributeValues = attributeValues;
	}

	public void setClusterNumber(int number) {
		clusterNumber = number;
	}

	public void setClusterNumber() {
		double max = belongProbability.get(0);
		for (Double d : belongProbability) {
			if (d > max) {
				max = d;
			}
		}
		clusterNumber = belongProbability.indexOf(max);
	}

	public int getClusterNumber() {
		return clusterNumber;
	}

	public ArrayList<String> printValues() {
		ArrayList<String> values = new ArrayList<String>();

		values.add("CLUSTER NUMBER : " + getClusterNumber());
		// print attribute values
		values.add("ATTRIBUTE VALUES : ");
		String temp = "";
		for (Double d : attributeValues) {
			temp = temp + " " + d.toString();
		}
		values.add(temp);

		values.add("BELONG PROBABILITIES : ");
		temp = "";
		for (Double d : belongProbability) {
			temp = temp + " " + d.toString();
		}
		values.add(temp);
		return values;
	}

}
