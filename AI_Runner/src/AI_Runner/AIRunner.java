package AI_Runner;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.DoubleToLongFunction;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class AIRunner {

	private RealMatrix Inputs;
	private RealMatrix[] baises;
	private RealMatrix[] weights;
	private RealMatrix Outputs;
	
	private double sigmoid(double x) {
		return ((2 / (1 + Math.exp(-x)))-1);
	}

	String[] readFile(String filepath) {
		ArrayList<String> str = new ArrayList<String>();
		try {
			FileReader load = new FileReader(filepath);
			BufferedReader br = new BufferedReader(load);

			int i = 0;
			try {
				while (str.add(br.readLine())) {
					if (str.get(i) == null) {
						str.remove(i);
						break;
					} else {
						// System.out.println(str.get(i));
						i++;
					}
				}
				br.close();
				load.close();

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str.toArray(new String[0]);
	}

	public AIRunner(String path) {
		String[] file = readFile(path);
		int InputNumber = Integer.valueOf(file[0]);
		int OutputNumber = Integer.valueOf(file[1]);
		int HiddenLenght = Integer.valueOf(file[2]);
		int[] hidden = new int[HiddenLenght];
		for (int i = 0; i < HiddenLenght; i++) {
			hidden[i] = Integer.valueOf(file[3+i]);
		}
		Inputs = MatrixUtils.createRealMatrix(1, InputNumber);
		baises = new RealMatrix[hidden.length + 1];
		weights = new RealMatrix[hidden.length + 1];
		int last_layer = InputNumber;
		Random random = new Random();
		for (int i = 0; i < hidden.length; i++) {
			baises[i] = MatrixUtils.createRealMatrix(1, hidden[i]);
			weights[i] = MatrixUtils.createRealMatrix(last_layer, hidden[i]);
			last_layer = hidden[i];
			
		}
		baises[hidden.length] = MatrixUtils.createRealMatrix(1, OutputNumber);
		weights[hidden.length] = MatrixUtils.createRealMatrix(last_layer, OutputNumber);
		int line = hidden.length+3;
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[i].getColumnDimension(); j++) {
				for (int k = 0; k < weights[i].getRowDimension(); k++) {
					weights[i].setEntry(k, j, Double.parseDouble(file[line]));
					line ++;
				}
			}
		}
		for (int i = 0; i < baises.length; i++) {
			for (int j = 0; j < baises[i].getColumnDimension(); j++) {
				baises[i].setEntry(0, j,  Double.parseDouble(file[line]));
				line ++;
			}
		}
	}
	
	public void setInput(int Input, double Value) {
		Inputs.setEntry(0, Input, Value);
	}
	
	public void run() {
		RealMatrix last_layer = Inputs;
		for (int i = 0; i < weights.length; i++) {
			last_layer = last_layer.multiply(weights[i]);
			last_layer = last_layer.add(baises[i]);
			for (int j = 0; j < last_layer.getColumnDimension(); j++) {
				last_layer.setEntry(0, j, sigmoid(last_layer.getEntry(0, j)));
			}
		}
		Outputs = last_layer;
	}

	public double getOutput(int Output) {
		return Outputs.getEntry(0, Output);
	}

}
