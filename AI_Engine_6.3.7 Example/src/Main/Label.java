package Main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;

public final class Label extends JLabel {
	private static final long serialVersionUID = 1L;
	int temp = 0;
	
	int blocksize = 800 / Main.gridSize;

	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(new Color(0,0,0));
		g.fillRect(
				(blocksize)
						* Main.player[Main.AIEngine.getAINumberPerScoreList(0)][0],
				(blocksize)
						* Main.player[Main.AIEngine.getAINumberPerScoreList(0)][1],
				blocksize, blocksize);
		g2.setColor(new Color(0,200,0));
		g.fillRect(
				(blocksize)
						* Main.food[0],
				(blocksize)
						* Main.food[1],
				blocksize, blocksize);
		g2.setColor(new Color(200, 0, 0));
		for (int i = 0; i < Main.enemies; i++) {
			g.fillRect(blocksize * Main.enemy[i][0], blocksize * Main.enemy[i][1], blocksize, blocksize);
		}

		g2.setColor(new Color(0,0,0));
		g.drawString("G: " + String.valueOf(Main.AIEngine.getGeneration()), 10, 20);
		g.drawString("GS: " + String.valueOf(Main.AIEngine.getGenerationStep()), 10, 35);
		g.drawString("S: " + String.valueOf(Main.speed), 10, 50);
		for (int i = 0; i < 20; i++) {
			g.drawString(String.valueOf(Main.AIEngine.getAINumberPerScoreList(i)) + ": "
					+ String.valueOf(Main.AIEngine.getAIScorePerScoreList(i)), 100, 20 + i * 10);
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
