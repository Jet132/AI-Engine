package GPUClasses;

import java.util.Random;

import JNN.JNN;

import static jcuda.driver.JCudaDriver.*;


class child implements Runnable{
	
	int child;
	int[] parents;
	Random random;
	
	public child(int child,int[] parents) {
		this.child = child;
		this.parents = parents;
		random = new Random();
	}

	@Override
	public void run() {
		float[][] baises = new float[JNN.layout.length - 1][];
		float[][] weights = new float[JNN.layout.length - 1][];
		int last_layer = JNN.layout[0];
		for (int j = 0; j < weights.length; j++) {
			baises[j] = new float[JNN.layout[j+1]];
			weights[j] = new float[last_layer* JNN.layout[j+1]];
			for (int k = 0; k < JNN.layout[j+1]; k++) {
				if (random.nextInt(100) <= 50) {
					baises[j][k] = GPUEvolver.pBaises[parents[0]][j][k];
				} else {
					baises[j][k] = GPUEvolver.pBaises[parents[1]][j][k];
				}
				if (random.nextInt(100) <= JNN.MR) {
					baises[j][k] =  random.nextFloat() * 2 - 1;
				}
				for (int l = 0; l < last_layer; l++) {
					if (random.nextInt(100) <= 50) {
						weights[j][k*last_layer+l] = GPUEvolver.pWeights[parents[0]][j][k*last_layer+l];
					} else {
						weights[j][k*last_layer+l] = GPUEvolver.pWeights[parents[1]][j][k*last_layer+l];
					}
					if (random.nextInt(100) <= JNN.MR) {
						weights[j][k*last_layer+l] = random.nextFloat() * 2 - 1;
					}
				}
			}
			last_layer = JNN.layout[j+1];
		}
		
		cuCtxPushCurrent(JNN.context);
		((GPUAgent) JNN.Agents[child]).setBaises(baises);
		((GPUAgent) JNN.Agents[child]).setWeights(weights);
		cuCtxPushCurrent(JNN.context);
	}
	
}

public class GPUEvolver {

	public static float[] tempPoints;
	public static int[] fitness_list;
	Random random = new Random();
	static float[][][] pWeights;
	static float[][][] pBaises;
	
	public GPUEvolver() {
		tempPoints = new float[JNN.Agent_NR];
		fitness_list = new int[JNN.Agent_NR];
	}

	public void fitnessLister() {
		float score = 0;
		float score_2 = 0;
		int score_array = 0;
		boolean[] score_check = new boolean[JNN.Agent_NR];

		for (int i = 0; i < JNN.Agent_NR; i++) {
			tempPoints[i] = ((GPUAgent) JNN.Agents[i]).getAvarageScore();
			score_check[i] = false;
		}
		score = 99999999999999999999999999999999999999f;

		for (int i = 0; i < JNN.Agent_NR; i++) {
			for (int j = 0; j < JNN.Agent_NR; j++) {
				if (score_2 <= tempPoints[j] && tempPoints[j] <= score && score_check[j] == false) {
					score_2 = tempPoints[j];
					score_array = j;
				}
			}
			score_check[score_array] = true;
			score = tempPoints[score_array];
			score_2 = 0;
			fitness_list[i] = score_array;
		}
	}

	public void evolve() {
		
		pWeights = new float[JNN.Agent_NR / 2][][];
		pBaises = new float[JNN.Agent_NR / 2][][];
		for(int i = 0; i < pWeights.length; i++) {
			pWeights[i] = ((GPUAgent) JNN.Agents[fitness_list[i]]).getWeights();
			pBaises[i] = ((GPUAgent) JNN.Agents[fitness_list[i]]).getBaises();
		}
		
		Thread[] childrens = new Thread[JNN.Agent_NR / 2];		
		
		int parent = 0;
		for (int i = 0; i < JNN.Agent_NR / 2; i++) {
			childrens[i] = new Thread(new child(i + (JNN.Agent_NR / 2), new int[] {parent,parent+1}) );
			childrens[i].start();
			if (parent < JNN.Agent_NR / 2-2) {
				parent += 2;
			} else {
				parent = 0;
			}
		}
		
		for(int i = 0; i < JNN.Agent_NR / 2; i++) {
			try {
				childrens[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		pWeights = null;
		pBaises = null;
		
		cuCtxSetCurrent(JNN.context);
		System.out.println("last generation:"+ JNN.generation);
		System.out.println("next generation");
	}

}
