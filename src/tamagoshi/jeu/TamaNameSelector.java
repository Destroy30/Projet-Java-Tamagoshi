package tamagoshi.jeu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TamaNameSelector extends JFrame {
	
	private TamaGameGraphic tamaGame;
	private int limit;
	private int count=0;
	private String[] nameArray;
	private JLabel numberLabel;
	private JTextField nameInput;
	private JButton confirmButton;
	
	public TamaNameSelector(TamaGameGraphic tamaGame,int tamaNumbers) {
		this.tamaGame = tamaGame;
		
		this.limit = tamaNumbers;
		this.nameArray = new String[tamaNumbers];
		
		JPanel panelSelection = new JPanel(new GridLayout(3,1));
		
		JPanel panelLabel = new JPanel();
		JPanel panelInput = new JPanel();
		JPanel panelConfirm = new JPanel();
		
		this.numberLabel = new JLabel();
		setLabelNumberText(1);
		
		this.nameInput = new JTextField();
		this.nameInput.setColumns(20);
		this.nameInput.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					registerTamagoshi();
				}
			}
		});
		
		this.confirmButton = new JButton("Confirmer");
		this.confirmButton.addActionListener((e->{registerTamagoshi();}));
		
		
		panelLabel.add(this.numberLabel);
		panelSelection.add(panelLabel);
		
		panelInput.add(this.nameInput);
		panelSelection.add(panelInput);
		
		panelConfirm.add(this.confirmButton);
		panelSelection.add(panelConfirm);

		this.add(BorderLayout.CENTER,panelSelection);
		
		init();
	}
	
	public void init() {
		this.setTitle("Selection des noms des tamagoshis");
		this.setSize(new Dimension(300,175));
		this.setResizable(false);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	private void setLabelNumberText(int number) {
		this.numberLabel.setText("Nom du tamagoshi n°="+number);
	}
	
	private void registerTamagoshi() {
		String name = this.nameInput.getText().trim();
		if(!name.equals("")) {
			this.nameArray[this.count]=name;
			this.count++;
		}
		goNextRegister();
	}
	
	private void goNextRegister() {
		if(this.count<this.limit) {
			setLabelNumberText(count+1);
			this.nameInput.setText("");
		}
		else {
			endNameSelection();
		}
	}
	
	private void endNameSelection() {
		this.tamaGame.registerByNames(this.nameArray);
		this.dispose();
	}
	
}
