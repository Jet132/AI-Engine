package Lable;

import javax.swing.JFrame;
import Main.MainGameLoop;

public class Gui {

	public Gui() {
		MainGameLoop.jf1 = new JFrame();
		MainGameLoop.jf1.setSize(MainGameLoop.screenwidth+100, MainGameLoop.screenheight+100);
		MainGameLoop.jf1.setLocationRelativeTo(null);
		MainGameLoop.jf1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		MainGameLoop.jf1.setLayout(null);
		MainGameLoop.jf1.addKeyListener(new Handler.KeyHandler());
		MainGameLoop.jf1.addMouseListener(new Handler.MouseHandler());
		MainGameLoop.jf1.addMouseMotionListener(new Handler.MouseMovement());
		MainGameLoop.jf1.setTitle("Pong AI");
		MainGameLoop.jf1.setResizable(false);
		MainGameLoop.jf1.requestFocus();
		MainGameLoop.jf1.setVisible(true);
		
		MainGameLoop.lbl1 = new Label();
		MainGameLoop.lbl1.setBounds(0, 0, MainGameLoop.screenwidth+100, MainGameLoop.screenheight+100);
		MainGameLoop.lbl1.setVisible(true);
		MainGameLoop.jf1.add(MainGameLoop.lbl1);
	}

}
