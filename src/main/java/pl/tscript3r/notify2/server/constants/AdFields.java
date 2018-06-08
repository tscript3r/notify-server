package pl.tscript3r.notify2.server.constants;

public class AdFields {

	public static final String KEY_BODY = "ads";
	public static final String KEY_ID = "id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_URL = "url";
	public static final String KEY_PRICE = "price";
	public static final String KEY_THUMBNAIL_URL = "thumbnail";
	public static final String KEY_CATEGORY = "category";
	public static final String KEY_LOCATION = "location";
	public static final String KEY_DESCRIPTION = "description";

	/*
	 * Those fields will be checked before the e-mail content will be created.
	 * If any of them wont be set by the parser - the e-mail thread will raise 
	 * an exception and will be stopped.
	 * 
	 * If all of them will be set - the thread will create the message content 
	 * with those fields (the singleAd.html needs to has tags with %KEY%, for
	 * example: %title% in the script).
	 * 
	 */
	public static final String[] REQUIERD_FIELDS = {KEY_TITLE, KEY_URL, KEY_PRICE, KEY_THUMBNAIL_URL, 
			KEY_CATEGORY, KEY_LOCATION};

}
