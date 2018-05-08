package AI_Engine;

import static jcuda.driver.JCudaDriver.cuCtxCreate;
import static jcuda.driver.JCudaDriver.cuDeviceGet;
import static jcuda.driver.JCudaDriver.cuInit;
import static jcuda.driver.JCudaDriver.cuModuleGetFunction;
import static jcuda.driver.JCudaDriver.cuModuleLoad;

import CPUClasses.CPUAI;
import CPUClasses.CPUDataManager;
import CPUClasses.CPUEvolver;
import GPUClasses.GPUAI;
import GPUClasses.GPUDataManager;
import GPUClasses.GPUEvolver;
import jcuda.driver.CUcontext;
import jcuda.driver.CUdevice;
import jcuda.driver.CUfunction;
import jcuda.driver.CUmodule;
import jcuda.driver.JCudaDriver;

public class AIEngine {

	public static Object[] AIs;

	public static int[] layout;
	public static int AI_NR;
	public static int GENSTEP_NR;
	public static int MR;
	public static boolean cuda;
	public static CUfunction LayerFunction;

	public static int generationStep;
	public static int generation;
	public static Object evolver;
	public static Object DataManager;

	public int getGeneration() {
		return generation;
	}

	public int getGenerationStep() {
		return generationStep;
	}

	public int getAINumberPerScoreList(int Number) {
		if (cuda) {
			return ((GPUEvolver) evolver).score_list[Number];
		} else {
			return ((CPUEvolver) evolver).score_list[Number];
		}
	}

	public float getAIScorePerScoreList(int Number) {
		if (cuda) {
			return ((GPUEvolver) evolver).tempPoints[GPUEvolver.score_list[Number]];
		} else {
			return ((CPUEvolver) evolver).tempPoints[CPUEvolver.score_list[Number]];
		}
	}

	public void saveAI(int AI) {
		if (cuda) {
			((GPUDataManager) DataManager).saveAI(AI);
		} else {
			((CPUDataManager) DataManager).saveAI(AI);
		}
	}

	public void setupEngine(int[] layout, int AINumber, int Generationsteps, int MutationRate, boolean cuda) {
		this.layout = layout;
		AI_NR = AINumber;
		GENSTEP_NR = Generationsteps;
		MR = MutationRate;
		this.cuda = cuda;
		if (cuda) {
			GPUsetup();
		} else {
			CPUsetup();
		}
	}

	private void CPUsetup() {
		DataManager = new CPUDataManager();
		evolver = new CPUEvolver();
		AIs = new CPUAI[AI_NR];
		for (int i = 0; i < AI_NR; i++) {
			AIs[i] = new CPUAI(GENSTEP_NR, layout);
		}
	}

	private void GPUsetup() {
		cuInit(0);
		JCudaDriver.setExceptionsEnabled(true);
		CUdevice device = new CUdevice();
		cuDeviceGet(device, 0);
		CUcontext context = new CUcontext();
		cuCtxCreate(context, 0, device);

		CUmodule LayerModule = new CUmodule();
		cuModuleLoad(LayerModule, "LayerKernel.ptx");
		LayerFunction = new CUfunction();
		cuModuleGetFunction(LayerFunction, LayerModule, "layer");
		
		DataManager = new GPUDataManager();
		evolver = new GPUEvolver();
		AIs = new GPUAI[AI_NR];
		for (int i = 0; i < AI_NR; i++) {
			AIs[i] = new GPUAI(GENSTEP_NR, layout);
		}
	}

	public void setInput(int Input, double Value, int AI) {
		if (cuda) {
			((GPUAI) AIs[AI]).setInput(Input, (float)Value);
		} else {
			((CPUAI) AIs[AI]).setInput(Input, Value);
		}
	}

	public void runEngine(int AI) {
		if (cuda) {
			((GPUAI) AIs[AI]).run();
		} else {
			((CPUAI) AIs[AI]).run();
		}
	}

	public double getOutput(int Output, int AI) {
		if (cuda) {
			return ((GPUAI) AIs[AI]).getOutput(Output);
		} else {
			return ((CPUAI) AIs[AI]).getOutput(Output);
		}
	}

	public void setScore(float Score, int AI) {
		if (cuda) {
			((GPUAI) AIs[AI]).setScore(Score);
		} else {
			((CPUAI) AIs[AI]).setScore(Score);
		}
	}

	public void nextGenerationStep() {
		if (generationStep < GENSTEP_NR - 1) {
			generationStep++;
		} else {
			generationStep = 0;
			if (cuda) {
				((GPUEvolver) evolver).scoreLister();
				((GPUEvolver) evolver).evolve();
			} else {
				((CPUEvolver) evolver).scoreLister();
				((CPUEvolver) evolver).evolve();
			}
			generation++;
		}
	}

}
