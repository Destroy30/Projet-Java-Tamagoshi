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
import tamagoshi.properties.TamaConfiguration;

public class TamagoshiLanguageMenu extends JMenu implements LanguageObserver {
	
	private TamaConfiguration config;
	private LanguageAccessor languageAccessor;

	public TamagoshiLanguageMenu() {
		super();
		this.config = TamaConfiguration.getInstance();
		this.languageAccessor = LanguageAccessor.getInstance();
		this.languageAccessor.addObservator(this);
	}
	
	public void setUpRadioItems() {
		this.removeAll();
		Locale currentGameLocale = this.languageAccessor.getGameLocale();
		List<Locale> localesList = this.config.getManagedLanguages();
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
	public void languageUpdate(LanguageAccessor languageAcc) {
		ResourceBundle language = languageAcc.getBundle("Home");
		this.setText(language.getString("language"));
		setUpRadioItems();	
	}

}
