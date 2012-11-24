import javax.swing.JWindow;


@SuppressWarnings("serial")
public class Gamemain extends JWindow{
//
//	
//	JButton start;
//	JCheckBox fullscreen, windowed;
//	int option =0;
//	public Gamemain(){
//		setSize(300,500);
//		setLayout(new BorderLayout());
//		
//		start = new JButton("Kick some ass");
//		
//		start.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent e) {
//				
//				if(fullscreen.isEnabled() && !windowed.isEnabled()){
//					option = 1;
//					try {
//						
//						
//						dispose();
//						new GameWindow();
//					} catch (UnsupportedAudioFileException e1) {e1.printStackTrace();} catch (IOException e1) {e1.printStackTrace();} catch (LineUnavailableException e1) {e1.printStackTrace();}
//					
//				}else if(!fullscreen.isEnabled() && windowed.isEnabled()){
//					option = 2;
//					//new GameWindow(option);
//					startGame();
//				}
//			}
//		});
//		
//		fullscreen = new JCheckBox("Play in fullscreen mode");
//		windowed = new JCheckBox("Play in windowed mode");
//		
//		this.getContentPane().add(windowed,BorderLayout.NORTH );//
//		this.getContentPane().add(fullscreen ,BorderLayout.CENTER);//
//		this.getContentPane().add(start ,BorderLayout.SOUTH);//
//		
//		setVisible(true);
//		
//		
//		
//	}
//	public void startGame(){
//		this.dispose();
//			try {
//			new GameWindow();
//		} catch (UnsupportedAudioFileException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (LineUnavailableException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	public static void main(String[] args){
//		Gamemain gamemain = new Gamemain();
//	}
}
