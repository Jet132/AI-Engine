package Handler;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import Main.MainGameLoop;

public class MouseHandler implements MouseListener {

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

		MainGameLoop.m_x = (int) e.getX();
		MainGameLoop.m_y = (int) e.getY();

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

}
