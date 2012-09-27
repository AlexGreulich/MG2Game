import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class Level {

	//ArrayList<BufferedImage> tileset;
	BufferedImage alletiles, kartenbild;
	int[][][] karte;
	
	public Level(){
		
		try{
			//alletiles = ImageIO.read(arg0);
			//BufferedImage erstestile = ImageIO.read(getClass().getResource("ersterisotileentwurf.gif"));
			//BufferedImage zweitestile = ImageIO.read(getClass().getResource("zweiterisotileentwurf.gif"));
			kartenbild = ImageIO.read(getClass().getResource("resources/ersterraum.gif"));
			//tileset = new ArrayList<BufferedImage>();
			//tileset.add(erstestile);
			//tileset.add(zweitestile);
		}catch(IOException e){
			e.printStackTrace();
		}
		
		karteLaden();
	}
	public void  karteLaden(){
		int hoehe =kartenbild.getHeight();
		int breite = kartenbild.getWidth();
		karte = new int[breite][hoehe][4];
		for(int x = 0; x < breite;x++){
			for(int y = 0; y < hoehe;y++){
				Color c = new Color(kartenbild.getRGB(x, y));
				if(c.equals(Color.BLACK)){
					karte[x][y][0]=0;
				}
				else if(c.equals(Color.RED)){
					karte[x][y][0]=1;
				}
			}
		}
	}
}
