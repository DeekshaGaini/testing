package main;

import java.util.ArrayList;

import main.datastructures.ArrayListWritable;
import main.datastructures.InstanceWritable;

import org.apache.hadoop.io.DoubleWritable;

public class Statistics {

	// static final constants
	private static final double CONST_2PI = 6.2831853;

	// get maximum value index in a given array
	public static int maxValue(ArrayListWritable<DoubleWritable> values) {
		int max = 0;
		int temp;
		for (int i = 1; i < values.size(); i++) {
			if (values.get(max).get() < values.get(i).get()) {
				max = i;
			} else if (values.get(max) == values.get(i)) {
				temp = Util.randInt(0, 2);
				max = (temp == 0) ? max : i;
			}
		}
		return max;
	}

	// calculate and return mean array for given set of instances
	public static ArrayListWritable<DoubleWritable> mean(int key,
			ArrayList<InstanceWritable> instances) {
		double num;
		ArrayListWritable<DoubleWritable> mean = new ArrayListWritable<>(); // mean

		for (int i = 0; i < instances.get(0).getAttribureValues().size(); i++) {
			num = 0.0;
			for (InstanceWritable a : instances) {
				num += a.getAttribureValues().get(i).get();
			}
			num /= instances.size();
			mean.add(new DoubleWritable(num));
		}
		return mean;
	}

	// calculate and return variance array for given set of instances
	public static ArrayListWritable<DoubleWritable> variance(int key,
			ArrayList<InstanceWritable> instances,
			ArrayListWritable<DoubleWritable> mean) {

		double num;
		ArrayListWritable<DoubleWritable> variance = new ArrayListWritable<>();

		for (int i = 0; i < instances.get(0).getAttribureValues().size(); i++) {
			num = 0.0;
			for (InstanceWritable a : instances) {
				num += Math.pow((a.getAttribureValues().get(i).get() - mean
						.get(i).get()), 2);
			}
			num /= (instances.size() - 1);
			variance.add(new DoubleWritable(num));
		}
		return variance;
	}

	// calculate and return logliklihood for given set of instances
	public static double getllh(int key, ArrayList<InstanceWritable> instances) {
		double loglikelihood = 0.0, sum;
		for (InstanceWritable a : instances) {
			sum = 0.0;
			for (int i = 0; i < a.getBelongProbabilities().size(); i++) {
				sum += a.getBelongProbabilities().get(i).get();
			}
			loglikelihood += Math.log(sum);
		}
		loglikelihood /= instances.size();
		return loglikelihood;
	}

	// calculate and return pdf value for an instance
	public static DoubleWritable getPDFValue(int dimension,
			ArrayListWritable<DoubleWritable> mean,
			ArrayListWritable<DoubleWritable> variance,
			ArrayListWritable<DoubleWritable> iValues) {

		double constant, temp;
		ArrayList<Double> difference;
		ArrayList<Double> inverseMatrix;
		ArrayList<Double> interm;

		// initialization
		temp = 1;
		constant = 1 / Math.pow(CONST_2PI, dimension / 2);
		for (DoubleWritable d : variance) {
			temp *= d.get();
		}
		temp = Math.sqrt(temp);
		constant /= temp;

		inverseMatrix = new ArrayList<>();
		difference = new ArrayList<>();
		interm = new ArrayList<>();

		// inverse covariance matrix
		for (int i = 0; i < variance.size(); i++) {
			inverseMatrix.add(1 / variance.get(i).get());
		}

		// difference matrix
		double sub;
		for (int i = 0; i < iValues.size(); i++) {
			sub = iValues.get(i).get() - mean.get(i).get();
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
		return new DoubleWritable(constant * Math.exp(-0.5 * mul));
	}
}
