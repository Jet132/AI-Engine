# AI-Engine
AI Engine for neural networks with evolutionary training

there is a java and c# version
* java fully tested
* C# version in beta because i didn't test it

# Example
It has en build in example.
The actual engine is under AIEngine.

# api
* [Setup](#Setup)
* [Training](#Training)
* [save AI](#saveAI)
* [info](#info)

# Setup

	.setupEngine(boolean menu, int InputNumber, int[] hidden, int OutputNumber, int AINumber, int Generationsteps, int MutationRate);

* menu should allways be false
* hidden should be an array with the neurons in each hiddenlayer

			for example {2,3} makes two hidden layers
			the first one with 2 neurons
			the second one with3
			
* Generationsteps is the Amount of times it makes a generation before actually making children, so no AI can get to the next generation by luck

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
