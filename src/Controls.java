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
				richtung = 3;
				hoch=true;
				break;
			case KeyEvent.VK_DOWN:
				inBewegung = true;
				richtung=0;
				runter=true;
				break;
			case KeyEvent.VK_LEFT:
				inBewegung = true;
				richtung=1;
				links=true;
				break;
			case KeyEvent.VK_RIGHT:
				inBewegung = true;
				richtung=2;
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
