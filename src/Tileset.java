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
		for(int x =0; x<tilesetimg.getWidth()/64; x++){
			for(int y =0; y<tilesetimg.getHeight()/64; y++){
				BufferedImage i = tilesetimg.getSubimage(x*64, y*64, 64, 64);
				tileset.add(i);
			}
		}
	}
}
