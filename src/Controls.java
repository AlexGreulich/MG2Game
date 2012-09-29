import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Controls implements KeyListener{

	int richtung=0;
	boolean hoch = false,runter = false, links = false, rechts = false, inBewegung = false;
	boolean hochfeuern, runterfeuern, linksfeuern, rechtsfeuern;
	
	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
			case KeyEvent.VK_UP:
				hochfeuern = true;
				break;
			case KeyEvent.VK_DOWN:
				runterfeuern = true;
				break;
			case KeyEvent.VK_LEFT:
				linksfeuern = true;
				break;
			case KeyEvent.VK_RIGHT:
				rechtsfeuern = true;
				break;
			case KeyEvent.VK_W:
				inBewegung = true;
				hoch=true;
				break;
			case KeyEvent.VK_A:
				inBewegung = true;
				links=true;
				break;
			case KeyEvent.VK_S:
				inBewegung = true;
				runter=true;
				break;
			case KeyEvent.VK_D:
				inBewegung = true;
				rechts=true;
				break;
				
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()){
			case KeyEvent.VK_UP:
				hochfeuern = false;
				break;
			case KeyEvent.VK_DOWN:
				runterfeuern = false;
				break;
			case KeyEvent.VK_LEFT:
				linksfeuern = false;
				break;
			case KeyEvent.VK_RIGHT:
				rechtsfeuern = false;
				break;
			case KeyEvent.VK_W:
				hoch = false;
				inBewegung = false;
				break;
			case KeyEvent.VK_A:
				links = false;
				inBewegung = false;
				break;
			case KeyEvent.VK_S:
				runter = false;
				inBewegung = false;
				break;
			case KeyEvent.VK_D:
				rechts = false;
				inBewegung = false;
				break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	
}
