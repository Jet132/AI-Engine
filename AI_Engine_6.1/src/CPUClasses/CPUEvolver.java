package CPUClasses;

import java.util.Random;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import AI_Engine.AIEngine;

public class CPUEvolver {

	public static float[] tempPoints;
	public static int[] score_list;

	public CPUEvolver() {
		tempPoints = new float[AIEngine.AI_NR];
		score_list = new int[AIEngine.AI_NR];
	}

	public void scoreLister() {
		float score = 0;
		float score_2 = 0;
		int score_array = 0;
		boolean[] score_check = new boolean[AIEngine.AI_NR];

		for (int i = 0; i < AIEngine.AI_NR; i++) {
			tempPoints[i] = ((CPUAI) AIEngine.AIs[i]).getAvarageScore();
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
			RealMatrix[] baises = new RealMatrix[AIEngine.layout.length - 1];
			RealMatrix[] weights = new RealMatrix[AIEngine.layout.length - 1];
			int last_layer = AIEngine.layout[0];
			for (int j = 0; j < weights.length; j++) {
				baises[j] = MatrixUtils.createRealMatrix(1, AIEngine.layout[j+1]);
				weights[j] = MatrixUtils.createRealMatrix(last_layer, AIEngine.layout[j+1]);
				for (int k = 0; k < AIEngine.layout[j+1]; k++) {
					if (random.nextInt(100) <= 50) {
						baises[j].setEntry(0, k, ((CPUAI) AIEngine.AIs[score_list[parent]]).getBaises()[j].getEntry(0, k));
					} else {
						baises[j].setEntry(0, k, ((CPUAI) AIEngine.AIs[score_list[parent + 1]]).getBaises()[j].getEntry(0, k));
					}
					if (random.nextInt(100) <= AIEngine.MR) {
						baises[j].setEntry(0, k, random.nextDouble() * 2 - 1);
					}
					for (int l = 0; l < last_layer; l++) {
						if (random.nextInt(100) <= 50) {
							weights[j].setEntry(l, k, ((CPUAI) AIEngine.AIs[score_list[parent]]).getWeights()[j].getEntry(l, k));
						} else {
							weights[j].setEntry(l, k,
									((CPUAI) AIEngine.AIs[score_list[parent + 1]]).getWeights()[j].getEntry(l, k));
						}
						if (random.nextInt(100) <= AIEngine.MR) {
							weights[j].setEntry(l, k, random.nextDouble() * 2 - 1);
						}
					}
				}
				last_layer = AIEngine.layout[j+1];
			}

			((CPUAI) AIEngine.AIs[score_list[i + (AIEngine.AI_NR / 2)]]).setBaises(baises);
			((CPUAI) AIEngine.AIs[score_list[i + (AIEngine.AI_NR / 2)]]).setWeights(weights);
			if (parent > AIEngine.AI_NR / 2) {
				parent = 0;
			} else {
				parent += 2;
			}
		}
	}

}
