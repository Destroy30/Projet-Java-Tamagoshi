package tamagoshi.jeu.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import tamagoshi.properties.TamaConfiguration;

public class ActionExit extends AbstractAction {
	
	public ActionExit(String actionName) {
		super(actionName);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		TamaConfiguration config = TamaConfiguration.getInstance();
		config.storeProperties();
		System.exit(1);
	}

}