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
	SQLStatements sentStmt = new SQLStatements();
	
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
	protected void differColumn(List<Column> columnOld, List<Column> columnNew, String tableName,
			Connection oldSchema, Connection newSchema) throws SQLException {
		
		missingColumn(columnOld, columnNew, tableName, newSchema);
		wrongDatatypSize(columnOld, columnNew, tableName);
		nullable(columnOld, columnNew,tableName);
		unwantedColumn(columnOld, columnNew, tableName, oldSchema, newSchema);
		
		
		
	}

	/**
	 * STRUKTUR:  Funktion überprüft ob eine Spalte zuviel ist und löscht diese dann.
	 * DATEN: Vergleichen der bestehenden Spaltendaten, anschließend inkongruente Daten ändern.
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

					// ÄNDERUNG DATEN: Vergleichen der bestehenden Spaltendaten, anschließend inkongruente Daten ändern.
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
			// ÄNDERUNG STRUKTUR: Überflüsige Zeile gelöscht
			if (columnNotThere == true) {
				sentStmt.drop(tableName, columnOld.getName());
			}
		}
	}

	/**
	 * STRUKTUR: Funktion die eine fehlende Spalte hinzufügt.
	 * DATEN: Die zuvor hinzugefügte Spalte erhält die zugehörigen Daten
	 * @param allColumnsOld
	 * @param allColumnsNew
	 * @param tableName
	 * @param newSchema
	 * @throws SQLException
	 */
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
				// ÄNDERUNG STRUKTUR: Fehlende Spalte wird hinzugefügt.
				sentStmt.insert(tableName, columnNew.getName(), columnNew.getType(), columnNew.getSize());

				// ÄNDERUNG DATEN: Zuvor erstellte Spalte erhält jetzt die zugehörigen Daten.
				listOfnewData = dataCrawler.crawlData(newSchema, columnNew.getName(), tableName,
						columnNew.getType());
				dataComparer.newColumnData(listOfnewData, tableName);
			}
		}
	}

/**
 * Überprüfung und korrektur der Datentypgröße
 * @param allColumnOld
 * @param allColumnNew
 * @param tableName
 */
	protected void wrongDatatypSize(List<Column> allColumnOld, List<Column> allColumnNew, String tableName) {
		for (Column columnOld : allColumnOld) {
			for (Column columnNew : allColumnNew) {
				if (StringUtils.equals(columnNew.getName(), columnOld.getName())) {
					if (StringUtils.equals(columnNew.getType(), columnOld.getType())
							&& (columnNew.getSize() != columnOld.getSize())) {
						sentStmt.modifyDatasize(tableName, columnNew.getName(), columnNew.getType(), columnNew.getSize());
						break;
					}
					break;
				}
			}
		}
	}

	/**
	 * Überprüfen und korrektur des Feldes Nullable
	 * @param allColumnsOld
	 * @param allColumnsNew
	 * @param tableName
	 */
	protected void nullable(List<Column> allColumnsOld, List<Column> allColumnsNew, String tableName) {

		for (Column columnOld : allColumnsOld) {
			for (Column columnNew : allColumnsNew) {
				if (StringUtils.equals(columnNew.getName(), columnOld.getName())) {
					if (columnNew.isNullable() != columnOld.isNullable()) {
						sentStmt.modifyNullable(tableName, columnNew.getName(), columnNew.getType(), columnNew.isNullable(), columnNew.getSize());
						break;
					}
					break;
				}
			}
		}
	}

}
