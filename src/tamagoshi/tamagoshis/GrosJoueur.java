package tamagoshi.tamagoshis;

/**
 * 
 * GrosJoueur est une classe heritant de {@link Tamagoshi} <br>
 * Sa spécificité est de ré-imlémenter la fonction de consommation de fun afin qu'elle consomme un point supplémentaire
 */

public class GrosJoueur extends Tamagoshi{
	
	/**
	 * Cree un objet GrosMangeur <br>
	 * Fait appel au constructeur de {@link Tamagoshi}
	 * @param name Nom du Tamagoshi
	 */

	public GrosJoueur(String name) {
		super(name);
	}
	
	/**
	 * Consomme deux points de fun et affiche un message si le tamagoshi décède <br>
	 * Cosomme un point de fun et fait appel à la méthode parente dans {@link Tamagoshi}
	 */
	
	public boolean consommeFun() {
		this.fun--;
		return super.consommeFun();
	}

}
