import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class Tileset {

	BufferedImage tilesetimgfloor, tilesetimgwalls; 
	ArrayList<BufferedImage> tilesetfloor, tilesetwalls;
	
	public Tileset(){
		tilesetfloor = new ArrayList<BufferedImage>();	
		tilesetwalls = new ArrayList<BufferedImage>();
		
		try {
			tilesetimgfloor = ImageIO.read(getClass().getResource("resources/tilesetFloor32.gif"));
			tilesetimgwalls = ImageIO.read(getClass().getResource("resources/tilesetWalls32.gif")); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(int y =0; y<tilesetimgfloor.getHeight()/64; y++){
			for(int x =0; x<tilesetimgfloor.getWidth()/32; x++){
				BufferedImage i = tilesetimgfloor.getSubimage(x*32, y*64, 32, 64);
				tilesetfloor.add(i);
			}
		}
		//int index =0;
		for(int y =0; y<tilesetimgwalls.getHeight()/64; y++){
			for(int x =0; x<tilesetimgwalls.getWidth()/32; x++){
				BufferedImage i = tilesetimgwalls.getSubimage(x*32, y*64, 32, 64);
				tilesetwalls.add(i);
//				tilesetwalls..set(index,i);
				//index++;
			}
		}
	}
}
