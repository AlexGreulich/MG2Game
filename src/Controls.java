import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Controls implements KeyListener{

	int direction=0;
	boolean up = false,down = false, left = false, right = false, isMoving = false, reload = false;
	boolean fire;
	boolean equip =false;
	boolean levelChange = false;
	boolean leftSwitch=false, rightSwitch=false;
	boolean melee;
	
	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
			case KeyEvent.VK_SPACE:
				fire = true;
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
			case KeyEvent.VK_UP:
				levelChange = true;
				break;
			case KeyEvent.VK_R:
				reload = true;
				break;
			case KeyEvent.VK_LEFT:
				leftSwitch = true;
				break;
			case KeyEvent.VK_RIGHT:
				rightSwitch = true;
				break;
			case KeyEvent.VK_C:
				melee = true;
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()){
			case KeyEvent.VK_SPACE:
				fire = false;
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
			case KeyEvent.VK_UP:
				levelChange = false;
				break;
			case KeyEvent.VK_R:
				reload = false;
				break;
			case KeyEvent.VK_LEFT:
				leftSwitch = false;
				break;
			case KeyEvent.VK_RIGHT:
				rightSwitch = false;
				break;
			case KeyEvent.VK_C:
				melee = false;
				break;
		}
	}
	
	public int getDirection(){
		return this.direction;
	}
	public void setDirection(int z){
		this.direction = z;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
}
