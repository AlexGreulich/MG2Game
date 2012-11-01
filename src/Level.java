import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class Level {

	BufferedImage allTiles, mapPic,mapPicwalls,mapPicItems;
	int[][][] map;
	Polygon collisionshape; 
	ArrayList<Point> collisionpoints;
	
	ArrayList<Point> sortpoints;
	Point p1,p2,p3,p4,p5,p6,p7,p8;

	
	public Level(BufferedImage mapimgfloor, BufferedImage mapimgwalls, BufferedImage mapimgItems){
		
		mapPic = mapimgfloor;
		mapPicwalls = mapimgwalls;
		mapPicItems = mapimgItems;
		 
		loadMap();
		
		
	}
	public void  loadMap(){
		int mapHeight =mapPic.getHeight();
		int mapWidth = mapPic.getWidth();
		map = new int[mapWidth][mapHeight][4];
		collisionshape = new Polygon();
		collisionpoints = new ArrayList<Point>();
		sortpoints = new ArrayList<Point>();
		/* legende:
		 * [x][y][z]
		 * 
		 * z=0 	-> tiles 0 bis ...
		 * 1 	-> begehbar oder nicht (0 / 1)
		 * 2	-> item vorhanden? 0/1
		 * 3	-> wände
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
		
	//	BODEN	
		Color schotter = Color.BLACK;
		Color schottergrob = new Color(0,0,50);
		Color steinplatten = new Color(0,0,100);
		Color steinplatten2 = new Color(0,0,150);
		Color plattenriss = new Color(0,0,200);
		Color plattenriss2 = new Color(0,0,250);
		
		Color tuer01 = new Color(50,50,50);
		
		
	//	WAENDE
		Color wandrechts = new Color(0,150,0);
		Color wandlinks = new Color(0,150,100);
		
		Color invisibleCollisionWall = new Color(0,150,250);
	
	//	ITEMS
		Color itemColour = new Color(255,0,0);
		
		
		for(int x = 0; x < mapWidth;x++){
			for(int y = 0; y < mapHeight;y++){
				Color c = new Color(mapPic.getRGB(x, y));
				Color d =new Color(mapPicwalls.getRGB(x, y));
				Color e = new Color(mapPicItems.getRGB(x, y));
				
			//bodentiles
				
				if(c.equals(	 schotter		)){		map[x][y][0]=0;}
				else if(c.equals(schottergrob	)){		map[x][y][0]=1;}
				else if(c.equals(steinplatten	)){		map[x][y][0]=2;}
				else if(c.equals(steinplatten2	)){		map[x][y][0]=3;}
				else if(c.equals(plattenriss	)){		map[x][y][0]=4;}
				else if(c.equals(plattenriss2	)){		map[x][y][0]=5;}
				else if(c.equals(tuer01	)){				map[x][y][0]=5;}
				
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
			//items auslesen
				
				if(e.equals(itemColour)){
					map[x][y][2] = 1;
				}
				
				
				
				
			}
		}
		//zum testen:
//		Point lowest = new Point(0,0);
//		Point middleLeft = new Point(0,0);
//		Point middleRight = new Point(0,0);
//		Point highest = new Point(0,0);
		for(Point p: collisionpoints){
			
			if(p.y%2 ==0){
				p.x = p.x*32 +16 ;
				p.y = p.y *8 +32;
			}else if(p.y%2 ==1){
				p.x = p.x*32 +32;
				p.y = p.y*8 +32 ;
			}
			
//					collisionshape.addPoint(p.x, p.y);
					sortpoints.add(p);
//					collisionshape = collisionshape.
					
		}
		generatePolygon(sortpoints.get(0));//Testweise <--- waende sollten dann aber auch durchgaengig sein und nicht irgendwo aufhoeren
		
	}
	public void generatePolygon(Point p){
			p1 = new Point();
			p2 = new Point();
			p3 = new Point();
			p4 = new Point();
			p5 = new Point();
			p6 = new Point();
			p7 = new Point();
			p8 = new Point();
			p1.setLocation(p.x, p.y-16);
			p2.setLocation(p.x+32, p.y);
			p3.setLocation(p.x, p.y+16);
			p4.setLocation(p.x-32, p.y);
			p5.setLocation(p.x+16, p.y-8);
			p6.setLocation(p.x+16, p.y+8);
			p7.setLocation(p.x-16, p.y+8);
			p8.setLocation(p.x-16, p.y-8);
			collisionshape.addPoint(p.x,p.y);
			sortpoints.remove(p);
			sortpoints.trimToSize();
			if (sortpoints.isEmpty()==true){
			}//nichts machen
			else if (sortpoints.contains(p5)||sortpoints.contains(p6)||sortpoints.contains(p7)||sortpoints.contains(p8)){
				if (sortpoints.contains(p5)){
					generatePolygon(p5);
				}
				else if (sortpoints.contains(p6)){
					generatePolygon(p6);
				}
				else if (sortpoints.contains(p7)){
					generatePolygon(p7);
				}
				else if (sortpoints.contains(p8)){
					generatePolygon(p8);
				}
			}
			else if (sortpoints.contains(p1)||sortpoints.contains(p2)||sortpoints.contains(p3)||sortpoints.contains(p4)){
				if (sortpoints.contains(p1)){
					generatePolygon(p1);
				}
				else if (sortpoints.contains(p2)){
					generatePolygon(p2);
				}
				else if (sortpoints.contains(p3)){
					generatePolygon(p3);
				}
				else if (sortpoints.contains(p4)){
					generatePolygon(p4);
				}
			}
		}

		
	
}
