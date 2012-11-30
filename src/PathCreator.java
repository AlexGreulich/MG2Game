import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.*;
import java.io.*;
import java.util.*;

public class PathCreator{
	
	File file = new File("raum.txt");
	int maxRooms=20;
	int roomCounter;
	static HashMap<Integer,int[]> map = new HashMap<Integer,int[]>(36);
	ArrayList<Integer> list = new ArrayList<Integer>();//Hilfsliste fuer roomChooser()
	ArrayList<Integer> list2 = new ArrayList<Integer>();//Hilfsliste fuer createRoom()
	
	//Variablen fuer array auslesen/testen
	int elementcounter=0;
	
	public void printStuff(){
		try (PrintWriter writer1 = new PrintWriter(new FileWriter(file));){
		for(int i=1; i<=map.size(); i++){
			int[] tmp = new int[6];
			tmp = getValue(i-1);
			
			if(tmp[0]==1){
				writer1.print(tmp[0]+" ");
			}
			else if(tmp[0]==-1){
				writer1.print("x ");
			}
			elementcounter++;
			if(elementcounter==6){
				writer1.println();
				elementcounter=0;
			}
		}
		writer1.close();
		} catch (IOException f){
			//
		}
	}
	
	//dient zum gucken welche gueltigen Nachbarn man haben koennte/oben links in der ecke sollte man nur 2 nachbarn errechnen
	public int[] roomNeighbour(int roomNumber){
		int room1,room2,room3,room4;
		//room1=oben, room2=rechts, room3=unten, room4=links
		if((roomNumber-6)>=0){
			room1 = roomNumber-6;
		}
		else{
			room1 = -1;//-1steht fuer eine ungueltige raumnummer
		}
		if((roomNumber+1)%6!=0){
			room2 = roomNumber+1;
		}
		else{
			room2 = -1;
		}
		if((roomNumber+6)<36){
			room3 = roomNumber+6;
		}
		else{
			room3 = -1;
		}
		if(roomNumber%6!=0){
			room4 = roomNumber-1;
		}
		else{
			room4 = -1;
		}
		int[] rooms = {room1,room2,room3,room4};
		
		
		return rooms;
	}
	public void createRoom(int roomNumber){
		list2.clear();
		int[] neighbours = new int[4];
		neighbours = roomNeighbour(roomNumber); //neighbours[0] ist oben, neighbours[1] ist rechts, neighbours[2] ist unten,neighbours[3] ist links
		for(int i=0; i<4; i++){
			if(neighbours[i]!=-1){
				int[] tmp = new int[6];
				tmp = getValue(neighbours[i]);
				if((roomCounter<maxRooms)&&(tmp[0]==-1)){
					list2.add(neighbours[i]);//alle gueltigen Nachbaroptionen werden gespeichert
				}
			}
		}
		if(list2.size()>0){//zufallselement aus der liste der gueltigen Nachbarn auswaehlen
			int a = (int)(Math.random()*(list2.size()));
			int[] tmp2 = new int[6];
			tmp2=getValue(list2.get(a));
			tmp2[0]=1;
			tmp2[1]=1;
			map.put(list2.get(a),tmp2);
			if(list2.size()==1){
				int[] tmp3 = new int[6];
				tmp3 = getValue(roomNumber);
				tmp3[1]=-1;//letzer freie nachbar wird belegt
				map.put(roomNumber, tmp3);
			}
			map.put(list2.get(a),tmp2);
			roomCounter++;
		}
	}
	
	//Zufallsraum im Raumarray suchen an dem spaeter Raeume angehaengt werden
	public int roomChooser(){
		list.clear();
		for(int i=0; i<map.size(); i++){
			int[] tmp = new int[6];
			tmp = getValue(i);
			if ((tmp[0]==1)&&(tmp[1]==1)){
				list.add(i);
			}
		}
		int l = list.size();
		int m = (int)(Math.random()*l);
		return list.get(m);
	}
	
	//startpunkt fuer createRoom()
	public void initializeArray(){

		for(int ii=0;ii<36;ii++){
			int[] array = new int[6];
			array[0]=-1;//raum vorhanden 1=ja /-1=nein
			array[1]=-1;//freie nachbarn 1=ja /-1=nein
			array[2]=-1;//obere tuer
			array[3]=-1;//rechte tuer
			array[4]=-1;//untere tuer
			array[5]=-1;//linke tuer
			map.put(ii, array);
		}
		int a = (int)(Math.random()*36);
		int[] tmp = new int[6];
		tmp = getValue(a);
		
		tmp[0]=1;
		tmp[1]=1;
		map.put(a,tmp);
		roomCounter++;

	}
	
	public static int[] getValue(Integer key) {

		for (Integer i : map.keySet()) {
			if (i==key) {
				return map.get(i);
			}
		}

		return null;
	}
	
	public void createDoors(){
		for(int i=0; i<map.size(); i++){
			int[] tmp = new int[6];
			tmp = getValue(i);
			int[] neighbours = new int[4];
			neighbours = roomNeighbour(i);
			int orientation = 2;//startindex in map fuer tuer oben
			for(int x=0; x<4; x++){
				if(neighbours[x]!=-1){
					int[] tmp2 = new int[6];
					tmp2 = getValue(neighbours[x]);
					if(tmp2[0]==1){//nachbarraum vorhanden
						tmp[orientation]=neighbours[x];//tuer wird gesetzt in tmp falls nachbar vorhanden
					}
				}
				orientation++;//naechste tuerstelle abfragen/eintragen
			}//tueren in tmp alle gesetzt
			int rand = (int)(Math.random()*4);
			tmp[1]=rand;
			map.put(i,tmp);
			System.out.println(i+"raumgesetzt: "+tmp[0]+" raumtyp: "+tmp[1]+"  oben: "+tmp[2]+" rechts: "+tmp[3]+" unten: "+tmp[4]+" links: "+tmp[5]);
		}
	}
	
	public PathCreator(){
		roomCounter=0;
		initializeArray();
		while(roomCounter<maxRooms){
			int x = roomChooser();
			createRoom(x);
		}
		createDoors();
		printStuff();
		
	}
	
	
}