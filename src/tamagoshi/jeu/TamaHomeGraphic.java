package tamagoshi.jeu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import tamagoshi.audio.AudioPlayer;
import tamagoshi.jeu.actions.ActionExit;
import tamagoshi.jeu.actions.ActionHelp;
import tamagoshi.jeu.actions.ActionPlay;
import tamagoshi.jeu.actions.ActionScore;
import tamagoshi.language.LanguageAccessor;
import tamagoshi.language.LanguageObserver;
import tamagoshi.properties.TamaConfiguration;

/**
 * Cette classe correspond à l'interface d'accueil du jeu <br>
 * Elle va donc être le point d'entrée de toutes les actions <br>
 * Elle va également permettre de configurer/paramétrer le jeu
 */
public class TamaHomeGraphic extends JFrame implements LanguageObserver{
	
	/**
	 * Actions jouer, quitter et score : partagées entre les menus et les boutons
	 */
	private AbstractAction actionPlay,actionExit,actionScore;
	
	/**
	 * Permet de fixer la difficulté
	 */
	private JSpinner spinDifficulty;
	
	/**
	 * Permet de savoir si la difficulté doit être fixée ou non
	 */
	private boolean difficultyFixed;
	
	/**
	 * Permet de savoir si les noms sont auto-générés ou non
	 */
	private boolean autoGeneratedNames;
	
	/**
	 * Objet de configuration pour accèder aux différents paramètres du jeu
	 */
	private TamaConfiguration config;
	
	/**
	 * Ressources de langue pour afficher les messages dans la langue selectionnée
	 */
	private ResourceBundle language;
	
	/**
	 * Menu fichier
	 */
	private JMenu menuFile;
	
	/**
	 * Menu configuration
	 */
	private JMenu menuConfig;
	
	/**
	 * Option pour choisir les noms au lancement
	 */
	private JRadioButtonMenuItem namesToDefineRadio;
	
	/**
	 * Option pour fixer la difficulté
	 */
	private JRadioButtonMenuItem difficultyFixedRadio;
	
	/**
	 * Option pour définir la difficulté au lancement
	 */
	private JRadioButtonMenuItem difficultyFree;
	
	/**
	 * Menu informations
	 */
	private JMenu menuInformations;
	
	/**
	 * Pour voir les règles du jeu
	 */
	private JMenuItem itemGameInfos;
	
	/**
	 * Pour lancer la fonction d'affichage des scores
	 */
	private JMenuItem itemScore;
	
	/**
	 * Pour afficher les informations sur le créateur du jeu
	 */
	private JMenuItem itemAboutTeam;
	
	/**
	 * Label placé devant le spinner de difficulté
	 */
	private JLabel labelDifficulty;
	
	/**
	 * Option pour générer les noms automatiquement
	 */
	private JRadioButtonMenuItem namesAutoRadio;
	
