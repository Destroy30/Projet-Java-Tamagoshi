package tamagoshi.tamagoshis;

import java.util.Random;
import java.util.ResourceBundle;

import tamagoshi.audio.AudioPlayer;
import tamagoshi.graphic.TamaFrame;
import tamagoshi.language.LanguageAccessor;
import tamagoshi.language.LanguageObserver;

/**
 * Tamagoshi est une classe abstraite permettant de modéliser le comportement d'un Tamagoshi <br>
 * Celui-ci pourra évoluer, il mangera, jouera, consommera de l'énergie, vieillira... <br>
 * Cette classe est abstraite car un Tamagoshi est forcément soit un {@link GrosMangeur} ou un {@link GrosJoueur} <br>
 */

public abstract class Tamagoshi implements LanguageObserver {
	
	/**
	 * Âge du Tamagoshi (celui-ci peut vieillir jusqu'à lifetime)
	 */
	private int age;
	
	/**
	 * Energie maximum du Tamagoshi (la variable energy ne peut pas dépasser cette valeur)
	 */
	private int maxEnergy;
	
	/**
	 * Energie du Tamagoshi (ne peut pas dépasser maxEnergy) il en récupère en mangeant
	 */
	protected int energy;
	
	/**
	 * Amusement du Tamagoshi (ne peut pas dépasser maxFun) il en récupère en jouant
	 */
	protected int fun;
	
	/**
	 * Amusement maximum du Tamagoshi (la variable fun ne peut pas dépasser cette valeur)
	 */
	private int maxFun;
	
	/**
	 * Nom du Tamagoshi
	 */
	private String name;
	
	/**
	 * Durée de vie des tamagoshis
	 */
	private static int lifeTime=10;
	
	/**
	 * Variable Random permettant d'assigner des valeurs aléatoire (à énergy, fun...)
	 */
	private Random alea=new Random();
	
	/**
	 * Frame d'affichage du tamagoshi, les différents messages émits par le Tamagoshi sont envoyés à cette frame
	 */
	private TamaFrame tamaFrame;
	
	/**
	 * Bundle de langue, permet d'obtenir les message à afficher dans la bonne langue
	 */
	private ResourceBundle language;

	/**
	 * Player pour jouer les sons du Tamagoshi
	 */
	private AudioPlayer audio;
	
	
	/**
	 * Cree un objet Tamagoshi <br>
	 * L'energie maximum varie entre 11 et 15 <br>
	 * L'energie entre 11 et  15<br>
	 * La variable "fun" d'amusement suit les mêmes probabilités
	 * @param name Nom du Tamagoshi
	 */
	
	public Tamagoshi(String name) {
		this.name=name;
		this.age=0;
		this.maxEnergy=this.alea.nextInt(5)+11;
		setEnergy(this.alea.nextInt(5)+11);
		this.maxFun=this.alea.nextInt(5)+11;
		setFun(this.alea.nextInt(5)+11);
		
		//Language
		LanguageAccessor accessor = LanguageAccessor.getInstance();
		accessor.addObservator(this);
		
		//Audio
		this.audio = AudioPlayer.getInstance();
	}
	
	/**
	 * Modifie la valeur actuelle de l'energie <br>
	 * Si celle-ci est superieure à maxEnergy, la variable energy prend la valeure de maxEnergy
	 * @param energyVal Valeur de la nouvelle energie
	 */
	private void setEnergy(int energyVal) {
		if(energyVal>this.maxEnergy) {
			this.energy=this.maxEnergy;
		}
		else {
			this.energy=energyVal;
		}
	}
	
	/**
	 * Modifie la valeur actuelle de l'amusement <br>
	 * Si celle-ci est superieure à maxFun, la variable fun prend la valeure de maxFun
	 * @param funVal Valeur du nouveau "fun"
	 */
	private void setFun(int funVal) {
		if(funVal>this.maxFun) {
			this.fun=this.maxFun;
		}
		else {
			this.fun=funVal;
		}
	}
	
	/**
	 * Le tamagoshi affiche son état : <br>
	 * - Tout vas bien si son energy et son fun sont supérieurs à 5 <br>
	 * - Affamé si et seulement si son energie est inférieure à 5 <br>
	 * - S'ennui à mourrir si et seulement si son fun est inférieur à 5 <br>
	 * - Affamé et s'ennui à mourrir si son enregy et son fun sont inférieur à 5 <br>
	 * Le tamagoshi demande ensuite à sa frame de se mettre à jour selon l'état en affichant une image
	 * @return Un booléen "true" si tout vas bien ou "false" si quelque chose ne va pas
	 */
	public boolean parle() {
		if(this.energy>=5 && this.fun>=5) {
			this.tamaFrame.displayText(language.getString("allRight"));
			this.tamaFrame.setHappyImage();
			return true;
		}
		else if(this.energy<5 && this.fun<5) {
			this.tamaFrame.displayText(language.getString("hungryBored"));
			this.tamaFrame.setAngryImage();
			return false;
		}
		else if(this.fun<5) {
			this.tamaFrame.displayText(language.getString("bored"));
			this.tamaFrame.setBoringImage();
			return false;
		}
		this.tamaFrame.displayText(language.getString("hungry"));
		this.tamaFrame.setHungryImage();
		return false;
	}
	
