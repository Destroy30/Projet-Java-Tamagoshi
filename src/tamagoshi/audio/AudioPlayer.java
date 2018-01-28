package tamagoshi.audio;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Cette classe va permettre de jouer de la musique dans l'application <br>
 * Elle va gérer les musiques de fond et les effets sonores
 */
public class AudioPlayer {
	
	/**
	 * Musique de fond actuellement jouée
	 */
	private AudioInputStream currentMusic;
	
	/**
	 * Clip contenant le muique de fond actuellement jouée
	 */
	private Clip currentClip;
	
	/**
	 * Instance de Singleton
	 */
	private static AudioPlayer instance;
	
	/**
	 * Initialise le clip du fond musical
	 */
	private AudioPlayer() {
        try {
			this.currentClip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			System.err.println("Erreur lors de la gestion des ressources audio");
		}
	};
	
	/**
	 * Permet d'obtenir une instance unique de cette classe, suivant le pattern Singleton
	 * @return Une instance unique de cetet classe
	 */
	public static synchronized AudioPlayer getInstance() {
		if(AudioPlayer.instance == null) {
			AudioPlayer.instance = new AudioPlayer();
		}
		return AudioPlayer.instance;
	}
	
	/**
	 * Permet de jouer une muqiue de fond
	 * @param musicFileName Nom de la musique à jouer (format wav)
	 */
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
	
	/**
	 * Permet de jouer une ressource audio depuis un Clip
	 * @param clip Clip que l'on doit démarer
	 */
	private void setUpAudioRessource(Clip clip) {
	    FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
	    double gain = .5D;
	    float dB = (float) (Math.log(gain) / Math.log(10.0) * 60.0); // la dernière opérande : Plus elle augmente plus le volume est nas
	    gainControl.setValue(dB);
	    clip.start();
	}
	
	/**
	 * Permet d'arrêter la diffusion actuelle d'une muqiue de fond <br>
	 * Utile lorsque par exemple, on veut jouer une autre musique de fond <br>
	 */
	private void closeCurentBackground() {
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
	
	/**
	 * Permet de diffuser un son, bruitage... <br>
	 * Cette méthode utilise des variables indépendantes  de la classe, car les ons peuvent se superposer, on a pas besoin de les arrêter non plus
	 * @param soundFileName Nom du son à jouer (format wav)
	 */
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
