package tamagoshi.jeu;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TamaScoreFrame extends JFrame{
	
	public TamaScoreFrame(int niveau,double[]scores) {
		setTitle("Scores (difficulté "+niveau+")");
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		int numScore = 1;
		for(double score : scores) {
			JLabel labelScore = new JLabel("Score n°="+numScore+" : "+score+"%");
			labelScore.setAlignmentX(CENTER_ALIGNMENT);
			add(labelScore);
			numScore++;
		}
	}

}
