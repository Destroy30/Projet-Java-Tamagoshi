package tamagoshi.jeu;

import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

import tamagoshi.language.LanguageAccessor;
import tamagoshi.language.LanguageObserver;
import tamagoshi.properties.TamaConfiguration;

/**
 * Va gérer l'affichage des 3 meilleurs scores pour un niveau de difficulté donné
 */
public class TamaScoreFrame extends JFrame implements LanguageObserver{
	
	/**
	 * Bundle de la langue courante selctionnée
	 */
	private ResourceBundle language;
	
	/**
	 * Niveau de difficulté dont on demande le score
	 */
	private int niveau;
	
	/**
	 * Scores pour le niveau de difficulté
	 */
	private double[]scores;
	
	/**
	 * Va récupérer les scores dans la configuration pour le niveau de difficulté. <br>
	 * S'attache au manager de langue pour être mis à jour
	 * @param niveau Niveau de difficulté dont on veut voir les scores
	 */
	public TamaScoreFrame(int niveau) {
		this.niveau = niveau;
		TamaConfiguration config = TamaConfiguration.getInstance();
		this.scores =  config.getScores(niveau);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		
		//Language
		LanguageAccessor accessor = LanguageAccessor.getInstance();
		accessor.addObservator(this);
	}

	@Override
	/**
	 * Met à jour les scores selon la langue choisie <br>
	 * Pour les 3 meilleures scores, affiche une ligne avec le score
	 */
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
