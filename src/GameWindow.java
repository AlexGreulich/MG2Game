import java.awt.Point;

import javax.swing.JFrame;


public class GameWindow extends JFrame{

	GamePanel panel;
	Player player;
	Controls controls;
	Thread panelThread, gameloopthread;
	Level level;
	Gameloop gameloop;
	public GameWindow(){
		super("Cybercalypse");
		
		setSize(1920,1080);
		controls = new Controls();
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		level = new Level();
		
		player = new Player(this);//,50,50
		panel = new GamePanel(this);
		gameloop = new Gameloop(this);
		addKeyListener(controls);
		
		add(panel);
		setVisible(true);
		this.setIgnoreRepaint(true);
		
		panelThread = new Thread(panel);
		gameloopthread = new Thread(gameloop);
		
		panelThread.start();
		gameloopthread.start();
		
		setResizable(false);
		pack();
	}
	
	public static void main(String[] args){
		
		new GameWindow();
		
	}
}
