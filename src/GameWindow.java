import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
//import java.util.HashMap;
import java.util.Random;

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


@SuppressWarnings("serial")
public class GameWindow extends JFrame{

	GamePanel panel;
	Player player;
	//Enemy enemy;
	Controls controls;
	Thread panelThread, gameloopthread, bulletthread, enemythread, itemthread;
	Level level;
	Gameloop gameloop;
	BulletHandler bullethandler;
	EnemyController enemycontrol;
	ItemHandler itemHandler;
	ArrayList<Bullet> bulletsInRoom;
	ArrayList<Enemy> enemylist;
	ArrayList<SpecialEffect> specialEffects;
	
	
	Clip clip;
	int framePosition;
	
	Point cameraPosition = new Point(0,0);
	Dimension screen;
	
	JCheckBox fullscreen, windowed;
	JButton start;
	int screenoption=0;
	JWindow splashwindow;
	JLabel inProgress;
	
	//HashMap levels;
	ArrayList<BufferedImage> allMapsFloors;
	ArrayList<BufferedImage> allMapsWalls;
	ArrayList<BufferedImage> allMapsItems;
	
	Polygon doorShape;
	
	//JWindow loadingScreen;
	
	ArrayDeque<Integer> roomsEntered = new ArrayDeque<Integer>();
	Random rdm = new Random();
	
	public GameWindow() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
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
//		fullscreen.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent e){
//				windowed.setEnabled(false);
//				
//			}
//		});
//		windowed.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent e){
//				fullscreen.setEnabled(false);
//			}
//		});
		inProgress = new JLabel("Just click Start to play! Screenmode is set to fullscreen by default");
		
		start.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(windowed.isEnabled()){
					screenoption =1;
				}else{
					screenoption =2;
				}
				/*
				 * der splashscreen wird geschlossen,
				 * und es können alle dinge fürs spiel geladen werden
				 * */
				//reInitWindow();
				splashwindow.dispose();
				//startMusic();
				startGame(0);	//startraum
				
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
		new GameWindow();
	}
	
	public void reInitWindow(int roomNumber){
		
		this.level = new Level(allMapsFloors.get(roomNumber), allMapsWalls.get(roomNumber), allMapsItems.get(roomNumber));
		panel.initPanel();	//panel neu initialisiert mit level, level.map, enemylist 
		gameloop.initLoop();
		bullethandler.initBulletHandler();
		this.bulletsInRoom.clear();
		this.enemylist.clear();
		this.specialEffects.clear();
		itemHandler.initItemHandler();
		loadSpecificRoomStuff();
	}
	
	public void startGame(int roomNumber){
		
		allMapsFloors = new ArrayList<BufferedImage>();
		allMapsWalls = new ArrayList<BufferedImage>();
		allMapsItems = new ArrayList<BufferedImage>();
		
		loadLevelPics();
		this.level = new Level(allMapsFloors.get(roomNumber), allMapsWalls.get(roomNumber), allMapsItems.get(roomNumber));
		this.controls = new Controls();
		
		this.player = new Player(this);
		this.bulletsInRoom = new ArrayList<Bullet>();
		this.enemylist = new ArrayList<Enemy>();
		this.specialEffects = new ArrayList<SpecialEffect>();
		this.panel = new GamePanel(this, screenoption);
		
		this.gameloop = new Gameloop(this);
		this.itemHandler = new ItemHandler(this);
		this.enemycontrol = new EnemyController(this);
		this.bullethandler = new BulletHandler(this);
		loadSpecificRoomStuff();
		addKeyListener(controls);
		add(panel);
		this.setVisible(true);
		
//		panel.itemsInLevel = itemHandler
		
		this.setIgnoreRepaint(true);
		
		panelThread = new Thread(panel);
		gameloopthread = new Thread(gameloop);
		bulletthread = new Thread(bullethandler);
		enemythread = new Thread(enemycontrol);
		itemthread = new Thread(itemHandler);
		panelThread.start();
		gameloopthread.start();
		bulletthread.start();
		enemythread.start();
		itemthread.start();
		panel.running = true;
		gameloop.running = true;
		bullethandler.running = true;
		enemycontrol.running = true;
		itemHandler.running = true;
		pack();
	}
	
	public void startMusic(){
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
	}
	
	public void loadLevelPics(){
		/*
		 * Um alle karten zu laden
		 * - floor, walls und items getrennt
		 * - dateien heissen z.b. 001walls.gif, 001floors und 001items
		 * 		würden dann ja in der richtigen reihenfolge ausgelesen
		 * 		und können in der reihenfolge in die arrays gespeichert werden.
		 * 		dann kann ein neuer level mit den parametern (floors[index],walls[index],items[index]) erstellt werden
		 * */
		URL path = getClass().getResource("maps/");
		File f = new File(path.getPath());
		File[] files = f.listFiles();
		Arrays.sort(files);
		for(int i =0; i< files.length; i++){
			try {
				if(files[i].getAbsolutePath().endsWith("walls.gif")){
					allMapsWalls.add(ImageIO.read(files[i]));
				}else if(files[i].getAbsolutePath().endsWith("items.gif")){
					allMapsItems.add(ImageIO.read(files[i]));
				}else{
					allMapsFloors.add(ImageIO.read(files[i]));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void loadSpecificRoomStuff(){
		/*
		 * es gibt neben floor- und wall- map auch eine itemmap,
		 * z.b. 5 verschiedene farben für 5 verschiedene typen von items
		 * und dann per random, also typ 1 -> waffe; random 4 -> waffe nr 4 
		 * so kann festgelegt werden an welcher stelle man etwas findet und es gibt trotzdem abwechslung
		 * 
		 * truhen/ spinde etc:
		 * eine spawnfarbe für truhen, die truhe ist ein item
		 * beim aufnehmen verschwindet die truhe nicht (ändert nur grafik in geöffnet)
		 * -> extramethode um item in truhe zu erstellen
		 *  
		 * */
		//doorShape = new Polygon();
		for(int x = 0; x < level.mapPic.getWidth(); x++){
			for(int y = 0; y< level.mapPic.getHeight(); y++){
			//items laden	
				//if(level.map[x][y][2] == 1){
					//int rdm = (int)Math.random()*5;
				//}
				int itemtypee = level.map[x][y][2];
				if(itemtypee != 0){
					Item it = new Item(this,x*32,y*8,itemtypee-1);
					itemHandler.itemsInLevel.add(it);
				}
				
				if(level.map[x][y][5] < 666){
					SpecialEffect sE = new SpecialEffect(level.map[x][y][5]);
					sE.setPos(x,y);
					specialEffects.add(sE);
				}
			}
		}
	}
	
	public void levelChanging(){
		
		
//		panel.running = false;
//		//gameloop.running = false;
//		bullethandler.running = false;
//		enemycontrol.running = false;
//		itemHandler.running = false;
		//this.setVisible(false);
		int nextRoom = rdm.nextInt(4);//(int) (Math.random() *4);
		
		//roomsEntered.addLast(nextRoom);
		reInitWindow(nextRoom);
		
		panel.running = true;
		gameloop.running = true;
		bullethandler.running = true;
		enemycontrol.running = true;
		itemHandler.running = true;
		//loadingScreen.dispose();
		//this.setVisible(true);
	}
	
	public void createDungeon(){
		
	}
}
