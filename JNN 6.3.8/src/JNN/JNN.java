package JNN;

import static jcuda.driver.JCudaDriver.*;

import CPUClasses.CPUAgent;
import CPUClasses.CPUDataManager;
import CPUClasses.CPUEvolver;
import GPUClasses.GPUAgent;
import GPUClasses.GPUDataManager;
import GPUClasses.GPUEvolver;
import jcuda.driver.CUcontext;
import jcuda.driver.CUdevice;
import jcuda.driver.CUfunction;
import jcuda.driver.CUmodule;
import jcuda.driver.JCudaDriver;

public class JNN {

	public static Object[] Agents;

	public static int[] layout;
	public static int Agent_NR;
	public static int GENSTEP_NR;
	public static int MR;
	public static boolean cuda;
	public static CUfunction LayerFunction;
	public static CUcontext context;

	public static int generationStep;
	public static int generation;
	public static Object evolver;
	public static Object DataManager;

	public static int getGeneration() {
		return generation;
	}

	public static int getGenerationStep() {
		return generationStep;
	}

	public static int getAgentIDPerFitnessList(int Number) {
		if (cuda) {
			return GPUEvolver.fitness_list[Number];
		} else {
			return CPUEvolver.fitness_list[Number];
		}
	}

	public static float getAgentFitnessPerFitnessList(int Number) {
		if (cuda) {
			return GPUEvolver.tempPoints[GPUEvolver.fitness_list[Number]];
		} else {
			return CPUEvolver.tempPoints[CPUEvolver.fitness_list[Number]];
		}
	}

	public static void saveAI(int AI) {
		if (cuda) {
			((GPUDataManager) DataManager).saveAI(AI);
		} else {
			((CPUDataManager) DataManager).saveAI(AI);
		}
	}

	public void setupEngine(int[] layout, int AgentCount, int Generationsteps, int MutationRate, boolean cuda) {
		this.layout = layout;
		Agent_NR = AgentCount;
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
		Agents = new CPUAgent[Agent_NR];
		for (int i = 0; i < Agent_NR; i++) {
			Agents[i] = new CPUAgent(GENSTEP_NR, layout);
		}
	}

	private void GPUsetup() {
		cuInit(0);
		JCudaDriver.setExceptionsEnabled(true);
		CUdevice device = new CUdevice();
		cuDeviceGet(device, 0);
		context = new CUcontext();
		cuCtxCreate(context, 0, device);

		CUmodule LayerModule = new CUmodule();
		cuModuleLoad(LayerModule, "LayerKernel.ptx");
		LayerFunction = new CUfunction();
		cuModuleGetFunction(LayerFunction, LayerModule, "layer");

		DataManager = new GPUDataManager();
		evolver = new GPUEvolver();
		Agents = new GPUAgent[Agent_NR];
		for (int i = 0; i < Agent_NR; i++) {
			Agents[i] = new GPUAgent(GENSTEP_NR, layout);
		}
	}

	public static void setInput(int Input, double Value, int AI) {
		if (cuda) {
			((GPUAgent) Agents[AI]).setInput(Input, (float) Value);
		} else {
			((CPUAgent) Agents[AI]).setInput(Input, Value);
		}
	}

	public static void runAgent(int Agent) {
		if (cuda) {
			cuCtxPushCurrent(context);
			((GPUAgent) Agents[Agent]).runNN();
			cuCtxPopCurrent(context);
		} else {
			((CPUAgent) Agents[Agent]).runNN();
		}
	}

	public static void runAllAgents() {
		if (cuda) {
			for (int i = 0; i < Agents.length; i++) {
				((GPUAgent) Agents[i]).runNNMT();
			}
			for (int i = 0; i < Agents.length; i++) {
				((GPUAgent) Agents[i]).joinThread();
			}
			cuCtxSetCurrent(context);
		} else {
			for (int i = 0; i < Agents.length; i++) {
				((CPUAgent) Agents[i]).runNNMT();
			}
			for (int i = 0; i < Agents.length; i++) {
				((CPUAgent) Agents[i]).joinThread();
			}
		}
	}

	public static double getOutput(int Output, int Agent) {
		if (cuda) {
			return ((GPUAgent) Agents[Agent]).getOutput(Output);
		} else {
			return ((CPUAgent) Agents[Agent]).getOutput(Output);
		}
	}

	public static void setFitness(float Score, int Agent) {
		if (cuda) {
			((GPUAgent) Agents[Agent]).setScore(Score);
		} else {
			((CPUAgent) Agents[Agent]).setScore(Score);
		}
	}

	public static void nextGenerationstep() {
		if (generationStep < GENSTEP_NR - 1) {
			generationStep++;
		} else {
			generationStep = 0;
			if (cuda) {
				((GPUEvolver) evolver).fitnessLister();
				((GPUEvolver) evolver).evolve();
			} else {
				((CPUEvolver) evolver).fitnessLister();
				((CPUEvolver) evolver).evolve();
			}
			generation++;
		}
	}

}
