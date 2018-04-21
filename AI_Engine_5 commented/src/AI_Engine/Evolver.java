package AI_Engine;

import java.util.Random;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class Evolver {
	
	static float[] tempPoints;//Average score of all AIs
	static int[] score_list;//list that holds AIID for scorelist
	
	public Evolver() {
		//initializes both arrays with lenght AInumber
		tempPoints = new float[AIEngine.AI_NR];
		score_list = new int[AIEngine.AI_NR];
	}

	public void scoreLister() {
		//just creates a scorelist
		//simple check if other AI is bigger
		float score = 0;
		float score_2 = 0;
		int score_array = 0;
		boolean[] score_check = new boolean[AIEngine.AI_NR];
		
		for (int i = 0; i < AIEngine.AI_NR; i++) {
				tempPoints[i] = AIEngine.AIs[i].getAvarageScore();
				score_check[i] = false;
		}
		score = 99999999999999999999999999999999999999f;
		
		for (int i = 0; i < AIEngine.AI_NR; i++) {
			for (int j = 0; j < AIEngine.AI_NR; j++) {
				if (score_2 <= tempPoints[j] && tempPoints[j] <= score && score_check[j] == false) {
					score_2 = tempPoints[j];
					score_array = j;
				}
			}
			score_check[score_array] = true;
			score = tempPoints[score_array];
			score_2 = 0;
			score_list[i] = score_array;
		}
	}
	
	public void evolve() {
		Random random = new Random();//class that makes random values
		int parent = 0;//parent based on scorelist
		//runs through half of all AIs
		for (int i = 0; i < AIEngine.AI_NR / 2; i++) {
			RealMatrix[] baises = new RealMatrix[AIEngine.hidden.length + 1];//creates array for new baises
			RealMatrix[] weights = new RealMatrix[AIEngine.hidden.length + 1];//creates array for new weights
			int last_layer = AIEngine.Inputs;//holds neuroncount of last layer
			//runs through all layers except the outputlayer
			for (int j = 0; j < AIEngine.hidden.length; j++) {
				baises[j] = MatrixUtils.createRealMatrix(1, AIEngine.hidden[j]);//initializes matrix for baises[1,neuronnumber of current layer]
				weights[j] = MatrixUtils.createRealMatrix(last_layer,AIEngine.hidden[j]);//initializes matrix for weights [neuronnumber of last layer, neuronnumber of current layer]
				
				//runs throught every column
				for(int k = 0; k < AIEngine.hidden[j]; k ++) {
					//50% chance the child gets value of second parent
					if (random.nextInt(100) <= 50) {
						baises[j].setEntry(0, k, AIEngine.AIs[score_list[parent]].getBaises()[j].getEntry(0, k));//sets to value of first parent
					} else {
						baises[j].setEntry(0, k, AIEngine.AIs[score_list[parent+1]].getBaises()[j].getEntry(0, k));//sets to value of second parent
					}
					//mutationrate% chance child gets random value (mutation)
					if(random.nextInt(100) <= AIEngine.MR){
						baises[j].setEntry(0, k,random.nextDouble()*2-1);//sets to random value between 1 and -1
					}
					//runs through every row
					for(int l = 0; l < last_layer; l++) {
						//same thing as by the baises
						if (random.nextInt(100) <= 50) {
							weights[j].setEntry(l, k, AIEngine.AIs[score_list[parent]].getWeights()[j].getEntry(l, k));
						} else {
							weights[j].setEntry(l, k, AIEngine.AIs[score_list[parent+1]].getWeights()[j].getEntry(l, k));
						}
						if(random.nextInt(100) <= AIEngine.MR){
							weights[j].setEntry(l, k,random.nextDouble()*2-1);
						}
					}
				}
				last_layer = AIEngine.hidden[j];//sets new last layer
			}
			baises[AIEngine.hidden.length] = MatrixUtils.createRealMatrix(1, AIEngine.Outputs);//initializes matrix for baises for the outputlayer [1,outputnumber]
			weights[AIEngine.hidden.length] = MatrixUtils.createRealMatrix(last_layer, AIEngine.Outputs);//initializes matrix for weights for the outputlayer [neuronnumber of last layer, outputnumber]
			//same thing as by the hidden layers
			//there would be a way to make this easier, but it isn't implemented and if your translating this, please make it this way for consistency
			for (int j = 0; j < AIEngine.Outputs; j++) {
				if (random.nextInt(100) <= 50) {
					baises[AIEngine.hidden.length].setEntry(0, j, AIEngine.AIs[score_list[parent]].getBaises()[AIEngine.hidden.length].getEntry(0, j));
				} else {
					baises[AIEngine.hidden.length].setEntry(0, j, AIEngine.AIs[score_list[parent+1]].getBaises()[AIEngine.hidden.length].getEntry(0, j));
				}
				if(random.nextInt(100) <= AIEngine.MR){
					baises[AIEngine.hidden.length].setEntry(0, j,random.nextDouble()*2-1);
				}
				for (int k = 0; k < last_layer; k++) {
					if (random.nextInt(100) <= 50) {
						weights[AIEngine.hidden.length].setEntry(k, j, AIEngine.AIs[score_list[parent]].getWeights()[AIEngine.hidden.length].getEntry(k, j));
					} else {
						weights[AIEngine.hidden.length].setEntry(k, j, AIEngine.AIs[score_list[parent+1]].getWeights()[AIEngine.hidden.length].getEntry(k, j));
					}
					if(random.nextInt(100) <= AIEngine.MR){
						weights[AIEngine.hidden.length].setEntry(k, j,random.nextDouble()*2-1);
					}
				}
			}
			
			
			AIEngine.AIs[score_list[i + (AIEngine.AI_NR / 2)]].setBaises(baises);//overrides baises of bad AI to make a child AI
			AIEngine.AIs[score_list[i + (AIEngine.AI_NR / 2)]].setWeights(weights);//overrides weights of bad AI to make a child AI
			//if parent is getting to bad
			if(parent > AIEngine.AI_NR / 2) {
				parent = 0;//reset parent to the start
			}else{
				parent += 2;//takes next to parents
			}
		}
	}

}
