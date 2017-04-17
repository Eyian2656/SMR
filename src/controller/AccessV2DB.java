package controller;

/**
 * Diese Klasse steuert den Datenbankzugriff. Stellt eine Connection zum alten
 * Schema her.
 * 
 * @author inoack
 *
 */
public class AccessV2DB extends BasicDBAccess {
	private static AccessV2DB instance;

	public static AccessV2DB getInstance() {
		if (instance == null) {
			instance = new AccessV2DB();
		}
		return instance;
	}

	private AccessV2DB() {
		super();
	}
}
