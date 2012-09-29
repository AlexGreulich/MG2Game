import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioClip{
	private Clip clip;
	private int framePosition;
	public void open(File file) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
		AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
		if (audioInputStream.getFormat().getEncoding() != Encoding.PCM_SIGNED &&
			audioInputStream.getFormat().getEncoding() != Encoding.PCM_UNSIGNED){
			audioInputStream = decode(audioInputStream);
		}
		framePosition = 0;
		clip = AudioSystem.getClip();
		clip.open(audioInputStream);
	}
	
	private AudioInputStream decode(AudioInputStream mp3) {
		AudioFormat baseFormat = mp3.getFormat();
		AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 
			baseFormat.getSampleRate(),
			16,
			baseFormat.getChannels(),
			baseFormat.getChannels() * 2,
			baseFormat.getSampleRate(),
			false);
		AudioInputStream decodedAudioInputStream = AudioSystem.getAudioInputStream(decodedFormat, mp3);
		return decodedAudioInputStream;
	}
	
	public void setLoopEnabled(boolean enabled){
		if (enabled == true){
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}
	public void start(){
		clip.setFramePosition(framePosition);
		clip.start();
	}
	public void stop(){
		clip.stop();
		framePosition = 0;
	}
	public void pause(){
		clip.stop();
		framePosition = clip.getFramePosition();
	}
	
	public static void main(String[] arg) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
		AudioClip audioClip = new AudioClip();
		File file = new File("marcus_kellis_theme.mp3");
		System.out.println("decoding File .. ");
		audioClip.open(file);
		System.out.println("File decoded");
		audioClip.start();
		Scanner scanner = new Scanner(System.in);
		String nextLine = scanner.nextLine();
		//audioClip.stop();
	}

	public AudioFormat getFormat() {
		if (clip == null){
			return null;
		}
		return clip.getFormat();
	}
}