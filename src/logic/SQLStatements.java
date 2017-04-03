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
		if(datasize == 0){
		String statement = ("ALTER TABLE " + tableName + " ADD " + columnName + " " + datatype + ";");
		scriptwriter.writeScript(statement);
		}else {
		String statement = ("ALTER TABLE " + tableName + " ADD " + columnName + " " + datatype + " (" + datasize + ");");
		scriptwriter.writeScript(statement);
		}

	}

	/**
	 * Funktion die eine Spalte löscht.
	 * 
	 * @param tableName
	 * @param columnName
	 */
	public void drop(String tableName, String columnName) {
		String statement = ("ALTER TABLE " + tableName + " DROP COLUMN " + columnName+ " ;");
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
	public void modifyNullable(String tableName, String columnName, String datatyp, Boolean bool, int datasize) {
		if (bool == true) {
			String statement = ("ALTER TABLE " + tableName + " MODIFY " + columnName + "  null" + " ;");
			scriptwriter.writeScript(statement);
		} else {
			String statement = ("ALTER TABLE " + tableName + " MODIFY " + columnName + " not null" + " ;");
			scriptwriter.writeScript(statement);
		}
//		if (bool == true) {
//			String statement = ("ALTER TABLE " + tableName + " MODIFY " + columnName + " " + datatyp + " ("+ datasize+ ") " + "  null" + " ;");
//			scriptwriter.writeScript(statement);
//		} else {
//			String statement = ("ALTER TABLE " + tableName + " MODIFY " + columnName + " " + datatyp + " ("+ datasize+ ") " + " not null" + " ;");
//			scriptwriter.writeScript(statement);
//		}
	}

	/**
	 * Funktion um die Daten einer bestehenden Spalte zu korrigieren.
	 * @param tableName
	 * @param columnName
	 * @param value
	 * @param rowNr
	 */
	public void updateData(String tableName, String columnName, String value, int rowNr) {
	
		if(value == null){
		String statement = ("UPDATE " + tableName + " SET " + columnName + " = " + value + " WHERE NR = " + rowNr + " ;");
		scriptwriter.writeScript(statement);
		}else{
		String statement = ("UPDATE " + tableName + " SET " + columnName + " = '" + value + "' WHERE NR = " + rowNr + " ;");
		scriptwriter.writeScript(statement);
		}
	}
}
