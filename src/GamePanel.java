import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;


@SuppressWarnings("serial")
public class GamePanel extends Canvas implements Runnable{
	boolean running = true;
	GameWindow window;
	
	Graphics graphics;
	Graphics2D g2d ;
	BufferStrategy buffer;
	BufferedImage bi;
	int panelwidth,panelheight; 
	Level level;
	int mapHeight=0, mapWidth =0;
	ArrayList<BufferedImage> tilesetfloor, tilesetwalls;
	int[][][]map;
	
	Tileset tileset;
	
	Player player;
	Enemy enemy;
	int gamespeed =16;
	
	ArrayList<Bullet> bulletsInRoom;
	ArrayList<Enemy> enemylist, corpses;
	ArrayList<Item> itemsInLevel;
	ArrayList<ActionMessage> actionMessages= new ArrayList<ActionMessage>();
	ArrayList<ActionMessage> tempmessages;
	
	CopyOnWriteArrayList<Enemy> enemiesToDraw, corpsesToDraw;
	CopyOnWriteArrayList<ActionMessage> messagesToDraw;
	
	Polygon collision;
	double transformModifier; 
	
	public GamePanel(GameWindow w ){//int scrsizeopt
		//transformModifier = scrsizeopt;
		window = w;
		enemylist = window.enemylist;
		corpses = window.corpses;
		bulletsInRoom = window.bulletsInRoom;//
		/*hier stand:
		 * 
		 * level = window.level;
		mapWidth = level.mapPic.getWidth()*32;//
		mapHeight = level.mapPic.getHeight()*64;//vllt raus? lvl hat ja immer selbe gr��e
		 * */
		initPanel();
		tileset = new Tileset();
		tilesetfloor = tileset.tilesetfloor;
		tilesetwalls = tileset.tilesetwalls;
		
		player = window.player;
		// hier stand: enemylist = window.enemylist;
//		itemsInLevel = new ArrayList<Item>();
		
		this.setIgnoreRepaint(true);
		
		graphics =null;
		g2d = null;
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();
		
		bi = gc.createCompatibleImage(gc.getBounds().width,gc.getBounds().height);
		panelwidth = gc.getBounds().width;
		panelheight = gc.getBounds().height;
	}
	
	public void initPanel(){
		level = window.activeLevel;
		mapWidth = window.activeLevel.mapwidth*32;
		mapHeight = level.mapheight*64;//vllt raus? lvl hat ja immer selbe gr��e
		map = level.map;
		collision = level.collisionshape;//
		System.out.println("raumtyp: "+ level.roomtype + " floor[x]: "+ window.allMapsFloors.indexOf(level.mapPic)+ " walls[y]: "+ window.allMapsWalls.indexOf(level.mapPicwalls));
//		for(int i=0;i< collision.npoints; i++){
//			System.out.println("Collisionshape x[]:"+ collision.xpoints[i]+" collisionshape y[]: " + collision.ypoints[i]);
//		}
		
	}
	
	public Dimension getPreferredSize(){
		return new Dimension(panelwidth,panelheight);		//?
	}
	
