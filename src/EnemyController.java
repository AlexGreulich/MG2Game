import java.awt.Polygon;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;


public class EnemyController implements Runnable{

	boolean running= true;
	int gamespeed = 16;
	GameWindow window;
	Controls controls;
	GamePanel panel;
	ArrayList<Enemy> enemylist, corpses;
	Player player;
	CopyOnWriteArrayList<Enemy> theEnemies;
	Level level;
	boolean meleeHit;
	public EnemyController(GameWindow w){
		window = w;
		controls = w.controls;
		panel = window.panel;
		player = window.player;
		level = window.activeLevel;
		enemylist = level.thisLevelsEnemies;
		corpses = level.corpsesInThisLevel;
		meleeHit=false;
	}
	
	public void initEnemyController(){
		this.enemylist = window.activeLevel.thisLevelsEnemies;
		this.corpses = window.activeLevel.corpsesInThisLevel;
	}

	public void collisionDetect(){
		meleeHit=false;
		theEnemies = new CopyOnWriteArrayList<Enemy>(enemylist);
		for(Enemy e:theEnemies){
			e.updateBounds();
			if(e.enemyBounds.intersects(player.playerBounds)){
				if(e.countToNextAttack ==0){
					e.canAttack = true;
				}
				if(e.canAttack){
					e.dealDamage();
					SpecialEffect se = new SpecialEffect(2);
					se.setPos(player.getX()/32, player.getY()/8);
					se.setNotLooping();
					window.activeLevel.specialEffects.add(se);
				}
			}
			if((player.meleeHitDetection(e.posX, e.posY)==true)&&(controls.melee==true)&&(player.meleeReady==true)){
				e.energy = e.energy - 8;
				SpecialEffect se = new SpecialEffect(2);
				se.setPos(e.getX()/32, e.getY()/8);
				se.setNotLooping();
				window.activeLevel.specialEffects.add(se);
				panel.actionMessages.add(new ActionMessage(panel,"Enemy hit"));
				meleeHit=true;
			}
		}
		if(meleeHit==true){
			 player.meleeReset();
		}
	}
	
	public void updateCollisionShape(Polygon col){
		theEnemies = new CopyOnWriteArrayList<Enemy>(enemylist);
		for(Enemy e : theEnemies){
			e.collisionshape=col;
		}
	}
	
	@Override
	public void run() {
		while(running){
			float onStart = System.currentTimeMillis();
			if(player.meleeCounter==0){
				player.meleeReady = true;
			}else if(player.meleeCounter > 0){
				player.meleeCounter--;
			}
			
			collisionDetect();
			theEnemies = new CopyOnWriteArrayList<Enemy>(enemylist);
			for(Enemy e : theEnemies){

				e.move();
				if(!e.canAttack){
					e.countToNextAttack--;
				}
				if(e.countToNextAttack ==0){
					e.canAttack=true;
				}
				if(e.energy <= 0){
					e.isDead =true;
					e.speed=0;
					enemylist.remove(e);
					corpses.add(e);
					Item i = new Item(window,e.posX,e.posY, (int)Math.random()*2 + 3);
					window.activeLevel.thisLevelsItems.add(i);
					panel.actionMessages.add(new ActionMessage(panel,"Enemy killed"));
				}
			}
			float onEnd = System.currentTimeMillis()-onStart;
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