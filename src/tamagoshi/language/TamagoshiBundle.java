package tamagoshi.language;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class TamagoshiBundle {
	private static final String BUNDLE_NAME = "tamagoshi.tamagoshis.messages"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

	private TamagoshiBundle() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
