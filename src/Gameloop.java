
public class Gameloop implements Runnable{

	GameWindow window;
	Player player;
	Controls controls;
	
	GamePanel panel;
	
	int speed = 5;
	int gamespeed =20;
	
	public Gameloop(GameWindow w){
		
		window = w;
		player = window.player;
		controls = window.controls;
		panel = window.panel;
	}
	
	@Override
	public synchronized void run() {
		while (true){
			float onStart = System.currentTimeMillis();
			
			if(player.posX < 65){
				player.posX =65;
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
			
			if((controls.up) && (controls.left)){
				if((player.posX > 64) && (player.posY > 96)){
					if(panel.map[player.posX/64 -1][player.posY/96 -1][1] != 1){
						player.posX = player.posX - speed;
						player.posY = player.posY - speed;
						controls.direction = 7;
					}
				}
				
				
			}else if((controls.up) && (controls.right)){
				if((player.posX < panel.mapWidth -64) && (player.posY >96)){
					if(panel.map[player.posX/64 +1][player.posY/96 -1][1] != 1){
						player.posX = player.posX + speed;
						player.posY = player.posY - speed;
						controls.direction = 6;
					}
				}
				
			}else if((controls.down) && (controls.left)){
				if((player.posX > 64) && (player.posY < panel.mapHeight-96)){
					if(panel.map[player.posX/64 -1][player.posY/96 +1][1] != 1){
						player.posX = player.posX - speed;
						player.posY = player.posY + speed;
						controls.direction = 4;
				}
				}
				
			}else if((controls.down) && (controls.right)){
				if((player.posX < panel.mapWidth -64) && (player.posY < panel.mapHeight -96)){
					if(panel.map[player.posX/64 +1][player.posY/96 +1][1] != 1){
						player.posX = player.posX + speed;
						player.posY = player.posY + speed;
						controls.direction = 5;
					}
				}
			}else if(controls.up){
				if(player.posY > 96){
					if(panel.map[player.posX/64 ][player.posY/96 -1][1] != 1){
						player.posY = player.posY - speed;
						controls.direction = 3;
					}
				}
				
			}else if(controls.down){
				if(player.posY < panel.mapHeight -96){
					if(panel.map[player.posX/64][player.posY/96 +1][1] != 1){
						player.posY = player.posY + speed;
						controls.direction = 0;
					}
				}
				
			}else if(controls.left){
				if(player.posX > 64){
					if(panel.map[player.posX/64 -1][player.posY/96][1] != 1){
						player.posX = player.posX - speed;
						controls.direction = 1;
					}
				}
				
			}else if(controls.right){
				if(player.posX < panel.mapWidth-64){
					if(panel.map[player.posX/64 +1][player.posY/96][1] != 1){
						player.posX = player.posX + speed;
						controls.direction = 2;
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
