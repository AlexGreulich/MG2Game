import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Controls implements KeyListener{

	int direction=0;
	boolean up = false,down = false, left = false, right = false, isMoving = false;
	boolean fireUP, fireDOWN, fireLEFT, fireRIGHT;
	boolean equip =false;
	boolean levelChange = false;
	
	/*
	 * WASD - bewegen
	 * Pfeiltasten - schiessen
	 * E - equip
	 * Space - benutzen/ sprechen/ öffnen
	 *  TAB oder so - inventar durchlaufen
	 *  1-5 - itemslots anwählen
	 * 
	 * */
	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
			case KeyEvent.VK_UP:
				fireUP = true;
				break;
			case KeyEvent.VK_DOWN:
				fireDOWN = true;
				break;
			case KeyEvent.VK_LEFT:
				fireLEFT = true;
				break;
			case KeyEvent.VK_RIGHT:
				fireRIGHT = true;
				break;
			case KeyEvent.VK_W:
				isMoving = true;
				up=true;
				break;
			case KeyEvent.VK_A:
				isMoving = true;
				left=true;
				break;
			case KeyEvent.VK_S:
				isMoving = true;
				down=true;
				break;
			case KeyEvent.VK_D:
				isMoving = true;
				right=true;
				break;
			case KeyEvent.VK_E:
				equip = true;
				break;
			case KeyEvent.VK_C:
				levelChange = true;
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()){
			case KeyEvent.VK_UP:
				fireUP = false;
				break;
			case KeyEvent.VK_DOWN:
				fireDOWN = false;
				break;
			case KeyEvent.VK_LEFT:
				fireLEFT = false;
				break;
			case KeyEvent.VK_RIGHT:
				fireRIGHT = false;
				break;
			case KeyEvent.VK_W:
				up = false;
				isMoving = false;
				break;
			case KeyEvent.VK_A:
				left = false;
				isMoving = false;
				break;
			case KeyEvent.VK_S:
				down = false;
				isMoving = false;
				break;
			case KeyEvent.VK_D:
				right = false;
				isMoving = false;
				break;
			case KeyEvent.VK_Q:
				System.exit(0);
			case KeyEvent.VK_E:
				equip = false;
				break;
			case KeyEvent.VK_C:
				levelChange = false;
				break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
}
