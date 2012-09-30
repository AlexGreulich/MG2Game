import java.util.ArrayList;


public class EnemyController implements Runnable{

	
	int gamespeed = 5;
	GameWindow window;
	ArrayList<Enemy> enemylist;
	
	public EnemyController(GameWindow w){
		window = w;
		enemylist = window.panel.enemylist;
		
	}
	
	@Override
	public void run() {
		while(true){
			float onStart = System.currentTimeMillis();
			
			for(Enemy e : enemylist){
				e.move();
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
