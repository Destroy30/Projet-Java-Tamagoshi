package tamagoshi.jeu.actions;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import java.awt.event.KeyEvent;

import tamagoshi.jeu.TamaScoreFrame;
import tamagoshi.language.LanguageAccessor;
import tamagoshi.language.LanguageObserver;

/**
 * Cette classe va gérer l'action "Score" appellée afin d'afficher les scores d'un niveau de difficulté
 */

public class ActionScore extends AbstractAction implements LanguageObserver {
	
	/**
	 * Bundle de langue
	 */
	private ResourceBundle language;
	
	public ActionScore() {
		super();
		this.putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		this.putValue(MNEMONIC_KEY, KeyEvent.VK_R);
		LanguageAccessor languageAcc = LanguageAccessor.getInstance();
		languageAcc.addObservator(this);
	}

	@Override
	/**
	 * Va afficher un {@link JOptionPane} afin de sélectionner un niveau de difficulté <br>
	 * Un spinner va permettre de limiter la saisie <br>
	 * Lorsque la difficulté est choisie, va afficher la {@link TamaScoreFrame} correspondante
	 */
	public void actionPerformed(ActionEvent arg0) {
		SpinnerModel model = new SpinnerNumberModel(1,1,10,1);
		JSpinner spin = new JSpinner(model);
		JFormattedTextField tf = ((JSpinner.DefaultEditor) spin.getEditor()).getTextField();
		tf.setEditable(false);
		int result = JOptionPane.showOptionDialog(null, spin, language.getString("enterDifficulty"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if(result == JOptionPane.OK_OPTION) {
			int level=(int) spin.getValue();
			TamaScoreFrame frameScore = new TamaScoreFrame(level);
			frameScore.setLocationRelativeTo(null);
			frameScore.setResizable(false);
			frameScore.setSize(250,100);
			frameScore.setVisible(true);
		}
	}
	
	@Override
	/**
	 * Permet de mettre à jour le nom de l'action au changement de langue
	 */
	public void languageUpdate(LanguageAccessor languageAcc) {
		this.language = languageAcc.getBundle("Home");
		this.putValue(Action.NAME, language.getString("score"));
	}

}
