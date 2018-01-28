package tamagoshi.language;

/**
 * Interface implémenté par toutes les classes qui veulent être avertit d'un changement d'état de {@link LanguageAccessor} <br>
 * Ainsi, tous les observateurs pourront se mettre à jour.
 */

public interface LanguageObserver {
	
	/**
	 * Méthode appellée sur chaque observateur afin que celui-ci se mette à jour avec les nouvelles donénes de la langue
	 * @param languageAcc Permet notamment d'accèser aux bundles de la langue courante de l'application
	 */
	public abstract void languageUpdate(LanguageAccessor languageAcc);
	
}