	public void drawEnemies(Graphics g){
		if(enemylist.size() > 0){
			enemiesToDraw = new CopyOnWriteArrayList<Enemy>(enemylist);
			for (Enemy e : enemiesToDraw){
				g.drawImage(e.getImage(),e.getX(),e.getY()-16,32,48,null);
			}
		}
		
		if(corpses.size() >0){
			corpsesToDraw = new CopyOnWriteArrayList<Enemy>(corpses);
			for(Enemy e: corpsesToDraw){
				g.drawImage(e.corpseImage,e.getX(),e.getY()-16,48,32,null);
			}
		}
	}
	public void drawLevelFloor(Graphics g){
		for(int y = 0; y < mapHeight/64;y++){
			for(int x = 0; x < mapWidth/32;x++){
				if(level.map[x][y][0] <200){
					BufferedImage i = tilesetfloor.get(level.map[x][y][0]);
					int z = y%2;
					switch(z){
					case(0):
						g.drawImage(i, x*32,y*8,null);
					//alternativ 64pixel:
					//g.drawImage(i, x*64,y*16-16,null);-16
					break;
					case(1):
						g.drawImage(i, x*32+16, y*8,null);
					//alternativ 64pixel:
					//g.drawImage(i, x*64 +32, y*16-16,null);
						break;
					}
				}
				
				//64 pixel:
//			for(int x = 0; x < mapWidth/64;x++){
//				for(int y = 0; y < mapHeight/128;y++){
//					BufferedImage i = tileset.get(level.map[x][y][0]);
//					if(y%2 == 0){
//						g.drawImage(i, x*64,y*16,null);//g.drawImage(i, x*64,y*16-16,null);
//					}else{
//						g.drawImage(i, x*64 +32, y*16,null);//g.drawImage(i, x*64 +32, y*16-16,null);
//					}
				//i=null;
			}
		}
	}
	public void drawLevelWalls(Graphics g){
		for(int y = 0; y < mapHeight/64;y++){
			for(int x = 0; x < mapWidth/32;x++){
				if((level.map[x][y][3] < 200)){//&&(level.map[x][y][3]>=0)
					
					if(level.map[x][y][3] !=666){
						BufferedImage i = tilesetwalls.get(level.map[x][y][3]);
						//					if(y%2 == 0){
						int z = y%2;
						switch(z){
							case(0):
								g.drawImage(i, x*32,y*8+8,null);
							//alternativ 64pixel:
							//g.drawImage(i, x*64,y*16-16,null);
								break;
							case(1):
								g.drawImage(i, x*32 +16, y*8+8,null);
							//alternativ 64pixel:
							//g.drawImage(i, x*64 +32, y*16-16,null);
								break;
						}
					}
				}
			}
		}
	}
	
