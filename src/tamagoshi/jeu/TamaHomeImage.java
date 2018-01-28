package tamagoshi.jeu;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Composant présentant une image de tamagoshi
 */
public class TamaHomeImage extends JPanel {
	
	@Override
	/**
	 * Paint le composant en affichant l'image d'un tamagoshi
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		try {
			BufferedImage tamaImage;
			tamaImage = ImageIO.read(this.getClass().getResource("/tamagoshi/graphic/ressources/tamaHomeImage.png"));
			g.drawImage(tamaImage, 70, 0, 250,250,this);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
}
