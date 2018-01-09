package tamagoshi.tamagoshis;

/**
 * 
 * GrosMangeur est une classe heritant de {@link Tamagoshi} <br>
 * Sa spécificité est de ré-imlémenter la fonction de consommation d'énergie afin qu'elle consomme un point supplémentaire
 */

public class GrosMangeur extends Tamagoshi {
	
	/**
	 * Cree un objet GrosMangeur <br>
	 * Fait appel au constructeur de {@link Tamagoshi}
	 * @param name Nom du Tamagoshi
	 */

	public GrosMangeur(String name) {
		super(name);
	}
	
	/**
	 * Consomme deux points d'energie et affiche un message si le tamagoshi décède <br>
	 * Cosomme un point d'energie et fait appel à la méthode parente dans {@link Tamagoshi}
	 */
	
	public boolean consommeEnergie() {
		this.energy--;
		return super.consommeEnergie();
	}

}
