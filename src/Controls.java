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
	EnemyController enemycontroller;
	GameWindow window;
	int movementkeycounter = 0;
	
	public Controls(GameWindow w){
		window = w;
		enemycontroller = window.enemycontrol;
	}
	
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
				melee = true;
				if(enemycontroller.collisionDetect()==false){
					fire = true;
				}
				//fire = true;
				break;
			case KeyEvent.VK_W:
				if(up==false){
					increaseCounter();
				}
				up=true;
				break;
			case KeyEvent.VK_A:
				if(left==false){
					increaseCounter();
				}
				left=true;
				break;
			case KeyEvent.VK_S:
				if(down==false){
					increaseCounter();
				}
				down=true;
				break;
			case KeyEvent.VK_D:
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
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()){
			case KeyEvent.VK_SPACE:
				fire = false;
				melee = false;
				break;
			case KeyEvent.VK_W:
				up = false;
				decreaseCounter();
				break;
			case KeyEvent.VK_A:
				left = false;
				decreaseCounter();
				break;
			case KeyEvent.VK_S:
				down = false;
				decreaseCounter();
				break;
			case KeyEvent.VK_D:
				right = false;
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
