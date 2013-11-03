package business;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageBundle {
	ResourceBundle rb;

	public LanguageBundle () {
		rb = ResourceBundle.getBundle("language.LanguageBundle");
	}
	public String getMessage(String key) {
		return rb.getString(key);
	}
}
