import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class Level {

	BufferedImage allTiles, mapPic,mapPicwalls;
	int[][][] map;
	Polygon collisionshape; 
	ArrayList<Point> collisionpoints;
	
	public Level(){
		
		try{
			
			mapPic = ImageIO.read(getClass().getResource("resources/neuerraum2.gif"));
			mapPicwalls = ImageIO.read(getClass().getResource("resources/neuerraum2waende.gif"));
		}catch(IOException e){
			e.printStackTrace();
		}
		
		loadMap();
	}
	public void  loadMap(){
		int mapHeight =mapPic.getHeight();
		int mapWidth = mapPic.getWidth();
		map = new int[mapWidth][mapHeight][4];
		collisionshape = new Polygon();
		collisionpoints = new ArrayList<Point>();
		/* legende:
		 * [x][y][z]
		 * 
		 * z=0 	-> tiles 0 bis ...
		 * 1 	-> begehbar oder nicht (0 / 1)
		 * 2	-> item vorhanden? 0/1
		 * 3	-> noch nicht vergeben
		 * 4	-> "	"	"
		 * */
		
		
		/*
		 * Um karten zu zeichnen definieren wir bestimmte Farben, die später 
		 * als ID gespeichert werden.
		 * Da es viel Tiles werden, vorher fest definieren:
		 * 
		 * Color c001 = new Color(100,100,100);
		 * Color c002 = new Color(110,110,110);
		 * 
		 * Color c015 = new Color(0,0,100);
		 * usw ...
		 * möglichst nach geländeart sortiert 
		 * und später statt "c001" ruhig umschreibende namen nehmen wie gras oder steinlinksoben
		 * */
		Color schotter = Color.BLACK;
		Color schottergrob = new Color(0,0,50);
		Color steinplatten = new Color(0,0,100);
		Color steinplatten2 = new Color(0,0,150);
		Color plattenriss = new Color(0,0,200);
		Color plattenriss2 = new Color(0,0,250);
		Color wandrechts = new Color(0,150,0);
		Color wandlinks = new Color(0,150,100);
		Color invisibleCollisionWall = new Color(0,150,250);
		for(int x = 0; x < mapWidth;x++){
			for(int y = 0; y < mapHeight;y++){
				Color c = new Color(mapPic.getRGB(x, y));
				Color d =new Color(mapPicwalls.getRGB(x, y));
				
			//bodentiles
				
				if(c.equals(	 schotter		)){		map[x][y][0]=0;}
				else if(c.equals(schottergrob	)){		map[x][y][0]=1;}
				else if(c.equals(steinplatten	)){		map[x][y][0]=2;}
				else if(c.equals(steinplatten2	)){		map[x][y][0]=3;}
				else if(c.equals(plattenriss	)){		map[x][y][0]=4;}
				else if(c.equals(plattenriss2	)){		map[x][y][0]=5;}
				
				
				else if(c.equals(Color.WHITE)){				map[x][y][3]=666;
															map[x][y][0]=666;
															map[x][y][1]=1;}
				else{
						map[x][y][1]=1;
				}
			
			//wand-tiles
				
				if(d.equals(wandrechts)){					map[x][y][3]=1;
															map[x][y][1]=1;
															collisionpoints.add(new Point(x,y));
															//collisionshape.addPoint(x*32, y*32);
				}else if(d.equals(wandlinks)){				map[x][y][3]=0;
															map[x][y][1]=1;
															collisionpoints.add(new Point(x,y));
				}
				else if (d.equals(invisibleCollisionWall)){
															
															collisionpoints.add(new Point(x,y));
				}
				else{
					map[x][y][3]=666;
				}
				
				
				
				
				
			}
		}
		//zum testen:
		for(Point p: collisionpoints){
			
			if(p.y%2 ==0){
				p.x = p.x*32 +16 ;
				p.y = p.y *8 +32;
			}else if(p.y%2 ==1){
				p.x = p.x*32 +32;
				p.y = p.y*8 +32 ;
			}
					collisionshape.addPoint(p.x, p.y);
					
		}
		
	}
}
