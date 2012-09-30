import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;


public class GameWindow extends JFrame{

	GamePanel panel;
	Player player;
	Controls controls;
	Thread panelThread, gameloopthread, bulletthread;
	Level level;
	Gameloop gameloop;
	BulletHandler bullethandler;
	
//	AudioClip audioClip;
	
	public GameWindow(){
		super("Cybercalypse");
		
		setSize(1920,1080);
		controls = new Controls();
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		level = new Level();
		
		player = new Player(this);//,50,50
		panel = new GamePanel(this);
		gameloop = new Gameloop(this);
		addKeyListener(controls);
		
		add(panel);
		bullethandler = new BulletHandler(this);
		
//		audioClip = new AudioClip();
//		File audiofile = new File(""+getClass().getResource("audio/test_track_01.mp3"));
//		try {
//			audioClip.open(audiofile);
//		} catch (UnsupportedAudioFileException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (LineUnavailableException e) {
//			e.printStackTrace();
//		}
//		AudioFormat audioFormat = audioClip.getFormat();
		
		setVisible(true);
		this.setIgnoreRepaint(true);
		
		panelThread = new Thread(panel);
		gameloopthread = new Thread(gameloop);
		bulletthread = new Thread(bullethandler);
		panelThread.start();
		gameloopthread.start();
		bulletthread.start();
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int scrx = (int) (screen.getWidth()/2)-350;
		int scry = (int) (screen.getHeight()/2)-370;
		setLocation(scrx, scry);
		
		setResizable(false);
		pack();
	}
	
	public static void main(String[] args){
		
		new GameWindow();
		
	}
}
