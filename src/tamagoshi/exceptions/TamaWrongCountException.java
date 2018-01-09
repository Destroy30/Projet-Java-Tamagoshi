package tamagoshi.exceptions;

/**
 * 
 * TamaWrongCountException est une exception lié à l'éxécution de {@link tamagoshi.jeu.TamaGame} <br>
 * Cette exception est levée si l'utilisateur rentre un nombre de tamagoshis à gérer invalide (en dessous de 1)
 */

public class TamaWrongCountException extends RuntimeException{
	
	/**
	 * Constructeur de l'exception, spécifie un message d'erreur spécifique
	 * @param i Valeur erronée du nombre de tamagoshis rentrée par l'utilisateur
	 */
	
	public TamaWrongCountException(int i) {
		super("Vous ne pouvez pas gérer "+i+" tamagoshis!");
	}

}
