import java.awt.Dimension;
import java.awt.image.BufferedImage;


public class Item extends Entity{

	BufferedImage img;
	GamePanel panel;
	
	int damage =0;
 	int healthPts =0;
 	int shots =0;
 	int itemType;
 	boolean isEquipped = false;
 	
 	Dimension bounds;
 	
	public Item(GamePanel p, int x, int y, int type){
		
		panel = p;
		energy =100;
		posX =x;
		posY =y;
		bounds = new Dimension(32,32);
		
		itemType = type;
		
		switch(itemType){
		case(0):			//Waffe
			damage = 1;
		break;
		case(1):			//medikit, nahrung
			healthPts = 10;
		break;
		case(2):			//ammo
			shots = 5;
		break;
		case(3):			//div objekt
			break;
		}
	}
	
	public BufferedImage getImage(){
		return null;
	}
	public void equip(){
		isEquipped = true;
		panel.level.itemsInLevel.remove(this);		//direkt in gamepanel oder so?
		if(this.itemType == 0){
			panel.player.equipment[0] = this;
		}else if(this.itemType == 1){
			panel.player.energy += healthPts;
		}else if(this.itemType == 2){
			panel.player.ammo += shots;
		}
		
	}
}
