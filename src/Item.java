import java.awt.Rectangle;
import java.awt.image.BufferedImage;


public class Item extends Entity{

	BufferedImage img;
	GameWindow window;
	GamePanel panel;
	
	int damage =0;
 	int healthPts =0;
 	int shots =0;
 	int itemType;
 	boolean isEquipped = false;
 	String name;
 	Rectangle bounds;
 	
	public Item(GameWindow w, int x, int y, int type){
		
		window = w;
		panel = w.panel;
		energy =100;
		posX =x;
		posY =y;
		bounds = new Rectangle(posX,posY,32,32);
		
		itemType = type;
		
		switch(itemType){
			case(0):			//Waffe
				damage = 1;
				name = "Waffe";
				
				break;
			case(1):			//medikit, nahrung
				healthPts = 10;
				name = "Medikit";
				
				break;
			case(2):			//ammo
				shots = 10;
				name = "Ammo";
				break;
			case(3):			//div objekt
				name = "nul";
				break;
			case(4):
				name = "nul";
				break;
			case(5):
				name = "nul";
				break;
		}
		img = window.itemHandler.picturesOfAllItems[itemType];
	}
	
	public void createActionMessage(){
		ActionMessage am = new ActionMessage(panel, "Picked up " + name);
		//return "Picked up " + name ;
		panel.actionMessages.add(am);
	}
	
	public BufferedImage getImage(){
		return img;
	}
	public void equip(){
		isEquipped = true;
		window.activeLevel.thisLevelsItems.remove(this);		//direkt in gamepanel oder so?
		createActionMessage();
		if(this.itemType == 0){
			panel.player.weapons[0] = this;
			//panel.actionMessages.add(s);
		}else if(this.itemType == 1){
			panel.player.energy += healthPts;
			//panel.actionMessages.add(s);
		}else if(this.itemType == 2){
			panel.player.ammo += shots;
			//panel.actionMessages.add(s);
		}
	}
}
