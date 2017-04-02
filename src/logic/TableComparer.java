package logic;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import model.Column;
import model.Data;

public class TableComparer {
	List<Data> allTableData = new ArrayList<Data>();
	DataCrawler dataCrawler = new DataCrawler();

	public String tablename;

	/**
	 * Die aufzurufende Funktion der Klasse TableComparer zu starten. Sie
	 * enthält alle Daten um in weiter Methoden um herauszufinden welche
	 * Strukturen angepasst werden müssen.Anschließend werden die Unterschiede
	 * an die Klasse SQLStatements geschickt. Returned werden die TableNames der
	 * neuen Tabelle welches gebraucht wird um den Daten zu vergleiche.
	 * 
	 * @param columnOld
	 * @param columnNew
	 * @param conn
	 * @param tableName
	 * @param getColNames
	 * @return
	 * @throws SQLException
	 */
	public void differColumn(List<Column> columnOld, List<Column> columnNew, String tableName,
			Connection oldSchema, Connection newSchema) throws SQLException {

		unwantedColumn(columnOld, columnNew, tableName, oldSchema, newSchema);
		missingColumn(columnOld, columnNew, tableName, newSchema);
		wrongDatatypSize(columnOld, columnNew);
		nullable(columnOld, columnNew);
	}

	/**
	 * Diese Funktion überprüft ob eine Spalte zuviel ist und löscht dieses
	 * anschließend
	 * 
	 * @param allColumnsOld
	 * @param allColumnsNew
	 * @throws SQLException
	 */
	protected void unwantedColumn(List<Column> allColumnsOld, List<Column> allColumnsNew, String tableName,
			Connection oldSchema, Connection newSchema) throws SQLException {
		boolean columnNotThere = false;
		List<Data> listOfOldData = new ArrayList<Data>();
		List<Data> listOfnewData = new ArrayList<Data>();
		DataComparer dataComparer = new DataComparer();

		for (Column columnOld : allColumnsOld) {

			for (Column columnNew : allColumnsNew) {
				if (StringUtils.equals(columnNew.getName(), columnOld.getName())) {
					columnNotThere = false;

					// Nachdem die Struktur Änderung einer Tabelle abeschlossen
					// sind werden
					// die Daten geprüft.
					listOfOldData = dataCrawler.crawlData(oldSchema, columnOld.getName(), tableName,
							columnOld.getType());
					listOfnewData = dataCrawler.crawlData(newSchema, columnNew.getName(), tableName,
							columnNew.getType());
					dataComparer.compareData(listOfOldData, listOfnewData, tableName);

					break;
				} else {
					columnNotThere = true;
				}
			}
			if (columnNotThere == true) {
				// Übergabe der zuändernden Daten an SqlStatement
				SQLStatements sentStmt = new SQLStatements();
				sentStmt.drop(tableName, columnOld.getName());
			}
		}
	}

	protected void missingColumn(List<Column> allColumnsOld, List<Column> allColumnsNew, String tableName, Connection newSchema)
			throws SQLException {
		boolean columnNotThere = false;
		List<Data> listOfnewData = new ArrayList<Data>();
		DataComparer dataComparer = new DataComparer();

		for (Column columnNew : allColumnsNew) {

			for (Column columnOld : allColumnsOld) {
				if (StringUtils.equals(columnNew.getName(), columnOld.getName())) {
					columnNotThere = false;
					break;
				} else {
					columnNotThere = true;
				}
			}
			if (columnNotThere == true) {
				// hier kommt die Funktion zum hinzufügen des Namens oder eine
				// Übergabe des Columns als String um diese ins Skript zu
				// schreiben
				System.out.println("Die anzuhängende Spalte ist: " + columnNew.getName() + " Mit dem Datentyp "
						+ columnNew.getType() + " " + columnNew.getSize());

				// Die neue Zeile braucht Daten. oldColumn daten werden null
				// sein ( da sie noch nicht existiert) und müssen mit newColumn
				// Daten überschrieben werden.
				listOfnewData = dataCrawler.crawlData(newSchema, columnNew.getName(), tableName,
						columnNew.getType());
				dataComparer.newColumnData(listOfnewData, tableName);
			}
		}
	}

	// TODO hier muss nur die Größe veränder werden und NICHT der Datentyp noch
	// anpassen
	protected void wrongDatatypSize(List<Column> allColumnOld, List<Column> allColumnNew) {
		for (Column columnOld : allColumnOld) {
			for (Column columnNew : allColumnNew) {
				if (StringUtils.equals(columnNew.getName(), columnOld.getName())) {
					if (StringUtils.equals(columnNew.getType(), columnOld.getType())
							&& (columnNew.getSize() != columnOld.getSize())) {
						// hier muss der columnName geholt werden und an der
						// stelle den Datatyp geändert werden müssen
						System.out
								.println("Typ von: " + columnOld.getName() + " muss zu  " + columnNew.getType()
										+ " mit der Größe " + columnNew.getSize() + " geändert werden");
						break;
					}
					break;
				}
			}
		}
	}

	protected void nullable(List<Column> allColumnsOld, List<Column> allColumnsNew) {

		for (Column columnOld : allColumnsOld) {
			for (Column columnNew : allColumnsNew) {
				if (StringUtils.equals(columnNew.getName(), columnOld.getName())) {
					if (columnNew.isNullable() != columnOld.isNullable()) {
						// der boolean check muss richtige gesetzt werden je
						// nach dem soll die funktion nullable oder nciht
						// nullabel sein
						System.out.println("Typ von: " + columnNew.getName() + " muss zu "
								+ columnNew.isNullable() + " geändert werden ");
						break;
					}
					break;
				}
			}
		}
	}

}
