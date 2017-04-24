package controller;

import java.io.File;
import java.io.IOException;

import controller.Skriptwriter;

/**
 * Klasse die SQL Statements an den Skriptwriter schickt. Die Klasse wird vom 
 * Datacomparer und Tablecomparer aufgerufen.
 * @author Ian Noack
 *
 */
public class SQLStatements {
	private Skriptwriter scriptwriter;

	public SQLStatements(File outputFile) {
		this.scriptwriter = new Skriptwriter(outputFile);
	}

	/**
	 * Funktion die eine Spalte hinzufügt.
	 * 
	 * @param tableName Enthält den Tabellennamen.
	 * @param columnName Enthält den Spaltennamen.
	 * @param datatype Enthält den Datentypen.
	 * @param datasize Enthält die Grüße eines Datentyps.
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
	 * @param tableName Enthält den Tabellennamen.
	 * @param columnName Enthält den Spaltennamen.
	 * @throws IOException 
	 */
	public void drop(String tableName, String columnName) throws IOException {
		String statement = ("ALTER TABLE " + tableName + " DROP COLUMN " + columnName + " ;");
		scriptwriter.writeScript(statement);
	}

	/**
	 * Funktion die die Datentypgröße anpasst.
	 * 
	 * @param tableName Enthält den Tabellennamen.
	 * @param columnName Enthält den Spaltennamen.
	 * @param datatype Enthält den Datentypen.
	 * @param datasize Enthält die Grüße eines Datentyps.
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
	 * @param tableName Enthält den Tabellennamen.
	 * @param columnName Enthält den Spaltennamen.
	 * @param datatyp Enthält die Grüße eines Datentyps.
	 * @param bool bestimmt ob der Wert eines Feldes Nullable sein darf.
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
	 * @param tableName Enthält den Tabellennamen.
	 * @param columnName Enthält den Spaltennamen.
	 * @param value Enthält den Wert eines Feldes.
	 * @param rowNr Enthält den Zeilennummer.
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
	
	/**
	 * Funktion die das Sql Skript zu einer Transaction macht.
	 * @throws IOException
	 */
	public void transaction() throws IOException{
		String statement = ("COMMIT;");
		scriptwriter.writeScript(statement);
	}
}
