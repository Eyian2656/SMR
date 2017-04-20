package model;

/**
 * Model Klasse des Typs Data.
 * Der Datentyp Data beschreibt die Daten einer Tabelle. 
 * Gespeichert werden ein Wert und die zugehörige ID einer Zeile.
 * @author Ian Noack
 *
 */
public class Data {

	private int nr;
	private String value;
	private String columnName;

	// Getters & Setters \\
	public int getNr() {
		return nr;
	}

	public void setNr(int nr) {
		this.nr = nr;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

}
