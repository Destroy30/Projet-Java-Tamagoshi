package tamagoshi.jeu;

import java.awt.BorderLayout;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import tamagoshi.language.LanguageAccessor;
import tamagoshi.language.LanguageObserver;

/**
 * Fenetre présentant les règles du jeu
 */
public class TamaHelp extends JFrame implements LanguageObserver {
	
	/**
	 * Contient les règles du jeu
	 */
	private JTextPane content;

	public TamaHelp() {
		
		this.content = new JTextPane();
		JScrollPane scroll = new JScrollPane(this.content);
		this.content.setEditable(false);
		LanguageAccessor language = LanguageAccessor.getInstance();
		language.addObservator(this);
		this.add(scroll,BorderLayout.CENTER);
		
	}

	@Override
	/**
	 * Permet de mettre à jour le texte par rapport à la langue choisie
	 */
	public void languageUpdate(LanguageAccessor languageAcc) {
		ResourceBundle bundle = languageAcc.getBundle("Home");
		this.setTitle(bundle.getString("rules"));
		this.content.setText(bundle.getString("help"));
	}
}
