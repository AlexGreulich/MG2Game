import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


public class Level {
	GameWindow window;
	BufferedImage allTiles, mapPic,mapPicwalls,mapPicItems;
	int[][][] map;
	int mapwidth, mapheight;
	Polygon collisionshape; 
	ArrayList<Point> collisionpoints, doorPoints, sortpoints;
	ArrayList<Item> thisLevelsItems;
	ArrayList<Enemy> thisLevelsEnemies;
	ArrayList<Enemy> corpsesInThisLevel;
	ArrayList<SpecialEffect> specialEffects;
	Point p1,p2,p3,p4,p5,p6,p7,p8;
	Polygon[] doorShapes;
	
	Level links, rechts,oben,unten;
	int nr;
	int[] neighbors = new int[4];
	int roomtype;
	HashMap<Integer,Point[]> doorsAssignment; 
	Random random = new Random();
	
	public Level(GameWindow w,BufferedImage mapimgfloor, BufferedImage mapimgwalls, BufferedImage mapimgItems,int type, int roomNumber, int[] nextrooms){
		if(roomNumber > (-1)){
			window =w;
			mapPic = mapimgfloor;
			mapwidth = mapPic.getWidth();
			mapheight = mapPic.getHeight();
			
			mapPicwalls = mapimgwalls;
			mapPicItems = mapimgItems;
			nr = roomNumber;
			roomtype = type;
		
			neighbors[0] = nextrooms[2];//oben
			neighbors[1] = nextrooms[3];//rechts
			neighbors[2] = nextrooms[4];//unten
			neighbors[3] = nextrooms[5];//links
			assignDoors(roomtype);
			thisLevelsEnemies = new ArrayList<Enemy>();
			thisLevelsItems = new ArrayList<Item>();
			corpsesInThisLevel = new ArrayList<Enemy>();
			specialEffects = new ArrayList<SpecialEffect>();
			
			loadMap();
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
		int zombieCount =2;
		
		for(int x = 0; x < this.mapPic.getWidth(); x++){
			for(int y = 0; y< this.mapPic.getHeight(); y++){
			
				int itemtypee = this.map[x][y][2];
				if(itemtypee != 0){
					Item it = new Item(window, x*32,y*8,itemtypee-1);
					thisLevelsItems.add(it);
				}
				
				if(this.map[x][y][5] < 666){
					SpecialEffect sE = new SpecialEffect(this.map[x][y][5]);
					sE.setPos(x,y);
					specialEffects.add(sE);
				}
				
				if(map[x][y][0] < 666){
					if(zombieCount >=0){
						if(random.nextInt(420) == 1){
							//Enemy enemy1 = new Enemy(window,x*16,y*8,1);
							thisLevelsEnemies.add(new Enemy(window,x,y,1));
							zombieCount--;
						}
					}
				}
			}
		}
	}
	public void assignDoors(int t){
		doorPoints = new ArrayList<Point>();
		switch(t){
				
		case 0:
			if(neighbors[0] != (-1)){
				doorPoints.add(new Point(7,22));
			}else{
				doorPoints.add(null);
			}
			if(neighbors[1] != (-1)){
				doorPoints.add(new Point(22,22));
			}else{
				doorPoints.add(null);
			}	
			if(neighbors[2] != (-1)){
				doorPoints.add(new Point(23,44));
			}else{
				doorPoints.add(null);
			}
			if(neighbors[3] != (-1)){
				doorPoints.add(new Point(6,46));
			}else{
				doorPoints.add(null);
			}	
			break;
		case 1:
			if(neighbors[0] != (-1)){
				doorPoints.add(new Point(4,27));
			}else{
				doorPoints.add(null);
			}
			if(neighbors[1] != (-1)){
				doorPoints.add(new Point(21,20));
			}else{
				doorPoints.add(null);
			}	
			if(neighbors[2] != (-1)){
				doorPoints.add(new Point(26,39));
			}else{
				doorPoints.add(null);
			}
			if(neighbors[3] != (-1)){
				doorPoints.add(new Point(3,41));
			}else{
				doorPoints.add(null);
			}	
			break;
		case 2:
			if(neighbors[0] != (-1)){
				doorPoints.add(new Point(6,24));
			}else{
				doorPoints.add(null);
			}
			if(neighbors[1] != (-1)){
				doorPoints.add(new Point(24,27));
			}else{
				doorPoints.add(null);
			}	
			if(neighbors[2] != (-1)){
				doorPoints.add(new Point(23,44));
			}else{
				doorPoints.add(null);
			}
			if(neighbors[3] != (-1)){
				doorPoints.add(new Point(5,44));
			}else{
				doorPoints.add(null);
			}	
			break;
		case 3:
			if(neighbors[0] != (-1)){
				doorPoints.add(new Point(8,31));
			}else{
				doorPoints.add(null);
			}
			if(neighbors[1] != (-1)){
				doorPoints.add(new Point(21,21));
			}else{
				doorPoints.add(null);
			}	
			if(neighbors[2] != (-1)){
				doorPoints.add(new Point(22,34));
			}else{
				doorPoints.add(null);
			}
			if(neighbors[3] != (-1)){
				doorPoints.add(new Point(7,48));
			}else{
				doorPoints.add(null);
			}	
			break;
		}
	}
	
	public void  loadMap(){
		int mapHeight =mapPic.getHeight();
		int mapWidth = mapPic.getWidth();
		map = new int[mapWidth][mapHeight][6];
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
		 * 4	-> türen
		 * 5	-> special effect -> 1 -> effect 1 usw 
 		 * */
		
		
	//	BODEN	
		Color schotter = Color.BLACK;
		Color schottergrob = 		new Color(0,	0,		50);
		Color steinplatten = 		new Color(0,	0,		100);
		Color steinplatten2 = 		new Color(0,	0,		150);
		Color plattenriss = 		new Color(0,	0,		200);
		Color plattenriss2 = 		new Color(0,	0,		250);
		Color stroh1 = 				new Color(50,	0,		10);
		Color stroh2 = 				new Color(50,	0,		20);
		Color schlacke1 = 			new Color(50,	0,		30);
		Color schlacke2 = 			new Color(50,	0,		40);
		Color bodenplatte = 		new Color(50,	0,		50);
		Color schlackeuebergang1 = 	new Color(50,	0,		60);
		Color schlackeuebergang2 = 	new Color(50,	0,		70);
		Color schlackeende1 = 		new Color(50,	0,		80);
		Color schlackeende2 = 		new Color(50,	0,		90);
		
		Color tuer01 = 				new Color(250,	50,		50);
		
		
	//	WAENDE
		Color wandrechts = 			new Color(0,	150,	0);
		Color wandlinks = 			new Color(0,	150,	100);
		Color wandrohrrechts = 		new Color(0,	150,	110);
		Color wandrohrlinks = 		new Color(0,	150,	120);
		Color wandlichtrechts = 	new Color(0,	150,	130);
		Color wandlichtlinks = 		new Color(0,	150,	140);
		Color wandabwasserlinks = 	new Color(0,	150,	142);
		Color wandabwasserrechts = 	new Color(0,	150,	144);
		Color wandkabel1rechts = 	new Color(0,	150,	150);
		Color wandkabel1links = 	new Color(0,	150,	160);
		Color wandposterrechts = 	new Color(0,	150,	162);
		Color wandposterlinks = 	new Color(0,	150,	164);
		Color eckeoben = 			new Color(0,	150,	170);
		Color wandmonitorre = 		new Color(0,	150,	172);
		Color wandmonitorli = 		new Color(0,	150,	174);
		Color wandbeton1re = 		new Color(0,	150,	176);
		Color wandbeton1li = 		new Color(0,	150,	178);
		Color wandbeton2re = 		new Color(0,	150,	180);
		Color wandbeton2li = 		new Color(0,	150,	182);
		Color wandbetonrohrre = 	new Color(0,	150,	184);
		Color wandbetonrohrli = 	new Color(0,	150,	186);
		Color wandbeton3re = 		new Color(0,	150,	188);
		Color wandbeton3li = 		new Color(0,	150,	190);
		
		Color doorToRight = 		new Color(0,	250,	150);
		Color doorToLeft = 			new Color(0,	250,	160);
		Color doorToTop = 			new Color(0,	250,	170);
		Color doorToBottom = 		new Color(0,	250,	180);
		
		Color invisibleCollisionWall=new Color(0,	150,	250);
	
	//	ITEMS
		Color itemColour1 = 		new Color(255,	0,		0);
		Color itemColour2 = 		new Color(255,	0,		10);
		Color itemColour3 = 		new Color(255,	0,		20);
		Color itemColour4 = 		new Color(255,	0,		30);
		Color itemColour5 = 		new Color(255,	0,		40);
		Color itemColour6 = 		new Color(255,	0,		50);
		
		
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
				else if(c.equals(tuer01	)){				map[x][y][0]=30;}
				else if(c.equals(stroh1	)){				map[x][y][0]=6;}
				else if(c.equals(stroh2	)){				map[x][y][0]=7;}
				else if(c.equals(schlacke1)){			map[x][y][0]=5;}
				else if(c.equals(schlacke2)){			map[x][y][0]=11;}
				else if(c.equals(bodenplatte)){			map[x][y][0]=9;}
				else if(c.equals(schlackeuebergang1	)){	map[x][y][0]=10;}
				else if(c.equals(schlackeuebergang2	)){	map[x][y][0]=16;}
				else if(c.equals(schlackeende1	)){		map[x][y][0]=17;}
				else if(c.equals(schlackeende2	)){		map[x][y][0]=23;}
				
				//else if(c.equals()){				map[x][y][0]=5;}
				
				else if(c.equals(Color.WHITE)){				map[x][y][3]=666;
															map[x][y][0]=666;
															map[x][y][1]=1;}
				else{
						map[x][y][1]=1;
				}
			
			//wand-tiles
				
				if(d.equals(wandrechts)){					map[x][y][3]=1;
															map[x][y][1]=1;
															map[x][y][5]=666;
															collisionpoints.add(new Point(x,y));
															//collisionshape.addPoint(x*32, y*32);
				}else if(d.equals(wandlinks)){				map[x][y][3]=0;
															map[x][y][1]=1;
															map[x][y][5]=666;
															collisionpoints.add(new Point(x,y));
				}
				else if(d.equals(wandrohrrechts)){			map[x][y][3]=2;
															map[x][y][1]=1;
															map[x][y][5]=666;
															collisionpoints.add(new Point(x,y));
					
				}
				else if(d.equals(wandrohrlinks)){			map[x][y][3]=3;
															map[x][y][1]=1;
															map[x][y][5]=666;
															collisionpoints.add(new Point(x,y));
					
				}
				else if(d.equals(wandlichtrechts)){			map[x][y][3]=4;
															map[x][y][1]=1;
															map[x][y][5]=666;
															collisionpoints.add(new Point(x,y));
					
				}
				else if(d.equals(wandlichtlinks)){			map[x][y][3]=5;
															map[x][y][1]=1;
															map[x][y][5]=666;
															collisionpoints.add(new Point(x,y));
	
				}
				else if(d.equals(wandkabel1rechts)){		map[x][y][3]=8;
															map[x][y][1]=1;
															map[x][y][5]=0;
															collisionpoints.add(new Point(x,y));
															
				}
				else if(d.equals(wandkabel1links)){			map[x][y][3]=9;
															map[x][y][1]=1;
															map[x][y][5]=1;
															collisionpoints.add(new Point(x,y));
															
				}
				else if(d.equals(wandabwasserrechts)){		map[x][y][3]=6;
															map[x][y][1]=1;
															map[x][y][5]=666;
															collisionpoints.add(new Point(x,y));
				
}
				else if(d.equals(wandabwasserlinks)){		map[x][y][3]=7;
															map[x][y][1]=1;
															map[x][y][5]=666;
															collisionpoints.add(new Point(x,y));
				
}
				else if(d.equals(wandposterrechts)){		map[x][y][3]=10;
															map[x][y][1]=1;
															map[x][y][5]=666;
															collisionpoints.add(new Point(x,y));
				
}
				else if(d.equals(wandposterlinks)){			map[x][y][3]=11;
															map[x][y][1]=1;
															map[x][y][5]=666;
															collisionpoints.add(new Point(x,y));
				
}
				else if(d.equals(wandmonitorre)){			map[x][y][3]=14;
															map[x][y][1]=1;
															map[x][y][5]=666;
															collisionpoints.add(new Point(x,y));
				
}
				else if(d.equals(wandmonitorli)){			map[x][y][3]=15;
															map[x][y][1]=1;
															map[x][y][5]=666;
															collisionpoints.add(new Point(x,y));
				
}
				else if(d.equals(wandbeton1re)){			map[x][y][3]=16;
															map[x][y][1]=1;
															map[x][y][5]=666;
															collisionpoints.add(new Point(x,y));
				
}
				else if(d.equals(wandbeton1li)){			map[x][y][3]=17;
															map[x][y][1]=1;
															map[x][y][5]=666;
															collisionpoints.add(new Point(x,y));
				
}
				else if(d.equals(wandbeton2re)){			map[x][y][3]=18;
															map[x][y][1]=1;
															map[x][y][5]=666;
															collisionpoints.add(new Point(x,y));
				
}
				else if(d.equals(wandbeton2li)){			map[x][y][3]=19;
															map[x][y][1]=1;
															map[x][y][5]=666;
															collisionpoints.add(new Point(x,y));
				
}
				else if(d.equals(wandbetonrohrre)){			map[x][y][3]=20;
															map[x][y][1]=1;
															map[x][y][5]=666;
															collisionpoints.add(new Point(x,y));
				
}
				else if(d.equals(wandbetonrohrli)){			map[x][y][3]=21;
															map[x][y][1]=1;
															map[x][y][5]=666;
															collisionpoints.add(new Point(x,y));
				
}
				else if(d.equals(wandbeton3re)){			map[x][y][3]=22;
															map[x][y][1]=1;
															map[x][y][5]=666;
															collisionpoints.add(new Point(x,y));
				
}
				else if(d.equals(wandbeton3li)){			map[x][y][3]=23;
															map[x][y][1]=1;
															map[x][y][5]=666;
															collisionpoints.add(new Point(x,y));
				
}
				else if(d.equals(eckeoben)){
															map[x][y][3] = 10;
															map[x][y][1] = 1;
															collisionpoints.add(new Point(x,y));	
															map[x][y][5]=666;
				}
				else if (d.equals(invisibleCollisionWall)){
															collisionpoints.add(new Point(x,y));
															map[x][y][3]=666;
															map[x][y][5]=666;
				}
				else if(d.equals(doorToRight)){
															map[x][y][3]=666;
															collisionpoints.add(new Point(x,y));
															//doorPoints.add(new Point(x,y));
															map[x][y][4]=1;
															map[x][y][5]=666;
				}else if(d.equals(doorToLeft)){
															map[x][y][3]=666;
															collisionpoints.add(new Point(x,y));
															//doorPoints.add(new Point(x,y));
															map[x][y][4]=1;
															map[x][y][5]=666;
				}else if(d.equals(doorToTop)){
															map[x][y][3]=666;
															collisionpoints.add(new Point(x,y));
															//doorPoints.add(new Point(x,y));
															map[x][y][4]=1;
															map[x][y][5]=666;
				}else if(d.equals(doorToBottom)){
															map[x][y][3]=666;
															collisionpoints.add(new Point(x,y));
															//doorPoints.add(new Point(x,y));
															map[x][y][4]=1;
															map[x][y][5]=666;
				}else{
					map[x][y][3]=666;
					map[x][y][5]=666;
				}
			//items auslesen
				
				if(e.equals(itemColour1)){
					map[x][y][2] = 1;
				}else if(e.equals(itemColour2)){
					map[x][y][2] = 2;
				}else if(e.equals(itemColour3)){
					map[x][y][2] = 3;
				}else if(e.equals(itemColour4)){
					map[x][y][2] = 4;
				}else if(e.equals(itemColour5)){
					map[x][y][2] = 5;
				}else if(e.equals(itemColour6)){
					map[x][y][2] = 6;
				}
			}
		}
		for(Point p: collisionpoints){
			
			if(p.y%2 ==0){
				p.x = p.x*32 +16 ;
				p.y = p.y *8 +32;
			}else if(p.y%2 ==1){
				p.x = p.x*32 +32;
				p.y = p.y*8 +32 ;
			}
					sortpoints.add(p);
		}
		generatePolygon(sortpoints.get(0));//Testweise <--- waende sollten dann aber auch durchgaengig sein und nicht irgendwo aufhoeren
		createDoors();
		for(int x =0; x<mapWidth;x++){
			for(int y =0;y< mapHeight;y++){
				
			}
		}
		for(int i =0;i < doorPoints.size();i++){
			if(doorPoints.get(i) != null){
				map[doorPoints.get(i).x][doorPoints.get(i).y][3] = 666;
			}
		}
	}
	
	public void createDoors(){
	
		doorShapes = new Polygon[doorPoints.size()];
		for(int i =0; i < doorPoints.size();i++){
			if(doorPoints.get(i) != null){
				Polygon p = new Polygon();
				//y%2 implementieren für korrekte position im türrahmen
				//if()
				p.addPoint(doorPoints.get(i).x * 32+16, doorPoints.get(i).y * 8);
				p.addPoint(doorPoints.get(i).x * 32+32, doorPoints.get(i).y * 8+8);
				p.addPoint(doorPoints.get(i).x * 32+32, doorPoints.get(i).y * 8+56);
				p.addPoint(doorPoints.get(i).x * 32+16, doorPoints.get(i).y * 8+48);
				doorShapes[i] = p;
			}
		}
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
				}else if (sortpoints.contains(p6)){
					generatePolygon(p6);
				}else if (sortpoints.contains(p7)){
					generatePolygon(p7);
				}else if (sortpoints.contains(p8)){
					generatePolygon(p8);
				}
			}
			else if (sortpoints.contains(p1)||sortpoints.contains(p2)||sortpoints.contains(p3)||sortpoints.contains(p4)){
				if (sortpoints.contains(p1)){
					generatePolygon(p1);
				}else if (sortpoints.contains(p2)){
					generatePolygon(p2);
				}else if (sortpoints.contains(p3)){
					generatePolygon(p3);
				}else if (sortpoints.contains(p4)){
					generatePolygon(p4);
				}
			}
		}
}
