import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Player extends Objekt{

	GameWindow window;
	Controls controls;
	BufferedImage img,image;
	float animation = 0.0f;
	BufferedImage[] hoch_cycle,runter_cycle, links_cycle,rechts_cycle,hochlinks_cycle,hochrechts_cycle,runterlinks_cycle,runterrechts_cycle;
	
	public Player(GameWindow w){//, int x, int y
		
		window = w;
		controls = w.controls;
		
		posX =100;
		posY = 100;
		energy = 100;
		image = null;
		hoch_cycle = new BufferedImage[8];
		runter_cycle  = new BufferedImage[8];
		links_cycle = new BufferedImage[8];
		rechts_cycle = new BufferedImage[8];
		hochlinks_cycle = new BufferedImage[8];
		hochrechts_cycle = new BufferedImage[8];
		runterlinks_cycle = new BufferedImage[8];
		runterrechts_cycle = new BufferedImage[8];
		
		try{
			img = ImageIO.read(getClass().getResource("resources/charset.gif"));
			int count =0;
			for(int a = 0; a< img.getHeight()/96; a++){
				for(int b =0; b<img.getWidth()/64;b++){
					//
					switch(a){
						case(0):
							runter_cycle[b] = img.getSubimage(b*64, a*96, 64, 96);
							break;
						case(1):	
							links_cycle[b] = img.getSubimage(b*64, a*96, 64, 96);
							break;
						case(2):
							rechts_cycle[b] = img.getSubimage(b*64, a*96, 64, 96);
							break;
						case(3):
							hoch_cycle[b] = img.getSubimage(b*64, a*96, 64, 96);
							break;
						case(4):
							runterlinks_cycle[b] = img.getSubimage(b*64, a*96, 64, 96);
							break;
						case(5):
							runterrechts_cycle[b] = img.getSubimage(b*64, a*96, 64, 96);
							break;
						case(6):
							hochrechts_cycle[b] = img.getSubimage(b*64, a*96, 64, 96);
							break;
						case(7):
							hochlinks_cycle[b] = img.getSubimage(b*64, a*96, 64, 96);
							break;
					}
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		
	}
	
	public int getX(){
		return posX;
	}
	public int getY(){
		return posY;
	}
	public void setX(int x){
		posX += x;
	}
	public void setY(int y){
		posY += y;
	}
	public BufferedImage getImage(){
		if(controls.isMoving){
			animation += 0.33;
		}else{
			animation = 0.0f;
		}
		if((int)animation == 8){
			animation =0.0f;
		}
		
		
		switch(controls.direction){
			case(0):
				image = runter_cycle[(int)animation];
				break;
			case(1):
				image = links_cycle[(int)animation];
				break;
			case(2):
				image = rechts_cycle[(int)animation];
				break;
			case(3):
				image = hoch_cycle[(int)animation];
				break;
			case(4):
				image = runterlinks_cycle[(int)animation];
				break;
			case(5):
				image = runterrechts_cycle[(int)animation];
				break;
			case(6):
				image = hochrechts_cycle[(int)animation];
				break;
			case(7):
				image = hochlinks_cycle[(int)animation];
				break;
		}
		return image;
		
	}
}