	/**
	 * Le tamagoshi essaye de manger : <br>
	 * - Si son energy est au maximum, il ne mange pas et affichera "Pas miam" <br>
	 * - Si son energy n'est pas au maximum, il mange et son energy augmente par une valeur aléatoire comprise entre 1 et 3 <br>
	 * Un son est joué selon le resultat
	 * @return Un booléen "true" si le tamagoshi a mangé ou "false" si il n'a pas mangé 
	 */
	public boolean mange() {
		if(this.energy<this.maxEnergy) {
			this.tamaFrame.displayText(language.getString("eating"));
			this.audio.playSound("pikaHappy");
			setEnergy(this.energy+this.alea.nextInt(3)+1);
			return true;
		}
		this.tamaFrame.displayText(language.getString("dontWantEat"));
		this.audio.playSound("pikaNotHappy");
		return false;
	}
	
	/**
	 * Le tamagoshi essaye de jouer : <br>
	 * - Si son fun est au maximum, il ne joue pas et affichera "Dégage, je suis deja au max de MON FUN!" <br>
	 * - Si son fun n'est pas au maximum, il joue et son fun augmente par une valeur aléatoire comprise entre 1 et 3
	 * Un son est joué selon le resultat
	 * @return Un booléen "true" si le tamagoshi a joué ou "false" si il n'a pas joué
	 */
	public boolean joue() {
		if(this.fun<this.maxFun) {
			this.tamaFrame.displayText(language.getString("yeah"));
			this.audio.playSound("pikaHappy");
			setFun(this.fun+this.alea.nextInt(3)+1);
			return true;
		}
		this.tamaFrame.displayText(language.getString("dontWantPlay"));
		this.audio.playSound("pikaNotHappy");
		return false;
	}
	
	/**
	 * Le tamagoshi conomme un point d'énergie : <br>
	 * - Si jamais son energie passe à 0 (ou inférieur) il est considéré comme mort et affiche une phrase de décès
	 * @return Un booléen "true" si le tamagoshi survit ou "false" si il ne survit pas
	 */
	protected boolean consommeEnergie() {
		this.energy--;
		if(this.energy<=0) {
			this.tamaFrame.displayText(language.getString("koHungry"));
			return false;
		}
		return true;
	}
	
	/**
	 * Le tamagoshi conomme un point de fun  : <br>
	 * - Si jamais son fun passe à 0 (ou inférieur) il est considéré comme mort et affiche une phrase de décès
	 * @return Un booléen "true" si le tamagoshi survit ou "false" si il ne survit pas
	 */
	protected boolean consommeFun() {
		this.fun--;
		if(this.fun<=0) {
			this.tamaFrame.displayText(language.getString("koBored"));
			return false;
		}
		return true;
	}
	
	/**
	 * Le tamagoshi consomme un point de fun et un point d'energy : <br>
	 * - Fait appel aux méthodes {@link Tamagoshi#consommeEnergie()} et  {@link Tamagoshi#consommeFun()} <br>
	 * - Si jamais une des variables (fun ou energy) descend à 0 ou moins, le tamagoshi meurt et affiche un message <br>
	 * - Si le tamagoshi meurt, il jouera également un son correspondant et mettra à jour son image dans sa frame
	 * @return Un booléen "true" si le tamagoshi survit ou "false" si il ne survit pas
	 */
	public boolean consomme() {
		boolean isAlive = (this.consommeEnergie() && this.consommeFun());
		if(!isAlive) {
			this.tamaFrame.setKoImage();
			this.audio.playSound("pikaNotHappy");
		}
		return isAlive;
	}
	
	/*public String toString() {
		return this.name+" "+this.age+" an(s) Energie : "+this.energy+" MaxEnergie : "+this.maxEnergy;
	}*/
	
	/**
	 * toString d'un tamagoshi
	 * @return Un String contenant le nom du tamagoshi
	 */
	public String toString() {
		return this.name;
	}
	
	/**
	 * Méthode de classe permettant de récupérer la durée de vie des tamagoshis) <br>
	 * @return La variable de classe lifetime (durée de vie)
	 */
	public static int getLifeTime() {
		return Tamagoshi.lifeTime;
	}
	
	/**
	 * Permet d'augmenter l'age du tamagoshi de 1, connaitre l'age est utile pour le calcul du score dans la partie
	 */
	public void incrementerAge() {
		this.age++;
	}
	
	/**
	 * Permet de vérifier si un tamagoshi est mort ou vivant (en se basant sur son energy et son fun) <br>
	 * @return Un booléen "true" si le tamagoshi est mort, "false" si il est vivant
	 */
	public boolean isDead() {
		return this.energy<=0 || this.fun<=0;
	}
	
	/**
	 * Permet de récupérer l'âge d'un tamagoshi, connaitre l'age est utile pour le calcul du score <br>
	 * @return Un int correspondant à l'âge du Tamagoshi
	 */
	public int getAge() {
		return this.age;
	}

	/**
	 * Permet de lier une frame au tamagoshi <br>
	 * @param tamaFrame Frame qui va interagir avec le tamagoshi
	 */
	public void setTamaFrame(TamaFrame tamaFrame) {
		this.tamaFrame = tamaFrame;
	}
	
	/**
	 * Methode implémenté de {@link LanguageObserver} <br>
	 * Permet de mettre à jour le fichier de ressources de langue des tamagoshis
	 */
	public void languageUpdate(LanguageAccessor languageAcc) {
		this.language = languageAcc.getBundle("Tamagoshi");
	}
	
	

}
