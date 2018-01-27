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
	
	private BufferedImage tamaImage;
	
	public TamaJPanel() {	
		this.setTamaImage("pikabase");
	}
	
	private void setTamaImage(String ressourceName) {
		try {
			this.tamaImage = ImageIO.read(TamaJPanel.class.getResource("/tamagoshi/graphic/ressources/"+ressourceName+".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(this.tamaImage, 20, 0, 250,250,this);
	}
	
	public void changeImage(String ressourceName) {
		this.setTamaImage(ressourceName);
		this.repaint();
	}
	

}
