package tamagoshi.graphic;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import tamagoshi.jeu.TamaGameGraphic;
import tamagoshi.tamagoshis.GrosJoueur;
import tamagoshi.tamagoshis.GrosMangeur;
import tamagoshi.tamagoshis.Tamagoshi;


public class TamaFrame extends JFrame{
	
	private TamaGameGraphic tamaGame;
	private Tamagoshi tamaManaged;
	private TamaJPanel tamaPanel;
	private JTextPane talkZone;
	private JButton feedButton,playButton;
	
	
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
		
		this.feedButton = new JButton("Nourrir");
		feedButton.addActionListener((e->{goFeed();}));
		
		this.playButton = new JButton("Jouer");
		playButton.addActionListener((e->{goPlay();}));
		
		panelBoutons.add(feedButton);
		panelBoutons.add(playButton);
		
		panelAction.add(panelBoutons);
		
		this.add(tamaPanel);
		
		this.add(BorderLayout.SOUTH, panelAction);
		
	}
	
	public void displayText(String text) {
		this.talkZone.setText(text);
	}
	
	private void goFeed() {
		this.tamaManaged.mange();
		this.tamaGame.tamagoshiEat();
	}
	
	private void goPlay() {
		this.tamaManaged.joue();
		this.tamaGame.tamagoshiPlay();
	}
	
	
	public Tamagoshi getTamaManaged() {
		return this.tamaManaged;
	}
	
	public void disableButtonFeed() {
		this.feedButton.setEnabled(false);
	}
	
	public void disableButtonPlay() {
		this.playButton.setEnabled(false);
	}
	
	private void enableButtonFeed() {
		this.feedButton.setEnabled(true);
	}
	
	private void enableButtonPlay() {
		this.playButton.setEnabled(true);
	}
	
	public void renableButtons() {
		if(!this.tamaManaged.isDead()) {
			this.enableButtonFeed();
			this.enableButtonPlay();
		}
	}
	
	public void setHappyImage() {
		this.tamaPanel.changeImage("pikabase");
	}
	
	public void setAngryImage() {
		this.tamaPanel.changeImage("pikaangry");
	}
	
	public void setHungryImage() {
		this.tamaPanel.changeImage("pikahungry");
	}
	
	public void setBoringImage() {
		this.tamaPanel.changeImage("pikaborring");
	}
	
	public void setKoImage() {
		this.tamaPanel.changeImage("pikako");
	}
	
	//Test
//	public static void main(String[] args) {
//		
//		TamaFrame tam = new TamaFrame(new TamaGameGraphic(),"hey");
//	}

}