	public void drawPlayer(Graphics g){
		g.drawImage(player.getImage(),player.getX(),player.getY(),32,48,null);
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
	
	public void drawLatestActionMessage(Graphics g){
		g.setColor(Color.WHITE);
		//actionMessages = tempmessages;
		int b=0;
		//tempmessages = new ArrayList<ActionMessage>();
		messagesToDraw = new CopyOnWriteArrayList<ActionMessage>(actionMessages);
		for(ActionMessage m : messagesToDraw){
			if(m.lifetime >0){
//				if((m.lifetime <=40) && (m.lifetime >30)){
//					
//					g.setColor(new Color(200,200,200,200));
//				}
//				if((m.lifetime <=30) && (m.lifetime >20)){
//					g.setColor(new Color(150,150,150,150));
//				}
//				if((m.lifetime <=20) && (m.lifetime >10)){
//					g.setColor(new Color(100,100,100,100));
//				}
//				if((m.lifetime <=10) && (m.lifetime >=0)){
//					g.setColor(new Color(50,50,50,50));
//				}
				
				g.setColor(new Color(255,50,50,200 - (2*m.lifetime)));
				g.drawString(m.getText(), panelwidth/4-(m.getText().length()*2), panelheight/4 + (b * 10));
				b++;
				m.reduceLife();
				if(m.lifetime ==0){
					actionMessages.remove(m);
				}
				//tempmessages.add(m);
			}
		}
		g.setColor(Color.WHITE);
		//actionMessages = tempmessages;
	}
	
	public void drawGUI(Graphics g){
		//hier wird sp�ter noch ein schicker rahmen mit feldern f�r lebensenergie, inventar etc gezeichnet
		//erstmal nur anzeige der lebensenergie und sowas zum debuggen
		g.setColor(Color.WHITE);
		if(player.energy >0){
			g.drawString("Lebensenergie: " + player.energy,50 ,150);
			int a = (int)player.energy/10;
			for(int b =0; b<a;b++){
				if((int)player.energy < 40){
					g.setColor(Color.RED);
				}
				g.fillRect(180 + b*4,140,2,10);
				g.setColor(Color.WHITE);
			}
			
		}else{
			g.drawString("Spieler waere jetzt tot, Energie: "+ player.energy,50 ,150 );
		}
		if(player.equipment[0] != null){
			g.drawString("Itemslot 1: " + player.equipment[0].name, 50,50);
		}else{
			g.drawString("No Equipment in Itemslot 1 ", 50, 50);
		}
		if(player.equipment[1] != null){
			g.drawString("Itemslot 2: " + player.equipment[1], 50,70);
		}else{
			g.drawString("No Equipment in Itemslot 2 ", 50, 70);
		}
		if(player.equipment[2] != null){
			g.drawString("Itemslot 3: " + player.equipment[2], 50,90);
		}else{
			g.drawString("No Equipment in Itemslot 3 ", 50, 90);
		}
		if(player.equipment[3] != null){
			g.drawString("Itemslot 4: " + player.equipment[3], 50,110);
		}else{
			g.drawString("No Equipment in Itemslot 4 ", 50, 110);
		}
		
		g.drawString("Spieler an position map[x][y]: "+ player.posX/32 +", "+ player.posY/48,50,180);
		int index =0;
		CopyOnWriteArrayList<Enemy> enemies = new CopyOnWriteArrayList<Enemy>(enemylist);
		for(Enemy e : enemies){
			g.drawString("Enemy: "+ e.posX+" "+e.posY + " bounds: "+ e.enemyBounds.x+", "+e.enemyBounds.y+ ", Energy: "+ e.energy, 50, 200 + (index*20));
			index++;
		}
		g.drawString("[Q]uit", 50,30);
						for(Point p: level.collisionpoints){
							g.setColor(Color.RED);
							g.fillRect(p.x, p.y, 1, 1);
						}
		g.drawPolygon(collision);
		for(int i=0; i< level.doorShapes.length;i++){
			if(level.doorPoints.get(i) != null){
				g.drawPolygon(level.doorShapes[i]);
//				System.out.println("t�r "+i+": "+ level.doorPoints.get(i).x+" , "+level.doorPoints.get(i).y );
			}
		}
		for(int i =0;i< level.doorShapes.length;i++){
			if(level.doorShapes[i] != null){
				if(level.doorShapes[i].intersects(player.playerBounds)){
					System.out.println("neighbors[] -> t�r f�hrt zu:" + level.neighbors[i]);
					//System.out.println(""+ level.);
				}
			}
		}
		
						g.setColor(Color.GREEN);
						g.drawRect(player.playerBounds.x,player.playerBounds.y, player.playerBounds.width, player.playerBounds.height);
						g.drawRect(player.playermiddle.x, player.playermiddle.y, 1,1);
	}
	
	public void drawItems(Graphics g){
		CopyOnWriteArrayList<Item> itemList = new CopyOnWriteArrayList<Item>(window.activeLevel.thisLevelsItems); 
		for(Item i: itemList){
			if(i != null){
				g.drawImage(i.getImage(), i.posX,i.posY,null);
			}
		}
	}
	
	public void drawSpecialeffects(Graphics g){
		CopyOnWriteArrayList<SpecialEffect> seList = new CopyOnWriteArrayList<SpecialEffect>(window.activeLevel.specialEffects); 
		for(SpecialEffect se : seList){
			BufferedImage i = se.getEffectImage();
			int z = se.yPos%2;
			switch(z){
				case(0):
					g.drawImage(i, se.xPos*32,se.yPos*8+8,null);
					break;
				case(1):
					g.drawImage(i, se.xPos*32 +16, se.yPos*8+8,null);
					break;
			}
		}
	}
	
	public void changeColorScheme(Graphics g){
		
	}
	
	@Override
	public synchronized void run() {
		
		this.createBufferStrategy(2);	//Bufferstrategie setzen, 2= double buffer, 3=triple buffer
		buffer = this.getBufferStrategy();
		
		while(running){
			float onStart = System.currentTimeMillis();
			try{
				g2d = bi.createGraphics();
				g2d.setColor(Color.BLACK);
				g2d.fillRect(window.cameraPosition.x,window.cameraPosition.y,window.screen.width,window.screen.height);
				
				//hier dinge zeichnen
				
				
				drawLevelFloor(g2d);
				drawLevelWalls(g2d);
				
				drawItems(g2d);
				drawEnemies(g2d);
				drawPlayer(g2d);
				drawSpecialeffects(g2d);
				drawBullets(g2d);
				
				drawGUI(g2d);
				
				drawLatestActionMessage(g2d);
				graphics = buffer.getDrawGraphics();
				
				AffineTransform at = new AffineTransform();
				
//				if(window.screenoption == 1){
//					at.scale(1.25,1.25);
//				}
//				if(window.screenoption == 2){
//					at.scale(2,2);
//				}
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
