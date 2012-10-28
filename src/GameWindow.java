import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
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
	
	HashMap levels;
	ArrayList<BufferedImage> allMapsFloors;
	ArrayList<BufferedImage> allMapsWalls;
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
		
		allMapsFloors = new ArrayList<BufferedImage>();
		allMapsWalls = new ArrayList<BufferedImage>();
		
		loadLevelPics();
		controls = new Controls();
		player = new Player(this);
		enemylist = new ArrayList<Enemy>();
		enemycontrol = new EnemyController(this);
		level = new Level(allMapsFloors.get(0), allMapsWalls.get(0));
		
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
		//	später alternative für soundprobleme:
//		SourceDataLine sourceDataLine = AudioSystem.getSourceDataLine(audioFormat); 
//		sourceDataLine.open(audioFormat);
		
		
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
		 
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		
		
		
		clip.start();
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		
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
	
	public void loadLevelPics(){
		
		URL path = getClass().getResource("maps/");
		File f = new File(path.getPath());
		File[] files = f.listFiles();
		
		for(int i =0; i< files.length; i++){
			try {
				if(files[i].getAbsolutePath().endsWith("walls.gif")){
					allMapsWalls.add(ImageIO.read(files[i]));
				}else{
					allMapsFloors.add(ImageIO.read(files[i]));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
