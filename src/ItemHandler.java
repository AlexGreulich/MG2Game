import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;


public class ItemHandler implements Runnable{
	boolean running = true;
	public int gamespeed = 16;
	GameWindow window;
	GamePanel panel;
	Player player;
	Controls controls;
	Level level;
	BufferedImage[] picturesOfAllItems, picturesInventory;
	ArrayList<Item> itemsInLevel;
	HashMap<Integer, ArrayList<Item>> totalItems;
		
	public ItemHandler(GameWindow w){
		
		window = w;
		panel = window.panel;
		player = window.player;
		controls = window.controls;
		initItemHandler();
		
		//Bilder des Itemsets in array laden:
		picturesOfAllItems = new BufferedImage[100];
		picturesInventory = new BufferedImage[100];
		try {
			BufferedImage itemset = ImageIO.read(getClass().getResource("resources/itemset.gif"));
			BufferedImage tempInvItemset = ImageIO.read(getClass().getResource("resources/inventoryitems.gif"));
			int count =0;
			for(int x =0; x < itemset.getWidth()/32; x++){
				for(int y =0; y< itemset.getHeight()/32;y++){
					BufferedImage i = itemset.getSubimage(x*32, y*32, 32, 32);
					picturesOfAllItems[count] = i;
					count++;
				}
			}
			int count2 =0;
			for(int x =0;x< tempInvItemset.getWidth()/20;x++){
				for(int y =0; y< tempInvItemset.getHeight()/20;y++){
					BufferedImage i = tempInvItemset.getSubimage(x*20,y*20,20,20);
					picturesInventory[count2] =i;
					count2++;
				}
			}
		} catch (IOException e) {e.printStackTrace();}
	}
	
	public void initItemHandler(){
		level = window.activeLevel;
	}
	
	public synchronized void run(){
		
		while(running){
			float onStart = System.currentTimeMillis();
			
			if(controls.equip){
				for(int i =0; i< window.activeLevel.thisLevelsItems.size(); i++){
					if(window.activeLevel.thisLevelsItems.get(i).bounds.intersects(player.playerBounds)){	
						if(player.equipment.size()<=10){
							window.activeLevel.thisLevelsItems.get(i).equip();
						}
						
//						if(window.activeLevel.thisLevelsItems.get(i).itemType == 0){
//							//ACHTUNG: erstmal nur schusswaffe, später unterschieden ob melee oder distance-waffe (player.weapons[0/1])
//							player.weapons[0] = window.activeLevel.thisLevelsItems.get(i);
//							window.activeLevel.thisLevelsItems.remove(i);
//							break;
//						}
//						for(int j=0;j < player.equipment.size();j++){
//							
//							if(player.equipment.get(j) == null){
//								player.equipment.get(j) = window.activeLevel.thisLevelsItems.get(i);
//								window.activeLevel.thisLevelsItems.remove(i);
//								break;
//							}
//						}
					}
				}
			}
			
			float onEnd = System.currentTimeMillis() - onStart;
			if( gamespeed> onEnd){
				try{
					if((gamespeed -(int)onEnd) > 0){
						Thread.sleep(gamespeed - (int)onEnd);
					}
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
		}
	}
}
