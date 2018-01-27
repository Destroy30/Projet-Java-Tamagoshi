package tamagoshi.jeu;

import java.awt.BorderLayout;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import tamagoshi.language.LanguageAccessor;
import tamagoshi.language.LanguageObserver;

public class TamaAbout extends JFrame implements LanguageObserver {
	
	private JTextPane content;

	public TamaAbout() {
		
		this.content = new JTextPane();
		JScrollPane scroll = new JScrollPane(this.content);
		this.content.setEditable(false);
		LanguageAccessor language = LanguageAccessor.getInstance();
		language.addObservator(this);
		this.add(scroll,BorderLayout.CENTER);
		
	}

	@Override
	public void languageUpdate(LanguageAccessor languageAcc) {
		ResourceBundle bundle = languageAcc.getBundle("Home");
		this.setTitle(bundle.getString("about"));
		this.content.setText(bundle.getString("infosCreator"));
	}
}