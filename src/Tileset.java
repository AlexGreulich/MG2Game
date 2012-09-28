import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class Tileset {

	BufferedImage tilesetimg; 
	ArrayList<BufferedImage> tileset;
	
	public Tileset(){
		tileset = new ArrayList<BufferedImage>();	
		
		try {
			tilesetimg = ImageIO.read(getClass().getResource("resources/tileset.gif"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(int y =0; y<tilesetimg.getHeight()/128; y++){
			for(int x =0; x<tilesetimg.getWidth()/64; x++){
				BufferedImage i = tilesetimg.getSubimage(x*64, y*128, 64, 128);
				tileset.add(i);
			}
		}
	}
}
