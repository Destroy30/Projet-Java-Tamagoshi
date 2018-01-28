package tamagoshi.jeu.actions;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import tamagoshi.language.LanguageAccessor;
import tamagoshi.language.LanguageObserver;
import tamagoshi.properties.TamaConfiguration;

/**
 * Va permettre d'effectuer l'action de "quitter le jeu" et va fermer l'application
 */
public class ActionExit extends AbstractAction implements LanguageObserver  {
	
	public ActionExit() {
		super();
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		this.putValue(MNEMONIC_KEY, KeyEvent.VK_Q);
		LanguageAccessor languageAcc = LanguageAccessor.getInstance();
		languageAcc.addObservator(this);
	}

	@Override
	/**
	 * Va sauvegarder la configuration du jeu dans le fichier de propriétés et va fermer l'application
	 */
	public void actionPerformed(ActionEvent arg0) {
		TamaConfiguration config = TamaConfiguration.getInstance();
		config.storeProperties();
		System.exit(1);
	}

	@Override
	/**
	 * Permet de mettre à jour le nom de l'action au changement de langue
	 */
	public void languageUpdate(LanguageAccessor languageAcc) {
		ResourceBundle languageBundle = languageAcc.getBundle("Home");
		this.putValue(Action.NAME, languageBundle.getString("exit"));
	}

}