import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class GamePanel extends Canvas implements Runnable{

	GameWindow window;
	
	Graphics graphics;
	Graphics2D g2d ;
	BufferStrategy buffer;
	BufferedImage bi;
	int panelwidth,panelheight; 
	Level level;
	int mapHeight, mapWidth;
	ArrayList<BufferedImage> tileset;
	int[][][]map;
	
	Tileset set;
	
	Player player;
	int gamespeed =5;
	//BulletHandler bullethandler;
	
	ArrayList<Bullet> bulletsInRoom;
	ArrayList<Enemy> enemylist;
	Collection  bullets;
	
	public GamePanel(GameWindow w){
		
		window = w;
		level = window.level;
		mapWidth = level.mapPic.getWidth()*64;
		mapHeight = level.mapPic.getHeight()*128;
		
		set = new Tileset();
		tileset = set.tileset;
		map = level.map;
		player = window.player;
		this.setIgnoreRepaint(true);
		
//		try {
////			soundStart();
//		} catch (UnsupportedAudioFileException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (LineUnavailableException e) {
//			e.printStackTrace();
//		}
		
		graphics =null;
		g2d = null;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		
		bi = gc.createCompatibleImage(1920,1080);
		panelwidth = gc.getBounds().width;
		panelheight = gc.getBounds().height;
		bulletsInRoom = new ArrayList<Bullet>();
		bullets = Collections.synchronizedList(bulletsInRoom);
	}
	
//	public void soundStart() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
//		//URL url = new URL(getClass().getResource("audio/"), null);
//	//	String filestr = url.getFile();
//		File file = new File("test_track_01.mp3");
//		AudioInputStream ais = AudioSystem.getAudioInputStream(file);
//		AudioFormat mp3AudioFormat = ais.getFormat();
//		AudioFormat decodedAudioFormat = new AudioFormat( AudioFormat.Encoding.PCM_SIGNED, 
//				mp3AudioFormat.getSampleRate(), 
//				16, 
//				mp3AudioFormat.getChannels(), 
//				mp3AudioFormat.getChannels() * 2, 
//				mp3AudioFormat.getSampleRate(), 
//				false);
//		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(decodedAudioFormat, ais);
//		Clip clip = AudioSystem.getClip();
//		clip.open(ais);
//		clip.start();
//	}
	
	public Dimension getPreferredSize(){
		
		
		
		return new Dimension(panelwidth,panelheight);		//?
	}
	
	public void drawLevel(Graphics g){
		
		for(int x = 0; x < mapWidth/64;x++){
			for(int y = 0; y < mapHeight/128;y++){
				BufferedImage i = tileset.get(level.map[x][y][0]);
				if(y%2 == 0){
					g.drawImage(i, x*64,y*16,null);//g.drawImage(i, x*64,y*16-16,null);
				}else{
					g.drawImage(i, x*64 +32, y*16,null);//g.drawImage(i, x*64 +32, y*16-16,null);
				}
				//i=null;
			}
		}
	}
	
	public void drawPlayer(Graphics g){
		g.drawImage(player.getImage(),player.getX(),player.getY()-32,64,96,null);
	}
	
	public void drawBullets (Graphics g){
		
//		Collections.synchronizedList(
//			
//		}
		for(int index =0; index < bulletsInRoom.size();index++){
			Bullet b = bulletsInRoom.get(index);
			if(b != null){
				g.drawImage(b.image,b.posX,b.posY,null);
				
			}
		}
		
	}
	
	
	@Override
	public synchronized void run() {
		
		this.createBufferStrategy(2);	//Bufferstrategie setzen, 2= double buffer, 3=triple buffer
		buffer = this.getBufferStrategy();
		
		while(true){
			float onStart = System.currentTimeMillis();
			try{
				g2d = bi.createGraphics();
				g2d.setColor(Color.BLACK);
				g2d.fillRect(0,0,1920,1080);
				
				//hier dinge zeichnen
				drawLevel(g2d);
				drawPlayer(g2d);
				drawBullets(g2d);
				
				
				
				graphics = buffer.getDrawGraphics();
				graphics.drawImage(bi,0,0,null);
				
				if(!buffer.contentsLost()){
					buffer.show();
				}
				
				Thread.yield();
			}finally{
				if(graphics != null){
					graphics.dispose();
				}
				if(g2d != null){
					g2d.dispose();
				}
			}
			
			float onEnd = System.currentTimeMillis()- onStart;
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
