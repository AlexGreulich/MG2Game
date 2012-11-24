import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;


public class EnemyController implements Runnable{

	boolean running= true;
	int gamespeed = 16;
	GameWindow window;
	GamePanel panel;
	ArrayList<Enemy> enemylist, corpses;
	Enemy enemy1;
	Player player;
	CopyOnWriteArrayList<Enemy> theEnemies;
	
	public EnemyController(GameWindow w){
		window = w;
		panel = window.panel;
		player = window.player;
		enemylist = window.enemylist;
		corpses = window.corpses;
		
		
	//test levelwechsel, gegner disabled	
		enemy1 = new Enemy(window,100,100,1);
		enemylist.add(enemy1);
	}
	
	public void initEnemyController(){
		
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
					SpecialEffect se = new SpecialEffect(1);
					se.setPos(e.getX(), e.getY());
					window.specialEffects.add(se);
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
					panel.actionMessages.add(new ActionMessage("Enemy killed"));
					
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
