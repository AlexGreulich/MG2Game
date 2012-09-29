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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


public class GamePanel extends Canvas implements Runnable{

	GameWindow window;
	
	Graphics graphics;
	Graphics2D g2d ;
	BufferStrategy buffer;
	BufferedImage bi;
	int panelbreite,panelhoehe; 
	Level level;
	int kartenhoehe, kartenbreite;
	ArrayList<BufferedImage> tileset;
	int[][][]karte;
	
	Tileset set;
	
	Player player;
	int gamespeed =5;
	//BulletHandler bullethandler;
	
	ArrayList<Bullet> kugelnimraum;
	Collection  kugeln;
	
	public GamePanel(GameWindow w){
		
		window = w;
		level = window.level;
		kartenbreite = level.kartenbild.getWidth()*64;
		kartenhoehe = level.kartenbild.getHeight()*128;
		
		set = new Tileset();
		tileset = set.tileset;
		karte = level.karte;
		player = window.player;
		this.setIgnoreRepaint(true);
		
		
		
		graphics =null;
		g2d = null;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		
		bi = gc.createCompatibleImage(1920,1080);
		panelbreite = gc.getBounds().width;
		panelhoehe = gc.getBounds().height;
		kugelnimraum = new ArrayList<Bullet>();
		kugeln = Collections.synchronizedList(kugelnimraum);
	}
	
	public Dimension getPreferredSize(){
		
		
		
		return new Dimension(panelbreite,panelhoehe);		//?
	}
	
	public void zeichneLevel(Graphics g){
		
		for(int x = 0; x < kartenbreite/64;x++){
			for(int y = 0; y < kartenhoehe/128;y++){
				BufferedImage i = tileset.get(level.karte[x][y][0]);
				if(y%2 == 0){
					g.drawImage(i, x*64,y*16,null);//g.drawImage(i, x*64,y*16-16,null);
				}else{
					g.drawImage(i, x*64 +32, y*16,null);//g.drawImage(i, x*64 +32, y*16-16,null);
				}
				//i=null;
			}
		}
	}
	
	public void zeichneSpieler(Graphics g){
		g.drawImage(player.getImage(),player.getX(),player.getY()-32,64,96,null);
	}
	
	public void zeichneKugeln(Graphics g){
		
		
		for(int index =0; index< kugelnimraum.size();index++){
			Bullet b = kugelnimraum.get(index);
			if(b != null){
				g.drawImage(b.image,b.posX,b.posY,null);
				
			}
		}
		//kugeln.clear();
	}
	
	
	@Override
	public synchronized void run() {
		
		this.createBufferStrategy(2);	//Bufferstrategie setzen, 2= double buffer, 3=triple buffer
		buffer = this.getBufferStrategy();
		
		while(true){
			float amAnfang = System.currentTimeMillis();
			try{
				g2d = bi.createGraphics();
				g2d.setColor(Color.BLACK);
				g2d.fillRect(0,0,1920,1080);
				
				//hier dinge zeichnen
				zeichneLevel(g2d);
				zeichneSpieler(g2d);
				zeichneKugeln(g2d);
				
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
			
			float amEnde = System.currentTimeMillis()- amAnfang;
			if(gamespeed > amEnde){
				try {
					Thread.sleep(gamespeed -(int)amEnde);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
	

}
