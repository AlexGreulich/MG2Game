import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Enemy extends Entity{

	int playerSafeSpace = 70;
	int enemyType;
	int targetX;
	int targetY;
	int speed;
	int enemyDirection;
	GameWindow window;
	Player player;
	BufferedImage img,image;
	float animation = 0.0f;
	BufferedImage[] hoch_cycle,runter_cycle, links_cycle,rechts_cycle,hochlinks_cycle,hochrechts_cycle,runterlinks_cycle,runterrechts_cycle;
	Rectangle enemyBounds;
	int countToNextAttack =0;	//wenn schaden ausgeteilt, warte etwas bis zum nächsten schaden
	boolean canAttack = true;
	
	public Enemy(GameWindow w, int x, int y, int type){//
		window = w;
		player = window.player;
		enemyType=type;

		speed=1;
		posX =300;
		posY = 300;
		energy = 100f;
		image = null;

		enemyBounds = new Rectangle(this.posX+4, this.posY+4, 24, 40);

		hoch_cycle = new BufferedImage[8];
		runter_cycle = new BufferedImage[8];
		links_cycle = new BufferedImage[8];
		rechts_cycle = new BufferedImage[8];
		hochlinks_cycle = new BufferedImage[8];
		hochrechts_cycle = new BufferedImage[8];
		runterlinks_cycle = new BufferedImage[8];
		runterrechts_cycle = new BufferedImage[8];

		try{
			img = ImageIO.read(getClass().getResource("resources/charset.gif"));
			int count =0;
			for(int a = 0; a< img.getHeight()/48; a++){
				for(int b =0; b<img.getWidth()/32;b++){
		//
					switch(a){
					case(0):
						runter_cycle[b] = img.getSubimage(b*32, a*48, 32, 48);
					break;
					case(1):	
						links_cycle[b] = img.getSubimage(b*32, a*48, 32, 48);
					break;
					case(2):
						rechts_cycle[b] = img.getSubimage(b*32, a*48, 32, 48);
					break;
					case(3):
						hoch_cycle[b] = img.getSubimage(b*32, a*48, 32, 48);
					break;
					case(4):
						runterlinks_cycle[b] = img.getSubimage(b*32, a*48, 32, 48);
					break;
					case(5):
						runterrechts_cycle[b] = img.getSubimage(b*32, a*48, 32, 48);
					break;
					case(6):
						hochrechts_cycle[b] = img.getSubimage(b*32, a*48, 32, 48);
					break;
					case(7):
						hochlinks_cycle[b] = img.getSubimage(b*32, a*48, 32, 48);
					break;
					}
				}
			}
		}catch(IOException e){e.printStackTrace();}
	
	}
	
	public int getX(){
		return posX;
	}
	public int getY(){
		return posY;
	}
	public void setX(int x){
		posX = x;
	}
	public void setY(int y){
		posY = y;
	}
	public int getXDistancePlayer(){
		//int x = player.getX() - posX;
		int x = targetX - posX;
		return x;
	}
	public int getYDistancePlayer(){
		//int y = player.getY() - posY;
		int y = targetY - posY;
		return y;
	}
	
	public void updateBounds(){
		enemyBounds.setBounds(this.posX+4, this.posY+4, 24, 40);
	}
	
	public void targetDistance(){
		if (enemyType == 1){
			targetX=player.getX();
			targetY=player.getY();
		}
		else if (enemyType ==2){
			int aX = player.getX();
			int aY = player.getY()-playerSafeSpace;
			int bX = player.getX()+playerSafeSpace;
			int bY = player.getY()-playerSafeSpace;
			int cX = player.getX()+playerSafeSpace;
			int cY = player.getY();
			int dX = player.getX()+playerSafeSpace;
			int dY = player.getY()+playerSafeSpace;
			int eX = player.getX();
			int eY = player.getY()+playerSafeSpace;
			int fX = player.getX()-playerSafeSpace;
			int fY = player.getY()+playerSafeSpace;
			int gX = player.getX()-playerSafeSpace;
			int gY = player.getY();
			int hX = player.getX()-playerSafeSpace;
			int hY = player.getY()-playerSafeSpace;
			double disA = Math.sqrt( (aX-posX)*(aX-posX) + (aY-posY)*(aY-posY) );
			double disB = Math.sqrt( (bX-posX)*(bX-posX) + (bY-posY)*(bY-posY) );
			double disC = Math.sqrt( (cX-posX)*(cX-posX) + (cY-posY)*(cY-posY) );
			double disD = Math.sqrt( (dX-posX)*(dX-posX) + (dY-posY)*(dY-posY) );
			double disE = Math.sqrt( (eX-posX)*(eX-posX) + (eY-posY)*(eY-posY) );
			double disF = Math.sqrt( (fX-posX)*(fX-posX) + (fY-posY)*(fY-posY) );
			double disG = Math.sqrt( (gX-posX)*(gX-posX) + (gY-posY)*(gY-posY) );
			double disH = Math.sqrt( (hX-posX)*(hX-posX) + (hY-posY)*(hY-posY) );
			double shortest = disA;
			targetX = aX;
			targetY = aY;
			double[] array = new double[7];
			for (int i=0; i<7;i++){
				if (i==0){
					array[i]=disB;
				}
				else if (i==1){
					array[i]=disC;
				}
				else if (i==2){
					array[i]=disD;
				}
				else if (i==3){
					array[i]=disE;
				}
				else if (i==4){
					array[i]=disF;
				}
				else if (i==5){
					array[i]=disG;
				}
				else if (i==6){
					array[i]=disH;
				}
			}
			for(int ii=0;ii<7;ii++){
				if (array[ii]<shortest){
					shortest = array[ii];
				}
			}
			if(shortest == disB){
				targetX=bX;
				targetY=bY;
		}
			else if(shortest == disC){
				targetX=cX;
				targetY=cY;
			}
			else if(shortest == disD){
				targetX=dX;
				targetY=dY;
			}
			else if(shortest == disE){
				targetX=eX;
				targetY=eY;
			}
			else if(shortest == disF){
				targetX=fX;
				targetY=fY;
			}
			else if(shortest == disG){
				targetX=gX;
				targetY=gY;
			}
			else if(shortest == disH){
				targetX=hX;
				targetY=hY;
			}
		}
	}
	
	public void calcDirection(){
	/*
	90Grad eines ViertelKreises aufgeteilt in 4 Teile um jeder Richtung den gleichen Anteil zu geben
	zb.0-22.5 fuer rechts = 1 Anteil (rechts wird in zwei Vierteln des Kreises erfasst und bekommt jeweils einen Anteil)
	22.5-67.5 fuer oben/rechts = 2Anteile (oben/rechts wird nur in einem Viertelkreis beruecksichtigt und bekommt direkt 2 Anteile)
	67.5-90 fuer oben = 1 Anteil (das gleiche wie fuer rechts)
	1/tan(67.5Grad) = 0.41
	1/tan(22.5Grad) = 2.42 --> wegen getXDistancePlayer()/getYDistancePlayer() entspricht
	Ankathete/Gegenkathete (also 1/tan(alpha)). Andersrum waere es der normal tangens
	*/
		if ((getXDistancePlayer()>0) && (getYDistancePlayer()<0)){ //spieler oben rechts vom gegner
	
			if(getYDistancePlayer() != 0){
				float tmp = getXDistancePlayer()/(getYDistancePlayer()*-1);
				if (tmp >= 2.42){
					enemyDirection=6;
				}
				else if (tmp<2.42 && tmp>=0.41){
					enemyDirection=5;
				}
				else if (tmp<0.41){
					enemyDirection=3;
				}
			}
		
		}
		else if ( (getXDistancePlayer()>0) && (getYDistancePlayer()>0)){ //spieler unten rechts vom gegner
			if(getYDistancePlayer() != 0){
				float tmp = getXDistancePlayer()/(getYDistancePlayer());
				if (tmp >= 2.42){
					enemyDirection=6;
				}
				else if (tmp<2.42 && tmp>=0.41){
					enemyDirection=7;
				}
				else if (tmp<0.41){
					enemyDirection=4;
				}
			}	
		
		}
		else if ( (getXDistancePlayer()<0) && (getYDistancePlayer()>0)){ //spieler unten links vom gegner
		
			if(getYDistancePlayer() != 0){
				float tmp = (getXDistancePlayer()*-1)/(getYDistancePlayer());
				if (tmp >= 2.42){
					enemyDirection=1;
				}
				else if (tmp<2.42 && tmp>=0.41){
					enemyDirection=2;
				}
				else if (tmp<0.41){
					enemyDirection=4;
				}
			}
		
		}
		else if ( (getXDistancePlayer()<0) && (getYDistancePlayer()<0)){ //spieler oben links vom gegner
			if(getYDistancePlayer() != 0){
				float tmp = (getXDistancePlayer()*-1)/(getYDistancePlayer()*-1);
				if (tmp >= 2.42){
					enemyDirection=1;
				}
				else if (tmp<2.42 && tmp>=0.41){
					enemyDirection=0;
				}
				else if (tmp<0.41){
					enemyDirection=3;
				}
			}
		
		}
		else if ((getXDistancePlayer()==0) || (getYDistancePlayer()==0)){
			if (getXDistancePlayer()==0){
				if (getYDistancePlayer()>0){
					enemyDirection=4;
				}
				else{
					enemyDirection=3;
				}
			}
			else if (getYDistancePlayer()==0){
				if (getXDistancePlayer()>0){
					enemyDirection=6;
				}
				else{
					enemyDirection=1;
				}
			}
		}
	}
	public void move(){
		targetDistance();
		calcDirection();
		if(enemyDirection == 0){
			int tmp = getX()-speed;
			setX(tmp);
			tmp = getY()-speed;
			setY(tmp);
		}
		else if(enemyDirection ==1){
			int tmp = getX() -speed;
			setX(tmp);
		}
		else if(enemyDirection ==2){
			int tmp = getX()-speed;
			setX(tmp);
			tmp = getY()+speed;
			setY(tmp);
		}
		else if(enemyDirection ==3){
			int tmp = getY()-speed;
			setY(tmp);
		}
		else if(enemyDirection ==4){
			int tmp = getY()+speed;
			setY(tmp);
		}
		else if(enemyDirection ==5){
			int tmp = getX()+speed;
			setX(tmp);
			tmp = getY()-speed;
			setY(tmp);
		}
		else if(enemyDirection ==6){
			int tmp = getX()+speed;
			setX(tmp);
		}
		else if(enemyDirection ==7){
			int tmp = getX()+speed;
			setX(tmp);
			tmp = getY()+speed;
			setY(tmp);
		}
	}
	public BufferedImage getImage(){
		animation += 0.33;
		if((int)animation == 8){
			animation =0.0f;
		}
	
	
		switch(enemyDirection){
		case(0):
			image = runter_cycle[(int)animation];
			break;
		case(1):
			image = links_cycle[(int)animation];
		break;
		case(2):
			image = rechts_cycle[(int)animation];
		break;
		case(3):
			image = hoch_cycle[(int)animation];
		break;
		case(4):
			image = runterlinks_cycle[(int)animation];
		break;
		case(5):
			image = runterrechts_cycle[(int)animation];
		break;
		case(6):
			image = hochrechts_cycle[(int)animation];
		break;
		case(7):
			image = hochlinks_cycle[(int)animation];
		break;
		}
		return image;
	
	}
	public void dealDamage(){
		player.energy = player.energy - 0.1f;
		window.panel.actionMessages.add(new ActionMessage("Player hit"));
		canAttack = false;
		countToNextAttack =10;
	}
}