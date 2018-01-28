package tamagoshi.jeu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tamagoshi.language.LanguageAccessor;
import tamagoshi.language.LanguageObserver;

/**
 * Ce composant va permettre de rentrer des noms de tamagoshis selon un nombre donné et de les enregistrer
 */
public class TamaNameSelector extends JFrame implements LanguageObserver{
	
	/**
	 * Pour envoyer l'information sur les noms à la fin
	 */
	private TamaGameGraphic tamaGame;
	
	/**
	 * Nombre de tamagoshi à enregistrer 
	 */
	private int limit;
	
	/**
	 * Nombre de tamagoshis actuellement enregistrés
	 */
	private int count=0;
	
	/**
	 * Liste de noms de tamagoshis enregistrés
	 */
	private String[] nameArray;
	
	/**
	 * Phrase présentant le numéro du tamagoshi à rentrer
	 */
	private JLabel numberLabel;
	
	/**
	 * Zone input pour rentrer un nom
	 */
	private JTextField nameInput;
	
	/**
	 * Pour confirmer le nom rentré
	 */
	private JButton confirmButton;
	
	/**
	 * Bundle de language pour les messages
	 */
	private ResourceBundle language;
	
	/**
	 * Construit et affiche le selectionneur de noms <br>
	 * A chaque confirmation, un nom sera enregistré et on passe au suivant
	 * @param tamaGame Vers qui envoyer l'informations sur les noms
	 * @param tamaNumbers Nombres de noms à saisir
	 */
	public TamaNameSelector(TamaGameGraphic tamaGame,int tamaNumbers) {
		this.tamaGame = tamaGame;
		
		this.limit = tamaNumbers;
		this.nameArray = new String[tamaNumbers];
		
		JPanel panelSelection = new JPanel(new GridLayout(3,1));
		
		JPanel panelLabel = new JPanel();
		JPanel panelInput = new JPanel();
		JPanel panelConfirm = new JPanel();
		
		this.numberLabel = new JLabel();
		
		this.nameInput = new JTextField();
		this.nameInput.setColumns(20);
		this.nameInput.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					registerTamagoshi();
				}
			}
		});
		
		this.confirmButton = new JButton();
		this.confirmButton.addActionListener((e->{registerTamagoshi();}));
		
		
		panelLabel.add(this.numberLabel);
		panelSelection.add(panelLabel);
		
		panelInput.add(this.nameInput);
		panelSelection.add(panelInput);
		
		panelConfirm.add(this.confirmButton);
		panelSelection.add(panelConfirm);

		this.add(BorderLayout.CENTER,panelSelection);
		
		//Language
		LanguageAccessor accessor = LanguageAccessor.getInstance();
		accessor.addObservator(this);
		
		setLabelNumberText(1);
		
		init();
	}
	
	/**
	 * Affiche la frame à l'écran
	 */
	private void init() {
		this.setSize(new Dimension(300,175));
		this.setResizable(false);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	/**
	 * Change la phrase d'input selon le numéro du tamagoshi
	 * @param number Numéro à afficher dans la phrase
	 */
	private void setLabelNumberText(int number) {
		this.numberLabel.setText(language.getString("numberOfTamagoshi")+number);
	}
	
	/**
	 * Enregistre le nom du tamagoshi contenu dans l'input <br>
	 * Passe à l'enregistrement suivant
	 */
	private void registerTamagoshi() {
		String name = this.nameInput.getText().trim();
		if(!name.equals("")) {
			this.nameArray[this.count]=name;
			this.count++;
		}
		goNextRegister();
	}
	
	/**
	 * Permet de demander l'enregistrer suivant <br>
	 * Met à jour la phrase du label et vide l'input <br>
	 * Si tous les noms requis sont enregistrés, déclenche l'envoi des noms au jeu
	 */
	private void goNextRegister() {
		if(this.count<this.limit) {
			setLabelNumberText(count+1);
			this.nameInput.setText("");
		}
		else {
			endNameSelection();
		}
	}
	
	/**
	 * Permet de terminer l'étape de selection des noms en les envoyant au gestionnaire du jeu <br>
	 * Ferme la fenêtre de selection
	 */
	private void endNameSelection() {
		this.tamaGame.registerByNames(this.nameArray);
		this.dispose();
	}

	@Override
	/**
	 * Permet de mettre à jour les phrases et titres au changement de langue
	 */
	public void languageUpdate(LanguageAccessor languageAcc) {
		this.language = languageAcc.getBundle("Home");
		this.setTitle(language.getString("selectionWindow"));
		this.confirmButton.setText(language.getString("confirm"));
	}
	
}
