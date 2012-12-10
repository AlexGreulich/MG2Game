import java.awt.Rectangle;
import java.awt.image.BufferedImage;


public class Item extends Entity{

	BufferedImage img, invMiniImg;
	GameWindow window;
	GamePanel panel;
	
	int damage =0;
 	int healthPts =0;
 	int shots =0;
 	int itemType;
 	boolean isEquipped = false;
 	String name;
 	Rectangle bounds;
 	int money =0;
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
				money = 10;
				name = "nul";
				break;
			case(4):
				money = 20;
				name = "nul";
				break;
			case(5):
				money =30;
				name = "nul";
				break;
		}
		img = window.itemHandler.picturesOfAllItems[itemType];
		invMiniImg = window.itemHandler.picturesInventory[itemType];
	}
	
	public void createActionMessage(){
		ActionMessage am = new ActionMessage(panel, "Picked up " + name);
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
		}else{
			panel.player.equipment.add(this);//energy += healthPts;
		}
		
	}
	
	public void useIt(){
		
		panel.player.equipment.remove(this);
		
		switch(this.itemType){
		case(1):
			panel.player.energy += healthPts;
			break;
		case(3):
			panel.player.cash += money;
			break;
		case(4):
			panel.player.cash += money;
			break;
		case(5):
			panel.player.cash += money;
			break;
		}
	}
}
