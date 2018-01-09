package tamagoshi.graphic;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import tamagoshi.tamagoshis.Tamagoshi;

public class TamaJPanel extends JPanel {
	
	private Tamagoshi tamagoshi;
	private JPanel tamaImagePanel;
	private BufferedImage tamaImage;
	private JTextPane talkZone;
	private JButton feedButton,playButton;
	
	public TamaJPanel(Tamagoshi tamagoshi) {
		this.tamagoshi = tamagoshi;
		
		this.setTamaImage("tamabase");
		
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
		
		this.add(BorderLayout.SOUTH, panelAction);
	}
	
	private void setTamaImage(String ressourceName) {
		try {
			this.tamaImage = ImageIO.read(TamaJPanel.class.getResource("./ressources/"+ressourceName+".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(this.tamaImage, 20, 0, 250,250,this);
	}
	
	public void displayText(String text) {
		this.talkZone.setText(text);
	}
	
	private void goFeed() {
		this.tamagoshi.mange();
	}
	
	private void goPlay() {
		this.tamagoshi.joue();
	}
	

}
