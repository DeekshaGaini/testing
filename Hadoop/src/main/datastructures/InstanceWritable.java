package main.datastructures;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Writable;

public class InstanceWritable implements Writable {

	// arrays to store attributes and probabilities
	ArrayListWritable<DoubleWritable> attribureValues = new ArrayListWritable<>();
	ArrayListWritable<DoubleWritable> belongProbabilities = new ArrayListWritable<>();

	// default constructor
	public InstanceWritable() {
	}

	// parameterised constructor
	public InstanceWritable(InstanceWritable t) {

		// clear values
		this.getAttribureValues().clear();
		this.getBelongProbabilities().clear();

		// set new values
		this.getAttribureValues().addAll(t.getAttribureValues());
		this.getBelongProbabilities().addAll(t.getBelongProbabilities());
	}

	// getter methods
	public ArrayListWritable<DoubleWritable> getAttribureValues() {
		return attribureValues;
	}

	public ArrayListWritable<DoubleWritable> getBelongProbabilities() {
		return belongProbabilities;
	}

	// read data from stream
	@Override
	public void readFields(DataInput in) throws IOException {
		// clear existing values
		this.attribureValues.clear();
		this.belongProbabilities.clear();
		this.attribureValues.readFields(in);
		this.belongProbabilities.readFields(in);
	}

	// write data to stream
	@Override
	public void write(DataOutput out) throws IOException {
		this.attribureValues.write(out);
		this.belongProbabilities.write(out);
	}

	// string form
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(attribureValues.toString());
		return sb.toString();
	}
}
