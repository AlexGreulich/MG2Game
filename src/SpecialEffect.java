import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;


public class SpecialEffect {

	BufferedImage img;
	BufferedImage[] animImages = new BufferedImage[3];
	float animation = 0.0f;
	//GameWindow window;
	int xPos;
	int yPos;
	
	public SpecialEffect(int nr){
		try {
			BufferedImage srcImage = ImageIO.read(getClass().getResource("resources/effects.gif"));
			for(int i = 0;i<3;i++){
				animImages[i]= srcImage.getSubimage(i * 32, nr * 64, 32, 64);
			}
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public BufferedImage getEffectImage(){
		if((int)animation == 3){
			animation =0.0f;
		}
		img = animImages[(int)animation];
		animation += 0.33f;
		return img;
	}
	public void setPos(int x,int y){
		this.xPos = x;
		this.yPos =y;
	}
}
