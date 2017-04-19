package controller;

import java.io.File;
import java.io.IOException;

import controller.Skriptwriter;

public class SQLStatements {
	private Skriptwriter scriptwriter;

	public SQLStatements(File outputFile) {
		this.scriptwriter = new Skriptwriter(outputFile);
	}

	/**
	 * Funktion die eine Spalte hinzufügt.
	 * 
	 * @param tableName
	 * @param columnName
	 * @param datatype
	 * @param datasize
	 * @throws IOException 
	 */
	public void insert(String tableName, String columnName, String datatype, int datasize) throws IOException {
		if (datasize == 0) {
			String statement = ("ALTER TABLE " + tableName + " ADD " + columnName + " " + datatype + ";");
			scriptwriter.writeScript(statement);
		} else {
			String statement = ("ALTER TABLE " + tableName + " ADD " + columnName + " " + datatype + " (" + datasize
					+ ");");
			scriptwriter.writeScript(statement);
		}

	}

	/**
	 * Funktion die eine Spalte löscht.
	 * 
	 * @param tableName
	 * @param columnName
	 * @throws IOException 
	 */
	public void drop(String tableName, String columnName) throws IOException {
		String statement = ("ALTER TABLE " + tableName + " DROP COLUMN " + columnName + " ;");
		scriptwriter.writeScript(statement);
	}

	/**
	 * Funktion die die Datentypgröße anpasst.
	 * 
	 * @param tableName
	 * @param columnName
	 * @param datatype
	 * @param datasize
	 * @throws IOException 
	 */
	public void modifyDatasize(String tableName, String columnName, String datatype, int datasize) throws IOException {
		String statement = ("ALTER TABLE " + tableName + " MODIFY (" + columnName + " " + datatype + " (" + datasize
				+ "));");
		scriptwriter.writeScript(statement);
	}

	/**
	 * Funktion die das Feld Nullable korrigiert.
	 * 
	 * @param tableName
	 * @param columnName
	 * @param datatyp
	 * @param bool
	 * @throws IOException 
	 */
	public void modifyNullable(String tableName, String columnName, String datatyp, Boolean bool, int datasize) throws IOException {
		if (bool == true) {
			String statement = ("ALTER TABLE " + tableName + " MODIFY " + columnName + "  null" + " ;");
			scriptwriter.writeScript(statement);
		} else {
			String statement = ("ALTER TABLE " + tableName + " MODIFY " + columnName + " not null" + " ;");
			scriptwriter.writeScript(statement);
		}
	}

	/**
	 * Funktion um die Daten einer bestehenden Spalte zu korrigieren.
	 * 
	 * @param tableName
	 * @param columnName
	 * @param value
	 * @param rowNr
	 * @throws IOException 
	 */
	public void updateData(String tableName, String columnName, String value, int rowNr) throws IOException {

		if (value == null) {
			String statement = ("UPDATE " + tableName + " SET " + columnName + " = " + value + " WHERE NR = " + rowNr
					+ " ;");
			scriptwriter.writeScript(statement);
		} else {
			String statement = ("UPDATE " + tableName + " SET " + columnName + " = '" + value + "' WHERE NR = " + rowNr
					+ " ;");
			scriptwriter.writeScript(statement);
		}
	}
}
