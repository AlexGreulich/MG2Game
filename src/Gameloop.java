
public class Gameloop implements Runnable{

	GameWindow window;
	Player player;
	Controls controls;
	
	Enemy enemy;
	GamePanel panel;
	
	int[][][] map;
	int speed = 2;
	int gamespeed =5;
	
	public Gameloop(GameWindow w){
		
		window = w;
		player = window.player;
//		enemy = window.enemy;
		controls = window.controls;
		panel = window.panel;
		map = panel.level.map;
		
	}
	
	@Override
	public synchronized void run() {
		while (true){
			float onStart = System.currentTimeMillis();
			
			player.updateBounds();
			
			//spieler soll den rand nicht überschreiten können
			if(player.posX < 32){
				player.posX =32;
			}
			if(player.posX > panel.mapWidth){
				player.posX = panel.mapWidth -130;
			}
			if(player.posY < 65){
				player.posY = 65;
			}
			if(player.posY > panel.mapHeight){
				player.posY = panel.mapHeight -192;
			}
			
			
			//Spielerbewegung, Tastenabfrage
			if((controls.up) && (controls.left)){
				if((player.posX > 64) && (player.posY > 96)){
					//wenn das tile begehbar ist (muss vllt noch anders geregelt werden):
//					if(map[player.posX/64][player.posY/96][1] == 0){
						player.posX = player.posX - speed;
						player.posY = player.posY - speed;
						controls.direction = 7;
//					}else {
						
//					}
				}
				
				
			}else if((controls.up) && (controls.right)){
				if((player.posX < panel.mapWidth -64) && (player.posY >96)){
//					if(map[player.posX/64 ][player.posY/96][1] ==0){
						player.posX = player.posX + speed;
						player.posY = player.posY - speed;
						controls.direction = 6;
//					}
				}
				
			}else if((controls.down) && (controls.left)){
				if((player.posX > 64) && (player.posY < panel.mapHeight-96)){
//					if(map[player.posX/64 ][player.posY/96][1] ==0){
						player.posX = player.posX - speed;
						player.posY = player.posY + speed;
						controls.direction = 4;
//				}
				}
				
			}else if((controls.down) && (controls.right)){
				if((player.posX < panel.mapWidth -64) && (player.posY < panel.mapHeight -96)){
//					if(map[player.posX/64 ][player.posY/96][1] ==0){
						player.posX = player.posX + speed;
						player.posY = player.posY + speed;
						controls.direction = 5;
//					}
				}
			}else if(controls.up){
				if(player.posY > 96){
//					if(map[player.posX/64 ][player.posY/96 ][1] ==0){
						player.posY = player.posY - speed;
						controls.direction = 3;
//					}
				}
				
			}else if(controls.down){
				if(player.posY < panel.mapHeight -96){
//					if(map[player.posX/64][player.posY/96][1] ==0){
						player.posY = player.posY + speed;
						controls.direction = 0;
//					}
				}
				
			}else if(controls.left){
				if(player.posX > 64){
//					if(map[player.posX/64 ][player.posY/96][1] ==0){
						player.posX = player.posX - speed;
						controls.direction = 1;
//					}
				}
				
			}else if(controls.right){
				if(player.posX < panel.mapWidth-64){
//					if(map[player.posX/64][player.posY/96][1] ==0){
						player.posX = player.posX + speed;
						controls.direction = 2;
//					}
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
