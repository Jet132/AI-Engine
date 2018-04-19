package AI_Engine;

public class BrainCalculator {
	
	private int CON_S = 0;

	private float calculateWeight(float Input, float CON) {
		float x = CON;
		CON_S++;
		return Input * x;
	}

	private float sigmoid(float x) {
		return (float) ((2 / (1 + Math.exp(-x)))-1);
	}
	
	private int MaxNumber() {
		int x = 0;
		int y[] = new int[4];
		y[0] = AIEngine.Inputs;
		y[1] = AIEngine.Outputs;
		y[2] = AIEngine.Hidden;
		y[3] = AIEngine.Layers;
		for (int i = 0; i < 4; i++) {
			if (x < y[i]) {
				x = y[i];
			}
		}
		return x;
	}

	float[] readDNA(float[] CONs, float[] Inputs, int AI) {
		CON_S = 0;
		float Neurons[][] = new float[AIEngine.Layers+2][MaxNumber()];
		for(int i = 0; i < Inputs.length; i++) {
			Neurons[0][i] = Inputs[i];
		}
		for (int i = 1; i < AIEngine.Layers+2; i++) {
			for (int j = 0; j < MaxNumber(); j++) {
					Neurons[i][j] = 0f;
			}
		}

		for (int i = 0; i < AIEngine.Hidden; i++) {
			for (int j = 0; j < AIEngine.Inputs; j++) {
				Neurons[1][i] += calculateWeight(Neurons[0][j],CONs[CON_S]);
			}
			Neurons[1][i] = sigmoid(Neurons[1][i]);
		}

		if (AIEngine.Layers > 1) {
			for (int i = 0; i < AIEngine.Layers - 1; i++) {
				for (int j = 0; j < AIEngine.Hidden; j++) {
					for (int k = 0; k < AIEngine.Hidden; k++) {
						Neurons[i + 2][j] += calculateWeight(Neurons[i + 1][k],CONs[CON_S]);
					}
					Neurons[i + 2][j] = sigmoid(Neurons[i + 2][j]);
				}
			}
		}
		for (int i = 0; i < AIEngine.Outputs; i++) {
			for (int j = 0; j < AIEngine.Hidden; j++) {
				Neurons[(2 + AIEngine.Layers) - 1][i] += calculateWeight(
						Neurons[(2 + AIEngine.Layers) - 2][j],CONs[CON_S]);
			}
			Neurons[(2 + AIEngine.Layers) - 1][i] = sigmoid(Neurons[(2 + AIEngine.Layers) - 1][i]);
		}
		
		if(AI == Evolver.score_list[0]) {
			ANNDisplayer.tempNeurons = Neurons;
		}
		
		return Neurons[(2 + AIEngine.Layers) - 1];
	}

}
