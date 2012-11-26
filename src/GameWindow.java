import java.awt.Dimension;
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
import java.util.ArrayDeque;
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
	Level level;
	Gameloop gameloop;
	BulletHandler bullethandler;
	EnemyController enemycontrol;
	ItemHandler itemHandler;
	ArrayList<Bullet> bulletsInRoom;
	ArrayList<Enemy> enemylist, corpses;
	ArrayList<SpecialEffect> specialEffects;
	
	Clip clip1,clip2,clip3,clip4;
	Clip tempClip ;
	int framePosition;
	
	Point cameraPosition = new Point(0,0);
	Dimension screen;
	
	int screenoption=0;
	JWindow splashwindow;
	BufferedImage startupBckgrd;
	BufferedImage[] startupMenuPics;
	
	//HashMap levels;
	ArrayList<BufferedImage> allMapsFloors;
	ArrayList<BufferedImage> allMapsWalls;
	ArrayList<BufferedImage> allMapsItems;
	
	Polygon doorShape;
	
	//JWindow loadingScreen;
	
	Random rdm = new Random();
	PathCreator pathCreate = new PathCreator();
	@SuppressWarnings("static-access")
	HashMap<Integer,int[]> dungeon = pathCreate.map;
	int startingRoom;
	ArrayList<Level> allPossibleRooms = new ArrayList<Level>();
	
	
	
	public GameWindow() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
		super("Cybercalypse");
		
		//aufwaendige dinge laden
		clip1 = loadMusic("test_track_01.mp3");
		clip2 = loadMusic("test_track_01.mp3");
		
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
							//startMusic("audio/test_track_01.mp3");
							clip1.stop();
							clip2.start();
							startGame(0);	
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
		//startMusic("audio/introtest.mp3");
		clip1.start();
		
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
		
//		this.level = new Level(allMapsFloors.get(roomNumber), allMapsWalls.get(roomNumber), allMapsItems.get(roomNumber));
		
		panel.initPanel();	//panel neu initialisiert mit level, level.map, enemylist 
		gameloop.initLoop();
		bullethandler.initBulletHandler();
		this.bulletsInRoom.clear();
		this.enemylist.clear();
		this.corpses.clear();
		this.specialEffects.clear();
		itemHandler.initItemHandler();
		loadSpecificRoomStuff();
	}
	
	public void startGame(int roomNumber){
		
		allMapsFloors = new ArrayList<BufferedImage>();
		allMapsWalls = new ArrayList<BufferedImage>();
		allMapsItems = new ArrayList<BufferedImage>();
		
		loadLevelPics();
		createDungeon();
//		this.level = new Level(allMapsFloors.get(roomNumber), allMapsWalls.get(roomNumber), allMapsItems.get(roomNumber));
		this.level = allPossibleRooms.get(0);
		this.controls = new Controls();
		
		this.player = new Player(this);
		this.bulletsInRoom = new ArrayList<Bullet>();
		this.enemylist = new ArrayList<Enemy>();
		this.corpses = new ArrayList<Enemy>();
		this.specialEffects = new ArrayList<SpecialEffect>();
		this.panel = new GamePanel(this, screenoption);
		
		this.gameloop = new Gameloop(this);
		this.itemHandler = new ItemHandler(this);
		this.enemycontrol = new EnemyController(this);
		this.bullethandler = new BulletHandler(this);
		
		level.map[10][20][5]=2;
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
//		clip.start();
//		clip.loop(Clip.LOOP_CONTINUOUSLY);
		
			return tempClip;
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
		
		//int nextRoom = rdm.nextInt(4);//(int) (Math.random() *4);
		
		//roomsEntered.addLast(nextRoom);
		//reInitWindow(nextRoom);
		
//		panel.running = true;
//		gameloop.running = true;
//		bullethandler.running = true;
//		enemycontrol.running = true;
//		itemHandler.running = true;
	}
	
	public void createDungeon(){
		for(int i= 0; i< dungeon.size();i++){
		
			if(dungeon.get(i)[0] == 1){	//ersten gueltigen raum gefunden
				startingRoom = i;
				int type = dungeon.get(i)[1];
				Level lvl = new Level(allMapsFloors.get(type), allMapsWalls.get(type), allMapsItems.get(type),type , i, dungeon.get(i));
				allPossibleRooms.add(lvl);
			}	
			
			
			
		}
//		int[] roomZ = dungeon.keySet();
//		for(int roomcnt =roomZ[0];)dungeon.
	}
}
