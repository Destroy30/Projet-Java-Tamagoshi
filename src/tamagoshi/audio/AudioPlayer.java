package tamagoshi.audio;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {
	
	private AudioInputStream currentMusic;
	private Clip currentClip;
	
	private static AudioPlayer instance;
	
	private AudioPlayer() {
        try {
			this.currentClip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			System.err.println("Erreur lors de la gestion des ressources audio");
		}
	};
	
	public static synchronized AudioPlayer getInstance() {
		if(AudioPlayer.instance == null) {
			AudioPlayer.instance = new AudioPlayer();
		}
		return AudioPlayer.instance;
	}
	
	public void playBackgroundMusic(String musicFileName) {
		try {
			closeCurentBackground();
			URL musicFile = this.getClass().getResource("ressources/background/"+musicFileName+".wav");
			this.currentMusic = AudioSystem.getAudioInputStream(musicFile);
	        this.currentClip.open(this.currentMusic);
	        setUpAudioRessource(this.currentClip);
	        this.currentClip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
			System.err.println("Fichier de musique de background non trouvé ou non géré");
		}
	}
	
	private void setUpAudioRessource(Clip clip) {
	    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
	    double gain = .5D;
	    float dB = (float) (Math.log(gain) / Math.log(10.0) * 60.0); // la dernière opérande : Plus elle augmente plus le volume est nas
	    gainControl.setValue(dB);
	    clip.start();
	}
	
	public void closeCurentBackground() {
		if(this.currentMusic != null && this.currentClip != null) {
			try {
				this.currentClip.stop();
				this.currentClip.close();
				this.currentMusic.close();
			} catch (IOException e) {
				System.err.println("Erreur lors de la fermeture des flux sonores");
			}
		}
	}
	
	public void playSound(String soundFileName) {
		try {
			URL soundFile = this.getClass().getResource("ressources/sounds/"+soundFileName+".wav");
			AudioInputStream sound = AudioSystem.getAudioInputStream(soundFile);
	        Clip soundClip = AudioSystem.getClip();
	        soundClip.open(sound);
	        setUpAudioRessource(soundClip);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			System.err.println("Fichier de bruitage non trouvé ou non géré");
		}
	}

}
