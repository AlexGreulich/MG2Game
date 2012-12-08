import java.awt.Rectangle;
import java.awt.image.BufferedImage;


public class Bullet extends Entity{
	
	int speed;
	int damageOnHit;
	int direction;
	Rectangle bounds;
	BufferedImage image;
	boolean playerBullet;
	
	public Bullet(int x, int y, int d, boolean f){//, int direction
		
		posX = x;
		posY = y;
		bounds = new Rectangle(this.posX,this.posY,3,3);
		energy = 100;
		
		speed = 10;	
		damageOnHit = d;
		playerBullet = f;
		
	}
	
	public int dealDamage(){
		return damageOnHit;
	}
	
	public void setDirection(int d){
		this.direction = d;
	}
	
	public void updateBounds(){
		bounds.setBounds(this.posX,this.posY,3,3);
	}
	
	public int getDirection(){
		return this.direction;
	}
}
