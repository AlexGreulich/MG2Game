import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Bullet extends Objekt{
	
	int speed;
	int damageOnHit;
	int richtung;
	Player player;
	
	
	
	BufferedImage image;
	public Bullet(int x, int y, int d){//, int direction
		
		posX = x;
		posY = y;
		
		energy = 100;
		
		speed = 10;	//?
		damageOnHit = d;
		//richtung = direction;
		
		
	}
	public void setDirection(int d){
		richtung = d;
	}
	
	public int getDirection(){
		return this.richtung;
	}

}
