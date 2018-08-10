# JNN
AI Engine for neural networks with evolutionary training. You will need to install the apache common math library (see in the example) and it can run on Nvidia GPUs if you install the JCuda library (see in the example).

# api
* [Setup](#Setup)
* [Training](#Training)
* [save NN](#save NN)
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
* set Input

			.setInput(int Input, double Value, int Agent);
* run Agent

			.runAgent(int Agent);
* run Agent with multithreading

			.runAllAgents();
* get Output (returns double)

			.getOutput(int Output, int Agent)
* set Fitness

			.setFitness(float Fitness, int Agent);
* next Generationstep
			
			.nextGenerationstep();
			
# save NN

* save NN

			.saveNN(int Agent);
			
* save best NN

			.saveNN(JNN.getAgentIDPerFitnessList(0));
			
# info

* get Generation (returns int)

			.getGeneration()
* get Generationstep (returns int)

			.getGenerationstep()
* get Fitness(returns float)

			.getAgentFitnessPerFitnessList(int Number)
* get Agent ID orderd with Fitness (returns int)

			.getAgentIDPerFitnessList(int Number)
