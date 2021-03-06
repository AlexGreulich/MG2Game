import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class Player {

	int posX, posY; 
	float energy;
	GameWindow window;
	Controls controls;
	BufferedImage img,image;
	float animation = 0.0f;
	BufferedImage[] hoch_cycle,runter_cycle, links_cycle,rechts_cycle,hochlinks_cycle,hochrechts_cycle,runterlinks_cycle,runterrechts_cycle;
	Rectangle playerBounds;
	ArrayList <Item> equipment;
	int inventorySelect = 0;
	boolean meleeReady;
	int meleeCounter;
	
	int ammo;
	Point playermiddle = new Point(0,0);
	boolean isDead = false;
	boolean isInjured =false;
	boolean isOkay = true;
	float speed = 2;
	
	Item[] weapons = new Item[2];	//0 - melee , 1 - distance
	
	int cash =0;
	
	public Player(GameWindow w){
		
		window = w;
		controls = w.controls;
		
		meleeReady = false;
		meleeCounter = 100;
		
		posX =500;
		posY = 250;
		energy = 100f;
		image = null;
		playerBounds = new Rectangle(this.posX+8, this.posY, 48, 80);
		hoch_cycle = new BufferedImage[8];
		runter_cycle  = new BufferedImage[8];
		links_cycle = new BufferedImage[8];
		rechts_cycle = new BufferedImage[8];
		hochlinks_cycle = new BufferedImage[8];
		hochrechts_cycle = new BufferedImage[8];
		runterlinks_cycle = new BufferedImage[8];
		runterrechts_cycle = new BufferedImage[8];
		getMiddle();
		
		equipment = new ArrayList<Item>();
		ammo = 10;
		try{
			img = ImageIO.read(getClass().getResource("resources/charset.gif"));

			for(int a = 0; a< img.getHeight()/48; a++){
				for(int b =0; b<img.getWidth()/32;b++){
					
					switch(a){
						case(0):
							runter_cycle[b] = img.getSubimage(b*32, a*48, 32, 48);
							break;
						case(1):	
							links_cycle[b] = img.getSubimage(b*32, a*48, 32, 48);
							break;
						case(2):
							rechts_cycle[b] = img.getSubimage(b*32, a*48, 32, 48);
							break;
						case(3):
							hoch_cycle[b] = img.getSubimage(b*32, a*48, 32, 48);
							break;
						case(4):
							runterlinks_cycle[b] = img.getSubimage(b*32, a*48, 32, 48);
							break;
						case(5):
							runterrechts_cycle[b] = img.getSubimage(b*32, a*48, 32, 48);
							break;
						case(6):
							hochrechts_cycle[b] = img.getSubimage(b*32, a*48, 32, 48);
							break;
						case(7):
							hochlinks_cycle[b] = img.getSubimage(b*32, a*48, 32, 48);
							break;
					}
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public boolean meleeHitDetection(int enemyPosX, int enemyPosY){
		double distance = Math.sqrt( (enemyPosX-posX)*(enemyPosX-posX) + (enemyPosY-posY)*(enemyPosY-posY) );
		if(distance<50){//50=meleerange
			 return true;
		}else{
			return false;
		}
	}
	public void meleeReset(){
		meleeCounter = 100;
		meleeReady = false;
	}
	
	public void getMiddle(){
		playermiddle.setLocation(posX+16, posY+16);
	}
	public int getX(){
		return posX;
	}
	public int getY(){
		return posY;
	}
	public void setX(int x){
		posX += x;
	}
	public void setY(int y){
		posY += y;
	}
	public void updateBounds(){
		playerBounds.setBounds(this.posX+4, this.posY+4, 24, 40);
	}
	public void changeState(int option){
		switch(option){
		case 0:
			this.isOkay = true;
			break;
		case 1:
			this.isInjured = true;
			this.isOkay = false;
			break;
		case 2:
			this.isInjured =false;
			this.isDead = true;
			break;
		}
	}
	
	public BufferedImage getImage(){
		if(controls.isMoving){
			animation += 0.33;
		}else{
			animation = 0.0f;
		}
		if((int)animation == 8){
			animation =0.0f;
		}
		switch(controls.direction){
			case(0):
				image = runter_cycle[(int)animation];
				break;
			case(1):
				image = links_cycle[(int)animation];
				break;
			case(2):
				image = rechts_cycle[(int)animation];
				break;
			case(3):
				image = hoch_cycle[(int)animation];
				break;
			case(4):
				image = runterlinks_cycle[(int)animation];
				break;
			case(5):
				image = runterrechts_cycle[(int)animation];
				break;
			case(6):
				image = hochrechts_cycle[(int)animation];
				break;
			case(7):
				image = hochlinks_cycle[(int)animation];
				break;
		}
		return image;
	}
	
	public Item getSelectedItemInInv(){
		
		return null;
	}
}
