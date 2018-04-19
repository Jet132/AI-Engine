package AI_Engine;

import java.util.Random;

public class Evolver {

	static float[] tempPoints = new float[AIEngine.AI_NR];
	static int[] score_list = new int[AIEngine.AI_NR];

	public void scoreLister() {
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
		Random random = new Random();
		for (int i = 0; i < AIEngine.AI_NR / 2; i+=2) {
			float[] tempCON = new float[AIEngine.CON_NR];
			for (int j = 0; j < AIEngine.CON_NR; j++) {
				if (random.nextInt(100) <= 50) {
					tempCON[j] = AIEngine.AIs[score_list[i]].getCON(j);
				} else {
					tempCON[j] = AIEngine.AIs[score_list[i+1]].getCON(j);
				}
				if(random.nextInt(100) <= AIEngine.DNA_Per){
					tempCON[j] = (random.nextInt(200)/100f)-1;
				}
			}
			AIEngine.AIs[score_list[i + (AIEngine.AI_NR / 2)]].setCON(tempCON);
		}
	}

}
