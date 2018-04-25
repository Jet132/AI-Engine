package AI_Engine;

import java.util.Random;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class AI {

	private RealMatrix Inputs;
	private RealMatrix[] baises;
	private RealMatrix[] weights;
	private RealMatrix Outputs;
	private float points[];

	private double sigmoid(double x) {
		return ((2 / (1 + Math.exp(-x)))-1);
	}
	
	public AI(int Generationsteps, int Inputs, int[] hidden, int Outputs) {
		points = new float[Generationsteps];
		this.Inputs = MatrixUtils.createRealMatrix(1, Inputs);
		baises = new RealMatrix[hidden.length + 1];
		weights = new RealMatrix[hidden.length + 1];
		int last_layer = Inputs;
		Random random = new Random();
		for (int i = 0; i < hidden.length; i++) {
			baises[i] = MatrixUtils.createRealMatrix(1, hidden[i]);
			weights[i] = MatrixUtils.createRealMatrix(last_layer, hidden[i]);

			for (int j = 0; j < hidden[i]; j++) {
				baises[i].setEntry(0, j, random.nextDouble() * 2 - 1);
				for (int k = 0; k < last_layer; k++) {
					weights[i].setEntry(k, j, random.nextDouble() * 2 - 1);
				}
			}
			last_layer = hidden[i];
			
		}
		baises[hidden.length] = MatrixUtils.createRealMatrix(1, Outputs);
		weights[hidden.length] = MatrixUtils.createRealMatrix(last_layer, Outputs);
		for (int j = 0; j < Outputs; j++) {
			baises[hidden.length].setEntry(0, j, random.nextDouble() * 2 - 1);
			for (int k = 0; k < last_layer; k++) {
				weights[hidden.length].setEntry(k, j, random.nextDouble() * 2 - 1);
			}
		}
	}

	public AI(int Generationsteps, int Inputs, RealMatrix[] baises, RealMatrix[] weights) {
		points = new float[Generationsteps];
		this.Inputs = MatrixUtils.createRealMatrix(1, Inputs);
		this.baises = baises;
		this.weights = weights;
	}

	void setInput(int Input, double Value) {
		Inputs.setEntry(0, Input, Value);
	}

	void run() {
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

	double getOutput(int Output) {
		return Outputs.getEntry(0, Output);
	}

	void setScore(float Score) {
		points[AIEngine.generationStep] = Score;
	}

	float getAvarageScore() {
		float temp = 0;
		for (int i = 0; i < points.length; i++) {
			temp += points[i];
		}
		return temp / points.length;
	}

	RealMatrix[] getWeights() {
		return weights;
	}

	RealMatrix[] getBaises() {
		return baises;
	}

	void setWeights(RealMatrix[] weights) {
		this.weights = weights;
	}

	void setBaises(RealMatrix[] baises) {
		this.baises = baises;
	}

}
