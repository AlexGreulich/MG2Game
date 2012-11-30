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
	BufferedImage[] picturesOfAllItems;
	ArrayList<Item> itemsInLevel;
	HashMap<Integer, ArrayList<Item>> totalItems;
	/*
	 * totalitems:
	 * alle items im kompletten dungeon sind hier gespeichert
	 * muss das sein?
	 * */
	
	public ItemHandler(GameWindow w){
		
		window = w;
		panel = window.panel;
		player = window.player;
		controls = window.controls;
	//	itemsInLevel = window.activeLevel;
		initItemHandler();
		
		//Bilder des Itemsets in array laden:
		picturesOfAllItems = new BufferedImage[100];
		try {
			BufferedImage itemset = ImageIO.read(getClass().getResource("resources/itemset.gif"));
			int count =0;
			for(int x =0; x < itemset.getWidth()/32; x++){
				for(int y =0; y< itemset.getHeight()/32;y++){
					BufferedImage i = itemset.getSubimage(x*32, y*32, 32, 32);
					picturesOfAllItems[count] = i;
					count++;
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
				for(int i =0; i< window.activeLevel.thisLevelsItems.size(); i++){//Item i: itemsInLevel){
					if(window.activeLevel.thisLevelsItems.get(i).bounds.intersects(player.playerBounds)){						//contains(player.playermiddle)){
						window.activeLevel.thisLevelsItems.get(i).equip();
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
