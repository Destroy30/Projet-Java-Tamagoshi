package tamagoshi.jeu;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.ResourceBundle;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import tamagoshi.audio.AudioPlayer;
import tamagoshi.graphic.TamaFrame;
import tamagoshi.jeu.actions.ActionExit;
import tamagoshi.jeu.actions.ActionHelp;
import tamagoshi.language.LanguageAccessor;
import tamagoshi.language.LanguageObserver;
import tamagoshi.properties.TamaConfiguration;
import tamagoshi.tamagoshis.GrosJoueur;
import tamagoshi.tamagoshis.GrosMangeur;
import tamagoshi.tamagoshis.Tamagoshi;

/**
 * Cette classe va permettre de gérer une partie de jeu <br>
 * Son affichage va être une console graphique affichant divers messages sur l'état de la partie
 */

public class TamaGameGraphic extends JFrame implements LanguageObserver {
	
	/**
	 * Liste contenant tous les tamagoshis (morts ou vivants), très utile pour le calcul du score en fin de partie
	 */
	private ArrayList<Tamagoshi> tamsStart;
	
	/**
	 * Liste contenant tous les tamagoshis courants (toujours vivants) utile lors de la gestion d'un cycle
	 */
	private ArrayList<Tamagoshi> tamsActual;
	
	/**
	 * Liste contenant toutes les frames de tamagoshis, utile pour gérer l'affichage des boutons
	 */
	private ArrayList<TamaFrame> framesTamagoshis;
	
	/**
	 * Nombres de tamagoshis gérés dans la aprtie (défini par l'utilisateur...)
	 */
	private int nbTams;
	
	/**
	 * Liste contenant des noms de Tamagoshis pré-définis, elle permet de choisir un nom aléatoirement si besoin
	 */
	
	private JTextArea consolePane;
	
	/**
	 * Informe sur le fait que l'action "Jouer" a été réalisée ou non pour le cycle en cours
	 */
	private boolean actionPlay;
	
	/**
	 * Informe sur le fait que l'action "Manger" a été réalisée ou non pour le cycle en cours
	 */
	private boolean actionEat;
	
	/**
	 * Numéro de cycle
	 */
	private int numCycle = 0;
	
	/**
	 * Permet de savoir si la génération des noms automatique doit être réalisée ou non
	 */
	private boolean autoGeneratedNames=false;
	
	/**
	 * Permet de savoir si la difficulté doit être paramétrée par l'utilisateur au lancement ou non
	 */
	private boolean difficultyFixed=false;;
	
	/**
	 * Permet d'accèder aux variables de paramètrages
	 */
	private TamaConfiguration config;
	
	/**
	 * Bundle de langue pour afficher les messages dans la bonne langue
	 */
	private ResourceBundle language;

	/**
	 * Menu "Fichier" pour accèder à diverses actions (comme quitter)
	 */
	private JMenu menuFile;

	/**
	 * Lecteur audio permettant de jouer des sons/des musiques de background
	 */
	private AudioPlayer audio;

	/**
	 * Menu d'informations (contenant l'item pour accèder aux règles du jeu)
	 */
	private JMenu menuInfos;
	
	/**
	 * Constructeur de la classe TamaGame <br>
	 * Initialise les trois listes (vides) <br>
	 * Initialise les menus <br>
	 * Ajoute la frame à la liste des observateurs de langage <br>
	 * Lance la musique de fond "fileCityNight" <br>
	 * Récupère la configuration <br>
	 * Sa visibilité est privée car il n'a pour but d'être appellé seulement par les autres constructeurs
	 */
	
