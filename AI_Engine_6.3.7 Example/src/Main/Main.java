package Main;

import java.awt.Dimension;
import Main.Label;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JFrame;

import AI_Engine.AIEngine;

class run implements Runnable {
	int[] pt;
	int AI;

	run(int AI) {
		this.AI = AI;
	}

	public void run() {
		for (int i = 0; i < 500; i++) {
			try {
				Thread.sleep(Main.speed - 1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			AIEngine.setInput(0, Main.player[AI][0] - Main.food[0], AI);
			AIEngine.setInput(1, Main.player[AI][1] - Main.food[1], AI);
			AIEngine.setInput(2, Main.checkPosition(Main.player[AI][0] - 1, Main.player[AI][1]), AI);
			AIEngine.setInput(3, Main.checkPosition(Main.player[AI][0] + 1, Main.player[AI][1]), AI);
			AIEngine.setInput(4, Main.checkPosition(Main.player[AI][0], Main.player[AI][1] - 1), AI);
			AIEngine.setInput(5, Main.checkPosition(Main.player[AI][0], Main.player[AI][1] + 1), AI);
			AIEngine.setInput(6, Main.checkPosition(Main.player[AI][0] - 1, Main.player[AI][1] - 1), AI);
			AIEngine.setInput(7, Main.checkPosition(Main.player[AI][0] - 1, Main.player[AI][1] + 1), AI);
			AIEngine.setInput(8, Main.checkPosition(Main.player[AI][0] + 1, Main.player[AI][1] - 1), AI);
			AIEngine.setInput(9, Main.checkPosition(Main.player[AI][0] + 1, Main.player[AI][1] + 1), AI);

			AIEngine.runNN(AI);

			if (AIEngine.getOutput(0, AI) < 0.5) {
				if (Main.player[AI][0] > 0) {
					Main.player[AI][0]--;
				}
			} else if (AIEngine.getOutput(0, AI) > 0.5) {
				if (Main.player[AI][0] < Main.gridSize) {
					Main.player[AI][0]++;
				}
			}

			if (AIEngine.getOutput(1, AI) < 0.5) {
				if (Main.player[AI][1] > 0) {
					Main.player[AI][1]--;
				}
			} else if (AIEngine.getOutput(1, AI) > 0.5) {
				if (Main.player[AI][1] < Main.gridSize) {
					Main.player[AI][1]++;
				}
			}

			if (Main.player[AI][0] == Main.food[0] && Main.player[AI][1] == Main.food[1]) {
				Main.points[AI]++;
				pt = Main.newPosition();
				Main.player[AI][0] = pt[0];
				Main.player[AI][1] = pt[1];
			}
			for (int j = 0; j < Main.enemies; j++) {
				if (Main.player[AI][0] == Main.enemy[j][0] && Main.player[AI][1] == Main.enemy[j][1]) {
					/*
					 * if (points[AI] <= 0) { points[AI]--; } else { points[AI] = 0; }
					 */
					Main.points[AI]--;
					pt = Main.newPosition();
					Main.player[AI][0] = pt[0];
					Main.player[AI][1] = pt[1];
				}
			}
		}
	}
}

public class Main {

	public static Random random = new Random();

	static JFrame Frame;
	static Label Label;

	static int speed = 1;
	static int AIs = 1000;
	static int enemies = 30;
	static int gridSize = 16;
	static int player[][] = new int[AIs][2];
	static int food[] = new int[2];
	static int enemy[][] = new int[enemies][2];
	static int points[] = new int[AIs];

	static AIEngine AIEngine = new AIEngine();

	public static void main(String[] args) {
		setup();
		while (true) {
			run();
		}
	}

	static int[] newPosition() {
		int[] temp = { random.nextInt(gridSize), random.nextInt(gridSize) };
		boolean temp_2 = true;
		while (temp_2) {
			if (temp[0] == food[0] && temp[1] == food[1]) {
				temp = new int[] { random.nextInt(gridSize), random.nextInt(gridSize) };
				temp_2 = true;
			} else {
				for (int j = 0; j < enemies; j++) {
					if (temp[0] == enemy[j][0] && temp[1] == enemy[j][1]) {
						temp = new int[] { random.nextInt(gridSize), random.nextInt(gridSize) };
						temp_2 = true;
					} else {
						temp_2 = false;
					}
				}
			}
		}
		return temp;
	}

	static int checkPosition(int x, int y) {
		if (x < 0 || x > gridSize) {
			return 0;
		}
		if (y < 0 || y > gridSize) {
			return 0;
		}
		for (int i = 0; i < enemies; i++) {
			if (x == enemy[i][0] && y == enemy[i][1]) {
				return -1;
			}
		}
		return 0;
	}

	static void run() {
		Thread[] Thread = new Thread[AIs];
		for (int i = 0; i < AIs; i++) {
			Thread[i] = new Thread(new run(i));
		}

		for (int i = 0; i < AIs; i++) {
			Thread[i].start();
		}

		for (int i = 0; i < AIs; i++) {
			try {
				Thread[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		int[] pt;
		for (int i = 0; i < AIs; i++) {
			AIEngine.setFitness(points[i] + food_distance(i), i);
			pt = newPosition();
			player[i][0] = pt[0];
			player[i][1] = pt[1];
			pt = newPosition();

			points[i] = 0;
		}
		AIEngine.nextGenerationstep();
	}

	static void setup() {
		Frame = new JFrame();
		Frame.setVisible(true);
		Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Frame.setResizable(false);
		Frame.setLocationRelativeTo(null);
		Frame.setPreferredSize(new Dimension(900, 900));
		Frame.setTitle("Neat test");
		Frame.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					if (speed != 1) {
						speed /= 2;
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					if (speed < 262144) {
						speed *= 2;
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					System.exit(0);
				}
			}
		});
		int[] pt;
		for (int i = 0; i < AIs; i++) {
			pt = newPosition();
			player[i][0] = pt[0];
			player[i][1] = pt[1];
			points[i] = 0;
		}
		pt = newPosition();
		food[0] = pt[0];
		food[1] = pt[1];
		for (int j = 0; j < enemies; j++) {
			pt = newPosition();
			enemy[j][0] = pt[0];
			enemy[j][1] = pt[1];
		}

		AIEngine.setupEngine(new int[] { 10, 5, 2 }, AIs, 4, 50, false);

		Label = new Label();
		Label.setVisible(true);
		Label.setSize(Frame.getContentPane().getWidth(), Frame.getContentPane().getHeight());
		Frame.add(Label);
		Frame.pack();
		Frame.requestFocus();
		System.out.println("finished loading");
	}

	static float food_distance(int AI) {
		float max_x_distance;
		float max_y_distance;
		float x_distance;
		float y_distance;
		float x_value;
		float y_value;

		if (food[0] > player[AI][0]) {
			max_x_distance = food[0];
		} else {
			max_x_distance = gridSize - food[0];
		}
		if (food[1] > player[AI][1]) {
			max_y_distance = food[1];
		} else {
			max_y_distance = gridSize - food[1];
		}
		if (player[AI][0] > food[0]) {
			x_distance = player[AI][0] - food[0];
		} else {
			x_distance = food[0] - player[AI][0];
		}
		if (player[AI][1] > food[1]) {
			y_distance = player[AI][1] - food[1];
		} else {
			y_distance = food[1] - player[AI][1];
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
