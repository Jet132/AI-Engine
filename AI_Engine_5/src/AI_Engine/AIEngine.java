package AI_Engine;

import javax.swing.JFrame;

public class AIEngine {

	static AI[] AIs;

	static int Inputs;
	static int Outputs;
	static int[] hidden;
	static int AI_NR;
	static int GENSTEP_NR;
	static int MR;

	static int generationStep;
	static int generation;
	static Evolver evolver;
	static DataManager DataManager = new DataManager();

	public int getGeneration() {
		return generation;
	}

	public int getGenerationStep() {
		return generationStep;
	}

	public int getAINumberPerScoreList(int Number) {
		return evolver.score_list[Number];
	}

	public float getAIScorePerScoreList(int Number) {
		return evolver.tempPoints[Evolver.score_list[Number]];
	}

	public void saveAI(int AI) {
		DataManager.saveAI(AI);
	}

	public void setupEngine(boolean menu, int InputNumber, int[] hidden, int OutputNumber, int AINumber, int Generationsteps,
			int MutationRate) {
		if (!menu) {
			
			Inputs = InputNumber;
			this.hidden = hidden;
			Outputs = OutputNumber;
			AI_NR = AINumber;
			GENSTEP_NR = Generationsteps;
			MR = MutationRate;
			evolver = new Evolver();
			AIs = new AI[AI_NR];
			for(int i = 0; i < AI_NR; i++) {
				AIs[i] = new AI(Generationsteps, InputNumber, hidden, OutputNumber);
			}
		}
	}

	public void setInput(int Input, double Value, int AI) {
		AIs[AI].setInput(Input, Value);
	}

	public void runEngine(int AI) {
		AIs[AI].run();
	}

	public double getOutput(int Output, int AI) {
		return AIs[AI].getOutput(Output);
	}

	public void setScore(float Score, int AI) {
		AIs[AI].setScore(Score);
	}

	public void nextGenerationStep() {
		if (generationStep < GENSTEP_NR - 1) {
			generationStep++;
		} else {
			generationStep = 0;
			evolver.scoreLister();
			evolver.evolve();
			generation++;
		}
	}

}
