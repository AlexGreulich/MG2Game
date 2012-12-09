import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JWindow;
//import java.util.HashMap;

//import org.omg.IOP.Encoding;


@SuppressWarnings("serial")
public class GameWindow extends JFrame{

	GamePanel panel;
	Player player;
	Controls controls;
	Thread panelThread, gameloopthread, bulletthread, enemythread, itemthread;
	Level activeLevel;
	Gameloop gameloop;
	BulletHandler bullethandler;
	EnemyController enemycontrol;
	ItemHandler itemHandler;
	ArrayList<Bullet> bulletsInRoom;
	ArrayList<SpecialEffect> specialEffects;
	
	Clip bckgrdTrack,pistolShot,pistolHit,clip4, pistolReload, emptyPistol, heartPumping;
	Clip tempClip ;
	int framePosition;
	
	Point cameraPosition = new Point(0,0);
	Dimension screen;
	
	int screenoption=0;
	JWindow splashwindow;
	BufferedImage startupBckgrd;
	BufferedImage[] startupMenuPics;
	
	ArrayList<BufferedImage> allMapsFloors;
	ArrayList<BufferedImage> allMapsWalls;
	ArrayList<BufferedImage> allMapsItems;
	
	Polygon doorShape;
	
	Random rdm = new Random();
	PathCreator pathCreate = new PathCreator();
	@SuppressWarnings("static-access")
	HashMap<Integer,int[]> dungeon = pathCreate.map;
	int startingRoom;
	HashMap<Integer,Level> availableRooms = new HashMap<Integer, Level>();
	Font gameFont;
	
	
	public GameWindow() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
		super("Cybercalypse");
		
		//aufwaendige dinge laden
		bckgrdTrack = loadMusic("audio/test_track_01.mp3");
		pistolShot = loadMusic("audio/pistolshot01.mp3");
		pistolHit = loadMusic("audio/pistolhit01.mp3");
		pistolReload = loadMusic("audio/pistolReload01.mp3");
		emptyPistol = loadMusic("audio/emptyPistol.mp3");
		heartPumping = loadMusic("audio/heartPumping.mp3");
		
