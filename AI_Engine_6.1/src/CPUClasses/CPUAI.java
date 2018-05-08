package CPUClasses;

import java.util.Random;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import AI_Engine.AIEngine;

public class CPUAI {

	private RealMatrix Inputs;
	private RealMatrix[] baises;
	private RealMatrix[] weights;
	private RealMatrix Outputs;
	private float points[];

	private double sigmoid(double x) {
		return ((2 / (1 + Math.exp(-x)))-1);
	}
	
	public CPUAI(int Generationsteps, int[] layout) {
		points = new float[Generationsteps];
		this.Inputs = MatrixUtils.createRealMatrix(1, layout[0]);
		baises = new RealMatrix[layout.length - 1];
		weights = new RealMatrix[layout.length - 1];
		int last_layer = layout[0];
		Random random = new Random();
		for (int i = 0; i < weights.length; i++) {
			baises[i] = MatrixUtils.createRealMatrix(1, layout[i+1]);
			weights[i] = MatrixUtils.createRealMatrix(last_layer, layout[i+1]);

			for (int j = 0; j < layout[i+1]; j++) {
				baises[i].setEntry(0, j, random.nextDouble() * 2 - 1);
				for (int k = 0; k < last_layer; k++) {
					weights[i].setEntry(k, j, random.nextDouble() * 2 - 1);
				}
			}
			last_layer = layout[i+1];
			
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

	public void setScore(float Score) {
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