	private TamaGameGraphic() {
		this.consolePane = new JTextArea();
		this.consolePane.setEditable(false);
		JScrollPane scrollConsole = new JScrollPane(this.consolePane);
		this.add(scrollConsole);
		
		this.tamsStart= new ArrayList<Tamagoshi>();
		this.tamsActual= new ArrayList<Tamagoshi>();
		this.framesTamagoshis = new ArrayList<TamaFrame>();

		this.config = TamaConfiguration.getInstance();
		
		//Menu (essentiellement pour changer de langue pendant le jeu, ou avoir des infos)
		JMenuBar menuBar = new JMenuBar();
		
		menuFile = new JMenu();
		menuFile.add(new ActionExit());
		
		menuInfos = new JMenu();
		menuInfos.add(new ActionHelp());
		
		TamagoshiLanguageMenu menuLanguage = new TamagoshiLanguageMenu();
		
		menuBar.add(menuFile);
		menuBar.add(menuLanguage);
		menuBar.add(menuInfos);
		
		setJMenuBar(menuBar);
		
		//Language
		LanguageAccessor accessor = LanguageAccessor.getInstance();
		accessor.addObservator(this);
		
		//Opération de fermeture (essentiellement pour sauvegarder la langue si elle a été changée)
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				config.storeProperties();
				System.exit(1);
			}
		});
		
		//Audio
		this.audio = AudioPlayer.getInstance();
		audio.playBackgroundMusic("fileCityNight");
	}
	
	/**
	 * Constructeur qui précise si les noms sont auto-générés ou non <br>
	 * @param autoGeneratedNames Précise si les noms sont auto-égénérés <br>
	 */
	public TamaGameGraphic(boolean autoGeneratedNames) {
		this();
		this.autoGeneratedNames = autoGeneratedNames;
	}
	
	/**
	 * Constructeur qui précise si les noms sont auto-générés ou non et le niveau de difficulté <br>
	 * @param autoGeneratedNames Précise si les noms sont auto-égénérés <br>
	 * @param nbTams Niveau de difficulté = Nombre de tamagoshis
	 */
	public TamaGameGraphic(boolean autoGeneratedNames,int nbTams) {
		this(autoGeneratedNames);
		this.difficultyFixed = true;
		this.nbTams = nbTams;
	}
	
	/**
	 * Cette méthode permet d'initialiser la partie <br>
	 * Elle demande à l'utilisateur combien de tamagoshis il souhaite gérer si la difficulté n'est pas fixéee<br>
	 * Elle demande également le nom des tamagoshis si ceux-cii ne sont pas auto-générés
	 */
	
	public void initialisation() {
		if(!this.difficultyFixed) {
			boolean register;
			do {
				register = regsiterNumberOfTamagoshis();
			}while(!register);
		}
		if(!this.autoGeneratedNames) {
			launchRegisterNames();
		}
		else {
			launchRegisterRandom();
		}
	}
	
	/**
	 * Cette méthode permet d'enregistrer le nombre de tamagoshi (donc la difficulté) de la partie <br>
	 * Elle va utiliser un {@link JOptionPane}} avec un {@link JSpinner} pour cela <br>
	 * @return true si le nombre a pu être fixé, false sinon
	 */
	private boolean regsiterNumberOfTamagoshis() {
		SpinnerModel model = new SpinnerNumberModel(1,1,10,1);
		JSpinner spin = new JSpinner(model);
		JFormattedTextField tf = ((JSpinner.DefaultEditor) spin.getEditor()).getTextField();
		tf.setEditable(false);
		
		int result = JOptionPane.showOptionDialog(this, spin, language.getString("enterNumberOfTamas"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if(result == JOptionPane.OK_OPTION) {
			this.nbTams=(int) spin.getValue();
			return true;
		}
		else {
			JOptionPane.showMessageDialog(this, language.getString("haveTospecifyNumber"),language.getString("error"),JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	
	/**
	 * Remplit la liste de tamagoshis avec des noms aléatoires
	 */
	private void launchRegisterRandom() {
		String[]names = config.getAutoGeneratedNames();
		Random rand = new Random();
		for(int i=0;i<this.nbTams;i++) {
			int choix = rand.nextInt(names.length);
			enregistrerTamagoshi(names[choix]);
		}
		nextTour();
	}
	
	
	/**
	 * Cette méthode initialise enregsitre un tamagoshis dans {@link TamaGamGraphice#tamsStart}} et {@link TamaGamGraphice#tamsActual}} <br>
	 * Le {@link Tamagoshi} à 50% de chances d'être un {@link GrosMangeur} et donc 50% de chances d'être un {@link GrosJoueur} <br>
	 * Une tamaFrame est également crée et ajoutée à la liste {@link TamaGamGraphice#framesTamagoshis} <br>
	 * @param tamName Nom du tamagoshi à enregistrer, il sert dans le constructeur de {@link Tamagoshi}
	 */
	
	private void enregistrerTamagoshi(String tamName) {
		Random rand = new Random();		
		Tamagoshi tamAdd = (rand.nextInt(2)==0)? new GrosMangeur(tamName): new GrosJoueur(tamName);
		this.tamsStart.add(tamAdd);
		this.tamsActual.add(tamAdd);
		
		TamaFrame tamaFrameToAdd = createTamaFrame(tamAdd);
		this.framesTamagoshis.add(tamaFrameToAdd);
	}

	/**
	 * Pour tout les tamagoshis contenus dans {@link TamaGamGraphice#tamsActual} déclenche la méthode {@link Tamagoshi#parle()} <br>
	 * Cela a pour effet d'afficher un par un l'état des tamagoshis toujours vivants
	 */
	
	private void allParler() {
		for(Tamagoshi tam : this.tamsActual) {
			tam.parle();
		}
	}
	
	/**
	 * Cette méthode permet de réaliser les action de fin de tour pour chaque tamagoshis <br>
	 * Elle appelle notament les méthodes {@link Tamagoshi#incrementerAge()} et {@link Tamagoshi#consomme()} <br>
	 * Les tamgoshis encore vivants vieilissent et consomme de l'enrgy et du fun. <br>
	 * Si la methode consomme renvoie "false" alors le tamagoshi est considéré comme mort et retiré de {@link TamaGame#tamsActual}
	 */
	
	private void endTour() {
		Iterator<Tamagoshi> it = this.tamsActual.iterator();
		while(it.hasNext()) {
			Tamagoshi tam = it.next();
			tam.incrementerAge();
			if(!tam.consomme()) {
				it.remove();
			}
		}
		nextTour();
	}
	
	/**
	 * Lance le prochain tour (avec {@link TamaGameGraphic#tour()}) si le nobmre de cycle maximum (10) n'est pas atteint, sinon, elle appelle la méthode pour faire le {@link TamaGameGraphic#bilan()}
	 */
	private void nextTour() {
		if(!(this.tamsActual.isEmpty() || numCycle == Tamagoshi.getLifeTime())) {
			tour();
		}
		else {
			bilan();
		}
	}

	/**
	 * Cette méthode va permettre de calculer la somme des ages des tamaghis dans {@link TamaGameGraphic#tamsStart}, utile pour le score <br>
	 * Chaque Tamagoshi affiche son état final (état ou mort) avec {@link Tamagoshi#finalState()} <br>
	 * @return Le score final en pourcentage, en faisant appel à {@link TamaGameGraphic#finalScoreFromSommeAge(int)} 
	 */
	
	private double calculBilanAndScore() {
		int sommeAge=0;
		for(Tamagoshi tam : this.tamsStart) {
			displayFinalStateOfTamaGochi(tam);
			sommeAge+=tam.getAge();
		}
		return finalScoreFromSommeAge(sommeAge);
	}
	
	/**
	 * Permet d'afficher dans la console graphique l'état final du tamagoshi (mort ou vivant) à la fin de la partie <br>
	 * @param tam
	 */
	private void displayFinalStateOfTamaGochi(Tamagoshi tam) {
		if(tam.isDead()) {
			appendInConsole(tam+" "+language.getString("whichWas")+" "+tam.getClass().getSimpleName()+" "+language.getString("isDead"));
		}
		else {
			appendInConsole(tam+" "+language.getString("survive"));
		}
		appendInConsole("");
	}
	
	/**
	 * Cette méthode fait le calcul du socre en pourcentage à partir de la somme des âges des tamaghis dans {@link TamaGameGraphic#tamsStart} <br>
	 * @param sommeAge Somme des ages des tamagshis passés par {@link TamaGameGraphic#calculBilanAndScore()} <br>
	 * @return Le score final en pourcentage arrondi au nombre le plus près
	 */
	
	private double finalScoreFromSommeAge(int sommeAge) {
		double score=sommeAge;
		score/=(Tamagoshi.getLifeTime()*this.tamsStart.size());
		score*=100;
		return Math.round(score);
	}
	
	/**
	 * Cette méthode va afficher le bilan de la partie <br>
	 * Elle récupère le score grâce à {@link TamaGameGraphic#calculBilanAndScore()} <br>
	 * En cas de nouveau record, un son est joué et le score est enregistré via la classe de configuration
	 */
	
	private void bilan() {
		appendInConsole("");
		appendInConsole(language.getString("bilan"));
		appendInConsole("");
		double score=calculBilanAndScore();
		appendInConsole("");
		appendInConsole(language.getString("difficulty")+" : "+this.nbTams);
		appendInConsole(language.getString("scoreObtain")+" : "+score+"%");
		boolean newHighScore = this.config.saveScore(this.nbTams, score);
		if(newHighScore) {
			this.config.storeProperties();
			this.audio.playSound("newRecord");
			appendInConsole(language.getString("brandNewRecord"));
		}
	}
	
	/**
	 * Cette méthode permet d'informer la classe qu'un tamagoshi a mangé <br>
	 * La classe va donc désactiver tous les boutons "manger" des frames et verifier si le tour est fini
	 */
	
	public void tamagoshiEat() {
		disableAllFeedButtons();
		this.actionEat=true;
		checkEndTour();
	}
	
	/**
	 * Verifie si le tour est fini, si c'est  le cas, lance {@link TamaGameGraphic#endTour()}
	 */
	private void checkEndTour() {
		if(this.actionEat && this.actionPlay) {
			endTour();
		}
	}

	/**
	 * Cette méthode permet d'informer la classe qu'un tamagoshi a joué <br>
	 * La classe va donc désactiver tous les boutons "jouer" des frames et verifier si le tour est fini
	 */
	
	public void tamagoshiPlay() {
		disableAllPlayButtons();
		this.actionPlay=true;
		checkEndTour();
	}
	
	
	/**
	 * Permet de lancer un tour <br>
	 * Passe {@link TamaGameGraphic#actionEat} et {@link TamaGameGraphic#actionPlay} à false <br>
	 * Reactive les boutons sur les frames <br>
	 * Demande l'execution du prochain cycle
	 */
	
	private void tour() {
		this.actionEat=false;
		this.actionPlay=false;
		renableButtonsOnFrames();
		cycle(this.numCycle);
		this.numCycle++;
	}
	
	/**
	 * Affiche le nuveau cycle et fait parler tous les tamagoshis vivants via {@link TamaGameGraphic#allParler()} <br>
	 * @param numCycle Numéro du cycle à éxécuté
	 */
	
	private void cycle(int numCycle) {
		appendInConsole(language.getString("cycleNumber")+(numCycle+1)+" "+language.getString("inProgress"));
		allParler();
	}
	
	/**
	 * Permet d'afficher un message dans la console graphique
	 * @param text : Message à afficher
	 */
	private void appendInConsole(String text) {
		this.consolePane.append(text+"\n");
	}
	
	/**
	 * Lance l'interface d'enregistrement des noms via {@link TamaNameSelector}
	 */
	private void launchRegisterNames() {
		new TamaNameSelector(this,this.nbTams);
	}
	
	/**
	 * Enregistre une liste de tamagoshis <br>
	 * Cette méthode est appellée lors de la confirmation de {@link TamaNameSelector} <br>
	 * @param tamaNames : Noms des tamagoshis
	 */
	public void registerByNames(String[]tamaNames) {
		for(String str : tamaNames) {
			enregistrerTamagoshi(str);
		}
		nextTour();
	}
	
	/**
	 * Désactive tous les boutons "jouer" sur chaque frame
	 */
	private void disableAllPlayButtons() {
		for(TamaFrame tamFrame : this.framesTamagoshis) {
			tamFrame.disableButtonPlay();
		}
	}
	
	/**
	 * Désactive tous les boutons "nourrir" sur chaque frame
	 */
	private void disableAllFeedButtons() {
		for(TamaFrame tamFrame : this.framesTamagoshis) {
			tamFrame.disableButtonFeed();
		}
	}
	
	/**
	 * Réactive tous les boutons sur les frames
	 */
	private void renableButtonsOnFrames() {
		for(TamaFrame tamFrame : this.framesTamagoshis) {
			tamFrame.renableButtons();
		}
	}
	
	/**
	 * Permet de créer et afficher une tamaframe 
	 * @param tamAdd Tamagoshi lié à la frame
	 * @return une nouvelle frame liée au tamagoshi passé en paramètres
	 */
	private TamaFrame createTamaFrame(Tamagoshi tamAdd) {
		TamaFrame newTamaFrame = new TamaFrame(this,tamAdd);
		newTamaFrame.setSize(300,400);
		newTamaFrame.setTitle(tamAdd.toString());
		newTamaFrame.setResizable(false);
		newTamaFrame.setVisible(true);
		newTamaFrame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		placeTamaFrame(newTamaFrame);
		return newTamaFrame;
	}
	
	/**
	 * Permet de placer sur l'écran une tamaFrame. <br>
	 * L'algorithme tente de trouver la position adéquate selon les données de l'écran et le nombre de tamagoshis actuellment affichés <br>
	 * Le but de la méthode est de placer les tamagoshis côte à côte, sans superposition et de passer à lla ligne si nécessaire
	 * @param frame : Frame à placer
	 */
	private void placeTamaFrame(TamaFrame frame) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double widthScreen = screenSize.getWidth();
		int numberOfTamas = this.framesTamagoshis.size();
		
		int numberOfFramesAllowedByLine = (int) widthScreen/310; //300 + 10 d'espace
		int lineNumber = (numberOfTamas>0) ? (((numberOfTamas)/numberOfFramesAllowedByLine)) : 0;
		int columnNumber = ((numberOfTamas)%numberOfFramesAllowedByLine);
		
		int nextPositionX = 300 * columnNumber + 10 * columnNumber;
		int nextPositionY = 400 * lineNumber + 10 * lineNumber;
		
		frame.setLocation(nextPositionX,nextPositionY);
	}

	@Override
	/**
	 * Méthode implémenté de l'interface {@link LanguageObserver} <br>
	 * Permet de mettre à jour les boutons, les messages qui seront affichés, le bundle... de la console graphique
	 */
	public void languageUpdate(LanguageAccessor languageAcc) {
		this.language = languageAcc.getBundle("Game");
		this.setTitle(language.getString("tamaGame"));
		this.menuFile.setText(this.language.getString("file"));
		this.menuInfos.setText(this.language.getString("help"));
	}
	
}
