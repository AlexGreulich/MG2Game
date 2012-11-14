import java.awt.Point;
import java.awt.Polygon;


public class Gameloop implements Runnable{
	boolean running = true;
	GameWindow window;
	Player player;
	Controls controls;
	
	Enemy enemy;
	GamePanel panel;
	
	int[][][] map;
	int speed = 2;
	int gamespeed =5;
	Point playermiddle;
	Point altePos;
	Polygon collisionshape;
	
	public Gameloop(GameWindow w){
		
		window = w;
		player = window.player;
//		enemy = window.enemy;
		initLoop();
		//vorher stand hier:
				controls = window.controls;
		//		panel = window.panel;
		//		map = panel.level.map;
		collisionshape = panel.collision;
		player.getMiddle();
		altePos = new Point(player.playermiddle.x,player.playermiddle.y);
	}
	
	public void initLoop(){
		panel = window.panel;
		map = panel.level.map;
		collisionshape = panel.collision;
	}
	
	@Override
	public synchronized void run() {
		while (running){
			float onStart = System.currentTimeMillis();
			
			player.updateBounds();
			player.getMiddle();
		
			/*	spieler soll den rand nicht überschreiten können
			*	- position in altePos speichern
			*	- tasten abfragen
			*	- wenn mittelpunkt im kollisionspolygon verändere position um speed
			*	- richtung ändern
			*	- mittelpunkt neu berechnen
			*	- abschliessend teten ob spielermitte immer noch im polygon
			*	- wenn nicht setze position wieder auf altePos
			*/	
			altePos.setLocation(player.playermiddle.x,player.playermiddle.y);
			
			//Spielerbewegung, Tastenabfrage
			
			if((controls.up) && (controls.left)){
				
				if(collisionshape.contains(player.playermiddle)){
					player.posX = player.posX - speed;
					player.posY = player.posY - speed;
					controls.direction = 7;
					player.getMiddle();
				}
			}else if((controls.up) && (controls.right)){
				if(collisionshape.contains(player.playermiddle)){
					player.posX = player.posX + speed;
					player.posY = player.posY - speed;
					controls.direction = 6;
					player.getMiddle();
				}
				
			}else if((controls.down) && (controls.left)){
				if(collisionshape.contains(player.playermiddle)){
					player.posX = player.posX - speed;
					player.posY = player.posY + speed;
					controls.direction = 4;
					player.getMiddle();
				}
				
			}else if((controls.down) && (controls.right)){
				if(collisionshape.contains(player.playermiddle)){
					player.posX = player.posX + speed;
					player.posY = player.posY + speed;
					controls.direction = 5;
					player.getMiddle();
				}
			}else if(controls.up){
				if(collisionshape.contains(player.playermiddle)){
					player.posY = player.posY - speed;
					controls.direction = 3;
					player.getMiddle();
				}
				
			}else if(controls.down){
				if(collisionshape.contains(player.playermiddle)){
					player.posY = player.posY + speed;
					controls.direction = 0;
					player.getMiddle();
				}
				
			}else if(controls.left){
				if(collisionshape.contains(player.playermiddle)){
					player.posX = player.posX - speed;
					controls.direction = 1;
					player.getMiddle();
				}
				
			}else if(controls.right){
				if(collisionshape.contains(player.playermiddle)){
					player.posX = player.posX + speed;
					controls.direction = 2;
					player.getMiddle();
				}
			}
			
			if(!collisionshape.contains(player.playermiddle)){
				player.posX = altePos.x-8;
				player.posY = altePos.y-16;
			}
			
			for(int i = 0; i< window.level.doorShapes.length;i++){
				if(window.level.doorShapes[i].contains(player.playermiddle)){
					if(controls.levelChange){
						window.levelChanging();
					}
				}
			}
			
			
			float onEnd = System.currentTimeMillis()- onStart;
			if(gamespeed > onEnd){
				try {
					Thread.sleep(gamespeed -(int)onEnd);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
