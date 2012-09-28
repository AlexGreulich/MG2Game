import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Controls implements KeyListener{

	int richtung=0;
	boolean hoch = false,runter = false, links = false, rechts = false, inBewegung = false;
	
	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
			case KeyEvent.VK_UP:
				inBewegung = true;
				hoch=true;
				break;
			case KeyEvent.VK_DOWN:
				inBewegung = true;
				runter=true;
				break;
			case KeyEvent.VK_LEFT:
				inBewegung = true;
				links=true;
				break;
			case KeyEvent.VK_RIGHT:
				inBewegung = true;
				rechts=true;
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()){
			case KeyEvent.VK_UP:
				hoch = false;
				inBewegung = false;
				break;
			case KeyEvent.VK_DOWN:
				runter = false;
				inBewegung = false;
				break;
			case KeyEvent.VK_LEFT:
				links = false;
				inBewegung = false;
				break;
			case KeyEvent.VK_RIGHT:
				rechts = false;
				inBewegung = false;
				break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	
}
