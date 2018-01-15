package tamagoshi.language;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageAccessor {
	
	public static ResourceBundle getBundle(Locale locale,String nameBundle) {
		return ResourceBundle.getBundle("tamagoshi.language."+nameBundle,locale);
	}
	
	public static ResourceBundle getDefaultBundle(String nameBundle) {
		Locale currentLocale = Locale.getDefault();
		return ResourceBundle.getBundle("tamagoshi.language."+nameBundle,currentLocale);
	}
	

	

}
