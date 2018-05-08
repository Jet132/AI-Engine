package GPUClasses;

import java.util.Random;

import AI_Engine.AIEngine;

public class GPUEvolver {

	public static float[] tempPoints;
	public static int[] score_list;

	public GPUEvolver() {
		tempPoints = new float[AIEngine.AI_NR];
		score_list = new int[AIEngine.AI_NR];
	}

	public void scoreLister() {
		float score = 0;
		float score_2 = 0;
		int score_array = 0;
		boolean[] score_check = new boolean[AIEngine.AI_NR];

		for (int i = 0; i < AIEngine.AI_NR; i++) {
			tempPoints[i] = ((GPUAI) AIEngine.AIs[i]).getAvarageScore();
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
		Random random = new Random();
		int parent = 0;
		for (int i = 0; i < AIEngine.AI_NR / 2; i++) {
			float[][] baises = new float[AIEngine.layout.length - 1][];
			float[][] weights = new float[AIEngine.layout.length - 1][];
			int last_layer = AIEngine.layout[0];
			for (int j = 0; j < weights.length; j++) {
				baises[j] = new float[AIEngine.layout[j+1]];
				weights[j] = new float[last_layer* AIEngine.layout[j+1]];
				for (int k = 0; k < AIEngine.layout[j+1]; k++) {
					if (random.nextInt(100) <= 50) {
						baises[j][k] = ((GPUAI) AIEngine.AIs[score_list[parent]]).getBaises()[j][k];
					} else {
						baises[j][k] = ((GPUAI) AIEngine.AIs[score_list[parent + 1]]).getBaises()[j][k];
					}
					if (random.nextInt(100) <= AIEngine.MR) {
						baises[j][k] =  random.nextFloat() * 2 - 1;
					}
					for (int l = 0; l < last_layer; l++) {
						if (random.nextInt(100) <= 50) {
							weights[j][l*last_layer+k] = ((GPUAI) AIEngine.AIs[score_list[parent]]).getWeights()[j][l*last_layer+k];
						} else {
							weights[j][l*last_layer+k] = ((GPUAI) AIEngine.AIs[score_list[parent + 1]]).getWeights()[j][l*last_layer+k];
						}
						if (random.nextInt(100) <= AIEngine.MR) {
							weights[j][l*last_layer+k] = random.nextFloat() * 2 - 1;
						}
					}
				}
				last_layer = AIEngine.layout[j+1];
			}

			((GPUAI) AIEngine.AIs[score_list[i + (AIEngine.AI_NR / 2)]]).setBaises(baises);
			((GPUAI) AIEngine.AIs[score_list[i + (AIEngine.AI_NR / 2)]]).setWeights(weights);
			if (parent > AIEngine.AI_NR / 2) {
				parent = 0;
			} else {
				parent += 2;
			}
		}
	}

}
