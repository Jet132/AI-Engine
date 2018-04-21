package AI_Engine;

import javax.swing.JFrame;

public class AIEngine {

	static AI[] AIs;//array of AI
	
	static int Inputs;//number of Inputs
	static int Outputs;//number of Outputs
	static int[] hidden;//array with number of hiddenlayers and neurons in each layer 
	//for Example
	//{3,4} means, there are two hidden layers
	//the first one with 3 neurons and the second with 4
	static int AI_NR;//number of AI
	static int GENSTEP_NR;//number of generatiosteps it makes, before the next generation
	//a generationstep is like a real generation, but without the "evolving" part
	// a generation is a full generation and takes the avarage score of all generationsteps
	static int MR;//mutationrate

	static int generationStep;//current generationstep
	static int generation;//current generation
	static Evolver evolver;//class that makes a scorelist and makes new ai based on this list
	static DataManager DataManager = new DataManager();//manages the saving of the AIs (used to also manage the saving and loading of the Enginestate)

	public int getGeneration() {
		//returns the current generation
		return generation;
	}

	public int getGenerationStep() {
		//returns the current generationstep
		return generationStep;
	}

	public int getAINumberPerScoreList(int Number) {
		//returns the AIID based on the scorelist
		return evolver.score_list[Number];
	}

	public float getAIScorePerScoreList(int Number) {
		//returns the score based on the scorelist
		return evolver.tempPoints[Evolver.score_list[Number]];
	}

	public void saveAI(int AI) {
		//saves the AI
		DataManager.saveAI(AI);
	}

	public void setupEngine(boolean menu, int InputNumber, int[] hidden, int OutputNumber, int AINumber, int Generationsteps,
			int MutationRate) {
		//menu should always be false
		//I planned to make a startmenu for loading a saved Engine state, but forgot it
		if (!menu) {
			
			Inputs = InputNumber;//sets inputnumber
			this.hidden = hidden;//set hiddenlayer dimensions (see line 12 for better explanation)
			Outputs = OutputNumber;//sets outputnumbers
			AI_NR = AINumber;//sets AInumbers
			GENSTEP_NR = Generationsteps;//sets generationsteps before next generation
			MR = MutationRate;//sets mutationrate
			evolver = new Evolver();//initializes the evolverclass
			AIs = new AI[AI_NR];//initializes array for the AIs
			//runs through array
			for(int i = 0; i < AI_NR; i++) {
				//initializes every AI with needed infos
				AIs[i] = new AI(Generationsteps, InputNumber, hidden, OutputNumber);
			}
		}
	}

	public void setInput(int Input, double Value, int AI) {
		//sets the input of a certain AI and certain inputneuron
		AIs[AI].setInput(Input, Value);
	}

	public void runEngine(int AI) {
		//runs a certain AI
		//runs means taking the input through the hiddenlayers to get the output
		AIs[AI].run();
	}

	public double getOutput(int Output, int AI) {
		//returns the calculated output of cerain AI and certain outputneuron
		return AIs[AI].getOutput(Output);
	}

	public void setScore(float Score, int AI) {
		//sets score for certain AI, so the engine can create the scorelist
		AIs[AI].setScore(Score);
	}

	public void nextGenerationStep() {
		//looks if the engine made enough generationsteps
		if (generationStep < GENSTEP_NR - 1) {
			//if no it makes another
			generationStep++;
		} else {
			//if yes it resets the current generationstep-count to 0
			generationStep = 0;
			//creates the scorelist based on the average score
			evolver.scoreLister();
			//makes child and overrides bad AIs based on scorelist
			evolver.evolve();
			//goes to next generation
			generation++;
		}
	}

}
