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
				name = "magnum";
				break;
			case(1):			//medikit, nahrung
				healthPts = 10;
				name = "medikit";
				break;
			case(2):			//ammo
				shots = 10;
				name = "ammo";
				break;
			case(3):			//div objekt
				money = 10;
				name = "pills";
				break;
			case(4):
				money = 20;
				name = "money";
				break;
			case(5):
				money =30;
				name = "money";
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
		window.activeLevel.thisLevelsItems.remove(this);		
		createActionMessage();
		if(this.itemType == 0){
			panel.player.weapons[0] = this;
		}else{
			panel.player.equipment.add(this);
		}
	}
	
	public void useIt(){
		
		panel.player.equipment.remove(this);
		
		switch(this.itemType){
		case(1):
			if(panel.player.energy <100){
				int diff = (int) (100 - panel.player.energy);
				if(diff > healthPts){
					panel.player.energy += healthPts;
					panel.actionMessages.add(new ActionMessage(panel,"Got " +healthPts+" HP"));
				}else{
					panel.player.energy += diff;
					panel.actionMessages.add(new ActionMessage(panel,"Got " +diff+" HP"));
				}
			}
			
			break;
		case(3):
			panel.player.cash += money;
			panel.actionMessages.add(new ActionMessage(panel,"Got " +money+"$"));
			break;
		case(4):
			panel.player.cash += money;
			panel.actionMessages.add(new ActionMessage(panel,"Got " +money+"$"));
			break;
		case(5):
			panel.player.cash += money;
			panel.actionMessages.add(new ActionMessage(panel,"Got " +money+"$"));
			break;
		}
	}
}
