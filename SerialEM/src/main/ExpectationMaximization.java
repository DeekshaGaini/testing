package main;

import java.io.IOException;
import java.util.StringTokenizer;
import java.util.ArrayList;

import datastructures.Cluster;
import datastructures.Instance;

public class ExpectationMaximization {

	// ===========================================================================================================================
	// declarations
	// ===========================================================================================================================

	static boolean flag = true;
	static int iteration = 0;
	static final double CONST_CONVERGENCE = 1e-6;
	private final static double CONST_2PI = 6.2831853;

	int clusterCount, attributeCount, instanceCount;
	double clusterProbability, loglikelihood_old = 0, loglikelihood;

	FileReadWrite fileReadWrite;

	ArrayList<Cluster> clusterArray;
	ArrayList<Instance> dataSet;

	// ===========================================================================================================================
	// public methods
	// ===========================================================================================================================

	public ExpectationMaximization(String inputFilename, String outputFilename,
			int clusterCount) {
		this.clusterCount = clusterCount;
		
		clusterArray = new ArrayList<Cluster>();
		for (int i = 0; i < clusterCount; i++) {
			Cluster temp = new Cluster(i);
			temp.setClusterProbability(1/(double)clusterCount);
			clusterArray.add(temp);
		}

		dataSet = new ArrayList<Instance>();
		fileReadWrite = new FileReadWrite(inputFilename, outputFilename);
	}

	public void readData() throws IOException {

		ArrayList<String> line;
		ArrayList<String> output = new ArrayList<String>();
		ArrayList<Double> temporary;

		line = fileReadWrite.GetFileData();
		instanceCount = line.size();
		StringTokenizer stringTokenizer = new StringTokenizer(line.get(0), ",");
		attributeCount = stringTokenizer.countTokens();
		output.add("ATTRIBUTE COUNT : " + attributeCount);
		fileReadWrite.PutFileData(output);

		for (String s : line) {

			Instance inst = new Instance();
			temporary = new ArrayList<Double>();

			output = new ArrayList<String>();

			stringTokenizer = new StringTokenizer(s, ",");

			for (int i = 0; i < attributeCount; i++) {
				temporary.add(Double.parseDouble(stringTokenizer.nextToken()));
			}
			inst.setAttributeValues(temporary);
			dataSet.add(inst);

		}
	}

	public void initialization() throws IOException {

		ArrayList<Double> temporary;
		int j = 0;

		for (Instance inst : dataSet) {
			temporary = new ArrayList<Double>();
			for (int i = 0; i < clusterCount; i++) {
				temporary.add(0.0);
			}
			inst.setbelongProbability(temporary);
		}

		for (Instance i : dataSet) {
			i.getbelongProbability().set(j % clusterCount, 1.0);
			i.setClusterNumber();
			j++;
		}

		rearrangeClusters();
		calcMean();
		calcVariance();

	}

	public void EStep() throws IOException {

		ArrayList<String> output = new ArrayList<String>();
		output.add("ITERATION : " + iteration);
		fileReadWrite.PutFileData(output);
		newPDFValues();

	}

	public void MStep() {

		rearrangeClusters();
		calcMean();
		calcVariance();
	}

	public void printValues() throws IOException {
		ArrayList<String> temp = new ArrayList<String>();

		for (Cluster c : clusterArray) {
			temp.addAll(c.printValues());
		}
		fileReadWrite.PutFileData(temp);
	}

	public boolean convergenceTest() throws IOException {
		// calculate loglikelihood
		ArrayList<String> temp = new ArrayList<String>();
		int i = 0;
		loglikelihood = 0;
		double sum;
		int t;
		while (i < instanceCount) {
			sum = 0;
			t = 0;
			while (t < clusterCount) {
				sum += dataSet.get(i).getbelongProbability().get(t);
				t++;
			}
			loglikelihood += Math.log(sum);
			i++;
		}
		loglikelihood /= instanceCount;
		// -------------------------------------------------------------------------------------------------------
		// print loglikelihood

		temp.add("LOG LIKELIHOOD : " + loglikelihood);
		temp.add("OLD LOG LIKELIHOOD : " + loglikelihood_old);
		fileReadWrite.PutFileData(temp);

		if (Math.abs(loglikelihood - loglikelihood_old) > CONST_CONVERGENCE) {
			flag = true;
			loglikelihood_old = loglikelihood;
		} else {
			flag = false;
		}
		iteration++;
		System.out.println("   "+iteration);
		return flag;
	}

