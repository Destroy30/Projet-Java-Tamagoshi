package tamagoshi.language;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import tamagoshi.properties.TamaConfiguration;

public class LanguageAccessor {
	
	private Locale gameLocal;
	
	private static LanguageAccessor instance;
	
	private List<LanguageObserver> languageObs;
	
	private TamaConfiguration config;
	
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
	
	public static synchronized LanguageAccessor getInstance() {
		if(LanguageAccessor.instance == null) {
			LanguageAccessor.instance = new LanguageAccessor();
		}
		return LanguageAccessor.instance;
	}
	
	public ResourceBundle getBundle(String nameBundle) {
		return ResourceBundle.getBundle("tamagoshi.language."+nameBundle,this.gameLocal);
	}
	
	public Locale getGameLocale() {
		return this.gameLocal;
	}
	
	public void setGameLocale(Locale locale) {
		this.gameLocal = locale;
		this.config.setUserLanguage(locale);
		notifyAllObserver();
	}
	
	public void addObservator(LanguageObserver obs) {
		this.languageObs.add(obs);
		obs.languageUpdate(this);
	}
	
	private void notifyAllObserver() {
		for(LanguageObserver obs : this.languageObs) {
			obs.languageUpdate(this);
		}
	}
	

	

}
