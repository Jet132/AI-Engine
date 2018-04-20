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
						* MainGameLoop.player[0],
				(MainGameLoop.screenheight / MainGameLoop.gridSize)
						* MainGameLoop.player[1],
				MainGameLoop.screenwidth / MainGameLoop.gridSize, MainGameLoop.screenwidth / MainGameLoop.gridSize);
		g2.setColor(new Color(0,200,0));
		g.fillRect(
				(MainGameLoop.screenwidth / MainGameLoop.gridSize)
						* MainGameLoop.food[0],
				(MainGameLoop.screenheight / MainGameLoop.gridSize)
						* MainGameLoop.food[1],
				MainGameLoop.screenwidth / MainGameLoop.gridSize, MainGameLoop.screenwidth / MainGameLoop.gridSize);

		g2.setColor(new Color(0,0,0));
		g.drawString("Points: " + String.valueOf(MainGameLoop.points), 10, 20);


		repaint();
	}

}
