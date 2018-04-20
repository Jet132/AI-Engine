package Main;

import java.awt.Point;
import java.awt.PointerInfo;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import AI_Runner.AIRunner;
import Lable.Gui;
import Lable.Label;

public class MainGameLoop {

	public static Random random = new Random();

	public static JFrame jf1;
	public static int screenwidth = 800, screenheight = 800;
	public static Label lbl1;
	public static int speed = 1;
	public static int temp = 0;
	public static int gridSize = 16;
	public static int player[] = new int[2];
	public static int food[] = new int[2];
	public static int points;
	public static AIRunner AI;

	public static PointerInfo a;
	public static Point b;
	public static int m_x;
	public static int m_y;

	static Timer position;

	public static void main(String[] args) {
		AI = new AIRunner("1518. AI (2, 2, [I@6e6c98c4).txt");
		int[] hidden = {2};

			player[0] = random.nextInt(gridSize);
			player[1] = random.nextInt(gridSize);
			food[0] = random.nextInt(gridSize);
			food[1] = random.nextInt(gridSize);
			points = 0;
		
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
					AI.setInput(0, player[0]-food[0]);
					AI.setInput(1, player[1]-food[1]);
					/*AI.setInput(2, player[0], i);
					AI.setInput(3, player[1], i);
					AI.setInput(4, food[0], i);
					AI.setInput(5, food[1], i);*/
					
					
					AI.run();

					if (AI.getOutput(0) < 0) {
						if (player[0] > 0) {
							player[0]--;
						}
					} else if (AI.getOutput(0) > 0) {
						if (player[0] < gridSize) {
							player[0]++;
						}
					}
					
					if (AI.getOutput(1) < 0) {
						if (player[1] > 0) {
							player[1]--;
						}
					} else if (AI.getOutput(1) > 0) {
						if (player[1] < gridSize) {
							player[1]++;
						}
					}
					
					if(player[0] ==  food[0] && player[1] ==  food[1]) {
						points ++;
						food[0] = random.nextInt(gridSize);
						food[1] = random.nextInt(gridSize);
					}
				
				if (temp > 500) {
						player[0] = random.nextInt(gridSize);
						player[1] = random.nextInt(gridSize);
						food[0] = random.nextInt(gridSize);
						food[1] = random.nextInt(gridSize);
						points = 0;
					
					temp = 0;
				} else {
					temp++;
				}

			}

		}, 0, 1);

	}
	
	public static float food_distance() {
		float max_x_distance;
		float max_y_distance;
		float x_distance;
		float y_distance;
		float x_value;
		float y_value;

		if (food[0] > player[0]) {
			max_x_distance = food[0];
		} else {
			max_x_distance = gridSize - food[0];
		}
		if (food[1] > player[1]) {
			max_y_distance = food[1];
		} else {
			max_y_distance = gridSize - food[1];
		}
		if (player[0] > food[0]) {
			x_distance =player[0] - food[0];
		} else {
			x_distance = food[0] - player[0];
		}
		if (player[1] > food[1]) {
			y_distance = player[1] - food[1];
		} else {
			y_distance = food[1] - player[1];
		}
		x_value = 1 - (x_distance / max_x_distance);
		y_value = 1 - (y_distance / max_y_distance);
		if (player[0] == 0 || player[0] == gridSize) {
			x_value = 0;
		}
		if (player[1] == 0 || player[1] == gridSize) {
			y_value = 0;
		}
		return ((x_value + y_value) / 2);
	}
}
