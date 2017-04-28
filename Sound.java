package assignment7;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sound {
	private static File welcomeSoundFile = new File("Welcome.mp3");
	private static File receiveSoundFile = new File("Receive.mp3");
	private static File sendSoundFile = new File("Send.mp3");
	private static File goodbyeSoundFile = new File("Goodbye.mp3");

	public static void playWelcomeSound() {
		String welcomeSound = "";
		try {
			welcomeSound = welcomeSoundFile.getCanonicalPath();
		} catch (Exception e) {
		}

		Media sound = new Media(new File(welcomeSound).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
	}

	public static void playReceiveSound() {
		String receiveSound = "";
		try {
			receiveSound = receiveSoundFile.getCanonicalPath();
		} catch (Exception e) {
		}
		
		Media sound = new Media(new File(receiveSound).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
	}
	
	public static void playSendSound() {
		String sendSound = "";
		try {
			sendSound = receiveSoundFile.getCanonicalPath();
		} catch (Exception e) {
		}
		
		Media sound = new Media(new File(sendSound).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
	}
	
	public static void playGoodbyeSound() {
		String goodbyeSound = "";
		try {
			goodbyeSound = receiveSoundFile.getCanonicalPath();
		} catch (Exception e) {
		}
		
		Media sound = new Media(new File(goodbyeSound).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(sound);
		mediaPlayer.play();
	}
}
