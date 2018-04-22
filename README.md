# AI-Runner
AI Runner for running saved neural networks

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
