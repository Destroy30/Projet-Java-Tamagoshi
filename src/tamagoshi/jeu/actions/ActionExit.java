package tamagoshi.jeu.actions;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

import com.sun.glass.events.KeyEvent;

import tamagoshi.language.LanguageAccessor;
import tamagoshi.language.LanguageObserver;
import tamagoshi.properties.TamaConfiguration;

public class ActionExit extends AbstractAction implements LanguageObserver  {
	
	public ActionExit() {
		super();
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		LanguageAccessor languageAcc = LanguageAccessor.getInstance();
		languageAcc.addObservator(this);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		TamaConfiguration config = TamaConfiguration.getInstance();
		config.storeProperties();
		System.exit(1);
	}

	@Override
	public void languageUpdate(LanguageAccessor languageAcc) {
		ResourceBundle languageBundle = languageAcc.getBundle("Home");
		this.putValue(Action.NAME, languageBundle.getString("exit"));
	}

}