package logic;

import logic.Skriptwriter;

public class SQLStatements {
	Skriptwriter scriptwriter = new Skriptwriter();

	/**
	 * Funktion die eine Spalte hinzufügt.
	 * 
	 * @param tableName
	 * @param columnName
	 * @param datatype
	 * @param datasize
	 */
	public void insert(String tableName, String columnName, String datatype, int datasize) {
		String statement = ("ALTER TABLE " + tableName + " ADD " + columnName + " " + datatype + " " + datasize);
		scriptwriter.writeScript(statement);

	}

	/**
	 * Funktion die eine Spalte löscht.
	 * 
	 * @param tableName
	 * @param columnName
	 */
	public void drop(String tableName, String columnName) {
		String statement = ("ALTER TABLE " + tableName + " DROP COLUMN " + columnName);
		scriptwriter.writeScript(statement);
	}

	/**
	 * Funktion die die Datentypgröße anpasst.
	 * 
	 * @param tableName
	 * @param columnName
	 * @param datatype
	 * @param datasize
	 */
	public void modifyDatasize(String tableName, String columnName, String datatype, int datasize) {
		String statement = ("ALTER TABLE " + tableName + " MODIFY (" + columnName + " " + datatype + " (" + datasize
				+ "));");
		scriptwriter.writeScript(statement);
	}

	/**
	 * Funktion die das Feld Nullable korrigiert.
	 * @param tableName
	 * @param columnName
	 * @param datatyp
	 * @param bool
	 */
	public void modifyNullable(String tableName, String columnName, String datatyp, Boolean bool) {
		if (bool = true) {
			String statement = ("ALTER TABLE " + tableName + " MODIFY " + columnName + " " + datatyp + " null");
			scriptwriter.writeScript(statement);
		} else {
			String statement = ("ALTER TABLE " + tableName + " MODIFY " + columnName + " " + datatyp + " not null");
			scriptwriter.writeScript(statement);
		}
	}

	/**
	 * Funktion um die Daten einer bestehenden Spalte zu korrigieren.
	 * @param tableName
	 * @param columnName
	 * @param value
	 * @param rowNr
	 */
	public void updateData(String tableName, String columnName, String value, int rowNr) {
		String statement = ("UPDATE " + tableName + " SET " + columnName + " = " + value + " WHERE NR = " + rowNr);
		scriptwriter.writeScript(statement);
	}

	/**
	 * Funktion um Daten einer neuerstellten Spalte hinzuzufügen.
	 * @param tableName
	 * @param columnName
	 * @param value
	 * @param rowNr
	 */
	public void insertIntoData(String tableName, String columnName, String value, int rowNr) {
		String statement = ("INSERT INTO " + tableName + " (" + columnName + ") VALUES " + " ( " + value + " );");
		scriptwriter.writeScript(statement);
	}

}
