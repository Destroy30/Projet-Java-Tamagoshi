package tamagoshi.jeu.actions;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import java.awt.event.KeyEvent;

import tamagoshi.jeu.TamaGameGraphic;
import tamagoshi.jeu.TamaHomeGraphic;
import tamagoshi.language.LanguageAccessor;
import tamagoshi.language.LanguageObserver;
import tamagoshi.properties.TamaConfiguration;

/**
 * Cette classe va gérer l'action pour lancer une partie
 */
public class ActionPlay extends AbstractAction implements LanguageObserver {
	
	/**
	 * Fenetre d'accueil, afin de récupérer des informations de configuration
	 */
	private TamaHomeGraphic home;
	
	public ActionPlay(TamaHomeGraphic home) {
		super();
		this.home=home;
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
		this.putValue(MNEMONIC_KEY, KeyEvent.VK_P);
		LanguageAccessor languageAcc = LanguageAccessor.getInstance();
		languageAcc.addObservator(this);
	}

	@Override
	/**
	 * Va permettre de lancer une partie en créant un {@link TamaGameGraphic} <br>
	 * Selon la configuration (noms auto-générés, difficulté fixée...) elle fait appel à un des deux constructeurs <br>
	 * Enfin, la méthode demande la sauvegarde les informations de configuration dans le fichier
	 */
	public void actionPerformed(ActionEvent arg0) {
		TamaGameGraphic tamaGame;
		if(home.difficultyIsFixed()) {
			tamaGame = new TamaGameGraphic(home.namesAreAutoGenerated(),home.getDifficulty()); 
		}
		else {
			tamaGame = new TamaGameGraphic(home.namesAreAutoGenerated()); 
		}
		tamaGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tamaGame.setSize(500, 250);
		tamaGame.setLocationRelativeTo(null);
		tamaGame.setVisible(true);
		
		TamaConfiguration config = TamaConfiguration.getInstance();
		config.storeProperties();
		
		home.dispose();
		tamaGame.initialisation();
	}
	
	@Override
	/**
	 * Permet de mettre à jour le nom de l'action au changement de langue
	 */
	public void languageUpdate(LanguageAccessor languageAcc) {
		ResourceBundle languageBundle = languageAcc.getBundle("Home");
		this.putValue(Action.NAME, languageBundle.getString("play"));
	}

}
