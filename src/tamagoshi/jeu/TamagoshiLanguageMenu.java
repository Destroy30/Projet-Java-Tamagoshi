package tamagoshi.jeu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

import tamagoshi.language.LanguageAccessor;
import tamagoshi.language.LanguageObserver;

/**
 * Cette classe définit un menu qui va contenir une liste de langues <br>
 * Il va permettre au programme de changer de langue <br>
 * Ce menu va pouvoir être ré-utilisable par plusieurs interfaces
 */
public class TamagoshiLanguageMenu extends JMenu implements LanguageObserver {
	
	/**
	 * Gestionnaire de langue de l'application
	 */
	private LanguageAccessor languageConfig;
	
	/**
	 * Objet de language fournissant les bundles pour la langue courante <br>
	 * La mise à  jour de la langue fera appel à cet objet qui répandra la mise à jour sur toute l'application
	 */
	private LanguageAccessor languageAccessor;

	public TamagoshiLanguageMenu() {
		super();
		this.languageConfig = LanguageAccessor.getInstance();
		this.languageAccessor = LanguageAccessor.getInstance();
		this.languageAccessor.addObservator(this);
	}
	
	/**
	 * Cette méthode crée et place une option pour chaque langue dans le menu en mode "radio" <br>
	 * Elle va récupérer les langues que l'application gère dans le fichier de configuration <br>
	 * L'affichage du nom des langues est gérée par les Locale en fonction de la langue slectionnée
	 */
	private void setUpRadioItems() {
		this.removeAll();
		Locale currentGameLocale = this.languageAccessor.getGameLocale();
		List<Locale> localesList = this.languageAccessor.getManagedLanguages();
		ButtonGroup groupLanguages = new ButtonGroup();
		for(Locale locale : localesList) {
			JRadioButtonMenuItem itemLanguage = new JRadioButtonMenuItem(locale.getDisplayLanguage(currentGameLocale));
			itemLanguage.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent evt) {
					languageAccessor.setGameLocale(locale);
				}
			});
			itemLanguage.setSelected(locale.equals(currentGameLocale));
			groupLanguages.add(itemLanguage);
			this.add(itemLanguage);
		}
	}

	@Override
	/**
	 * Méthode implémenté de l'interface {@link LanguageObserver} <br>
	 * Permet de mettre à jour le nom des langues dans le menu
	 */
	public void languageUpdate(LanguageAccessor languageAcc) {
		ResourceBundle language = languageAcc.getBundle("Home");
		this.setText(language.getString("language"));
		setUpRadioItems();	
	}

}
