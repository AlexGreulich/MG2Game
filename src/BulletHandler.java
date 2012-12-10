import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;


public class BulletHandler implements Runnable{
	boolean running = true;
	int gamespeed =16;
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

	CopyOnWriteArrayList<Bullet> bullets;
	CopyOnWriteArrayList<Enemy> enemies;

	public BulletHandler(GameWindow w){

		window = w;	
		panel = window.panel;
		initBulletHandler();

		controls = window.controls;
		player = window.player;

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

	public void initBulletHandler(){

		enemylist = window.activeLevel.thisLevelsEnemies;
		bulletsInRoom = window.bulletsInRoom;
		levelBorders = new Rectangle(0,0,panel.mapWidth,panel.mapHeight);
	}

	public void setImage(Bullet b){
		switch(b.getDirection()){
		case(0):
			b.image = bulletpics[6];
			break;
		case(1):
			b.image = bulletpics[3];
			break;
		case(2):
			b.image = bulletpics[4];
			break;
		case(3):
			b.image = bulletpics[1];
			break;
		case(4):
			b.image = bulletpics[5];
			break;
		case(5):
			b.image = bulletpics[7];
			break;
		case(6):
			b.image = bulletpics[2];
			break;
		case(7):
			b.image = bulletpics[0];
			break;
		}
	}

	public void collisionCheck(){
		if(bulletsInRoom.size() >0){
			bullets = new CopyOnWriteArrayList<Bullet>(bulletsInRoom);
			for(Bullet b: bullets){
				b.updateBounds();
				if(enemylist.size()>0){
					enemies = new CopyOnWriteArrayList<Enemy>(enemylist);
					for(Enemy e: enemies){
						if(b.bounds.intersects(e.enemyBounds)&&(b.playerBullet==true)){
							e.energy = e.energy - b.dealDamage();
							window.pistolHit.start();
							SpecialEffect se = new SpecialEffect(2);
							se.setPos(e.getX()/32, e.getY()/8);
							se.setNotLooping();
							window.activeLevel.specialEffects.add(se);
							panel.actionMessages.add(new ActionMessage(panel,"Enemy hit"));
							bulletsInRoom.remove(b);
						}
					}
				}
				if(b.bounds.intersects(player.playerBounds)&&b.playerBullet==false){
					player.energy = player.energy - 0.5f;
					window.pistolHit.start();
					SpecialEffect se = new SpecialEffect(2);
					se.setPos(player.getX()/32, player.getY()/8);
					se.setNotLooping();
					window.activeLevel.specialEffects.add(se);
					window.panel.actionMessages.add(new ActionMessage(window.panel,"Player hit"));
					bulletsInRoom.remove(b);
				}
			}
		}
	}

	public void createEnemyBullets(){
		enemies = new CopyOnWriteArrayList<Enemy>(enemylist);
		if(enemies.size()>0){
			for(Enemy e: enemies){
				if((e.enemyType==2)&&(e.canAttack==true)){
					Bullet b = new Bullet(e.posX + 16, e.posY+16, 10, false);
					b.setDirection(e.calcShotDirection());
					setImage(b);
					bulletsInRoom.add(b);
					window.pistolShot.start();
				}
			}
		}
	}

	@Override
	public synchronized void run() {
		while(running){
			float onStart = System.currentTimeMillis();
			if(firerate == 50){
	
				fired = false;
				firerate =0;
			}
			if(!window.pistolShot.isActive()){
				window.pistolShot.stop();
				window.pistolShot.setFramePosition(0);
			}
			if(!window.pistolHit.isActive()){
				window.pistolHit.stop();
				window.pistolHit.setFramePosition(0);
			}if(!window.emptyPistol.isActive()){
				window.emptyPistol.stop();
				window.emptyPistol.setFramePosition(0);
			}
			
			collisionCheck();
			createEnemyBullets();
	// es kann nur gefeuert werden wenn abgefeuert == false ist, sonst wird feuerrate bis 50 gewartet
			if(!fired){
	// pfeiltasten abfragen um neue kugeln zu erzeugen
				if(controls.fire){ //&& (controls.fireLEFT)){
					if((player.weapons[0] != null)&&(player.ammo >0)){
						Bullet b = new Bullet(player.posX + 16, player.posY+16, 10, true);	//spaeter fuer damageonhit: player.aktuelleWaffe.damage
						player.ammo--;
						b.setDirection(controls.getDirection());
						setImage(b);
						bulletsInRoom.add(b);
						window.pistolShot.start();
						fired = true;
					}else if((player.weapons[0] != null)&&(player.ammo ==0)){
						window.emptyPistol.start();
					}
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
							b.posY = b.posY+20;
							break;
						case(1):
							b.posX = b.posX - 20;
							break;
						case(2):
							b.posX = b.posX + 20;
							break;
						case(3):
							b.posY = b.posY - 20;
							break;
						case(4):
							b.posY = b.posY + 20;
							b.posX = b.posX-20;
							break;
						case(5):
							b.posX = b.posX + 20;
							b.posY = b.posY + 20;
							break;
						case(6):
							b.posX = b.posX + 20;
							b.posY = b.posY-20;
							break;
						case(7):
							b.posX = b.posX - 20;
							b.posY = b.posY - 20;
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