package controller;

/**
 * Diese Klasse steuert den Datenbankzugriff. Stellt eine Connection zum alten
 * Schema her.
 * 
 * @author inoack
 *
 */
public class AccessV1DB extends BasicDBAccess{
	private static AccessV1DB instance;

	public static AccessV1DB getInstance() {
		if (instance == null) {
			instance = new AccessV1DB();
		}
		return instance;
	}

	private AccessV1DB() {
		super();
	}

}
