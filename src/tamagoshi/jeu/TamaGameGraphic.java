package tamagoshi.jeu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Random;

import javax.swing.JFrame;

import tamagoshi.exceptions.TamaNotExist;
import tamagoshi.exceptions.TamaWrongCountException;
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
	
	/**
	 * Nombres de tamagoshis gérés dans la aprtie (défini par l'utilisateur...)
	 */
	private int nbTams;
	
	/**
	 * Liste contenant des noms de Tamagoshis pré-définis, elle permet de choisir un nom aléatoirement si besoin
	 */
	
	private static final String[]preDefNames={"Laulau","Guichaf","Erwan","Nico","Théophile","Malo","Xavier","Bella","Gouachi","Valentin","Toto","Fifi","Mario","Luigi","Nini"};
	
	/**
	 * Constructeur de la classe TamaGame <br>
	 * Initialise les deux listes (vides)
	 */
	
	public TamaGameGraphic() {
		this.tamsStart=new ArrayList<Tamagoshi>();
		this.tamsActual=new ArrayList<Tamagoshi>();
	}
	
	/**
	 * Cette méthode permet d'initialiser la partie <br>
	 * Elle demande à l'utilisateur combien de tamagoshis il souhaite gérer <br>
	 * Elle permet à l'utilisateur de saisir un nombre de tamagoshis. <br>
	 * Elle utilise la classe {@link Utilisateur} pour cela  <br>
	 * Elle gère le cas où le nombre renté est invalide en catchant l'expetion {@link TamaWrongCountException} levée par {@link TamaGame#initTamagoshisByNumber()}
	 */
	
	private void initialisation() {
		System.out.println("Bienvenue dans TamaGame!");
		boolean initialized=false;
		while(!initialized) {
			System.out.print("Combien de Tamagoshis voulez vous gérer ? : ");
			try {
				initialized=true;
				this.nbTams=Integer.parseInt(Utilisateur.saisieClavier());
				initTamagoshisByNumber();
			}
			catch(NumberFormatException | TamaWrongCountException e) {
				initialized=false;
				System.out.println(e.getMessage());
			}
		}
	}
	
	/**
	 * Cette classe lance l'enresgitrement de chaque tamagoshis par rapprot à {@link TamaGame#nbTams}
	 * @throws TamaWrongCountException Si jamais le nombre rentré est invalide (inférieur à 1)
	 */
	
	private void initTamagoshisByNumber() throws TamaWrongCountException {
		if(this.nbTams<1) {
			throw new TamaWrongCountException(this.nbTams);
		}
		for(int i=0;i<this.nbTams;i++) {
			goEnregistrementStep(i);
		}
	}
	
	/**
	 * Permet d'enresgitrer un tamagoshi (en demandant son nom) ou bien de manière aletoire
	 * @param step specifie le numéro du tamagoshi qu'on enregsitre
	 */
	
	private void goEnregistrementStep(int step) {
		System.out.print("Quel est le nom du Tamagoshi n°="+(step)+" ? (tapez '?' pour un nom aletoire) : ");
		String tamName = enregistrement();
		System.out.println(tamName+" enregistré!");
	}
	
	/**Permet d'enregistrer un tamagoshi, sois pas saisie clavier (avec la classe {@link Utilisateur} ou bien aleatoirement avec la méthode {@link TamaGame#enregistrementAlea()} <br>
	 * Par la suite, cette méthode lance l'enregsitrement du tamagoshis dans les différentes listes
	 * @return Un String correspondant au nom du tamagoshi enregsitré
	 */
	private String enregistrement() {
		String tamName = Utilisateur.saisieClavier();
		if(tamName.equals("?")) {
			return enregistrementAlea();
		}
		enregistrerTamagoshi(tamName);
		return tamName;
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
	}
	
	/**
	 * Cette méthode permet de récupérer un tamagoshi par son index dans la liste {@link TamaGame#tamsActual}
	 * @param i index du tamagoshi dans la liste
	 * @return Le {@link Tamagoshi} se trouvant à l'index i
	 * @throws TamaNotExist si jamais l'index du tamagoshi est invalide (pas dans la liste...)
	 */
	
	private Tamagoshi getTamagoshi(int i) throws TamaNotExist {
		try {
			return this.tamsActual.get(i);
		}
		catch(IndexOutOfBoundsException e) {
			throw new TamaNotExist(i);
		}
	}
	
	/**
	 * Cetet méthode permet d'afficher et de selectionner un tamagoshi par rapport à une action demandée <br>
	 * Elle va ensuite appeller la méthode {@link TamaGame#presentAllTamActual()} permet de présenter chaque Tamagoshi avec son index
	 * @param message correspond au message à afficher (ex : Quel Tamagoshi voulez vous nourrir ?)
	 * @return Le {@link Tamagoshi} sélectionné
	 */
	
	private Tamagoshi selectTamagoshi(String message) {
		System.out.println(message);
		presentAllTamActual();
		return boucleForSelection();
	}
	
	/**
	 * Cette méthode effectue une boucle afin de selectionner un  {@link Tamagoshi} dans la liste {@link TamaGame#tamsActual} <br>
	 * Elle utilise la classe {@link Utilisateur} <br>
	 * Elle gère en effet le cas où un index rentré est invalide (elle catch l'exception {@link TamaNotExist} levée par {@link TamaGame#getTamagoshi(int)}
	 * @return Le {@link Tamagoshi} selectionné
	 */
	
	private Tamagoshi boucleForSelection() {
		Tamagoshi tamSelected=null;
		boolean selected=false;
		while(!selected) {
			try {
				System.out.print("Entrez votre choix : ");
				int selection=Integer.parseInt(Utilisateur.saisieClavier());
				tamSelected=this.getTamagoshi(selection);
				selected=true;
			}
			catch(NumberFormatException | TamaNotExist e) {
				System.out.println(e.getMessage());
			}
		}
		return tamSelected;
	}
	
	/**
	 * Cette méthode permet à chaque tamagoshis vivants (présents dans {@link TamaGame#tamsActual}) de se présenter <br>
	 * Chaque tamagoshi affcihe son état grâce à {@link Tamagoshi#parle()}
	 */
	
	private void presentAllTamActual() {
		for (ListIterator<Tamagoshi> iterator = this.tamsActual.listIterator(); iterator.hasNext();) {
			Tamagoshi tam = (Tamagoshi) iterator.next();
			System.out.println("("+iterator.previousIndex()+") "+tam+" ");
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
			tam.finalState();
			sommeAge+=tam.getAge();
		}
		return finalScoreFromSommeAge(sommeAge);
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
		System.out.println("-------------Bilan------------ ");
		double score=calculBilanAndScore();
		System.out.println();
		System.out.println("Score obtenu : "+score+"%");
		
	}
	
	/**
	 * Cette méthode permet de faire manger un tamagoshi <br>
	 * Le tamagoshi est sélectionné grâce à {@link TamaGame#selectTamagoshi(String)} où on spécifie la question à poser <br>
	 * Enfin, la méthode {@link Tamagoshi#mange()} est appellée sur le {@link Tamagoshi} sélectionné
	 */
	
	private void tourManger() {
		Tamagoshi tamSelectedEat=this.selectTamagoshi("Nourrir quel Tamagoshi ?");
		tamSelectedEat.mange();
	}
	
	/**
	 * Cette méthode permet de jouer avec un tamagoshi <br>
	 * Le tamagoshi est sélectionné grâce à {@link TamaGame#selectTamagoshi(String)} où on spécifie la question à poser <br>
	 * Enfin, la méthode {@link Tamagoshi#joue()} est appellée sur le {@link Tamagoshi} sélectionné
	 */
	
	private void tourJouer() {
		Tamagoshi tamSelectedPlay=this.selectTamagoshi("Jouer avec quel Tamagoshi ?");
		tamSelectedPlay.joue();
	}
	
	/**
	 * Cette méthode effectue un tour (un cycle) <br>
	 * Elle apelle la méthode {@link TamaGame#allParler()} pour que les tamgoshis affichent leur etat <br>
	 * Puis elle appelle {@link TamaGame#tourManger()} puis {@link TamaGame#tourJouer()} <br>
	 * Enfin, elle termine le tour avec {@link TamaGame#endTour()}
	 */
	
	private void tour() {
		allParler();
		tourManger();
		tourJouer();
		endTour();
	}
	
	/**
	 * Permet d'effectuer le nombre de cycle (tour de jeu) définit par {@link Tamagoshi#getLifeTime()} <br>
	 * Tant que le nombre de cycles maximum n'est pas atteint et que {@link TamaGame#tamsActual} n'est pas vide (ce qui signifirait que tous les tamgoshis sont morts), la boucle continue <br>
	 * On va notament faire appel à la méthode {@link TamaGame#cycle(int)} pour l'éxécution de chaque cycle
	 */
	
	private void cyclesPLay() {
		int numCycle=0;
		while(!(this.tamsActual.isEmpty() || numCycle == Tamagoshi.getLifeTime())) {
			cycle(numCycle);
			numCycle++;
		}
	}
	
	/**
	 * Affiche le nuveau cycle et apelle la méthode {@link TamaGame#tour()} 
	 * @param numCycle Numéro du cycle à éxécuté (ce numéro est géré par {@link TamaGame#cyclesPLay()})
	 */
	
	private void cycle(int numCycle) {
		System.out.println("-----------------Cycle "+(numCycle+1)+"---------------------");
		tour();
	}
	
	/**
	 * Cette méthode va lancer le jeu des tamagoshis <br>
	 * Elle va passer par une phase d'initialisation pour enregsitrer tous les {@link Tamagoshi} <br>
	 * Ensuite elle va lancer la gestion les différents cycles et tours <br>
	 * Enfin, qaund la partie est finie, elle va afficher le bilan de la partie
	 */
	
	public void play() {
		initialisation();
		cyclesPLay();
		bilan();
	}
}
