package Handler;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import Main.MainGameLoop;

public class MouseMovement implements MouseMotionListener {

@Override
public void mouseDragged(MouseEvent e) {
	// TODO Auto-generated method stub
	
}

@Override
public void mouseMoved(MouseEvent e) {
	MainGameLoop.m_x = (int) e.getX();
	MainGameLoop.m_y = (int) e.getY();
	
}
}
