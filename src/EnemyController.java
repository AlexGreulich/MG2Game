import java.util.ArrayList;


public class EnemyController implements Runnable{

	boolean running= true;
	int gamespeed = 5;
	GameWindow window;
	GamePanel panel;
	ArrayList<Enemy> enemylist;
	Enemy enemy1;
	Player player;
	
	public EnemyController(GameWindow w){
		window = w;
		panel = window.panel;
		player = window.player;
		enemylist = window.enemylist;
		
		
	//test levelwechsel, gegner disabled	
		//enemy1 = new Enemy(window,100,100,2);
		//enemylist.add(enemy1);
	}
	
	public void initEnemyController(){
		
	}
	
	public void collisionDetect(){
		for(Enemy e:enemylist){
			e.updateBounds();
			if(e.enemyBounds.intersects(player.playerBounds)){
				if(e.countToNextAttack ==0){
					e.canAttack = true;
				}
				if(e.canAttack){
					e.dealDamage();
				}
			}
		}
	}
	
	@Override
	public void run() {
		while(running){
			float onStart = System.currentTimeMillis();
			collisionDetect();
			for(Enemy e : enemylist){
				e.move();
				if(!e.canAttack){
					e.countToNextAttack--;
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
