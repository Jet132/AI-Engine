package GPUClasses;



import java.util.Random;

import AI_Engine.AIEngine;
import jcuda.Pointer;
import jcuda.Sizeof;
import jcuda.driver.CUdeviceptr;
import static jcuda.driver.JCudaDriver.*;

public class GPUAI {

	private float[] Inputs;
	private float[][] baises;
	private float[][] weights;
	private float[] Outputs;
	private float points[];
	
	public GPUAI(int Generationsteps, int[] layout) {
		points = new float[Generationsteps];
		this.Inputs = new float[layout[0]];
		baises = new float[layout.length - 1][];
		weights = new float[layout.length - 1][];
		int last_layer = layout[0];
		Random random = new Random();
		for (int i = 0; i < weights.length; i++) {
			baises[i] = new float[layout[i+1]];
			weights[i] = new float[last_layer*layout[i+1]];

			for (int j = 0; j < layout[i+1]; j++) {
				baises[i][j] = random.nextFloat() * 2 - 1;
				for (int k = 0; k < last_layer; k++) {
					weights[i][j*last_layer+k] = random.nextFloat() * 2 - 1;
				}
				
			}
			last_layer = layout[i+1];
		}
	}

	public void setInput(int Input, float Value) {
		Inputs[Input] = Value;
	}

	public void run() {
		float[] last_layer = Inputs;
		for (int i = 0; i < weights.length; i++) {
			last_layer = layer(last_layer, weights[i], baises[i], new int[] {last_layer.length,baises[i].length});
		}
		Outputs = last_layer;
	}
	
	float[] layer(float[] input, float[] weights, float[] baises, int[] wd) {
		// Allocate the device input data, and copy the
		// host input data to the device
		
		CUdeviceptr inputInput = new CUdeviceptr();
		cuMemAlloc(inputInput,input.length * Sizeof.FLOAT);
		cuMemcpyHtoD(inputInput, Pointer.to(input), input.length * Sizeof.FLOAT);
		CUdeviceptr inputWeights = new CUdeviceptr();
		cuMemAlloc(inputWeights, weights.length * Sizeof.FLOAT);
		cuMemcpyHtoD(inputWeights, Pointer.to(weights), weights.length * Sizeof.FLOAT);
		CUdeviceptr inputBaises = new CUdeviceptr();
		cuMemAlloc(inputBaises, baises.length * Sizeof.FLOAT);
		cuMemcpyHtoD(inputBaises, Pointer.to(baises), baises.length * Sizeof.FLOAT);
		// Allocate device output memory
		CUdeviceptr deviceOutput = new CUdeviceptr();
		cuMemAlloc(deviceOutput, baises.length * Sizeof.FLOAT);
		// Set up the kernel parameters: A pointer to an array
		// of pointers which point to the actual values.
		Pointer kernelParameters = Pointer.to(
				Pointer.to(new int[] {wd[0]}),
				Pointer.to(new int[] {wd[1]}),
				Pointer.to(inputInput),
				Pointer.to(inputWeights), 
				Pointer.to(inputBaises), 
				Pointer.to(deviceOutput));

		// Call the kernel function.
		int blockSizeX = 1000;
		int gridSizeX = (int) Math.ceil((double) baises.length/ blockSizeX);
		cuLaunchKernel(AIEngine.LayerFunction, 
				gridSizeX, 1, 1, // Grid dimension
				blockSizeX, 1, 1, // Block dimension
				0, null, // Shared memory size and stream
				kernelParameters, null // Kernel- and extra parameters
		);

		float hostOutput[] = new float[baises.length];
		cuMemcpyDtoH(Pointer.to(hostOutput), deviceOutput, baises.length * Sizeof.FLOAT);

		// Clean up.
		cuMemFree(inputInput);
		cuMemFree(inputWeights);
		cuMemFree(inputBaises);
		cuMemFree(deviceOutput);
		return hostOutput;
	}

	public float getOutput(int Output) {
		return Outputs[Output];
	}

	public void setScore(float Score) {
		points[AIEngine.generationStep] = Score;
	}

	float getAvarageScore() {
		float temp = 0;
		for (int i = 0; i < points.length; i++) {
			temp += points[i];
		}
		return temp / points.length;
	}

	float[][] getWeights() {
		return weights;
	}

	float[][] getBaises() {
		return baises;
	}

	void setWeights(float[][] weights) {
		this.weights = weights;
	}

	void setBaises(float[][] baises) {
		this.baises = baises;
	}

}
