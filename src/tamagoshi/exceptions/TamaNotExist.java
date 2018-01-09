package tamagoshi.exceptions;

/**
 * 
 * TamaNotExist est une exception lié à l'éxécution de {@link tamagoshi.jeu.TamaGame} <br>
 * Cette exception est levée si l'utilisateur choisis de nourrir/s'amuser avec un Tamagoshi qui n'existe pas
 */


public class TamaNotExist extends RuntimeException{
	
	/**
	 * Constructeur de l'exception, spécifie un message d'erreur spécifique
	 * @param i Valeur erronée du tamagoshi rentrée par l'utilisateur
	 */
	
	public  TamaNotExist(int i) {
		super("Le Tamagoshi "+i+" n'existe pas");
	}

}
