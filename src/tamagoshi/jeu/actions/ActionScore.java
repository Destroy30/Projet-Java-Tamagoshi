package tamagoshi.jeu.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import tamagoshi.jeu.TamaScoreFrame;
import tamagoshi.properties.TamaConfiguration;

public class ActionScore extends AbstractAction {
	
	public ActionScore(String actionName) {
		super(actionName);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		SpinnerModel model = new SpinnerNumberModel(1,1,10,1);
		JSpinner spin = new JSpinner(model);
		JFormattedTextField tf = ((JSpinner.DefaultEditor) spin.getEditor()).getTextField();
		tf.setEditable(false);
		int result = JOptionPane.showOptionDialog(null, spin, "Entez le niveau de difficulté", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if(result == JOptionPane.OK_OPTION) {
			int level=(int) spin.getValue();
			TamaConfiguration config = TamaConfiguration.getInstance();
			double[]scores =  config.getScores(level);
			TamaScoreFrame frameScore = new TamaScoreFrame(level,scores);
			frameScore.setLocationRelativeTo(null);
			frameScore.setResizable(false);
			frameScore.setSize(250,100);
			frameScore.setVisible(true);
		}
	}

}
