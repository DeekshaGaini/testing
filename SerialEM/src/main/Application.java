package main;

import java.io.IOException;

public class Application {

	static long initTime = System.currentTimeMillis();
	
	public static void main(String[] args) throws IOException {

		if (args.length < 3) {
			System.out
					.println("Arguments are incorrect. Try Application #inputfile #outputfile #clusters");
			System.exit(0);
		} else {
			ExpectationMaximization expectationMaximization = new ExpectationMaximization(
					args[0], args[1], Integer.parseInt(args[2]));

			expectationMaximization.readData();
			expectationMaximization.initialization();
			expectationMaximization.printValues();

			do {
				expectationMaximization.EStep();
				expectationMaximization.MStep();
				expectationMaximization.printValues();
			} while (expectationMaximization.convergenceTest());
		}
		System.out.print(System.currentTimeMillis() - initTime);
	}

}
