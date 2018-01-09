package tamagoshi.jeu;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import tamagoshi.exceptions.TamaNotExist;
import tamagoshi.exceptions.TamaWrongCountException;
import tamagoshi.graphic.TamaFrame;
import tamagoshi.tamagoshis.GrosJoueur;
import tamagoshi.tamagoshis.GrosMangeur;
import tamagoshi.tamagoshis.Tamagoshi;
import tamagoshi.util.Utilisateur;

public class TamaGameGraphic extends JFrame{
	
	/**
	 * Liste contenant tous les tamagoshis (morts ou vivants), très utile pour le calcul du score en fin de partie
	 */
	private ArrayList<Tamagoshi> tamsStart;
	
	/**
	 * Liste contenant tous les tamagoshis courants (toujours vivants) utile lors de la gestion d'un cycle
	 */
	private ArrayList<Tamagoshi> tamsActual;
	
	private ArrayList<TamaFrame> framesTamagoshis;
	
	/**
	 * Nombres de tamagoshis gérés dans la aprtie (défini par l'utilisateur...)
	 */
	private int nbTams;
	
	/**
	 * Liste contenant des noms de Tamagoshis pré-définis, elle permet de choisir un nom aléatoirement si besoin
	 */
	
	private static final String[]preDefNames={"Laulau","Guichaf","Erwan","Nico","Théophile","Malo","Xavier","Bella","Gouachi","Valentin","Toto","Fifi","Mario","Luigi","Nini"};
	
	private JTextArea consolePane;
	
	private boolean actionPlay;
	private boolean actionEat;
	
	private int numCycle = 0;
	
	/**
	 * Constructeur de la classe TamaGame <br>
	 * Initialise les deux listes (vides)
	 */
	
	public TamaGameGraphic() {
		this.consolePane = new JTextArea();
		this.consolePane.setEditable(false);
		JScrollPane scrollConsole = new JScrollPane(this.consolePane);
		this.add(scrollConsole);
		
		this.tamsStart= new ArrayList<Tamagoshi>();
		this.tamsActual= new ArrayList<Tamagoshi>();
		this.framesTamagoshis = new ArrayList<TamaFrame>();
	}
	
	/**
	 * Cette méthode permet d'initialiser la partie <br>
	 * Elle demande à l'utilisateur combien de tamagoshis il souhaite gérer <br>
	 * Elle permet à l'utilisateur de saisir un nombre de tamagoshis. <br>
	 * Elle utilise la classe {@link Utilisateur} pour cela  <br>
	 * Elle gère le cas où le nombre renté est invalide en catchant l'expetion {@link TamaWrongCountException} levée par {@link TamaGame#initTamagoshisByNumber()}
	 */
	
	private void initialisation() {
		boolean register;
		do {
			register = regsiterNumberOfTamagoshis();
		}while(!register);
		launchRegisterNames();
	}
	
