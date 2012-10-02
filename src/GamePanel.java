import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import javax.sound.sampled.Clip;


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
	Enemy enemy;
	int gamespeed =5;
	
	ArrayList<Bullet> bulletsInRoom;
	ArrayList<Enemy> enemylist;
	
	public GamePanel(GameWindow w){
		
		window = w;
		level = window.level;
		mapWidth = level.mapPic.getWidth()*64;
		mapHeight = level.mapPic.getHeight()*128;
		
		set = new Tileset();
		tileset = set.tileset;
		map = level.map;
		player = window.player;
		enemylist = window.enemylist;
		
		this.setIgnoreRepaint(true);
		
		graphics =null;
		g2d = null;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		
		bi = gc.createCompatibleImage(1920,1080);
		panelwidth = gc.getBounds().width;
		panelheight = gc.getBounds().height;
		bulletsInRoom = window.bulletsInRoom;
	}
	
	public Dimension getPreferredSize(){
		return new Dimension(panelwidth,panelheight);		//?
	}
	
	public void drawEnemies(Graphics g){
		
		if(enemylist.size() > 0){
			for (Enemy e : enemylist){
				g.drawImage(e.getImage(),e.getX(),e.getY()-32,64,96,null);
			}
		}
		
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
		if(bulletsInRoom.size() > 0){
			for(int index =0; index < bulletsInRoom.size();index++){
			Bullet b = bulletsInRoom.get(index);
			if(b != null){
				g.drawImage(b.image,b.posX,b.posY,null);
			}
		}
		}
		
	}
	
	public void drawGUI(Graphics g){
		//hier wird später noch ein schicker rahmen mit feldern für lebensenergie, inventar etc gezeichnet
		//erstmal nur anzeige der lebensenergie und sowas zum debuggen
		g.setColor(Color.WHITE);
		if(player.energy >0){
			g.drawString("Lebensenergie: "+ (int)player.energy,50 ,50);
		}else{
			g.drawString("Spieler waere jetzt tot, Energie: "+ player.energy,50 ,50 );
		}
		
		int index =0;
		for(Enemy e : enemylist){
			g.drawString("Enemy: "+ e.posX+" "+e.posY + " bounds: "+ e.enemyBounds.x+", "+e.enemyBounds.y+ ", Energy: "+ e.energy, 50, 100 + (index*20));
			index++;
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
				drawEnemies(g2d);
				drawGUI(g2d);
				
				graphics = buffer.getDrawGraphics();
				
				AffineTransform at = new AffineTransform();
				at.scale(2,2);
				Graphics2D gr2d = (Graphics2D)graphics;
				gr2d.setTransform(at);
				
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
