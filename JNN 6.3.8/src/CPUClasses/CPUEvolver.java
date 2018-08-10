package CPUClasses;

import java.util.Random;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import JNN.JNN;

class child implements Runnable {

	int child;
	int[] parents;
	Random random;

	public child(int child, int[] parents) {
		this.child = child;
		this.parents = parents;
		random = new Random();
	}

	@Override
	public void run() {
		RealMatrix[] baises = new RealMatrix[JNN.layout.length - 1];
		RealMatrix[] weights = new RealMatrix[JNN.layout.length - 1];
		int last_layer = JNN.layout[0];
		for (int j = 0; j < weights.length; j++) {
			baises[j] = MatrixUtils.createRealMatrix(1, JNN.layout[j + 1]);
			weights[j] = MatrixUtils.createRealMatrix(last_layer, JNN.layout[j + 1]);
			for (int k = 0; k < JNN.layout[j + 1]; k++) {
				if (random.nextInt(100) <= 50) {
					baises[j].setEntry(0, k,
							((CPUAgent) JNN.Agents[CPUEvolver.fitness_list[parents[0]]]).getBaises()[j].getEntry(0, k));
				} else {
					baises[j].setEntry(0, k,
							((CPUAgent) JNN.Agents[CPUEvolver.fitness_list[parents[1]]]).getBaises()[j].getEntry(0, k));
				}
				if (random.nextInt(100) <= JNN.MR) {
					baises[j].setEntry(0, k, random.nextDouble() * 2 - 1);
				}
				for (int l = 0; l < last_layer; l++) {
					if (random.nextInt(100) <= 50) {
						weights[j].setEntry(l, k,
								((CPUAgent) JNN.Agents[CPUEvolver.fitness_list[parents[0]]]).getWeights()[j].getEntry(l,
										k));
					} else {
						weights[j].setEntry(l, k,
								((CPUAgent) JNN.Agents[CPUEvolver.fitness_list[parents[1]]]).getWeights()[j].getEntry(l,
										k));
					}
					if (random.nextInt(100) <= JNN.MR) {
						weights[j].setEntry(l, k, random.nextDouble() * 2 - 1);
					}
				}
			}
			last_layer = JNN.layout[j + 1];
		}
		((CPUAgent) JNN.Agents[CPUEvolver.fitness_list[child]]).setBaises(baises);
		((CPUAgent) JNN.Agents[CPUEvolver.fitness_list[child]]).setWeights(weights);

	}
}

public class CPUEvolver {

	public static float[] tempPoints;
	public static int[] fitness_list;

	public CPUEvolver() {
		tempPoints = new float[JNN.Agent_NR];
		fitness_list = new int[JNN.Agent_NR];
	}

	public void fitnessLister() {
		float fitness = 0;
		float fitness_2 = 0;
		int fitness_array = 0;
		boolean[] score_check = new boolean[JNN.Agent_NR];

		for (int i = 0; i < JNN.Agent_NR; i++) {
			tempPoints[i] = ((CPUAgent) JNN.Agents[i]).getAvarageScore();
			score_check[i] = false;
		}
		fitness = Float.MAX_VALUE;

		for (int i = 0; i < JNN.Agent_NR; i++) {
			for (int j = 0; j < JNN.Agent_NR; j++) {
				if (fitness_2 <= tempPoints[j] && tempPoints[j] <= fitness && score_check[j] == false) {
					fitness_2 = tempPoints[j];
					fitness_array = j;
				}
			}
			score_check[fitness_array] = true;
			fitness = tempPoints[fitness_array];
			fitness_2 = 0;
			fitness_list[i] = fitness_array;
		}
	}

	public void evolve() {
		System.out.println("last generation:" + JNN.generation + " best Fitness:" + tempPoints[fitness_list[0]]);
		
		Thread[] childrens = new Thread[JNN.Agent_NR / 2];

		int parent = 0;
		for (int i = 0; i < JNN.Agent_NR / 2; i++) {
			childrens[i] = new Thread(new child(i + (JNN.Agent_NR / 2), new int[] { parent, parent + 1 }));
			childrens[i].start();
			if (parent < JNN.Agent_NR / 2 - 2) {
				parent += 2;
			} else {
				parent = 0;
			}
		}

		for (int i = 0; i < JNN.Agent_NR / 2; i++) {
			try {
				childrens[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("next generation");
	}

}
