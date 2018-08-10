# JNN
AI Engine for neural networks with evolutionary training

there is a java and c# version
* java fully tested (needs "Apache common math" for CPU and "JCuda" for Nvidia GPU)
* C# version in beta because i didn't test it (and is based on a really old Java version of this lib)

# Example
The Java version has an Example, where the black rectangle needs to eat the green food and avoid the red enemies

# api
* [Setup](#Setup)
* [Training](#Training)
* [save AI](#saveAI)
* [info](#info)

# Setup

	.setup(int[] layout, int AgentCount, int Generationsteps, int MutationRate, boolean cuda);

* layout describes the NN. for example:

		{2,2,2} means 
		NN has 2 inputs
		2 hidden neurons in one layer
		2 outputs
			
* Generationsteps is the Amount of times it makes a generation before actually making children, so no AI can get to the next generation by luck

* If cuda is true the NN will run on your Nvidia GPU. Note: for small NNs CPU is recommended

# Training
* setInput

			.setInput(int Input, double Value, int Agent);
* runNN

			.runAgent(int Agent);
* runNN with multithreading

			.runAllAgents();
* getOutput(returns double)

			.getOutput(int Output, int Agent)
* setFitness

			.setFitness(float Fitness, int Agent);
* nextGenerationstep
			
			.nextGenerationstep();
			
# saveAI

* saveAI

			.saveNN(int Agent);
			
* saveBestAI

			.saveNN(JNN.getAgentIDPerFitnessList(0));
			
# info

* getGeneration(returns int)

			.getGeneration()
* getGenerationStep(returns int)

			.getGenerationStep()
* getScorelist(returns float)

			.getAgentFitnessPerFitnessList(int Number)
* getAIIDforScorelist(returns int)

			.getAgentIDPerFitnessList(int Number)
