package tamagoshi.graphic;

import java.util.Random;

import javax.swing.JFrame;

import tamagoshi.jeu.TamaGameGraphic;
import tamagoshi.tamagoshis.GrosJoueur;
import tamagoshi.tamagoshis.GrosMangeur;
import tamagoshi.tamagoshis.Tamagoshi;


public class TamaFrame extends JFrame{
	
	private TamaGameGraphic tamaGame;
	private Tamagoshi tamaManaged;
	private TamaJPanel tamaPanel;
	
	
	public TamaFrame(TamaGameGraphic tamaGame,String tamaName) {
		super();
		this.tamaGame = tamaGame;
		Random rand = new Random();		
		this.tamaManaged = (rand.nextInt(2)==0)? new GrosMangeur(tamaName): new GrosJoueur(tamaName);
		this.tamaPanel = new TamaJPanel(tamaManaged);
		init();
	}
	
	private void init() {
		setSize(300,350);
		setTitle(tamaManaged.toString());
		add(tamaPanel);
		this.setResizable(false);
		setVisible(true);
	}
	
	//Test
	/*public static void main(String[] args) {
		
		TamaFrame tam = new TamaFrame(new GrosJoueur("Coucou"));
	}*/

}
