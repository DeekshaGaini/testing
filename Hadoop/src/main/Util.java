package main;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.hadoop.io.DoubleWritable;

import main.datastructures.ArrayListWritable;

public class Util {

	// time format printer
	public static String getTime(Long millis) {
		return String.format(
				"%02dH:%02dM:%02dS",
				TimeUnit.MILLISECONDS.toHours(millis),
				TimeUnit.MILLISECONDS.toMinutes(millis)
						- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
								.toHours(millis)),
				TimeUnit.MILLISECONDS.toSeconds(millis)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes(millis)));
	}
	
	// get sum of probabilities in an array
	public static double getProbSum(ArrayListWritable<DoubleWritable> values){
		double sum=0;
		for(DoubleWritable d : values){
			sum+=d.get();
		}
		return sum;
	}

	// random number generator in a range, upper bound not included
	public static int randInt(int min, int max) {
		Random random = new Random();
		return random.nextInt((max - min)) + min;
	}
}
