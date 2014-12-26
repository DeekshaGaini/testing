package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileReadWrite {
	
	private File inputFolder;
	private File outputFolder;

	public FileReadWrite(String inputFilename, String outputFilename) {
		inputFolder =  new File("Input/"+inputFilename);
		outputFolder = new File("Output/"+outputFilename);
	}

	public ArrayList<String> GetFileData() throws IOException {

		String line;
		ArrayList<String> fileData = new ArrayList<String>();
		BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFolder));

		while ((line = bufferedReader.readLine()) != null) {
			fileData.add(line);
		}

		bufferedReader.close();
		return fileData;
	}

	public void PutFileData(ArrayList<String> output) throws IOException {

		fileExists();
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFolder, true));
		bufferedWriter.newLine();
		bufferedWriter.newLine();
		for (String line : output) {
			bufferedWriter.write(line);
			bufferedWriter.newLine();
		}
		bufferedWriter.close();
	}

	private void fileExists() throws IOException {
		if (!outputFolder.exists()) {
			outputFolder.createNewFile();
		}
	}
}
