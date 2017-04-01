package logic;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import model.Column;
import model.Data;
import model.Difference;

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
	public List<String> differColumn(List<Column> columnOld, List<Column> columnNew, String tableName,
			Connection oldSchema, Connection newSchema) throws SQLException {

		unwantedColumn(columnOld, columnNew, tableName, oldSchema, newSchema);
		// wrongDatatypSize(columnOld, columnNew);
		// nullable(columnOld, columnNew);

		return dataCrawlerNames(columnNew);

	}

	/**
	 * Diese Funktion überprüft ob eine Spalte zuviel ist und löscht dieses
	 * anschließend
	 * 
	 * @param columnOld
	 * @param columnNew
	 * @throws SQLException
	 */
	protected void unwantedColumn(List<Column> columnOld, List<Column> columnNew, String tableName,
			Connection oldSchema, Connection newSchema) throws SQLException {
		boolean columnNotThere = false;
		List<Data> listOfOldData = new ArrayList<Data>();
		List<Data> listOfnewData = new ArrayList<Data>();
		DataComparer dataComparer = new DataComparer();

		for (Column columnNameOld : columnOld) {

			for (Column columnNameNew : columnNew) {
				if (StringUtils.equals(columnNameNew.getName(), columnNameOld.getName())) {
					columnNotThere = false;
					
					// Nachdem die Struktur Änderung einer Tabelle abeschlossen sind werden
					// die Daten geprüft.
						listOfOldData = dataCrawler.crawlData(oldSchema, columnNameOld.getName(), tableName,
								columnNameOld.getType());
						listOfnewData = dataCrawler.crawlData(newSchema, columnNameNew.getName(), tableName,
								columnNameNew.getType());
						dataComparer.compareData(listOfOldData, listOfnewData, tableName);
			
					break;
				} else {
					columnNotThere = true;
				}
			}
			if (columnNotThere == true) {
				// hier kommt die Funktion zum löschen des Namens oder eine
				// Übergabe des Columns als String um diese ins Skript zu
				// schreiben
				System.out.println("Die zu löschende Spalte ist: " + columnNameOld.getName());
			}
		}
		
	}

	protected void missingColumn(List<Column> columnOld, List<Column> columnNew, String tableName, Connection oldSchema,
			Connection newSchema) throws SQLException {
		boolean columnNotThere = false;
		List<Data> listOfnewData = new ArrayList<Data>();
		DataComparer dataComparer = new DataComparer();

		for (Column columnNameNew : columnNew) {

			for (Column columnNameOld : columnOld) {
				if (StringUtils.equals(columnNameNew.getName(), columnNameOld.getName())) {
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
				System.out.println("Die anzuhängende Spalte ist: " + columnNameNew.getName() + " Mit dem Datentyp "
						+ columnNameNew.getType() + " " + columnNameNew.getSize());

				// Die neue Zeile braucht Daten. oldColumn daten werden null
				// sein ( da sie noch nicht existiert) und müssen mit newColumn
				// Daten überschrieben werden.
				listOfnewData = dataCrawler.crawlData(newSchema, columnNameNew.getName(), tableName,
						columnNameNew.getType());
				dataComparer.newColumnData(listOfnewData, tableName);
			}
		}
	}

	// TODO hier muss nur die Größe veränder werden und NICHT der Datentyp noch
	// anpassen
	protected void wrongDatatypSize(List<Column> columnOld, List<Column> columnNew) {
		for (Column columnNameOld : columnOld) {
			for (Column columnNameNew : columnNew) {
				if (StringUtils.equals(columnNameNew.getName(), columnNameOld.getName())) {
					if (!StringUtils.equals(columnNameNew.getType(), columnNameOld.getType())
							|| (columnNameNew.getSize() != columnNameOld.getSize())) {
						// hier muss der columnName geholt werden und an der
						// stelle den Datatyp geändert werden müssen
						System.out
								.println("Typ von: " + columnNameOld.getName() + " muss zu  " + columnNameNew.getType()
										+ " mit der Größe " + columnNameNew.getSize() + " geändert werden");
						break;
					}
					break;
				}
			}

		}
	}

	protected void nullable(List<Column> columnOld, List<Column> columnNew) {

		for (Column columnNameOld : columnOld) {
			for (Column columnNameNew : columnNew) {
				if (StringUtils.equals(columnNameNew.getName(), columnNameOld.getName())) {
					if (columnNameNew.isNullable() != columnNameOld.isNullable()) {
						// der boolean check muss richtige gesetzt werden je
						// nach dem soll die funktion nullable oder nciht
						// nullabel sein
						System.out.println("Typ von: " + columnNameNew.getName() + " muss zu "
								+ columnNameNew.isNullable() + " geändert werden ");
						break;
					}
					break;
				}
			}

		}
	}

	protected List<String> dataCrawlerNames(List<Column> columnNew) {
		List<String> allTableNames = new ArrayList<String>();
		for (Column columnNameNew : columnNew) {
			allTableNames.add(columnNameNew.getName());
		}
		return allTableNames;
	}
}
