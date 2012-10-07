import java.awt.Rectangle;
import java.awt.image.BufferedImage;


public class Bullet extends Entity{
	
	int speed;
	int damageOnHit;
	int direction;
	Player player;
	Rectangle bounds;
	BufferedImage image;
	
	public Bullet(int x, int y, int d){//, int direction
		
		posX = x;
		posY = y;
		bounds = new Rectangle(this.posX,this.posY,3,3);
		energy = 100;
		
		speed = 10;	
		damageOnHit = d;
		
	}
	public void setDirection(int d){
		direction = d;
	}
	
	public void updateBounds(){
		bounds.setBounds(this.posX,this.posY,3,3);
	}
	
	public int getDirection(){
		return this.direction;
	}

}
