# AI-Runner
AI Runner for running saved neural networks

there is a java and c# version
* java fully tested
* C# version in beta because i didn't test it

# Example
It has en build in example.
The actual engine is under AIEngine.

# api
* [Setup](#Setup)
* [Running](#Running)

# Setup

	new AIRunner(path to saved file);

* path should include the named with ending of the savefile

# Running
* setInput

			.setInput(int Input, double Value);
* runEngine

			.runEngine();
* getOutput(returns double)

			.getOutput(int Output)
