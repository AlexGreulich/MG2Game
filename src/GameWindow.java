import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;

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
	
	JCheckBox fullscreen, windowed;
	JButton start;
	static int option=0;
	JWindow splashwindow;
	JLabel inProgress;
	public GameWindow(int screenoption) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
		super("Cybercalypse");
		
		
		screen = Toolkit.getDefaultToolkit().getScreenSize();
		int scrx = (int) (screen.getWidth()/2)-250;
		int scry = (int) (screen.getHeight()/2)-150;
		
		
		splashwindow = new JWindow();
		splashwindow.setLayout(new GridLayout(4,1));
		splashwindow.setSize(500, 300);
		splashwindow.setLocation(scrx, scry);
		start = new JButton("Come get some");
		fullscreen = new JCheckBox("Play in fullscreen mode");
		windowed = new JCheckBox("Play in windowed mode");
		inProgress = new JLabel("Just click Start to play, checkboxes do not work yet...");
		start.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				startGame();
			}
		});
		splashwindow.getContentPane().add(inProgress);
		splashwindow.getContentPane().add(fullscreen);
		splashwindow.getContentPane().add(windowed);
		splashwindow.getContentPane().add(start);
		splashwindow.setVisible(true);
		
		setResizable(false);
		setUndecorated(true);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.setIgnoreRepaint(true);
		
		pack();
	}
	
	public static void main(String[] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
		
		new GameWindow(option);
		
	}
	
	public void startGame(){
		splashwindow.dispose();
		this.setVisible(true);
		controls = new Controls();
		player = new Player(this);
		enemylist = new ArrayList<Enemy>();
		enemycontrol = new EnemyController(this);
		level = new Level();
		bulletsInRoom = new ArrayList<Bullet>();
		
		
		
		panel = new GamePanel(this);
		gameloop = new Gameloop(this);
		addKeyListener(controls);
		
		add(panel);
		
		bullethandler = new BulletHandler(this);
		
		AudioInputStream mp3audioInputStream;
		try {
			mp3audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource("audio/test_track_01.mp3"));
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
		 
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		
		
		//clip.open(audioInputStream);
//		clip.start();
		//clip.loop(Clip.LOOP_CONTINUOUSLY);
		
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
}
