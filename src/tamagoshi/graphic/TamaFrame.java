package tamagoshi.graphic;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import tamagoshi.jeu.TamaGameGraphic;
import tamagoshi.language.LanguageAccessor;
import tamagoshi.language.LanguageObserver;
import tamagoshi.tamagoshis.Tamagoshi;

/**
 * Cette classe va permettre l'affichage d'un tamagoshi <br>
 * Elle va également permettre d'effectuer les interactions (jouer/manger) avec le tamagoshi et de transmettre les infos au gestionnaire de jeu
 */
public class TamaFrame extends JFrame implements LanguageObserver{
	
	/**
	 * Gestionnaire de jeu
	 */
	private TamaGameGraphic tamaGame;
	
	/**
	 * Tamagoshi représenté par cette TamaFrame
	 */
	private Tamagoshi tamaManaged;
	
	/**
	 * Panel d'affichage du'n tamagoshi (image)
	 */
	private TamaJPanel tamaPanel;
	
	/**
	 * Zone où le tamagoshi parle (le tamagoshi envoi ses messages directement dans cette zone)
	 */
	private JTextPane talkZone;
	
	/**
	 * Les 2 boutons qui déclenchent les actions nourrir/jouer
	 */
	private JButton feedButton,playButton;
	
	/**
	 * Construit la frame <br>
	 * Place le panel représentant le tamagoshi <br>
	 * Place la zone pour permettre au tamagoshi de parler <br>
	 * Place la zone des boutons
	 * @param tamaGame Gestionnaire de jeu
	 * @param tamagoshi Tamagoshi géré
	 */
	public TamaFrame(TamaGameGraphic tamaGame,Tamagoshi tamagoshi) {
		super();
		this.tamaGame = tamaGame;
		
		this.tamaManaged = tamagoshi;
		this.tamaManaged.setTamaFrame(this);
		
		this.tamaPanel = new TamaJPanel();
		
		this.setLayout(new BorderLayout());
		
		JPanel panelAction = new JPanel(new GridLayout(2,1));
		
		this.talkZone = new JTextPane();
		talkZone.setPreferredSize(new Dimension(300,50));
		talkZone.setEditable(false);
		
		panelAction.add(talkZone);
		
		JPanel panelBoutons = new JPanel(new FlowLayout());
		
		this.feedButton = new JButton();
		feedButton.addActionListener((e->{goFeed();}));
		
		this.playButton = new JButton();
		playButton.addActionListener((e->{goPlay();}));
		
		panelBoutons.add(feedButton);
		panelBoutons.add(playButton);
		
		panelAction.add(panelBoutons);
		
		this.add(tamaPanel);
		
		this.add(BorderLayout.SOUTH, panelAction);
		
		//Language
		LanguageAccessor accessor = LanguageAccessor.getInstance();
		accessor.addObservator(this);
	}
	
	/**
	 * Permet d'afficher un message dans la zone de texte
	 * @param text Message à afficher
	 */
	public void displayText(String text) {
		this.talkZone.setText(text);
	}
	
	/**
	 * Déclenche l'action manger sur le tamagoshi et en informe le gestionnaire de jeu
	 */
	private void goFeed() {
		this.tamaManaged.mange();
		this.tamaGame.tamagoshiEat();
	}
	
	/**
	 * Déclenche l'action jouer sur le tamagoshi et en informe le gestionnaire de jeu
	 */
	private void goPlay() {
		this.tamaManaged.joue();
		this.tamaGame.tamagoshiPlay();
	}
	
	/**
	 * Permet d'obtenir le tamagoshi gérer par cette frame
	 * @return Le tamagoshi géré
	 */
	public Tamagoshi getTamaManaged() {
		return this.tamaManaged;
	}
	
	/**
	 * Permet de désactiver le bouton "Nourrir" sur cette frame
	 */
	public void disableButtonFeed() {
		this.feedButton.setEnabled(false);
	}
	
	/**
	 * Permet de désactiver le bouton "Jouer" sur cette frame
	 */
	public void disableButtonPlay() {
		this.playButton.setEnabled(false);
	}
	
	/**
	 * Permet de réactiver le bouton "Nourrir" sur cette frame
	 */
	private void enableButtonFeed() {
		this.feedButton.setEnabled(true);
	}
	
	/**
	 * Permet de réactiver le bouton "Jouer" sur cette frame
	 */
	private void enableButtonPlay() {
		this.playButton.setEnabled(true);
	}
	
	/**
	 * Permet de réactiver tous les boutons, seulement si le tamagoshi géré n'est pas mort
	 */
	public void renableButtons() {
		if(!this.tamaManaged.isDead()) {
			this.enableButtonFeed();
			this.enableButtonPlay();
		}
	}
	
	/**
	 * Change l'image du panel de la frame en tamagoshi content
	 */
	public void setHappyImage() {
		this.tamaPanel.changeImage("pikabase");
	}
	
	/**
	 * Change l'image du panel de la frame en tamagoshi enervé
	 */
	public void setAngryImage() {
		this.tamaPanel.changeImage("pikaangry");
	}
	
	/**
	 * Change l'image du panel de la frame en tamagoshi affamé
	 */
	public void setHungryImage() {
		this.tamaPanel.changeImage("pikahungry");
	}
	
	/**
	 * Change l'image du panel de la frame en tamagoshi qui s'ennuie
	 */
	public void setBoringImage() {
		this.tamaPanel.changeImage("pikaborring");
	}
	
	/**
	 * Change l'image du panel de la frame en tamagoshi KO
	 */
	public void setKoImage() {
		this.tamaPanel.changeImage("pikako");
	}

	@Override
	/**
	 * Permet de mettre à jour le bundle de langue lors du changement de langue, affin de mettre à jours les boutons, le titre...
	 */
	public void languageUpdate(LanguageAccessor languageAcc) {
		ResourceBundle language = languageAcc.getBundle("Game");
		this.playButton.setText(language.getString("play"));
		this.feedButton.setText(language.getString("feed"));
	}
	

}
