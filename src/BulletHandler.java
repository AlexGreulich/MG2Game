import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class BulletHandler implements Runnable{

	int gamespeed =5;
	GameWindow window;
	GamePanel panel;
	ArrayList<Bullet> kugelnimraum;
	Controls controls;
	
	Player player;
	BufferedImage[] bulletpics;
	Rectangle raumgrenzen;
	
	int feuerrate; //ob das funktioniert?
	boolean abgefeuert;
	
	public BulletHandler(GameWindow w){
		
		window = w;
		panel = window.panel;
		kugelnimraum = panel.kugelnimraum;
		controls = window.controls;
		player = window.player;
		raumgrenzen = new Rectangle(0,0,panel.kartenbreite,panel.kartenhoehe);
		feuerrate =0;
		
		
		try{
			int index =0;
			bulletpics = new BufferedImage[8];
			
			BufferedImage bulletset = ImageIO.read(getClass().getResource("resources/bulletversuch.gif"));
			for(int a = 0;a < 2; a++){
				for(int b = 0; b < 2; b++){
					if((a != 1) && (b !=1)){
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
			float amAnfang = System.currentTimeMillis();
			if(feuerrate == 50){
				abgefeuert = false;
				feuerrate =0;
			}
			
			// es kann nur gefeuert werden wenn abgefeuert == false ist, sonst wird feuerrate bis 50 gewartet
			if(!abgefeuert){
					// pfeiltasten abfragen um neue kugeln zu erzeugen
				if((controls.hochfeuern) && (controls.linksfeuern)){
					
					Bullet b = new Bullet(player.posX + 32, player.posY+64, 5);	//später für damageonhit: player.aktuelleWaffe.damage
					b.setDirection(0);
					setImage(b);
					panel.kugelnimraum.add(b);
					
					abgefeuert = true;
					
				}if((controls.hochfeuern) && (controls.rechtsfeuern)){
					Bullet b = new Bullet(player.posX + 32, player.posY+64, 5);	//später für damageonhit: player.aktuelleWaffe.damage
					b.setDirection(2);
					setImage(b);
					panel.kugelnimraum.add(b);
					abgefeuert = true;
					
				}if((controls.runterfeuern) && (controls.linksfeuern)){
					Bullet b = new Bullet(player.posX + 32, player.posY+64, 5);	//später für damageonhit: player.aktuelleWaffe.damage
					b.setDirection(5);
					setImage(b);
					panel.kugelnimraum.add(b);
					abgefeuert = true;
					
				}else if((controls.runterfeuern) && (controls.rechtsfeuern)){
					Bullet b = new Bullet(player.posX + 32, player.posY+64, 5);	//später für damageonhit: player.aktuelleWaffe.damage
					b.setDirection(7);
					setImage(b);
					panel.kugelnimraum.add(b);
					abgefeuert = true;
					
				}else if(controls.hochfeuern){
					Bullet b = new Bullet(player.posX + 32, player.posY+64, 5);	//später für damageonhit: player.aktuelleWaffe.damage
					b.setDirection(1);
					setImage(b);
					panel.kugelnimraum.add(b);
					abgefeuert = true;
					
				}else if(controls.runterfeuern){
					Bullet b = new Bullet(player.posX + 32, player.posY+64, 5);	//später für damageonhit: player.aktuelleWaffe.damage
					b.setDirection(6);
					setImage(b);
					panel.kugelnimraum.add(b);
					abgefeuert = true;
					
				}else if(controls.linksfeuern){
					Bullet b = new Bullet(player.posX + 32, player.posY+64, 5);	//später für damageonhit: player.aktuelleWaffe.damage
					b.setDirection(3);
					setImage(b);
					panel.kugelnimraum.add(b);
					abgefeuert = true;
					
				}else if(controls.rechtsfeuern){
					Bullet b = new Bullet(player.posX + 32, player.posY+64, 5);	//später für damageonhit: player.aktuelleWaffe.damage
					b.setDirection(4);
					setImage(b);
					panel.kugelnimraum.add(b);
					abgefeuert = true;
				}
			}
			
			
			feuerrate++;
			//kugel-array durchgehen und bewegung/position durchführen/aktualisieren
			
			for (int index=0;index < panel.kugelnimraum.size();index++){
				
//				if(kugelnimraum.get(index).posX <= 0){
//					
//				}
				
				if(kugelnimraum.size() >0){
				
					Bullet b= kugelnimraum.get(index);
					if (b != null){
						
						switch(b.richtung){
						
						case(0):
							b.posX--;
							b.posY--;
							break;
						case(1):
							b.posY++;
							break;
						case(2):
							b.posX++;
							b.posY--;
							break;
						case(3):
							b.posX--;
							break;
						case(4):
							b.posX++;
							break;
						case(5):
							b.posX--;
							b.posY++;
							break;
						case(6):
							b.posY++;
							break;
						case(7):
							b.posX++;
							b.posY++;
							break;
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
			float amEnde = System.currentTimeMillis() - amAnfang;
			if(amEnde > gamespeed){
				try{
					Thread.sleep(gamespeed - (int)amEnde);
				
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			}
			
		}
	}
	
}
