
public class ActionMessage {

	String text;
	int lifetime = 100;
	
	public ActionMessage(String s){
		text = s;
	}
	
	public String getText(){
		return text;
	}
	void reduceLife(){
		lifetime = lifetime -3;
	}
}
