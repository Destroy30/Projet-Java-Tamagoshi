package tamagoshi.jeu;

import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tamagoshi.language.LanguageAccessor;
import tamagoshi.language.LanguageObserver;

public class TamaScoreFrame extends JFrame implements LanguageObserver{
	
	private ResourceBundle language;
	private int niveau;
	private double[]scores;
	
	public TamaScoreFrame(int niveau,double[]scores) {
		this.niveau = niveau;
		this.scores=scores;
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		
		//Language
		LanguageAccessor accessor = LanguageAccessor.getInstance();
		accessor.addObservator(this);
	}

	@Override
	public void languageUpdate(LanguageAccessor languageAcc) {
		this.language = languageAcc.getBundle("Home");
		this.setTitle(language.getString("scores")+" (" +language.getString("difficulty") + " "+niveau+")");
		int numScore = 1;
		for(double score : scores) {
			JLabel labelScore = new JLabel(language.getString("score")+" n°="+numScore+" : "+score+"%");
			labelScore.setAlignmentX(CENTER_ALIGNMENT);
			add(labelScore);
			numScore++;
		}
	}

}
