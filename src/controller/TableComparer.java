package controller;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import model.Column;
import model.Data;

/**
 * Vergleicht die Metastruktur einer Tabelle von zwei Schemas.
 * @author Ian Noack
 *
 */
public class TableComparer {
	private DataCrawler dataCrawler;
	private SQLStatements sentStmt;

	public TableComparer(File outputFile) {
		this.dataCrawler = new DataCrawler();
		this.sentStmt = new SQLStatements(outputFile);
	}


	/**
	 * Die aufzurufende Funktion der Klasse TableComparer zu starten. Sie
	 * enthält alle Daten um in weiter Methoden um herauszufinden welche
	 * Strukturen angepasst werden müssen.Anschließend werden die Unterschiede
	 * an die Klasse SQLStatements geschickt. Returned werden die TableNames der
	 * neuen Tabelle welches gebraucht wird um den Daten zu vergleiche.
	 * 
	 * @param columnTarget
	 * @param columnSource
	 * @param conn
	 * @param tableName
	 * @param getColNames
	 * @return
	 * @throws Exception 
	 */
	public void differColumn(List<Column> columnTarget, List<Column> columnSource, String tableName, Connection targetSchema,
			Connection sourceSchema) throws Exception {

		missingColumn(columnTarget, columnSource, tableName, sourceSchema);
		wrongDatatypSize(columnTarget, columnSource, tableName);
		nullable(columnTarget, columnSource, tableName);
		unwantedColumn(columnTarget, columnSource, tableName, targetSchema, sourceSchema);

	}

	/**
	 * STRUKTUR: Funktion überprüft ob eine Spalte zuviel ist und löscht diese
	 * dann. DATEN: Vergleichen der bestehenden Spaltendaten, anschließend
	 * werden inkongruente Daten ändern.
	 * 
	 * @param allColumnsTarget
	 * @param allColumnsSource
	 * @throws Exception 
	 */
	protected void unwantedColumn(List<Column> allColumnsTarget, List<Column> allColumnsSource, String tableName,
			Connection targetSchema, Connection sourceSchema) throws Exception {
		boolean columnNotThere = false;
		List<Data> listOfTargetData = new ArrayList<Data>();
		List<Data> listOfNewData = new ArrayList<Data>();
		DataComparer dataComparer = new DataComparer(sentStmt);

		for (Column columnTarget : allColumnsTarget) {

			for (Column columnSource : allColumnsSource) {
				if (StringUtils.equals(columnSource.getName(), columnTarget.getName())) {
					columnNotThere = false;

					// ÄNDERUNG DATEN: Vergleichen der bestehenden Spaltendaten,
					// anschließend inkongruente Daten ändern.
					listOfTargetData = dataCrawler.crawlData(targetSchema, columnTarget.getName(), tableName,
							columnTarget.getType());
					listOfNewData = dataCrawler.crawlData(sourceSchema, columnSource.getName(), tableName,
							columnSource.getType());
					dataComparer.compareData(listOfTargetData, listOfNewData, tableName);

					break;
				} else {
					columnNotThere = true;
				}
			}
			// ÄNDERUNG STRUKTUR: Überflüsige Zeile gelöscht
			if (columnNotThere == true) {
				sentStmt.drop(tableName, columnTarget.getName());
			}
		}
	}

	/**
	 * STRUKTUR: Funktion die eine fehlende Spalte hinzufügt. DATEN: Die zuvor
	 * hinzugefügte Spalte erhält die zugehörigen Daten
	 * 
	 * @param allColumnsTarget
	 * @param allColumnsSource
	 * @param tableName
	 * @param sourceSchema
	 * @throws Exception 
	 */
	protected void missingColumn(List<Column> allColumnsTarget, List<Column> allColumnsSource, String tableName,
			Connection sourceSchema) throws Exception {
		boolean columnNotThere = false;
		List<Data> listOfnewData = new ArrayList<Data>();
		DataComparer dataComparer = new DataComparer(sentStmt);

		for (Column columnNew : allColumnsSource) {

			for (Column columnOld : allColumnsTarget) {
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

				// ÄNDERUNG DATEN: Zuvor erstellte Spalte erhält jetzt die
				// zugehörigen Daten.
				listOfnewData = dataCrawler.crawlData(sourceSchema, columnNew.getName(), tableName, columnNew.getType());
				dataComparer.newColumnData(listOfnewData, tableName);
			}
		}
	}

	/**
	 * Überprüfung und korrektur der Datentypgröße
	 * 
	 * @param allColumnTarget
	 * @param allColumnSource
	 * @param tableName
	 * @throws IOException 
	 */
	protected void wrongDatatypSize(List<Column> allColumnTarget, List<Column> allColumnSource, String tableName) throws IOException {
		for (Column columnTarget : allColumnTarget) {
			for (Column columnSource : allColumnSource) {
				if (StringUtils.equals(columnSource.getName(), columnTarget.getName())) {
					if (StringUtils.equals(columnSource.getType(), columnTarget.getType())
							&& (columnSource.getSize() != columnTarget.getSize())) {
						sentStmt.modifyDatasize(tableName, columnSource.getName(), columnSource.getType(),
								columnSource.getSize());
						break;
					}
					break;
				}
			}
		}
	}

	/**
	 * Überprüfen und korrektur des Feldes Nullable
	 * 
	 * @param allColumnsTarget
	 * @param allColumnsSource
	 * @param tableName
	 * @throws IOException 
	 */
	protected void nullable(List<Column> allColumnsTarget, List<Column> allColumnsSource, String tableName) throws IOException {

		for (Column columnTarget : allColumnsTarget) {
			for (Column columnSource : allColumnsSource) {
				if (StringUtils.equals(columnSource.getName(), columnTarget.getName())) {
					if (columnSource.isNullable() != columnTarget.isNullable()) {
						sentStmt.modifyNullable(tableName, columnSource.getName(), columnSource.getType(),
								columnSource.isNullable(), columnSource.getSize());
						break;
					}
					break;
				}
			}
		}
	}

}
