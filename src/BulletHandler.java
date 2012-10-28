import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class BulletHandler implements Runnable{

	int gamespeed =5;
	GameWindow window;
	GamePanel panel;
	ArrayList<Bullet> bulletsInRoom;
	ArrayList<Enemy> enemylist;
	Controls controls;
	
	Player player;
	BufferedImage[] bulletpics;
	Rectangle levelBorders;
	
	int firerate; 
	boolean fired;
	
	public BulletHandler(GameWindow w){
		
		window = w;
		panel = window.panel;
		bulletsInRoom = window.bulletsInRoom;
		enemylist = window.enemylist;
		controls = window.controls;
		player = window.player;
		levelBorders = new Rectangle(0,0,panel.mapWidth,panel.mapHeight);
		firerate =0;
		
		try{
			int index =0;
			bulletpics = new BufferedImage[8];
			
			BufferedImage bulletset = ImageIO.read(getClass().getResource("resources/bulletversuch.gif"));
			for(int a = 0;a <= 2; a++){
				for(int b = 0; b <= 2; b++){
					if(((a==1)&&(b==1))==false){
						BufferedImage i = bulletset.getSubimage(a*3, b*3, 3,3);
						bulletpics[index] = i;
						index++;
					}
				}
			}
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public void setImage(Bullet b){
		b.image = bulletpics[b.getDirection()];
		
	}
	
	public void collisionCheck(){
		if(bulletsInRoom.size() >0){
			for(Bullet b: bulletsInRoom){
				b.updateBounds();
				if(enemylist.size()>0){
					for(Enemy e: enemylist){
						if(b.bounds.intersects(e.enemyBounds)){
							e.energy = e.energy - 0.1f;
						//	bulletsInRoom.remove(b);
							
						/*-> kugel soll entfernt werden wenn getroffen, das
						 * gibt aber einen thread konflikt, daher 
						 * muss es eine temporäre kopie der bulletlist geben,
						 * mit der die eigentliche liste zum schluss 
						 * überschrieben wird 
						 * */
						}
					}
				}
				
			}
		}
	}
	
	@Override
	public synchronized void run() {
		while(true){
			float onStart = System.currentTimeMillis();
			if(firerate == 50){
				fired = false;
				firerate =0;
			}
			collisionCheck();
			// es kann nur gefeuert werden wenn abgefeuert == false ist, sonst wird feuerrate bis 50 gewartet
			if(!fired){
					// pfeiltasten abfragen um neue kugeln zu erzeugen
				if((controls.fireUP) && (controls.fireLEFT)){
					
					Bullet b = new Bullet(player.posX + 16, player.posY+24, 5);	//spaeter fuer damageonhit: player.aktuelleWaffe.damage
					b.setDirection(0);
					setImage(b);
					bulletsInRoom.add(b);
					
					fired = true;
					
				}else if((controls.fireUP) && (controls.fireRIGHT)){
					Bullet b = new Bullet(player.posX + 16, player.posY+8, 5);	//spaeter fuer damageonhit: player.aktuelleWaffe.damage
					b.setDirection(5);
					setImage(b);
					bulletsInRoom.add(b);
					fired = true;
					
				}else if((controls.fireDOWN) && (controls.fireLEFT)){
					Bullet b = new Bullet(player.posX + 16, player.posY+8, 5);	//spaeter fuer damageonhit: player.aktuelleWaffe.damage
					b.setDirection(2);
					setImage(b);
					bulletsInRoom.add(b);
					fired = true;
					
				}else if((controls.fireDOWN) && (controls.fireRIGHT)){
					Bullet b = new Bullet(player.posX + 16, player.posY+8, 5);	//spaeter fuer damageonhit: player.aktuelleWaffe.damage
					b.setDirection(7);
					setImage(b);
					bulletsInRoom.add(b);
					fired = true;
					
				}else if(controls.fireUP){
					Bullet b = new Bullet(player.posX + 16, player.posY+8, 5);	//spaeter fuer damageonhit: player.aktuelleWaffe.damage
					b.setDirection(3);
					setImage(b);
					bulletsInRoom.add(b);
					fired = true;
					
				}else if(controls.fireDOWN){
					Bullet b = new Bullet(player.posX + 16, player.posY+8, 5);	//spaeter fuer damageonhit: player.aktuelleWaffe.damage
					b.setDirection(4);
					setImage(b);
					bulletsInRoom.add(b);
					fired = true;
					
				}else if(controls.fireLEFT){
					Bullet b = new Bullet(player.posX + 16, player.posY+8, 5);	//spaeter fuer damageonhit: player.aktuelleWaffe.damage
					b.setDirection(1);
					setImage(b);
					bulletsInRoom.add(b);
					fired = true;
					
				}else if(controls.fireRIGHT){
					Bullet b = new Bullet(player.posX + 16, player.posY+8, 5);	//spaeter fuer damageonhit: player.aktuelleWaffe.damage
					b.setDirection(6);
					setImage(b);
					bulletsInRoom.add(b);
					fired = true;
				}
			}
			firerate++;
			//kugel-array durchgehen und bewegung/position durchfhren/aktualisieren
			if(bulletsInRoom.size() >0){
			for (int index=0;index < bulletsInRoom.size();index++){
				
					Bullet b= bulletsInRoom.get(index);
					if (b != null){
						
						switch(b.getDirection()){
						
						case(0):
							b.posX = b.posX - 20;
							b.posY = b.posY - 20;							//b.posY--;
							break;
						case(1):
							b.posX = b.posX - 20;//--;
							break;
						case(2):
							b.posX = b.posX - 20;//--;
							b.posY = b.posY + 20;//++;
							break;
						case(3):
							b.posY = b.posY - 20;//--;
							break;
						case(4):
							b.posY = b.posY + 20;//++;
							break;
						case(5):
							b.posX = b.posX + 20;//++;
							b.posY = b.posY - 20;//--;
							break;
						case(6):
							b.posX = b.posX + 20;//++;
							break;
						case(7):
							b.posX = b.posX + 20;//++;
							b.posY = b.posY + 20;//++;
							break;
						}
						
						if(b.posX < 0){
							bulletsInRoom.remove(index);
						}else if(b.posX > levelBorders.width){
							bulletsInRoom.remove(index);
						}else if(b.posY <0){
							bulletsInRoom.remove(index);
						}else if(b.posY > levelBorders.height){
							bulletsInRoom.remove(index);
						}
					}
				}
				
			}
			float onEnd = System.currentTimeMillis() - onStart;
			if( gamespeed> onEnd){
				try{
					if((gamespeed -(int)onEnd) > 0){
						Thread.sleep(gamespeed - (int)onEnd);
					}
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
		}
	}
}
