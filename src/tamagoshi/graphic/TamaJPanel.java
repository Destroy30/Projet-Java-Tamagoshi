package tamagoshi.graphic;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Panel affiché dans la {@link tamagoshi.graphic.TamaFrame} <br>
 * Représente l'image de l'état du tamagoshi
 */
public class TamaJPanel extends JPanel {
	
	/**
	 * Image du tamagoshi
	 */
	private BufferedImage tamaImage;
	
	/**
	 * Construit le panel avec une image de tamagoshi heureux
	 */
	public TamaJPanel() {	
		this.setTamaImage("pikabase");
	}
	
	/**
	 * Change l'image d'un tamagoshi
	 * @param ressourceName Nouveau nom d'image (en png)
	 */
	private void setTamaImage(String ressourceName) {
		try {
			this.tamaImage = ImageIO.read(TamaJPanel.class.getResource("/tamagoshi/graphic/ressources/"+ressourceName+".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	/**
	 * Paint le composant, affiche donc l'image du tamagoshi
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(this.tamaImage, 20, 0, 250,250,this);
	}
	
	/**
	 * Change l'image du tamagoshi et actualise l'affichage
	 * @param ressourceName Nom de la nouvelle image (en png)
	 */
	public void changeImage(String ressourceName) {
		this.setTamaImage(ressourceName);
		this.repaint();
	}
	

}
