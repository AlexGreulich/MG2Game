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
	int movementkeycounter = 0;
	boolean itemUse;
	
	public void increaseCounter(){
		movementkeycounter++;
		isMoving = true;
	}
	
	public void decreaseCounter(){
		movementkeycounter--;
		if (movementkeycounter==0){
			isMoving = false;
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
			case KeyEvent.VK_SPACE:
				fire = true;
				break;
			case KeyEvent.VK_W:
				//isMoving = true;
				if(up==false){
					increaseCounter();
				}
				up=true;
				break;
			case KeyEvent.VK_A:
				//isMoving = true;
				if(left==false){
					increaseCounter();
				}
				left=true;
				break;
			case KeyEvent.VK_S:
				//isMoving = true;
				if(down==false){
					increaseCounter();
				}
				down=true;
				break;
			case KeyEvent.VK_D:
				//isMoving = true;
				if(right==false){
					increaseCounter();
				}
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
			case KeyEvent.VK_DOWN:
				itemUse= true;
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
				//isMoving = false;
				decreaseCounter();
				break;
			case KeyEvent.VK_A:
				left = false;
				//isMoving = false;
				decreaseCounter();
				break;
			case KeyEvent.VK_S:
				down = false;
				//isMoving = false;
				decreaseCounter();
				break;
			case KeyEvent.VK_D:
				right = false;
				//isMoving = false;
				decreaseCounter();
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
			case KeyEvent.VK_DOWN:
				itemUse= false;
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
