package AI_Engine;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;

public class ANNDisplayer extends JLabel {
	
	
	private int MaxNumber() {
		int x = 0;
		int y[] = new int[4];
		y[0] = AIEngine.Inputs;
		y[1] = AIEngine.Outputs;
		y[2] = AIEngine.Hidden;
		y[3] = AIEngine.Layers;
		for (int i = 0; i < 4; i++) {
			if (x < y[i]) {
				x = y[i];
			}
		}
		return x;
	}
	
	static float tempNeurons[][];

	private static final long serialVersionUID = 1L;
	int NP[][][] = new int[2 + AIEngine.Layers][MaxNumber()][2];
	static float zoom = 0.5f;
	static int p_x = 0, p_y = 0;
	int NSize = 40;
	int CON_NR = 0;

	int NeutronColorconverter(float Input) {
		int x = (int) (100 / (1 / (Input)));
		if(x < -100){
			x = -100;
		}
		if(x > 100){
			x = 100;
		}
		return x;
	}

	int Strokeconverter(float Input) {
		float x = 0;
		if (Input > 0) {
			x = ((4 * Input) * zoom);
		} else {
			if (Input < 0) {
				x = ((-4 * Input) * zoom);
			} else {
				x = 0;
			}
		}
		return (int) x;
	}

	int LineColorConverter(float Input) {
		int x = 0;
		if (Input > 0) {
			x = 255;
		} else {
			if (Input < 0) {
				if (Input == 0) {
					x = 150;
				}
			}
		}
		return x;
	}

	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(new Color(100, 100, 100));
		g.fillRect(0, 0, 800000, 800000);
		g.translate(400, 400);

		
		//System.out.println(NeutronColorconverter(1));
		g.setColor(new Color(150, 150, 150));
		for (int i = 0; i < AIEngine.Inputs; i++) {
			NP[0][i][0] = (int) (((NSize) + p_x) * zoom);
			NP[0][i][1] = (int) (((i * NSize * 2) + p_y) * zoom);
		}

		for (int i = 0; i < AIEngine.Layers; i++) {
			for (int j = 0; j < AIEngine.Hidden; j++) {
				NP[i + 1][j][0] = (int) ((((1 + i) * 300) + p_x) * zoom);
				NP[i + 1][j][1] = (int) (((j * NSize * 2) + p_y) * zoom);
			}
		}

		for (int i = 0; i < AIEngine.Outputs; i++) {
			NP[AIEngine.Layers + 1][i][0] = (int) ((((AIEngine.Layers + 1) * 300) + p_x) * zoom);
			NP[AIEngine.Layers + 1][i][1] = (int) (((i * NSize * 2) + p_y) * zoom);
		}

		for (int i = 0; i < AIEngine.Inputs; i++) {
			for (int j = 0; j < AIEngine.Hidden; j++) {
				g2.setStroke(new BasicStroke(Strokeconverter(AIEngine.AIs[Evolver.score_list[0]].getCON(CON_NR))));
				g.setColor(new Color(LineColorConverter(AIEngine.AIs[Evolver.score_list[0]].getCON(CON_NR)),
						LineColorConverter(AIEngine.AIs[Evolver.score_list[0]].getCON(CON_NR)),
						LineColorConverter(AIEngine.AIs[Evolver.score_list[0]].getCON(CON_NR))));
				CON_NR++;
				g.drawLine((int) (NP[0][i][0] + ((NSize / 2) * zoom)), (int) (NP[0][i][1] + ((NSize / 2) * zoom)),
						(int) (NP[1][j][0] + ((NSize / 2) * zoom)), (int) (NP[1][j][1] + ((NSize / 2) * zoom)));
			}
		}
		if (AIEngine.Layers > 1) {
			for (int i = 1; i < AIEngine.Layers; i++) {
				for (int j = 0; j < AIEngine.Hidden; j++) {
					for (int k = 0; k < AIEngine.Hidden; k++) {
						g2.setStroke(new BasicStroke(Strokeconverter(AIEngine.AIs[Evolver.score_list[0]].getCON(CON_NR))));
						g.setColor(new Color(LineColorConverter(AIEngine.AIs[Evolver.score_list[0]].getCON(CON_NR)),
								LineColorConverter(AIEngine.AIs[Evolver.score_list[0]].getCON(CON_NR)),
								LineColorConverter(AIEngine.AIs[Evolver.score_list[0]].getCON(CON_NR))));
						CON_NR++;
						g.drawLine((int) (NP[i][j][0] + ((NSize / 2) * zoom)),
								(int) (NP[i][j][1] + ((NSize / 2) * zoom)),
								(int) (NP[i + 1][k][0] + ((NSize / 2) * zoom)),
								(int) (NP[i + 1][k][1] + ((NSize / 2) * zoom)));
					}
				}
			}
		}
		for (int i = 0; i < AIEngine.Hidden; i++) {
			for (int j = 0; j < AIEngine.Outputs; j++) {
				g2.setStroke(new BasicStroke(Strokeconverter(AIEngine.AIs[Evolver.score_list[0]].getCON(CON_NR))));
				g.setColor(new Color(LineColorConverter(AIEngine.AIs[Evolver.score_list[0]].getCON(CON_NR)),
						LineColorConverter(AIEngine.AIs[Evolver.score_list[0]].getCON(CON_NR)),
						LineColorConverter(AIEngine.AIs[Evolver.score_list[0]].getCON(CON_NR))));
				CON_NR++;
				g.drawLine((int) (NP[AIEngine.Layers][i][0] + ((NSize / 2) * zoom)),
						(int) (NP[AIEngine.Layers][i][1] + ((NSize / 2) * zoom)),
						(int) (NP[AIEngine.Layers + 1][j][0] + ((NSize / 2) * zoom)),
						(int) (NP[AIEngine.Layers + 1][j][1] + ((NSize / 2) * zoom)));
			}
		}
		CON_NR = 0;
		for (int i = 0; i < AIEngine.Inputs; i++) {
			g.setColor(new Color(150, (150 + (NeutronColorconverter(tempNeurons[0][i]))), 150));
			g.fillOval(NP[0][i][0], NP[0][i][1], (int) ((NSize) * zoom), (int) ((NSize) * zoom));
		}
		for (int i = 0; i < AIEngine.Layers; i++) {
			for (int j = 0; j < AIEngine.Hidden; j++) {
				g.setColor(new Color(150, (150 + (NeutronColorconverter(tempNeurons[i + 1][j]))), 150));
				g.fillOval(NP[i + 1][j][0], NP[i + 1][j][1], (int) ((NSize) * zoom), (int) ((NSize) * zoom));

			}
		}
		for (int i = 0; i < AIEngine.Outputs; i++) {
			g.setColor(new Color(150,
					(150 + (NeutronColorconverter(tempNeurons[AIEngine.Layers + 1][i]))), 150));
			g.fillOval(NP[AIEngine.Layers + 1][i][0], NP[AIEngine.Layers + 1][i][1], (int) ((NSize) * zoom),
					(int) ((NSize) * zoom));

		}

		repaint();
	}
}