	/**
	 * Constructeur, va initialiser les actions, placer les menus, définir la toolbar, placer les boutons <br>
	 * A la fermeture, demande la sauvegarde de la configuration actuelle <br>
	 * Lance une musique de fond "fileCityDay"
	 */
	public TamaHomeGraphic() {
		this.config = TamaConfiguration.getInstance();
		
		this.autoGeneratedNames = config.getAutoGeneratedNamesSelection();
		this.difficultyFixed = config.getDifficultyFixed();
		
		setUpActions();
		
		setUpMenus();
		
		setUpToolBar();
		
		//Image Tamagoshi
		add(new TamaHomeImage(),BorderLayout.CENTER);
		
		setUpButtons();
		
		//Opération de fermeture
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				config.storeProperties();
				System.exit(1);
			}
		});
		
		//Language
		LanguageAccessor accessor = LanguageAccessor.getInstance();
		accessor.addObservator(this);
		
		//Audio
		AudioPlayer audio = AudioPlayer.getInstance();
		audio.playBackgroundMusic("fileCityDay");
		
	}

	/**
	 * Crée et place les boutons au sud de la fenêtre <br>
	 * Ces boutons permettent de lancer une partie, quitter le jeu et voir les scores
	 */
	private void setUpButtons() {
		JPanel butonsPanel = new JPanel(new FlowLayout());
		
		JButton buttonPlay = new JButton(actionPlay);
		JButton buttonScore = new JButton(actionScore);
		JButton buttonExit = new JButton(actionExit);
		
		butonsPanel.add(buttonPlay);
		butonsPanel.add(buttonScore);
		butonsPanel.add(buttonExit);
		
		add(butonsPanel,BorderLayout.SOUTH);
	}


	/**
	 * Crée la toolbar <br>
	 * Cette toolbar contient un selecteur de difficulté
	 */
	private void setUpToolBar() {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		
		JPanel panelDifficulty = new JPanel(new FlowLayout());
		
		labelDifficulty = new JLabel();
		panelDifficulty.add(labelDifficulty);
		
		SpinnerModel modelDifficulty = new SpinnerNumberModel(1,1,10,1);
		spinDifficulty = new JSpinner(modelDifficulty);
		spinDifficulty.setValue(config.getDifficulty());
		spinDifficulty.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				config.setDifficulty((int) spinDifficulty.getValue());
			}	
		});
		
		//Desactivation edition par clavier sur le spinner
		JFormattedTextField tf = ((JSpinner.DefaultEditor) spinDifficulty.getEditor()).getTextField();
		tf.setEditable(false);
		
		panelDifficulty.add(spinDifficulty);
		
		toolBar.add(panelDifficulty);
		
		if(!this.difficultyFixed) {
			this.setDifficultyFixed(false);
		}
		
		add(toolBar,BorderLayout.PAGE_START);
	}


	/**
	 * Crée et place les différents menus
	 */
	private void setUpMenus() {
		JMenuBar menuBar = new JMenuBar();
		
		setUpMenuFile();
		
		setUpMenuConfig();
		
		setUpMenuInfos();
		
		TamagoshiLanguageMenu menuLanguage = new TamagoshiLanguageMenu();
		menuLanguage.setMnemonic(KeyEvent.VK_L);
		
		menuBar.add(menuFile);
		menuBar.add(menuConfig);
		menuBar.add(menuInformations);
		menuBar.add(menuLanguage);
		
		setJMenuBar(menuBar);
	}

	
	/**
	 * Crée le menu "Informations" <br>
	 * Ce menu contiendra les items pour accèder aux règles du jeu, aux informations sur le créateur et aux scores
	 */
	private void setUpMenuInfos() {
		menuInformations = new JMenu();
		menuInformations.setMnemonic(KeyEvent.VK_I);
		
		itemGameInfos = new JMenuItem(new ActionHelp());
		
		itemScore = new JMenuItem(actionScore);
		
		itemAboutTeam = new JMenuItem();
		itemAboutTeam.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,InputEvent.CTRL_MASK));
		itemAboutTeam.setMnemonic(KeyEvent.VK_I);
		itemAboutTeam.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				TamaAbout aboutFrame = new TamaAbout();
				aboutFrame.setLocationRelativeTo(null);
				aboutFrame.setSize(500,130);
				aboutFrame.setVisible(true);
			}});
		
		menuInformations.add(itemGameInfos);
		menuInformations.add(itemScore);
		menuInformations.add(itemAboutTeam);
	}

	/**
	 * Crée le menu de configuration <br>
	 * Dedans, il y a deux groupes radios <br>
	 * Le premier sert à chosiir la génération automatique des noms <br>
	 * Le second sert à choisir si la difficulté est fixée ou non
	 */
	private void setUpMenuConfig() {
		menuConfig = new JMenu();
		menuConfig.setMnemonic(KeyEvent.VK_C);
		
		ButtonGroup groupConfigName = new ButtonGroup();
		
		namesAutoRadio = new JRadioButtonMenuItem();
		namesAutoRadio.setSelected(this.namesAreAutoGenerated());
		namesAutoRadio.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				changeNameSelectionMethod(true);
			}
		});
		
		namesToDefineRadio = new JRadioButtonMenuItem();
		namesToDefineRadio.setSelected(!this.autoGeneratedNames);
		namesToDefineRadio.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				changeNameSelectionMethod(false);
			}
		});
		
		groupConfigName.add(namesAutoRadio);
		groupConfigName.add(namesToDefineRadio);
		
		menuConfig.add(namesAutoRadio);
		menuConfig.add(namesToDefineRadio);
		
		menuConfig.addSeparator();
		
		ButtonGroup groupConfigDifficulty = new ButtonGroup();
		
		difficultyFixedRadio = new JRadioButtonMenuItem();
		difficultyFixedRadio.setSelected(this.difficultyFixed);
		difficultyFixedRadio.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setDifficultyFixed(true);
			}
		});
		
		difficultyFree = new JRadioButtonMenuItem();
		difficultyFree.setSelected(!this.difficultyFixed);
		difficultyFree.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setDifficultyFixed(false);
			}
		});
		
		groupConfigDifficulty.add(difficultyFixedRadio);
		groupConfigDifficulty.add(difficultyFree);
		
		menuConfig.add(difficultyFixedRadio);
		menuConfig.add(difficultyFree);
	}

	/**
	 * Crée le menu "Fichier" <br>
	 * Ce menu contient les actions pour lancer une partie et quitter le jeu
	 */
	private void setUpMenuFile() {
		menuFile = new JMenu();
		menuFile.setMnemonic(KeyEvent.VK_F);
		
		JMenuItem itemPlay = new JMenuItem(actionPlay);
		JMenuItem itemExit = new JMenuItem(actionExit);

		menuFile.add(itemPlay);
		menuFile.add(itemExit);
	}

	/**
	 * Permet de créer les actions partagées entre les menus et les boutons
	 */
	private void setUpActions() {
		this.actionPlay = new ActionPlay(this);
		this.actionExit = new ActionExit();
		this.actionScore = new ActionScore();
	}
	
	/**
	 * Permet d'activer ou désactiver la difficulté fixée, et sauvegarde le choix dans la configuration
	 * @param difficultyFixed
	 */
	private void setDifficultyFixed(boolean difficultyFixed) {
		this.difficultyFixed = true;
		this.spinDifficulty.setEnabled(true);
		config.setDifficultyFixed(true);
	}
	
	/**
	 * Permet d'activer ou désactiver la génération autamatique des noms, et sauevegarde le choix dans la configuration
	 * @param autoGenerated
	 */
	private void changeNameSelectionMethod(boolean autoGenerated) {
		this.autoGeneratedNames = autoGenerated;
		config.setAutoGeneratedNamesSelection(autoGenerated);
	}
	
	/**
	 * Permet de savoir si les noms doivent êtres auto-générés <br>
	 * @return true si les noms sont auto-générés, false sinon
	 */
	public boolean namesAreAutoGenerated() {
		return this.autoGeneratedNames;
	}
	
	/**
	 * Permet de savoir si la difficulté est fixée avant le démarrage <br>
	 * @return true si la difficulté est fixée avant le démarrage, false sinon
	 */
	public boolean difficultyIsFixed() {
		return this.difficultyFixed;
	}
	
	/**
	 * Permet de récupérer le niveau de difficulté <br>
	 * @return Le niveau de difficulté (entre 1 et 10)
	 */
	public int getDifficulty() {
		return (int) this.spinDifficulty.getValue();
	}
	
	@Override
	/**
	 * Méthode implémenté de l'interface {@link LanguageObserver}  <br>
	 * Permet de mettre à jour les menus,boutons, les messages qui seront affichés, le bundle...
	 */
	public void languageUpdate(LanguageAccessor languageAcc) {
		this.language = languageAcc.getBundle("Home");
		this.menuFile.setText(language.getString("file"));
		this.menuConfig.setText(language.getString("config"));
		this.namesAutoRadio.setText(language.getString("autoGeneration"));
		this.namesToDefineRadio.setText(language.getString("chooseNames"));
		this.difficultyFixedRadio.setText(language.getString("fixedDifficulty"));
		this.difficultyFree.setText(language.getString("notFixedDifficulty"));
		this.menuInformations.setText(language.getString("infos"));
		this.itemAboutTeam.setText(language.getString("about"));
		this.setTitle(language.getString("tamaGame"));
		this.labelDifficulty.setText(language.getString("setUpDifficulty"));
	}
	
	public static void main(String[]args) {
		TamaHomeGraphic home = new TamaHomeGraphic();
		home.setSize(400, 400);
		home.setLocationRelativeTo(null);
		home.setResizable(false);
		home.setVisible(true);
	}

}
