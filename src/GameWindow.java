import java.awt.Point;

import javax.swing.JFrame;


public class GameWindow extends JFrame{

	GamePanel panel;
	Player player;
	Controls controls;
	Thread panelThread, gameloopthread;
	Level level;
	//Point pointOfView = new Point(0,0);
	Gameloop gameloop;
	public GameWindow(){
		super("");
		
		setSize(800,600);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		level = new Level();
		controls = new Controls();
		addKeyListener(controls);
		player = new Player(this,50,50);
		panel = new GamePanel(this);
		gameloop = new Gameloop(this);
		
		
		add(panel);
		setVisible(true);
		this.setIgnoreRepaint(true);
		
		panelThread = new Thread(panel);
		gameloopthread = new Thread(gameloop);
		panelThread.run();
		gameloopthread.run();
		
		setResizable(false);
		//pack();
	}
	
	public static void main(String[] args){
		
		new GameWindow();
		
	}
}
