package AI_Engine;

import java.util.Random;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class AI {

	private RealMatrix Inputs;//2Dmatrix for inputs used as 1DMatrix [1,Inputnumber]
	private RealMatrix[] baises;//array of all baises for all layers. baises are stored in 2Dmatrix for each layer, but is used as 1DMatrix [1,neuronnumber of current layer]
	private RealMatrix[] weights;//array of all weights for all layers. [neuronnumber of last layer, neuronnumber of current layer]
	private RealMatrix Outputs;//2Dmatrix for outputs used as 1DMatrix [1,outputnumber]. this array just used to store the output after you ran the AI
	private float points[];//array of score of every generationstep

	private double sigmoid(double x) {
		//sigmoid function
		return ((2 / (1 + Math.exp(-x)))-1);
	}
	
	public AI(int Generationsteps, int Inputs, int[] hidden, int Outputs) {
		points = new float[Generationsteps];//initializes in length of generationsteps before generation
		this.Inputs = MatrixUtils.createRealMatrix(1, Inputs);//initialize matrix for inputs [1,Inputnumber]
		baises = new RealMatrix[hidden.length + 1];//initializes array for baises with the lenght of hiddenlayernumber + outputslayer
		weights = new RealMatrix[hidden.length + 1];//initializes array for weights with the lenght of hiddenlayernumber + outputslayer
		int last_layer = Inputs;//int that holds number of neurons in the last layer
		Random random = new Random();//randomfunction used to assign random value between 1 and -1 to matrix
		//runs through all weights and baises (they are same lenght) except for outputweights and baises
		for (int i = 0; i < hidden.length; i++) {
			baises[i] = MatrixUtils.createRealMatrix(1, hidden[i]);//initializes matrix for baises[1,neuronnumber of current layer]
			weights[i] = MatrixUtils.createRealMatrix(last_layer, hidden[i]);//initializes matrix for weights [neuronnumber of last layer, neuronnumber of current layer]
			
			//runs throught every column
			for (int j = 0; j < hidden[i]; j++) {
				baises[i].setEntry(0, j, random.nextDouble() * 2 - 1);//sets to random double between 1 and -1
				//runs through every row
				for (int k = 0; k < last_layer; k++) {
					weights[i].setEntry(k, j, random.nextDouble() * 2 - 1);//sets to random double between 1 and -1
				}
			}
			last_layer = hidden[i];//sets new last layer
			
		}
		baises[hidden.length] = MatrixUtils.createRealMatrix(1, Outputs);//initializes matrix for baises for the outputlayer [1,outputnumber]
		weights[hidden.length] = MatrixUtils.createRealMatrix(last_layer, Outputs);//initializes matrix for weights for the outputlayer [neuronnumber of last layer, outputnumber]
		//runs throught every column
		for (int j = 0; j < Outputs; j++) {
			baises[hidden.length].setEntry(0, j, random.nextDouble() * 2 - 1);//sets to random double between 1 and -1
			//runs through every row
			for (int k = 0; k < last_layer; k++) {
				weights[hidden.length].setEntry(k, j, random.nextDouble() * 2 - 1);//sets to random double between 1 and -1
			}
		}
	}

	public AI(int Generationsteps, int Inputs, RealMatrix[] baises, RealMatrix[] weights) {
		//unused constructor that would set weights and baises to certain values
		//should be used when loading a saved Enginestate
		points = new float[Generationsteps];
		this.Inputs = MatrixUtils.createRealMatrix(1, Inputs);
		this.baises = baises;
		this.weights = weights;
	}

	void setInput(int Input, double Value) {
		//sets Input for certain inputneuron
		Inputs.setEntry(0, Input, Value);
	}

	void run() {
		//runs input through weights and baises
		RealMatrix last_layer = Inputs;//Matrix that hold result of last calculation (holds input at first)
		//runs through all layers
		for (int i = 0; i < weights.length; i++) {
			last_layer = last_layer.multiply(weights[i]);//multiplys last result by weights of current layer
			last_layer = last_layer.add(baises[i]);//adds baises of current layer
			//runs through every Entry in resultmatrix
			for (int j = 0; j < last_layer.getColumnDimension(); j++) {
				last_layer.setEntry(0, j, sigmoid(last_layer.getEntry(0, j)));//passes it through the sigmoidfunction
			}
		}
		Outputs = last_layer;//stores result in outputmatrix
	}

	double getOutput(int Output) {
		//returns stored value of certain outputneuron
		return Outputs.getEntry(0, Output);
	}

	void setScore(float Score) {
		//sets score for certain generationstep
		points[AIEngine.generationStep] = Score;
	}

	float getAvarageScore() {
		//returns avarage score of all generationsteps
		float temp = 0;
		for (int i = 0; i < points.length; i++) {
			temp += points[i];
		}
		return temp / points.length;
	}

	RealMatrix[] getWeights() {
		//returns all weights
		return weights;
	}

	RealMatrix[] getBaises() {
		//returns all baises
		return baises;
	}

	void setWeights(RealMatrix[] weights) {
		//sets all weights
		this.weights = weights;
	}

	void setBaises(RealMatrix[] baises) {
		//sets all baises
		this.baises = baises;
	}

}
