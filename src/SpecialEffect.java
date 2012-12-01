import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;


public class SpecialEffect {

	BufferedImage img;
	BufferedImage[] animImages = new BufferedImage[8];
	float animation = 0.0f;
	//GameWindow window;
	int xPos;
	int yPos;
	boolean stop = false;
	boolean isLooping = true;
	BufferedImage stoppedImg;
	
	public SpecialEffect(int nr){
		try {
			BufferedImage srcImage = ImageIO.read(getClass().getResource("resources/effects.gif"));
			stoppedImg = srcImage.getSubimage(256, nr*64, 32,64);
			for(int i = 0;i<=7;i++){
				animImages[i]= srcImage.getSubimage(i * 32, nr * 64, 32, 64);
			}
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public void setNotLooping(){
		this.isLooping = false;
	}
	
	public BufferedImage getEffectImage(){
		if((int)animation == 3){
			animation =0.0f;
			if(!isLooping){
				stop =true;
			}
		}
		if(!stop){
			img = animImages[(int)animation];
			animation += 0.33f;
			return img;
		}else{
			img = stoppedImg;
			return img;
		}
		
		
		
	}
	public void setPos(int x,int y){
		this.xPos = x;
		this.yPos =y;
	}
}