	private boolean regsiterNumberOfTamagoshis() {
		SpinnerModel model = new SpinnerNumberModel(1,1,10,1);
		JSpinner spin = new JSpinner(model);
		int result = JOptionPane.showOptionDialog(this, spin, "Entez le nombre de tamagoshis", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if(result == JOptionPane.OK_OPTION) {
			this.nbTams=(int) spin.getValue();
			return true;
		}
		else {
			JOptionPane.showMessageDialog(this, "Vous devez spécifier un nombre de tamagoshis! (de 1 à 10)","Erreur",JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	
	
	/**
	 * Choisis un nom aleatoirement dans {@link TamaGame#preDefNames}
	 * @return Le nom selectionné aletoirement
	 */
	
	private String enregistrementAlea() {
		int choix = (new Random()).nextInt(this.preDefNames.length);
		enregistrerTamagoshi(preDefNames[choix]);
		return preDefNames[choix];
	}
	
	
	/**
	 * Cette méthode initialise enregsitre un tamagoshis dans {@link TamaGame#tamsStart}} et {@link TamaGame#tamsActual}} <br>
	 * Le {@link Tamagoshi} à 50% de chances d'être un {@link GrosMangeur} et donc 50% de chances d'être un {@link GrosJoueur}
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
	 * Pour tout les tamagoshis contenus dans {@link TamaGame#tamsActual} déclenche la méthode {@link Tamagoshi#parle()} <br>
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
	
	
	private void nextTour() {
		if(!(this.tamsActual.isEmpty() || numCycle == Tamagoshi.getLifeTime())) {
			tour();
		}
		else {
			bilan();
		}
	}

	/**
	 * Cette méthode va permettre de calculer la somme des ages des tamaghis dans {@link TamaGame#tamsStart}, utile pour le score <br>
	 * Chaque Tamagoshi affiche son état final (état ou mort) avec {@link Tamagoshi#finalState()}
	 * @return Le score final en pourcentage, en faisant appel à {@link TamaGame#finalScoreFromSommeAge(int)} 
	 */
	
	private double calculBilanAndScore() {
		int sommeAge=0;
		for(Tamagoshi tam : this.tamsStart) {
			displayFinalStateOfTamaGochi(tam);
			sommeAge+=tam.getAge();
		}
		return finalScoreFromSommeAge(sommeAge);
	}
	
	private void displayFinalStateOfTamaGochi(Tamagoshi tam) {
		if(tam.isDead()) {
			appendInConsole(tam+" qui était un "+tam.getClass().getSimpleName()+" est malheureusement mort! :(");
		}
		else {
			appendInConsole(tam+" a survecu! :)");
		}
		appendInConsole("");
	}
	
	/**
	 * Cette méthode fait le calcul du socre en pourcentage à partir de la somme des âges des tamaghis dans {@link TamaGame#tamsStart}
	 * @param sommeAge Somme des ages des tamagshis passés par {@link TamaGame#calculBilanAndScore()}
	 * @return Le score final en pourcentage arrondi au nombre le plus près
	 */
	
	private double finalScoreFromSommeAge(int sommeAge) {
		double score=sommeAge;
		score/=(Tamagoshi.getLifeTime()*this.tamsStart.size());
		score*=100;
		return Math.round(score);
	}
	
	/**
	 * Cette méyhode va afficher le bilan de la partie <br>
	 * Elle récupère le score grâce à {@link TamaGame#calculBilanAndScore()}
	 */
	
	private void bilan() {
		appendInConsole("");
		appendInConsole("Bilan de la partie");
		appendInConsole("");
		double score=calculBilanAndScore();
		appendInConsole("");
		appendInConsole("Score obtenu : "+score+"%");
		
	}
	
	/**
	 * Cette méthode permet de faire manger un tamagoshi <br>
	 * Le tamagoshi est sélectionné grâce à {@link TamaGame#selectTamagoshi(String)} où on spécifie la question à poser <br>
	 * Enfin, la méthode {@link Tamagoshi#mange()} est appellée sur le {@link Tamagoshi} sélectionné
	 */
	
	public void tamagoshiEat() {
		disableAllFeedButtons();
		this.actionEat=true;
		checkEndTour();
	}
	
	private void checkEndTour() {
		if(this.actionEat && this.actionPlay) {
			endTour();
		}
	}

	/**
	 * Cette méthode permet de jouer avec un tamagoshi <br>
	 * Le tamagoshi est sélectionné grâce à {@link TamaGame#selectTamagoshi(String)} où on spécifie la question à poser <br>
	 * Enfin, la méthode {@link Tamagoshi#joue()} est appellée sur le {@link Tamagoshi} sélectionné
	 */
	
	public void tamagoshiPlay() {
		disableAllPlayButtons();
		this.actionPlay=true;
		checkEndTour();
	}
	
	
	/**
	 * Permet d'effectuer le nombre de cycle (tour de jeu) définit par {@link Tamagoshi#getLifeTime()} <br>
	 * Tant que le nombre de cycles maximum n'est pas atteint et que {@link TamaGame#tamsActual} n'est pas vide (ce qui signifirait que tous les tamgoshis sont morts), la boucle continue <br>
	 * On va notament faire appel à la méthode {@link TamaGame#cycle(int)} pour l'éxécution de chaque cycle
	 */
	
	private void tour() {
		this.actionEat=false;
		this.actionPlay=false;
		renableButtonsOnFrames();
		cycle(this.numCycle);
		this.numCycle++;
	}
	
	/**
	 * Affiche le nuveau cycle et apelle la méthode {@link TamaGame#tour()} 
	 * @param numCycle Numéro du cycle à éxécuté (ce numéro est géré par {@link TamaGame#cyclesPLay()})
	 */
	
	private void cycle(int numCycle) {
		appendInConsole("Cycle N°:"+(numCycle+1)+" en cours");
		allParler();
	}
	
	/**
	 * Cette méthode va lancer le jeu des tamagoshis <br>
	 * Elle va passer par une phase d'initialisation pour enregsitrer tous les {@link Tamagoshi} <br>
	 * Ensuite elle va lancer la gestion les différents cycles et tours <br>
	 * Enfin, qaund la partie est finie, elle va afficher le bilan de la partie
	 */
	
	public void play() {
		initialisation();
	}
	
	private void appendInConsole(String text) {
		this.consolePane.append(text+"\n");
	}
	
	private void launchRegisterNames() {
		new TamaNameSelector(this,this.nbTams);
	}
	
	public void registerByNames(String[]tamaNames) {
		for(String str : tamaNames) {
			enregistrerTamagoshi(str);
		}
		nextTour();
	}
	
	private void disableAllPlayButtons() {
		for(TamaFrame tamFrame : this.framesTamagoshis) {
			tamFrame.disableButtonPlay();
		}
	}
	
	private void disableAllFeedButtons() {
		for(TamaFrame tamFrame : this.framesTamagoshis) {
			tamFrame.disableButtonFeed();
		}
	}
	
	private void renableButtonsOnFrames() {
		for(TamaFrame tamFrame : this.framesTamagoshis) {
			tamFrame.renableButtons();
		}
	}
	
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
	
	public static void main(String[]args) {
		TamaGameGraphic tam = new TamaGameGraphic();
		tam.setTitle("Jeu des tamagoshis");
		tam.setDefaultCloseOperation(EXIT_ON_CLOSE);
		tam.setSize(500, 250);
		tam.setLocationRelativeTo(null);
		tam.setVisible(true);
		tam.play();
	}
}
