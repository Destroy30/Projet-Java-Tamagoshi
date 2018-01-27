package tamagoshi.jeu.actions;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import tamagoshi.jeu.TamaHelp;
import tamagoshi.language.LanguageAccessor;
import tamagoshi.language.LanguageObserver;
import tamagoshi.properties.TamaConfiguration;

public class ActionHelp extends AbstractAction implements LanguageObserver  {
	
	public ActionHelp() {
		super();
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK));
		this.putValue(MNEMONIC_KEY, KeyEvent.VK_T);
		LanguageAccessor languageAcc = LanguageAccessor.getInstance();
		languageAcc.addObservator(this);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		TamaHelp tamaHelp = new TamaHelp();
		tamaHelp.setLocationRelativeTo(null);
		tamaHelp.setSize(800,450);
		tamaHelp.setVisible(true);
	}

	@Override
	public void languageUpdate(LanguageAccessor languageAcc) {
		ResourceBundle languageBundle = languageAcc.getBundle("Home");
		this.putValue(Action.NAME, languageBundle.getString("rules"));
	}
	
}