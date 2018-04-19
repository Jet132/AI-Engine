package AI_Engine;

import java.util.Random;

public class AI {
	private float CON[];
	private float points[];
	private float Inputs[];
	private float Outputs[];
	private int AI;
	
	private BrainCalculator Brain;
	
	private Random random = new Random();
	
	AI(int CON_NR, int Generationsteps, int Inputs, int Outputs, int AI) {
		Brain = new BrainCalculator();
		this.Inputs = new float[Inputs];
		this.Outputs = new float[Outputs];
		points = new float[Generationsteps];
		CON = new float[CON_NR];
		this.AI = AI;
		for (int i = 0; i < CON_NR; i++) {
			CON[i] = (random.nextInt(200)/100f)-1;
		}
	}
	
	void setInput(int Input, float Value) {
		Inputs[Input] = Value;
	}
	
	void run() {
		float[] temp = Brain.readDNA(CON, Inputs, AI);
		for(int i = 0; i < Outputs.length; i++) {
			Outputs[i] = temp[i];
		}
	}
	
	float  getOutput(int Output) {
		return Outputs[Output];
	}
	
	void setScore(float Score) {
		points[AIEngine.generationStep] = Score;
	}
	
	float getAvarageScore() {
		float temp = 0; 
		for(int i = 0; i < points.length; i++) {
			temp += points[i];
		}
		return temp/points.length;
	}
	
	float getCON(int Con) {
		return CON[Con];
	}
	
	void setCON(float[] Con) {
		CON = Con;
	}

}
