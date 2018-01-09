package tamagoshi.jeu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TamaNameSelector extends JFrame {
	
	private int limit;
	private String[] nameArray;
	private JLabel numberLabel;
	private JTextField nameInput;
	private JButton confirmButton;
	
	public TamaNameSelector(int tamaNumbers) {
		this.limit = tamaNumbers-1;
		this.nameArray = new String[this.limit];
		
		JPanel panelSelection = new JPanel(new GridLayout(3,1));
		
		this.numberLabel = new JLabel("Nom du tamagoshi n°=1");
		this.nameInput = new JTextField();
		this.confirmButton = new JButton("Confirmer");
		
		panelSelection.add(this.numberLabel);
		
		panelSelection.add(this.nameInput);
		
		panelSelection.add(this.confirmButton);

		this.add(BorderLayout.CENTER,panelSelection);
		
		init();
	}
	
	public void init() {
		this.setTitle("Selection des noms des tamagoshis");
		this.setSize(new Dimension(200,150));
		this.setResizable(false);
		this.setVisible(true);
	}
	
	public static void main(String[]args) {
		new TamaNameSelector(5);
	}
	
}
