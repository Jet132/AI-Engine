package GPUClasses;

import java.util.Random;

import JNN.JNN;
import jcuda.Pointer;
import jcuda.Sizeof;
import jcuda.driver.CUdeviceptr;
import static jcuda.driver.JCudaDriver.*;

public class GPUAgent {

	private float[] Inputs;
	private int[] weightsD;
	private int[] baisesD;
	private float[] Outputs;
	private float points[];
	private CUdeviceptr[] weightPointers;
	private CUdeviceptr[] baisesPointers;
	private CUdeviceptr temp;
	private Thread thread;
	private Runnable runNN;

	public GPUAgent(int Generationsteps, int[] layout) {
		points = new float[Generationsteps];
		this.Inputs = new float[layout[0]];
		float[][] baises = new float[layout.length - 1][];
		float[][] weights = new float[layout.length - 1][];
		weightsD = new int[weights.length];
		baisesD = new int[baises.length];
		int last_layer = layout[0];
		Random random = new Random();
		for (int i = 0; i < weights.length; i++) {
			baises[i] = new float[layout[i + 1]];
			weights[i] = new float[last_layer * layout[i + 1]];
			weightsD[i] = weights[i].length;
			baisesD[i] = baises[i].length;
			
			for (int j = 0; j < layout[i + 1]; j++) {
				baises[i][j] = random.nextFloat() * 2 - 1;
				for (int k = 0; k < last_layer; k++) {
					weights[i][j * last_layer + k] = random.nextFloat() * 2 - 1;
				}
			}
			last_layer = layout[i + 1];
		}
		runNN = new Runnable() {
			
			@Override
			public void run() {
				cuCtxPushCurrent(JNN.context);
				temp = new CUdeviceptr();
				cuMemAlloc(temp, Inputs.length * Sizeof.FLOAT);
				cuMemcpyHtoD(temp, Pointer.to(Inputs), Inputs.length * Sizeof.FLOAT);

				int last_layer = Inputs.length;
				for (int i = 0; i < weightsD.length-1; i++) {

					layer(i, last_layer);
					last_layer = baisesD[i];
				}
				Outputs = final_layer(weightsD.length-1, last_layer);
				cuCtxPopCurrent(JNN.context);
			}
		};

		weightPointers = new CUdeviceptr[weights.length];
		for (int i = 0; i < weights.length; i++) {
			weightPointers[i] = new CUdeviceptr();
			cuMemAlloc(weightPointers[i], weights[i].length * Sizeof.FLOAT);
			cuMemcpyHtoD(weightPointers[i], Pointer.to(weights[i]), weights[i].length * Sizeof.FLOAT);
		}

		baisesPointers = new CUdeviceptr[baises.length];
		for (int i = 0; i < baises.length; i++) {
			baisesPointers[i] = new CUdeviceptr();
			cuMemAlloc(baisesPointers[i], baises[i].length * Sizeof.FLOAT);
			cuMemcpyHtoD(baisesPointers[i], Pointer.to(baises[i]), baises[i].length * Sizeof.FLOAT);
		}
	}

	public void setInput(int Input, float Value) {
		Inputs[Input] = Value;
	}

	public void runNN() {
		temp = new CUdeviceptr();
		cuMemAlloc(temp, Inputs.length * Sizeof.FLOAT);
		cuMemcpyHtoD(temp, Pointer.to(Inputs), Inputs.length * Sizeof.FLOAT);

		int last_layer = Inputs.length;
		for (int i = 0; i < weightsD.length-1; i++) {

			layer(i, last_layer);
			last_layer = baisesD[i];
		}
		Outputs = final_layer(weightsD.length-1, last_layer);
	}
	
	public void runNNMT() {
		thread = new Thread(runNN);
		thread.start();
	}
	
