import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class ActionMessage {

	String text;
	int lifetime = 100;
	GamePanel panel;
	public ActionMessage(GamePanel p,String s){
		text = s;
		panel =p;
	}
	
	public String getText(){
		return text;
	}
	void reduceLife(){
		lifetime = lifetime -3;
	}
	public BufferedImage producePicFromMessage(Graphics g){
		
		g.setColor(new Color(0,0,0,80));
		g.fillRect(panel.panelwidth/4-(this.getText().length()*2)-5, panel.panelheight/4  - 15, this.getText().length()*8-4,20);//(b * 10)
		g.setColor(new Color(255,255,255,80));		// old: new Color(255,50,50,200- (2*m.lifetime)		new Color(hudColor.getRGB() )
		g.drawRect(panel.panelwidth/4-(this.getText().length()*2)-5, panel.panelheight/4  - 15, this.getText().length()*8-4,20);//(b * 10)
		g.drawString(this.getText(), panel.panelwidth/4-(this.getText().length()*2), panel.panelheight/4 );//(b * 10)
		Graphics2D g2d= (Graphics2D)g;
		BufferedImage bi = new BufferedImage(panel.panelwidth/4-(this.getText().length()*2)-5, panel.panelheight/4  - 15, BufferedImage.TYPE_INT_RGB );
		
		return bi;
	}
}
