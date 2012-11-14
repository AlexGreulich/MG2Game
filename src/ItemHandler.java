import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class ItemHandler implements Runnable{
	boolean running = true;
	public int gamespeed = 5;
	GameWindow window;
	GamePanel panel;
	Player player;
	Controls controls;
	Level level;
	BufferedImage[] allItems;
	ArrayList<Item> itemsInLevel;
	
	public ItemHandler(GameWindow w){
		
		window = w;
		panel = window.panel;
		player = window.player;
		controls = window.controls;
		initItemHandler();
		
		//Bilder des Itemsets in array laden:
		allItems = new BufferedImage[100];
		try {
			BufferedImage itemset = ImageIO.read(getClass().getResource("resources/itemset.gif"));
			int count =0;
			for(int x =0; x < itemset.getWidth()/32; x++){
				for(int y =0; y< itemset.getHeight()/32;y++){
					BufferedImage i = itemset.getSubimage(x*32, y*32, 32, 32);
					allItems[count] = i;
					count++;
				}
			}
		} catch (IOException e) {e.printStackTrace();}
		itemsInLevel = new ArrayList<Item>();
	}
	
	public void initItemHandler(){
		level = window.level;
	}
	
	public synchronized void run(){
		
		while(running){
			float onStart = System.currentTimeMillis();
//			ArrayList<Item> l = panel.itemsInLevel;
			//Items aufheben
			if(controls.equip){
				
				for(int i =0; i< itemsInLevel.size(); i++){//Item i: itemsInLevel){
					if(itemsInLevel.get(i).bounds.contains(player.playermiddle)){
						itemsInLevel.get(i).equip();
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
