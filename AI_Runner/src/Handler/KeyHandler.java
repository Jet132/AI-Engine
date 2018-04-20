package Handler;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Main.MainGameLoop;

public class KeyHandler implements KeyListener {

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (MainGameLoop.speed != 1) {
				MainGameLoop.speed /= 2;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			if (MainGameLoop.speed < 262144) {
				MainGameLoop.speed *= 2;
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}


	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

}
