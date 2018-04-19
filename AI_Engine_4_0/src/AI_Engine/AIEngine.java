package AI_Engine;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class AIEngine {

	static int Layers = 1;
	static int Inputs = 0;
	static int Outputs = 0;
	static int Hidden = 0;
	static int AI_NR = 1;
	static int CON_NR = 0;
	static int GENSTEP_NR = 1;
	static int generationStep = 0;
	static int generation = 0;
	static int DNA_Per = 50;

	int m_x = 0, m_y = 0, l_m_x = 0, l_m_y = 0;
	JFrame jf1;
	JButton save;
	JButton saveE;
	JButton load;
	ANNDisplayer lbl1;
	String[] tempFile;
	boolean overrideWait;

	static AI[] AIs;
	Evolver evolver;
	public DataManager DataManager;

	public int getGeneration() {
		return generation;
	}

	public int getGenerationStep() {
		return generationStep;
	}

	public int getAINumberPerScoreList(int Number) {
		return Evolver.score_list[Number];
	}

	public float getAIScorePerScoreList(int Number) {
		return Evolver.tempPoints[Evolver.score_list[Number]];
	}

	public void openANNDisplayer() {
		GUI();
	}

	public void saveAI(int AI) {
		DataManager.saveAI(AI);
	}

	void extractData(String[] File) {
		int Inputs = Integer.valueOf(File[0]);
		int Outputs = Integer.valueOf(File[1]);
		int Hidden = Integer.valueOf(File[2]);
		int Layers = Integer.valueOf(File[3]);
		int CON_NR = Integer.valueOf(File[4]);
		int DNA_Per = Integer.valueOf(File[5]);
		int GENSTEP_NR = Integer.valueOf(File[6]);
		int AI_NR = Integer.valueOf(File[7]);
		int generation = Integer.valueOf(File[8]);
		int generationStep = Integer.valueOf(File[9]);
		float[][] CON = new float[AI_NR][CON_NR];
		int temp = 0;
		for (int i = 0; i < AI_NR; i++) {
			for (int j = 0; j < CON_NR; j++) {
				CON[i][j] = Float.valueOf(File[temp + 10]);
				temp++;
			}
		}
		overrideEngine(Inputs, Outputs, Hidden, Layers, AI_NR, GENSTEP_NR, DNA_Per, generation, generationStep, CON);
	}

	void overrideEngine(int Inputnumber, int Outputnumber, int Hiddennumber, int Layernumber, int AInumber,
			int Generationsteps, int MutationPerCent, int Generation, int GenerationStep, float[][] CON) {
		Inputs = Inputnumber;
		Outputs = Outputnumber;
		Hidden = Hiddennumber;
		Layers = Layernumber;
		AI_NR = AInumber;
		GENSTEP_NR = Generationsteps;
		DNA_Per = MutationPerCent;
		generation = Generation;
		generationStep = GenerationStep;

		DataManager = new DataManager();
		evolver = new Evolver();
		AIs = new AI[AI_NR];
		if (CON == null) {
			CON_NR = Inputs * Hidden + Hidden * Outputs;
			if (Layers > 1) {
				for (int i = 0; i < Layers - 1; i++) {
					CON_NR += Hidden * Hidden;
				}
			}
			for (int i = 0; i < AI_NR; i++) {
				AIs[i] = new AI(CON_NR, GENSTEP_NR, Inputs, Outputs, i);
			}
		}else {
			CON_NR = Inputs * Hidden + Hidden * Outputs;
			if (Layers > 1) {
				for (int i = 0; i < Layers - 1; i++) {
					CON_NR += Hidden * Hidden;
				}
			}
			for (int i = 0; i < AI_NR; i++) {
				AIs[i] = new AI(CON_NR, GENSTEP_NR, Inputs, Outputs, i);
					AIs[i].setCON(CON[i]);
			}
		}
		
	}

	public void setupEngine(int Inputnumber, int Outputnumber, int Hiddennumber, int Layernumber, int AInumber,
			int Generationsteps, int MutationPerCent) {
		overrideEngine(Inputnumber, Outputnumber, Hiddennumber, Layernumber, AInumber, Generationsteps, MutationPerCent,
				0, 0, null);
		GUI();

	}

	public void setInput(int Input, float Value, int AI) {
		AIs[AI].
		setInput(Input, Value);
	}

	public void runEngine(int AI) {
		AIs[AI].run();
	}

	public float getOutput(int Output, int AI) {
		return AIs[AI].getOutput(Output);
	}

	public void setScore(float Score, int AI) {
		AIs[AI].setScore(Score);
	}

	public void nextGenerationStep() {
		if (generationStep < GENSTEP_NR - 1) {
			generationStep++;
		} else {
			if (overrideWait) {
				extractData(tempFile);
				tempFile = null;
				overrideWait = false;
			} else {
				generationStep = 0;
				evolver.scoreLister();
				evolver.evolve();
				generation++;
			}
		}
	}

	void GUI() {

		jf1 = new JFrame();
		jf1.setSize(800, 800);
		jf1.setLocationRelativeTo(null);
		jf1.setLayout(null);
		jf1.setTitle("Best ANN");
		jf1.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent arg0) {
				m_x = (int) arg0.getX();
				m_y = (int) arg0.getY();
				ANNDisplayer.p_x += (m_x - l_m_x) * (1 / ANNDisplayer.zoom);
				ANNDisplayer.p_y += (m_y - l_m_y) * (1 / ANNDisplayer.zoom);
				l_m_x = m_x;
				l_m_y = m_y;

			}

			@Override
			public void mouseMoved(MouseEvent arg0) {
				l_m_x = m_x;
				l_m_y = m_y;
				m_x = (int) arg0.getX();
				m_y = (int) arg0.getY();

			}

		});
		jf1.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent arg0) {
				float zoomLevel = arg0.getWheelRotation() * 0.1f;
				ANNDisplayer.zoom -= zoomLevel / (1 / ANNDisplayer.zoom);
				if (ANNDisplayer.zoom > 1) {
					ANNDisplayer.zoom = 1;
				}
			}

		});
		jf1.setResizable(true);
		jf1.requestFocus();
		jf1.setVisible(true);

		save = new JButton("save Best AI");
		save.setVisible(true);
		save.setBounds(13, 13, 120, 25);
		save.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {

				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				DataManager.saveAI(0);
				System.out.println("Saved");

			}
		});

		saveE = new JButton("save current Enginestate");
		saveE.setVisible(true);
		saveE.setBounds(143, 13, 180, 25);
		saveE.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				DataManager.saveCurrentEngineState();

			}
		});

		load = new JButton("getEnginestate");
		load.setVisible(true);
		load.setBounds(332, 13, 125, 25);
		load.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				final JFileChooser fc = new JFileChooser();
				/*
				 * FileFilter filter = new FileNameExtensionFilter(".cos", "cos");
				 * fc.setFileFilter(filter);
				 */
				int response = fc.showOpenDialog(load);
				if (response == JFileChooser.APPROVE_OPTION) {
					tempFile = DataManager.getEngineState(fc.getSelectedFile().toString());
					lbl1 = null;
					load = null;
					saveE = null;
					save = null;
					jf1 = null;
					overrideWait = true;
					
				}

			}
		});

		lbl1 = new ANNDisplayer();
		lbl1.setVisible(true);
		lbl1.setBounds(0, 0, 8000, 8000);

		jf1.add(load);
		jf1.add(saveE);
		jf1.add(save);
		jf1.add(lbl1);

	}

}
