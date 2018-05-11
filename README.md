# AI-Engine
AI Engine for neural networks with evolutionary training

# Example
The black rectangle needs to eat the green food

# api
* [Setup](#Setup)
* [Training](#Training)
* [save AI](#saveAI)
* [info](#info)

# Setup

	.setupEngine(int[] layout, int AINumber, int Generationsteps, int MutationRate, boolean cuda);

* layout describes the NN. for example:

		{2,2,2} means 
		NN has 2 inputs
		2 hidden neurons in one layer
		2 outputs
			
* Generationsteps is the Amount of times it makes a generation before actually making children, so no AI can get to the next generation by luck

* If cuda is true the NN will run on your Nvidia GPU. Note: for small NNs CPU is recommended

# Training
* setInput

			.setInput(int Input, double Value, int AI);
* runEngine

			.runEngine(int AI);
* getOutput(returns double)

			.getOutput(int Output, int AI)
* setScore

			.setScore(float Score, int AI);
* nextGenerationStep
			
			.nextGenerationStep();
			
# saveAI

* saveAI

			.saveAI(int AI);
			
* saveBestAI

			.saveAI(AIEngine.getAINumberPerScoreList(0));
			
# info

* getGeneration(returns int)

			.getGeneration()
* getGenerationStep(returns int)

			.getGenerationStep()
* getScorelist(returns float)

			.getAIScorePerScoreList(int Number)
* getAIIDforScorelist(returns int)

			.getAINumberPerScoreList(int Number)
