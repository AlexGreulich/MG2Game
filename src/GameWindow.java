import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;

//import org.omg.IOP.Encoding;


public class GameWindow extends JFrame{

	GamePanel panel;
	Player player;
	//Enemy enemy;
	Controls controls;
	Thread panelThread, gameloopthread, bulletthread, enemythread;
	Level level;
	Gameloop gameloop;
	BulletHandler bullethandler;
	EnemyController enemycontrol;
	ArrayList<Bullet> bulletsInRoom;
	ArrayList<Enemy> enemylist;
	Clip clip;
	int framePosition;
	
	Point cameraPosition = new Point(0,0);
	Dimension screen;
	
	public GameWindow() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
		super("Cybercalypse");
		
		
		screen = Toolkit.getDefaultToolkit().getScreenSize();
//		int scrx = (int) (screen.getWidth()/2)-350;
//		int scry = (int) (screen.getHeight()/2)-370;
									//		setLocation(scrx, scry);
//		setSize(scrx,scry);
		
		setResizable(false);
		setUndecorated(true);
									//setSize(1920,1080);
		
		
		controls = new Controls();
		player = new Player(this);
		enemylist = new ArrayList<Enemy>();
		enemycontrol = new EnemyController(this);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		level = new Level();
		bulletsInRoom = new ArrayList<Bullet>();
		
		
		
		panel = new GamePanel(this);
		gameloop = new Gameloop(this);
		addKeyListener(controls);
		
		add(panel);
		
		bullethandler = new BulletHandler(this);
	
		
		
		
		AudioInputStream mp3audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("audio/test_track_01.mp3"));
		
		AudioFormat audioFormat = mp3audioInputStream.getFormat();
		AudioFormat decoded = new AudioFormat( 
				AudioFormat.Encoding.PCM_SIGNED, // encoding 
				audioFormat.getSampleRate(), // sample rate 
				16, // bits per sample 
				audioFormat.getChannels(), // number of channels 
				audioFormat.getChannels() * 2, // bytes per frame 
				audioFormat.getSampleRate(), // frames per second 
				false);
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(decoded, mp3audioInputStream);
		
//		SourceDataLine sourceDataLine = AudioSystem.getSourceDataLine(audioFormat); 
//		sourceDataLine.open(audioFormat);
		
		clip = AudioSystem.getClip();
		clip.open(audioInputStream);
//		clip.start();
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		
		setVisible(true);
		this.setIgnoreRepaint(true);
		
		panelThread = new Thread(panel);
		gameloopthread = new Thread(gameloop);
		bulletthread = new Thread(bullethandler);
		enemythread = new Thread(enemycontrol);
		panelThread.start();
		gameloopthread.start();
		bulletthread.start();
		enemythread.start();
		
		
		pack();
	}
	
	public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
		
		new GameWindow();
		
	}
}
