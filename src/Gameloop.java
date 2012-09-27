
public class Gameloop implements Runnable{

	GameWindow window;
	Player player;
	Controls controls;
	int speed = 5;
	int gamespeed =5;
	
	public Gameloop(GameWindow w){
		
		window = w;
		player = window.player;
		controls = window.controls;
		
	}
	
	@Override
	public void run() {
		while (true){
			float amAnfang = System.currentTimeMillis();
			
			if((controls.hoch) && (controls.links)){
//				player.posX = player.posX - speed;
//				player.posY = player.posY - speed;
				player.setX(player.posX -speed);
				player.setY(player.posY -speed);
				controls.richtung = 7;
			}else if((controls.hoch) && (controls.rechts)){
//				player.posX = player.posX + speed;
//				player.posY = player.posY - speed;
				player.setX(player.posX + speed);
				player.setY(player.posY - speed);
				controls.richtung = 6;
			}else if((controls.hoch)){
//				player.posY = player.posY - speed;
				player.setY(player.posY - speed);
				controls.richtung = 3;
			}else if((controls.runter) && (controls.links)){
//				player.posX = player.posX - speed;
//				player.posY = player.posY + speed;
				player.setX(player.posX - speed);
				player.setY(player.posY + speed);
				controls.richtung = 4;
			}else if((controls.runter) && (controls.rechts)){
//				player.posX = player.posX + speed;
//				player.posY = player.posY + speed;
				player.setX(player.posX + speed);
				player.setY(player.posY + speed);
				controls.richtung = 5;
			}else if((controls.runter)){
//				player.posY = player.posY + speed;
				player.setY(player.posY + speed);
				controls.richtung = 0;
			}else if((controls.links)){
//				player.posX = player.posX - speed;
				player.setX(player.posX - speed);
				controls.richtung = 1;
			}else if((controls.rechts)){
//				player.posX = player.posX + speed;
				player.setX(player.posX + speed);
				controls.richtung = 2;
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
