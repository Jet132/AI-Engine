package Main;

import java.awt.Point;
import java.awt.PointerInfo;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import AI_Engine.AIEngine;
import Lable.Gui;
import Lable.Label;

public class MainGameLoop {

	public static Random random = new Random();

	public static JFrame jf1;
	public static int screenwidth = 800, screenheight = 800;
	public static Label lbl1;
	public static int speed = 1;
	public static int temp = 0;
	public static int AIs = 5000;
	public static int gridSize = 16;
	public static int player[][] = new int[AIs][2];
	public static int food[][] = new int[AIs][2];
	public static int points[] = new int[AIs];
	public static AIEngine AI;

	public static PointerInfo a;
	public static Point b;
	public static int m_x;
	public static int m_y;

	static Timer position;

	public static void main(String[] args) {
		AI = new AIEngine();
		int[] hidden = {2};
		AI.setupEngine(false, 2, hidden, 2, AIs, 6, 50);
		for (int i = 0; i < AIs; i++) {
			player[i][0] = random.nextInt(gridSize);
			player[i][1] = random.nextInt(gridSize);
			food[i][0] = random.nextInt(gridSize);
			food[i][1] = random.nextInt(gridSize);
			points[i] = 0;
		}
		new Gui();
		position = new Timer();
		position.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				try {
					Thread.sleep(MainGameLoop.speed - 1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for (int i = 0; i < AIs; i++) {
					AI.setInput(0, player[i][0]-food[i][0], i);
					AI.setInput(1, player[i][1]-food[i][1], i);
					/*AI.setInput(2, player[i][0], i);
					AI.setInput(3, player[i][1], i);
					AI.setInput(4, food[i][0], i);
					AI.setInput(5, food[i][1], i);*/
					
					
					AI.runEngine(i);

					if (AI.getOutput(0, i) < 0) {
						if (player[i][0] > 0) {
							player[i][0]--;
						}
					} else if (AI.getOutput(0, i) > 0) {
						if (player[i][0] < gridSize) {
							player[i][0]++;
						}
					}
					
					if (AI.getOutput(1, i) < 0) {
						if (player[i][1] > 0) {
							player[i][1]--;
						}
					} else if (AI.getOutput(1, i) > 0) {
						if (player[i][1] < gridSize) {
							player[i][1]++;
						}
					}
					
					if(player[i][0] ==  food[i][0] && player[i][1] ==  food[i][1]) {
						points[i] ++;
						food[i][0] = random.nextInt(gridSize);
						food[i][1] = random.nextInt(gridSize);
					}
				}
				if (temp > 500) {
					for (int i = 0; i < AIs; i++) {
						AI.setScore(points[i]+food_distance(i), i);
						player[i][0] = random.nextInt(gridSize);
						player[i][1] = random.nextInt(gridSize);
						food[i][0] = random.nextInt(gridSize);
						food[i][1] = random.nextInt(gridSize);
						points[i] = 0;
					}
					temp = 0;
					AI.nextGenerationStep();
				} else {
					temp++;
				}

			}

		}, 0, 1);

	}
	
	public static float food_distance(int AI) {
		float max_x_distance;
		float max_y_distance;
		float x_distance;
		float y_distance;
		float x_value;
		float y_value;

		if (food[AI][0] > player[AI][0]) {
			max_x_distance = food[AI][0];
		} else {
			max_x_distance = gridSize - food[AI][0];
		}
		if (food[AI][1] > player[AI][1]) {
			max_y_distance = food[AI][1];
		} else {
			max_y_distance = gridSize - food[AI][1];
		}
		if (player[AI][0] > food[AI][0]) {
			x_distance =player[AI][0] - food[AI][0];
		} else {
			x_distance = food[AI][0] - player[AI][0];
		}
		if (player[AI][1] > food[AI][1]) {
			y_distance = player[AI][1] - food[AI][1];
		} else {
			y_distance = food[AI][1] - player[AI][1];
		}
		x_value = 1 - (x_distance / max_x_distance);
		y_value = 1 - (y_distance / max_y_distance);
		if (player[AI][0] == 0 || player[AI][0] == gridSize) {
			x_value = 0;
		}
		if (player[AI][1] == 0 || player[AI][1] == gridSize) {
			y_value = 0;
		}
		return ((x_value + y_value) / 2);
	}
}
