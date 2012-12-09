import java.awt.Point;
import java.awt.Polygon;


public class Gameloop implements Runnable{
	boolean running = true;
	GameWindow window;
	Player player;
	Controls controls;
	EnemyController enemycontroller;
	Enemy enemy;
	GamePanel panel;
	
	int[][][] map;
	int gamespeed =16;
	Point playermiddle;
	Point altePos;
	Polygon collisionshape;
	
	int keyCntright=0, keyCntleft=0;
	
	public Gameloop(GameWindow w){
		
		window = w;
		player = window.player;
		enemycontroller = window.enemycontrol;
		
		panel = window.panel;
		//initLoop();
		controls = window.controls;
		collisionshape = panel.collision;
		initloop();
		player.getMiddle();
		altePos = new Point(player.playermiddle.x,player.playermiddle.y);
	}
	
	public void initLoop(){
		map = panel.level.map;
		collisionshape = panel.collision;
		enemycontroller.updateCollisionShape(collisionshape);
	}
	
	@Override
	public synchronized void run() {
		while (running){
			float onStart = System.currentTimeMillis();
			if(!window.pistolReload.isActive()){
				window.pistolReload.stop();
				window.pistolReload.setFramePosition(0);
			}if(!window.heartPumping.isActive()){
				window.heartPumping.stop();
				window.heartPumping.setFramePosition(0);
			}
			
			if(!player.isDead){
				
				player.updateBounds();
				player.getMiddle();
				if(player.energy >50){
					player.changeState(0);
					player.speed=2;
				}else if((player.energy <=30) && (player.energy >0)){
					window.heartPumping.start();
					player.changeState(1);
					player.speed=1;
				}else if(player.energy == 0){
					player.changeState(2);
					player.speed =0;
				}
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
						player.posX = player.posX - (int)player.speed;
						player.posY = player.posY - (int)player.speed;
						controls.setDirection(7);
						player.getMiddle();
					}
				}else if((controls.up) && (controls.right)){
					if(collisionshape.contains(player.playermiddle)){
						player.posX = player.posX + (int)player.speed;
						player.posY = player.posY - (int)player.speed;
						controls.setDirection(6);
						player.getMiddle();
					}
					
				}else if((controls.down) && (controls.left)){
					if(collisionshape.contains(player.playermiddle)){
						player.posX = player.posX - (int)player.speed;
						player.posY = player.posY + (int)player.speed;
						controls.setDirection(4);
						player.getMiddle();
					}
					
				}else if((controls.down) && (controls.right)){
					if(collisionshape.contains(player.playermiddle)){
						player.posX = player.posX + (int)player.speed;
						player.posY = player.posY + (int)player.speed;
						controls.setDirection(5);
						player.getMiddle();
					}
				}else if(controls.up){
					if(collisionshape.contains(player.playermiddle)){
						player.posY = player.posY - (int)player.speed;
						controls.setDirection(3);
						player.getMiddle();
					}
					
				}else if(controls.down){
					if(collisionshape.contains(player.playermiddle)){
						player.posY = player.posY + (int)player.speed;
						controls.setDirection(0);
						player.getMiddle();
					}
					
				}else if(controls.left){
					if(collisionshape.contains(player.playermiddle)){
						player.posX = player.posX - (int)player.speed;
						controls.setDirection(1);
						player.getMiddle();
					}
					
				}else if(controls.right){
					if(collisionshape.contains(player.playermiddle)){
						player.posX = player.posX + (int)player.speed;
						controls.setDirection(2);
						player.getMiddle();
					}
				}
				
				if(controls.leftSwitch){
					keyCntleft++;
					if(keyCntleft>5){
						player.inventorySelect--;
						keyCntleft =0;
					}
					
					
				}
				if(controls.rightSwitch){
					keyCntright++;
					if(keyCntright>5){
						player.inventorySelect++;
						keyCntright=0;
					}
					
					
				}
				if(player.inventorySelect>9){
						player.inventorySelect=9;
				}
				if(player.inventorySelect < 0){
						player.inventorySelect =0;
				}
				if(!collisionshape.contains(player.playermiddle)){
					player.posX = altePos.x-16;
					player.posY = altePos.y-16;
				}
				
				if(controls.reload){
					for(int i=0; i< player.equipment.size();i++){
						if(player.equipment.get(i) != null){
							if(player.equipment.get(i).itemType == 2){
							window.pistolReload.start();
							player.equipment.remove(i);
							player.ammo += 10;
							break;
						}
						}
						
					}
					
				}
				
				for(int i = 0; i< window.activeLevel.doorShapes.length;i++){
					if(window.activeLevel.doorPoints.get(i) != null){
						if(window.activeLevel.doorShapes[i].intersects(player.playerBounds)){		//contains(player.playermiddle)){
							if(controls.levelChange){
								Thread.yield();	
								int lastDoorEntered = i; 
								window.levelChanging(window.activeLevel.neighbors[i], lastDoorEntered);
							}
						}
					}
				}
			}
						
			//Thread.yield();
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