		try {
			gameFont = Font.createFont(NORMAL, getClass().getResourceAsStream("resources/pixelmix.ttf")).deriveFont(8f);
			this.setFont(gameFont);
		} catch (FontFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		screen = Toolkit.getDefaultToolkit().getScreenSize();
		int scrx = (int) (screen.getWidth()/2)-500;
		int scry = (int) (screen.getHeight()/2)-375;
		splashwindow = new JWindow();
		splashwindow.setSize(1000, 750);
		splashwindow.setLocation(scrx, scry);
		startupBckgrd = ImageIO.read(getClass().getResource("resources/cybermood2small.gif"));
		startupMenuPics = new BufferedImage[4];
		for(int i =0; i<=3;i++){
			startupMenuPics[i] = startupBckgrd.getSubimage(i* 1000, 0, 1000, 750);
		}
		startupBckgrd = startupMenuPics[0];
		splashwindow.getContentPane().add(new JPanel(){
			public void paintComponent(Graphics g){
				g.drawImage(startupBckgrd,0,0,null);
			}
		});
		
		MouseAdapter ma = new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				int x = e.getX();
				int y = e.getY();
					
				if((x > 70) && (x <= 270)){
					if((y > 550) && (y <=600)){
						splashwindow.dispose();
						//bckgrdTrack.loop(Clip.LOOP_CONTINUOUSLY);
						//bckgrdTrack.start();
						
						startGame();	
					}else if((y > 650) && (y <= 700)){
						System.exit(0);
					}
				}
			}
			public void mouseMoved(MouseEvent ev){
				int x = ev.getX();
				int y = ev.getY();
				
				if((x > 70) && (x <= 270)){
					if((y > 550) && (y <=600)){
						startupBckgrd = startupMenuPics[1]; 
						splashwindow.repaint();
					}else if((y > 600) && (y <= 650)){
						startupBckgrd = startupMenuPics[2]; 
						splashwindow.repaint();
					}else if((y > 650) && (y <= 700)){
						startupBckgrd = startupMenuPics[3]; 
						splashwindow.repaint();
					}else{
						startupBckgrd = startupMenuPics[0]; 
						splashwindow.repaint();
					}
				}
			}
		};
		splashwindow.addMouseMotionListener(ma);
		splashwindow.addMouseListener(ma);
		splashwindow.setVisible(true);
		splashwindow.repaint();
		
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
		
		this.activeLevel = availableRooms.get(roomNumber);
		panel.initPanel();	//panel neu initialisiert mit level, level.map, enemylist 
		gameloop.initLoop();
		bullethandler.initBulletHandler();
		this.bulletsInRoom.clear();
		this.specialEffects = this.activeLevel.specialEffects;
		itemHandler.initItemHandler();
		enemycontrol.initEnemyController();
		System.out.println("Akt. Raum nr.: "+ activeLevel.nr);
	}
	
	public void startGame(){
		
		
		allMapsFloors = new ArrayList<BufferedImage>();
		allMapsWalls = new ArrayList<BufferedImage>();
		allMapsItems = new ArrayList<BufferedImage>();
		this.controls = new Controls();
		
		loadLevelPics();
		this.player = new Player(this);
		createDungeon();
		this.activeLevel = availableRooms.get(startingRoom);
		this.itemHandler = new ItemHandler(this);
		this.bulletsInRoom = new ArrayList<Bullet>();
		this.specialEffects = new ArrayList<SpecialEffect>();
		this.panel = new GamePanel(this);//, screenoption
		
		for(Level l : availableRooms.values()){
			l.loadSpecificRoomStuff();
		}
		
		this.gameloop = new Gameloop(this);
		this.enemycontrol = new EnemyController(this);
		this.bullethandler = new BulletHandler(this);
		
		activeLevel.map[10][20][5]=2;
		addKeyListener(controls);
		add(panel);
		this.setVisible(true);
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
		System.out.println("Der startraum ist nr.: " + this.startingRoom);
		System.out.println("und activeLevel nr: " + activeLevel.nr);
		pack();
	}
	
	public Clip loadMusic(String mp3Name){

		AudioInputStream mp3audioInputStream;
		try {
			mp3audioInputStream = AudioSystem.getAudioInputStream(getClass().getResource(mp3Name));
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
			tempClip = AudioSystem.getClip();
			tempClip.open(audioInputStream);
			
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (LineUnavailableException e) {
			e.printStackTrace();
		}
			return tempClip;
	}
	
	public void loadLevelPics(){
		
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
	
	public void levelChanging(int whichRoomToChangeTo, int doorPlayerCameFrom){
		
		if(doorPlayerCameFrom == 0){
			Point newXY = availableRooms.get(whichRoomToChangeTo).doorPoints.get(2);
			player.posX = newXY.x *32- 30;
			player.posY = newXY.y *8 -30;
		}else if(doorPlayerCameFrom == 1){
			Point newXY = availableRooms.get(whichRoomToChangeTo).doorPoints.get(3);
			player.posX = newXY.x*32 +30;
			player.posY = newXY.y*8 -30;
		}else if(doorPlayerCameFrom == 2){
			Point newXY = availableRooms.get(whichRoomToChangeTo).doorPoints.get(0);
			player.posX = newXY.x*32 +30;
			player.posY = newXY.y*8 +30;
		}else if(doorPlayerCameFrom == 3){
			Point newXY = availableRooms.get(whichRoomToChangeTo).doorPoints.get(1);
			player.posX = newXY.x*32 -30;
			player.posY = newXY.y*8 +30;
		}
		reInitWindow(whichRoomToChangeTo);
	}
	
	public void createDungeon(){
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for(int i= 0; i< dungeon.size();i++){
		
			if(dungeon.get(i)[0] == 1){	//ersten gueltigen raum gefunden
				temp.add(i);
			}	
			int type = dungeon.get(i)[1];
				Level lvl = new Level(this, allMapsFloors.get(type), allMapsWalls.get(type), allMapsItems.get(type),type , i, dungeon.get(i));
				availableRooms.put(i, lvl);
		}
		for(int i=0; i< availableRooms.size();i++){
			System.out.println("availablerooms; raum nr: "+ availableRooms.get(i).nr +" ; nachbarn:"+availableRooms.get(i).neighbors);
		}
		for(int i=0; i< dungeon.size();i++){
			System.out.println("dungeon; raum nr: "+ i );
		}
		startingRoom = temp.get(0);
	}
	
	public void playIntro(){
		
	}
}
