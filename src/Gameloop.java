
public class Gameloop implements Runnable{

	GameWindow window;
	Player player;
	Controls controls;
	
	GamePanel panel;
	
	int speed = 2;
	int gamespeed =5;
	
	public Gameloop(GameWindow w){
		
		window = w;
		player = window.player;
		controls = window.controls;
		panel = window.panel;
	}
	
	@Override
	public synchronized void run() {
		while (true){
			float amAnfang = System.currentTimeMillis();
			
			if(player.posX < 65){
				player.posX =65;
			}
			if(player.posX > panel.kartenbreite){
				player.posX = panel.kartenbreite -130;
			}
			if(player.posY < 65){
				player.posY = 65;
			}
			if(player.posY > panel.kartenhoehe){
				player.posY = panel.kartenhoehe -192;
			}
			
			if((controls.hoch) && (controls.links)){
				if((player.posX > 64) && (player.posY > 96)){
					if(panel.karte[player.posX/64 -1][player.posY/96 -1][1] != 1){
						player.posX = player.posX - speed;
						player.posY = player.posY - speed;
						controls.richtung = 7;
					}
				}
				
				
			}else if((controls.hoch) && (controls.rechts)){
				if((player.posX < panel.kartenbreite -64) && (player.posY >96)){
					if(panel.karte[player.posX/64 +1][player.posY/96 -1][1] != 1){
						player.posX = player.posX + speed;
						player.posY = player.posY - speed;
						controls.richtung = 6;
					}
				}
				
			}else if((controls.runter) && (controls.links)){
				if((player.posX > 64) && (player.posY < panel.kartenhoehe-96)){
					if(panel.karte[player.posX/64 -1][player.posY/96 +1][1] != 1){
						player.posX = player.posX - speed;
						player.posY = player.posY + speed;
						controls.richtung = 4;
				}
				}
				
			}else if((controls.runter) && (controls.rechts)){
				if((player.posX < panel.kartenbreite -64) && (player.posY < panel.kartenhoehe -96)){
					if(panel.karte[player.posX/64 +1][player.posY/96 +1][1] != 1){
						player.posX = player.posX + speed;
						player.posY = player.posY + speed;
						controls.richtung = 5;
					}
				}
			}else if(controls.hoch){
				if(player.posY > 96){
					if(panel.karte[player.posX/64 ][player.posY/96 -1][1] != 1){
						player.posY = player.posY - speed;
						controls.richtung = 3;
					}
				}
				
			}else if(controls.runter){
				if(player.posY < panel.kartenhoehe -96){
					if(panel.karte[player.posX/64][player.posY/96 +1][1] != 1){
						player.posY = player.posY + speed;
						controls.richtung = 0;
					}
				}
				
			}else if(controls.links){
				if(player.posX > 64){
					if(panel.karte[player.posX/64 -1][player.posY/96][1] != 1){
						player.posX = player.posX - speed;
						controls.richtung = 1;
					}
				}
				
			}else if(controls.rechts){
				if(player.posX < panel.kartenbreite-64){
					if(panel.karte[player.posX/64 +1][player.posY/96][1] != 1){
						player.posX = player.posX + speed;
						controls.richtung = 2;
					}
				}
				
			}
			
			
			
			float amEnde = System.currentTimeMillis()- amAnfang;
			if(gamespeed > amEnde){
				try {
					Thread.sleep(gamespeed -(int)amEnde);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
