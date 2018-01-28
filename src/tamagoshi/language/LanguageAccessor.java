package tamagoshi.language;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import tamagoshi.properties.TamaConfiguration;

/**
 * Cette classe va permettre d'accèder aux variables textuelles de l'application selon une langue donnée.<br>
 * Elle est le "sujet" de nombreux observateurs (notament graphiques) 
 */
public class LanguageAccessor {
	
	/**
	 * Langue actuelle
	 */
	private Locale gameLocal;
	
	/**
	 * Instance unique de la classe selon le pattern singleton
	 */
	private static LanguageAccessor instance;
	
	/**
	 * Liste des observateurs pour ise à jour selon le pattern Observateur
	 */
	private List<LanguageObserver> languageObs;
	
	/**
	 * Objet de configuration de l'application, utile pour récupérer la langue configurée.
	 */
	private TamaConfiguration config;
	
	/**
	 * Initiale principalement {@link LanguageAccessor#gameLocal} <br>
	 * Si l'utilisateur à choisis une langue de préférence, elle est iniitialisé avec celle-ci, sinon avec la Locale par défaut
	 */
	private LanguageAccessor() {
		this.languageObs = new ArrayList<LanguageObserver>();
		this.config = TamaConfiguration.getInstance();
		Locale userLanguage;
		if((userLanguage = config.getUserLanugage())!=null) {
			this.gameLocal = userLanguage;
		}
		else {
			this.gameLocal = Locale.getDefault();
		}
	};
	
	/**
	 * Permet d'obtenir une instance unique de la classe selon le pattern Singleton
	 * @return L'instance unique de laclasse
	 */
	public static synchronized LanguageAccessor getInstance() {
		if(LanguageAccessor.instance == null) {
			LanguageAccessor.instance = new LanguageAccessor();
		}
		return LanguageAccessor.instance;
	}
	
	/**
	 * Permet d'obtenir le bundle de langue selectinné selon la {@link LanguageAccessor#gameLocal} courrante
	 * @param nameBundle Nom du bundle à lire
	 * @return Le bundle correspondant au nom et à la langue courante
	 */
	public ResourceBundle getBundle(String nameBundle) {
		return ResourceBundle.getBundle("tamagoshi.language."+nameBundle,this.gameLocal);
	}
	
	/**
	 * Permet de récupérer {@link LanguageAccessor#gameLocal}
	 * @return La Locale courante de l'application
	 */
	public Locale getGameLocale() {
		return this.gameLocal;
	}
	
	/**
	 * Permet de changer la Locale de l'application <br>
	 * Va sauvegarder le choix dans la configuration <br>
	 * Va faire appel à {@link LanguageAccessor#notifyAllObserver()} pour notifier le changement de langue à tous les observateurs
	 * @param locale Nouvelle locale de l'application
	 */
	public void setGameLocale(Locale locale) {
		this.gameLocal = locale;
		this.config.setUserLanguage(locale);
		notifyAllObserver();
	}
	
	/**
	 * Permet d'ajouter un observateur à la liste <br>
	 * La méthode notifie directement l'observateur lors de l'ajout pour que celui-ci se mette à jour avec l'état courant de la langue
	 * @param obs Novuel observateur (Fenêtre graphique, un tamagoshi...)
	 */
	public void addObservator(LanguageObserver obs) {
		this.languageObs.add(obs);
		obs.languageUpdate(this);
	}
	
	/**
	 * Permet de notifier tous les observateurs de l'état actuel de l'objet <br>
	 * Par exemple lors du changement de la langue de l'application avec une nouvelle Locale
	 */
	private void notifyAllObserver() {
		for(LanguageObserver obs : this.languageObs) {
			obs.languageUpdate(this);
		}
	}
	

	

}
