import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class BulletHandler implements Runnable{

	int gamespeed =20;
	GameWindow window;
	GamePanel panel;
	ArrayList<Bullet> bulletsInRoom;
	Controls controls;
	
	Player player;
	BufferedImage[] bulletpics;
	Rectangle levelBorders;
	
	int firerate; //ob das funktioniert?
	boolean fired;
	
	public BulletHandler(GameWindow w){
		
		window = w;
		panel = window.panel;
		bulletsInRoom = panel.bulletsInRoom;
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
						BufferedImage i = bulletset.getSubimage(a*5, b*5, 5,5);
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
	
	@Override
	public synchronized void run() {
		while(true){
			float onStart = System.currentTimeMillis();
			if(firerate == 50){
				fired = false;
				firerate =0;
			}
			
			// es kann nur gefeuert werden wenn abgefeuert == false ist, sonst wird feuerrate bis 50 gewartet
			if(!fired){
					// pfeiltasten abfragen um neue kugeln zu erzeugen
				if((controls.fireUP) && (controls.fireLEFT)){
					
					Bullet b = new Bullet(player.posX + 32, player.posY+24, 5);	//spaeter fuer damageonhit: player.aktuelleWaffe.damage
					b.setDirection(0);
					setImage(b);
					panel.bulletsInRoom.add(b);
					
					fired = true;
					
				}else if((controls.fireUP) && (controls.fireRIGHT)){
					Bullet b = new Bullet(player.posX + 32, player.posY+24, 5);	//spaeter fuer damageonhit: player.aktuelleWaffe.damage
					b.setDirection(5);
					setImage(b);
					panel.bulletsInRoom.add(b);
					fired = true;
					
				}else if((controls.fireDOWN) && (controls.fireLEFT)){
					Bullet b = new Bullet(player.posX + 32, player.posY+24, 5);	//spaeter fuer damageonhit: player.aktuelleWaffe.damage
					b.setDirection(2);
					setImage(b);
					panel.bulletsInRoom.add(b);
					fired = true;
					
				}else if((controls.fireDOWN) && (controls.fireRIGHT)){
					Bullet b = new Bullet(player.posX + 32, player.posY+24, 5);	//spaeter fuer damageonhit: player.aktuelleWaffe.damage
					b.setDirection(7);
					setImage(b);
					panel.bulletsInRoom.add(b);
					fired = true;
					
				}else if(controls.fireUP){
					Bullet b = new Bullet(player.posX + 32, player.posY+24, 5);	//spaeter fuer damageonhit: player.aktuelleWaffe.damage
					b.setDirection(3);
					setImage(b);
					panel.bulletsInRoom.add(b);
					fired = true;
					
				}else if(controls.fireDOWN){
					Bullet b = new Bullet(player.posX + 32, player.posY+24, 5);	//spaeter fuer damageonhit: player.aktuelleWaffe.damage
					b.setDirection(4);
					setImage(b);
					panel.bulletsInRoom.add(b);
					fired = true;
					
				}else if(controls.fireLEFT){
					Bullet b = new Bullet(player.posX + 32, player.posY+24, 5);	//spaeter fuer damageonhit: player.aktuelleWaffe.damage
					b.setDirection(1);
					setImage(b);
					panel.bulletsInRoom.add(b);
					fired = true;
					
				}else if(controls.fireRIGHT){
					Bullet b = new Bullet(player.posX + 32, player.posY+24, 5);	//spaeter fuer damageonhit: player.aktuelleWaffe.damage
					b.setDirection(6);
					setImage(b);
					panel.bulletsInRoom.add(b);
					fired = true;
				}
			}
			
			
			firerate++;
			//kugel-array durchgehen und bewegung/position durchfhren/aktualisieren
			
			for (int index=0;index < panel.bulletsInRoom.size();index++){
				
//				if(kugelnimraum.get(index).posX <= 0){
//					
//				}
				
				if(bulletsInRoom.size() >0){
				
					Bullet b= bulletsInRoom.get(index);
					if (b != null){
						
						switch(b.getDirection()){
						
						case(0):
							b.posX--;
							b.posY--;
							break;
						case(1):
							b.posX--;
							break;
						case(2):
							b.posX--;
							b.posY++;
							break;
						case(3):
							b.posY--;
							break;
						case(4):
							b.posY++;
							break;
						case(5):
							b.posX++;
							b.posY--;
							break;
						case(6):
							b.posX++;
							break;
						case(7):
							b.posX++;
							b.posY++;
							break;
						}
						
						if((b.posX < 0) || (b.posX > levelBorders.width) || (b.posY <0) || (b.posY > levelBorders.height)){
							panel.bulletsInRoom.remove(index);
						}
//						if(b.richtung == 0){
//							b.posX --;
//							b.posY --;
//						}if(b.richtung == 1){
//							b.posY ++;
//						}if(b.richtung == 2){
//							b.posX++;
//							b.posY--;
//						}if(b.richtung == 3){
//							b.posX--;
//						}if(b.richtung == 4){
//							b.posX++;
//						}if(b.richtung == 5){
//							b.posX--;
//							b.posY++;
//						}if(b.richtung == 6){
//							b.posY++;
//						}if(b.richtung == 7){
//							b.posX++;
//							b.posY++;
//						} 
					}
				}
				
			}
//			Thread.yield();
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