	// ===========================================================================================================================
	// private methods
	// ===========================================================================================================================
	private void newPDFValues() {

		ArrayList<Double> temp;

		for (Instance i : dataSet) {
			temp = new ArrayList<Double>();
			for (Cluster c : clusterArray) {
				temp.add(getPDFValue(c.getAttributeMeans(), c.getvariance(),
						i.getAttributeValues()));
			}
			i.setbelongProbability(temp);
			i.setClusterNumber();
		}
	}

	private void rearrangeClusters() {
		ArrayList<Integer> count = new ArrayList<Integer>();

		for (int integer = 0; integer < clusterCount; integer++) {
			count.add(0);
		}
		for (Instance i : dataSet) {
			count.set(i.getClusterNumber(),
					(count.get(i.getClusterNumber()) + 1));
		}
		for (int i = 0; i < clusterCount; i++) {
			clusterArray.get(i).setInstanceCount(count.get(i));
		}
	}

	// ===========================================================================================================================
	// statistical methods
	// ========================================================================================


	private void calcMean() {

		ArrayList<Double> temp;
		double sum, denominator;

		// init mean
		for (Cluster c : clusterArray) {
			temp = new ArrayList<>();
			for (int i = 0; i < attributeCount; i++) {
				sum = 0.0;
				denominator = 0.0;
				for (Instance inst : dataSet) {
					if (inst.getClusterNumber() == c.getNumber()) {
						sum += inst.getAttributeValues().get(i)
								* inst.getbelongProbability()
										.get(c.getNumber());
						denominator += inst.getbelongProbability().get(
								c.getNumber());
					}
				}
				temp.add(sum / denominator);
			}
			c.setAttributeMeans(temp);
		}
	}

	private void calcVariance() {

		ArrayList<Double> temp = new ArrayList<Double>();
		double sum, difference;

		// varinace
		for (Cluster c : clusterArray) {
			temp = new ArrayList<Double>();
			for (int i = 0; i < attributeCount; i++) {
				sum = 0.0;
				for (Instance inst : dataSet) {
					if (inst.getClusterNumber() == c.getNumber()) {
						difference = inst.getAttributeValues().get(i)
								- c.getAttributeMeans().get(i);
						sum += Math.pow(difference, 2);
					}

				}
				temp.add(sum / (c.getInstanceCount() - 1));
			}
			c.setVariance(temp);
		}
	}

	private double getPDFValue(ArrayList<Double> mean,
			ArrayList<Double> variance, ArrayList<Double> attributeValues) {

		double constant, temp, result;

		ArrayList<Double> difference;
		ArrayList<Double> inverseMatrix;
		ArrayList<Double> interm;

		// initialization
		constant = 1 / Math.pow(CONST_2PI, attributeCount / 2);
		temp = 1;
		for (Double d : variance) {
			temp *= d;
		}
		temp = Math.sqrt(temp);
		constant /= temp;

		inverseMatrix = new ArrayList<>();
		difference = new ArrayList<>();
		interm = new ArrayList<>();

		// inverse covariance matrix
		for (int i = 0; i < variance.size(); i++) {
			inverseMatrix.add(1 / variance.get(i));
		}

		// difference matrix
		double sub;
		for (int i = 0; i < attributeValues.size(); i++) {
			sub = attributeValues.get(i) - mean.get(i);
			difference.add(sub);
		}

		double mul;
		// transpose . inverse multiplication
		for (int i = 0; i < difference.size(); i++) {
			mul = (difference.get(i) * inverseMatrix.get(i));
			interm.add(mul);
		}

		// above result . difference multiplication
		mul = 0.0;
		for (int i = 0; i < interm.size(); i++) {
			mul += (interm.get(i) * difference.get(i));
		}

		// result
		result = constant * Math.exp(-0.5 * mul);
		return result;
	}

}
