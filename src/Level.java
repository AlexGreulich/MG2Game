import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class Level {

	BufferedImage alletiles, kartenbild;
	int[][][] karte;
	
	public Level(){
		
		try{
			
			kartenbild = ImageIO.read(getClass().getResource("resources/ersterraum.gif"));
			
		}catch(IOException e){
			e.printStackTrace();
		}
		
		karteLaden();
	}
	public void  karteLaden(){
		int hoehe =kartenbild.getHeight();
		int breite = kartenbild.getWidth();
		karte = new int[breite][hoehe][4];
		
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
		
		for(int x = 0; x < breite;x++){
			for(int y = 0; y < hoehe;y++){
				Color c = new Color(kartenbild.getRGB(x, y));
				
				if(c.equals(	 schotter		)){		karte[x][y][0]=0;}
				else if(c.equals(schottergrob	)){		karte[x][y][0]=1;}
				else if(c.equals(steinplatten	)){		karte[x][y][0]=2;}
				else if(c.equals(steinplatten2	)){		karte[x][y][0]=3;}
				else if(c.equals(plattenriss	)){		karte[x][y][0]=4;}
				else if(c.equals(plattenriss2	)){		karte[x][y][0]=5;}
				
			}
		}
		//zum testen:
		karte[10][10][1]=1;
		karte[10][11][1]=1;
	}
}
