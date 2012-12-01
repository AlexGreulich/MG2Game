import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;


public class EnemyController implements Runnable{

	boolean running= true;
	int gamespeed = 16;
	GameWindow window;
	GamePanel panel;
	ArrayList<Enemy> enemylist, corpses;
	//Enemy enemy1;
	Player player;
	CopyOnWriteArrayList<Enemy> theEnemies;
	Level level;
	public EnemyController(GameWindow w){
		window = w;
		panel = window.panel;
		player = window.player;
		level = window.activeLevel;
		enemylist = level.thisLevelsEnemies;
		corpses = level.corpsesInThisLevel;
	}
	
	public void initEnemyController(){
		this.enemylist = window.activeLevel.thisLevelsEnemies;
		this.corpses = window.activeLevel.corpsesInThisLevel;
	}
	
	public void collisionDetect(){
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
		}
	}
	
	@Override
	public void run() {
		while(running){
			float onStart = System.currentTimeMillis();
			collisionDetect();
			theEnemies = new CopyOnWriteArrayList<Enemy>(enemylist);
			for(Enemy e : theEnemies){
				
				e.move();
				if(!e.canAttack){
					e.countToNextAttack--;
				}
				if(e.countToNextAttack ==0){
					e.canAttack=true;
					e.countToNextAttack =10;
				}
				if(e.energy == 0){
					e.isDead =true;
					e.speed=0;
					enemylist.remove(e);
					corpses.add(e);
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
