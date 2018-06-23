# AI-Engine
AI Engine for neural networks with evolutionary training. You will need to install the apache common math library (see in the example) and it can run on Nvidia GPUs if you install the JCuda library (see in the example).

# Example
The black rectangle needs to eat the green food and avoid the red enemies

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
* runNN

			.runNN(int AI);
* runNN with multithreading

			.runAllNNs();
* getOutput(returns double)

			.getOutput(int Output, int AI)
* setFitness

			.setFitness(float Score, int AI);
* nextGenerationstep
			
			.nextGenerationstep();
			
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