	public void joinThread() {
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void layer(int layer, int inputsize) {
		// Allocate device output memory
		CUdeviceptr deviceOutput = new CUdeviceptr();
		cuMemAlloc(deviceOutput, baisesD[layer] * Sizeof.FLOAT);
		// Set up the kernel parameters: A pointer to an array
		// of pointers which point to the actual values.
		Pointer kernelParameters = Pointer.to(Pointer.to(new int[] { inputsize }),
				Pointer.to(new int[] { baisesD[layer] }), Pointer.to(temp), Pointer.to(weightPointers[layer]),
				Pointer.to(baisesPointers[layer]), Pointer.to(deviceOutput));

		// Call the kernel function.
		int blockSizeX = 1000;
		int gridSizeX = (int) Math.ceil((double) baisesD[layer] / blockSizeX);
		cuLaunchKernel(JNN.LayerFunction, gridSizeX, 1, 1, // Grid dimension
				blockSizeX, 1, 1, // Block dimension
				0, null, // Shared memory size and stream
				kernelParameters, null // Kernel- and extra parameters
		);

		cuMemFree(temp);
		temp = deviceOutput;

	}

	float[] final_layer(int layer, int inputsize) {
		// Allocate device output memory
		CUdeviceptr deviceOutput = new CUdeviceptr();
		cuMemAlloc(deviceOutput, baisesD[layer] * Sizeof.FLOAT);
		// Set up the kernel parameters: A pointer to an array
		// of pointers which point to the actual values.
		Pointer kernelParameters = Pointer.to(Pointer.to(new int[] { inputsize }),
				Pointer.to(new int[] { baisesD[layer] }), Pointer.to(temp), Pointer.to(weightPointers[layer]),
				Pointer.to(baisesPointers[layer]), Pointer.to(deviceOutput));

		// Call the kernel function.
		int blockSizeX = 1024;
		int gridSizeX = (int) Math.ceil((double) baisesD[layer] / blockSizeX);
		cuLaunchKernel(JNN.LayerFunction, gridSizeX, 1, 1, // Grid dimension
				blockSizeX, 1, 1, // Block dimension
				0, null, // Shared memory size and stream
				kernelParameters, null // Kernel- and extra parameters
		);

		

		float hostOutput[] = new float[baisesD[layer]];
		cuMemcpyDtoH(Pointer.to(hostOutput), deviceOutput, baisesD[layer] * Sizeof.FLOAT);
		
		cuMemFree(temp);
		
		return hostOutput;
	}

	public float getOutput(int Output) {
		return Outputs[Output];
	}

	public void setScore(float Score) {
		points[JNN.generationStep] = Score;
	}

	float getAvarageScore() {
		float temp = 0;
		for (int i = 0; i < points.length; i++) {
			temp += points[i];
		}
		return temp / points.length;
	}

	float[][] getWeights() {
		float[][] weights = new float[JNN.layout.length - 1][];
		int last_layer = JNN.layout[0];
		for(int i = 0; i < weights.length; i++) {
			weights[i] = new float[last_layer * JNN.layout[i + 1]];
			cuMemcpyDtoH(Pointer.to(weights[i]), weightPointers[i], weights[i].length * Sizeof.FLOAT);
			last_layer = JNN.layout[i + 1];
		}
		return weights;
	}

	float[][] getBaises() {
		float[][] baises = new float[JNN.layout.length - 1][];
		for(int i = 0; i < baises.length; i++) {
			baises[i] = new float[JNN.layout[i + 1]];
			cuMemcpyDtoH(Pointer.to(baises[i]), baisesPointers[i], baises[i].length * Sizeof.FLOAT);
		}
		return baises;
	}

	void setWeights(float[][] weights) {
		weightsD = new int[weights.length];
		for(int i = 0; i < weightsD.length; i++) {
			weightsD[i] = weights[i].length;
		}
		
		for (int i = 0; i < weightPointers.length; i++) {
			cuMemFree(weightPointers[i]);
		}
		weightPointers = new CUdeviceptr[weights.length];
		for (int i = 0; i < weights.length; i++) {
			weightPointers[i] = new CUdeviceptr();
			cuMemAlloc(weightPointers[i], weights[i].length * Sizeof.FLOAT);
			cuMemcpyHtoD(weightPointers[i], Pointer.to(weights[i]), weights[i].length * Sizeof.FLOAT);
		}
	}

	void setBaises(float[][] baises) {
		baisesD = new int[baises.length];
		for(int i = 0; i < baisesD.length; i++) {
			baisesD[i] = baises[i].length;
		}
		
		for (int i = 0; i < baisesPointers.length; i++) {
			cuMemFree(baisesPointers[i]);
		}
		baisesPointers = new CUdeviceptr[baises.length];
		for (int i = 0; i < baises.length; i++) {
			baisesPointers[i] = new CUdeviceptr();
			cuMemAlloc(baisesPointers[i], baises[i].length * Sizeof.FLOAT);
			cuMemcpyHtoD(baisesPointers[i], Pointer.to(baises[i]), baises[i].length * Sizeof.FLOAT);
		}
	}

}
