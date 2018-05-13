package Lable;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;

import Main.MainGameLoop;

public final class Label extends JLabel {
	private static final long serialVersionUID = 1L;
	int temp = 0;

	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(new Color(0,0,0));
		g.fillRect(
				(MainGameLoop.screenwidth / MainGameLoop.gridSize)
						* MainGameLoop.player[MainGameLoop.AI.getAINumberPerScoreList(0)][0],
				(MainGameLoop.screenheight / MainGameLoop.gridSize)
						* MainGameLoop.player[MainGameLoop.AI.getAINumberPerScoreList(0)][1],
				MainGameLoop.screenwidth / MainGameLoop.gridSize, MainGameLoop.screenwidth / MainGameLoop.gridSize);
		g2.setColor(new Color(0,200,0));
		g.fillRect(
				(MainGameLoop.screenwidth / MainGameLoop.gridSize)
						* MainGameLoop.food[MainGameLoop.AI.getAINumberPerScoreList(0)][0],
				(MainGameLoop.screenheight / MainGameLoop.gridSize)
						* MainGameLoop.food[MainGameLoop.AI.getAINumberPerScoreList(0)][1],
				MainGameLoop.screenwidth / MainGameLoop.gridSize, MainGameLoop.screenwidth / MainGameLoop.gridSize);

		g2.setColor(new Color(0,0,0));
		g.drawString("G: " + String.valueOf(MainGameLoop.AI.getGeneration()), 10, 20);
		g.drawString("GS: " + String.valueOf(MainGameLoop.AI.getGenerationStep()), 10, 35);
		g.drawString("S: " + String.valueOf(MainGameLoop.speed), 10, 50);
		for (int i = 0; i < 20; i++) {
			g.drawString(String.valueOf(MainGameLoop.AI.getAINumberPerScoreList(i)) + ": "
					+ String.valueOf(MainGameLoop.AI.getAIScorePerScoreList(i)), 100, 20 + i * 10);
		}
		
		try {
			Thread.sleep(7);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		repaint();
	}

}
