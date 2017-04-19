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
 * Vergleicht die Metastruktur einer Tabelle von zwei Schemas
 * @author Ian
 *
 */
public class TableComparer {
	private DataCrawler dataCrawler;
	private SQLStatements sentStmt;

	public TableComparer(File outputFile) {
		this.dataCrawler = new DataCrawler();
		this.sentStmt = new SQLStatements(outputFile);
	}

	public TableComparer(DataCrawler dataCrawler, SQLStatements sentStmt) {
		this.dataCrawler = dataCrawler;
		this.sentStmt = sentStmt;
	}

	/**
	 * Die aufzurufende Funktion der Klasse TableComparer zu starten. Sie
	 * enth�lt alle Daten um in weiter Methoden um herauszufinden welche
	 * Strukturen angepasst werden m�ssen.Anschlie�end werden die Unterschiede
	 * an die Klasse SQLStatements geschickt. Returned werden die TableNames der
	 * neuen Tabelle welches gebraucht wird um den Daten zu vergleiche.
	 * 
	 * @param columnOld
	 * @param columnNew
	 * @param conn
	 * @param tableName
	 * @param getColNames
	 * @return
	 * @throws Exception 
	 */
	public void differColumn(List<Column> columnOld, List<Column> columnNew, String tableName, Connection oldSchema,
			Connection newSchema) throws Exception {

		missingColumn(columnOld, columnNew, tableName, newSchema);
		wrongDatatypSize(columnOld, columnNew, tableName);
		nullable(columnOld, columnNew, tableName);
		unwantedColumn(columnOld, columnNew, tableName, oldSchema, newSchema);

	}

	/**
	 * STRUKTUR: Funktion �berpr�ft ob eine Spalte zuviel ist und l�scht diese
	 * dann. DATEN: Vergleichen der bestehenden Spaltendaten, anschlie�end
	 * inkongruente Daten �ndern.
	 * 
	 * @param allColumnsOld
	 * @param allColumnsNew
	 * @throws Exception 
	 */
	protected void unwantedColumn(List<Column> allColumnsOld, List<Column> allColumnsNew, String tableName,
			Connection oldSchema, Connection newSchema) throws Exception {
		boolean columnNotThere = false;
		List<Data> listOfOldData = new ArrayList<Data>();
		List<Data> listOfnewData = new ArrayList<Data>();
		DataComparer dataComparer = new DataComparer(sentStmt);

		for (Column columnOld : allColumnsOld) {

			for (Column columnNew : allColumnsNew) {
				if (StringUtils.equals(columnNew.getName(), columnOld.getName())) {
					columnNotThere = false;

					// �NDERUNG DATEN: Vergleichen der bestehenden Spaltendaten,
					// anschlie�end inkongruente Daten �ndern.
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
			// �NDERUNG STRUKTUR: �berfl�sige Zeile gel�scht
			if (columnNotThere == true) {
				sentStmt.drop(tableName, columnOld.getName());
			}
		}
	}

	/**
	 * STRUKTUR: Funktion die eine fehlende Spalte hinzuf�gt. DATEN: Die zuvor
	 * hinzugef�gte Spalte erh�lt die zugeh�rigen Daten
	 * 
	 * @param allColumnsOld
	 * @param allColumnsNew
	 * @param tableName
	 * @param newSchema
	 * @throws Exception 
	 */
	protected void missingColumn(List<Column> allColumnsOld, List<Column> allColumnsNew, String tableName,
			Connection newSchema) throws Exception {
		boolean columnNotThere = false;
		List<Data> listOfnewData = new ArrayList<Data>();
		DataComparer dataComparer = new DataComparer(sentStmt);

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
				// �NDERUNG STRUKTUR: Fehlende Spalte wird hinzugef�gt.
				sentStmt.insert(tableName, columnNew.getName(), columnNew.getType(), columnNew.getSize());

				// �NDERUNG DATEN: Zuvor erstellte Spalte erh�lt jetzt die
				// zugeh�rigen Daten.
				listOfnewData = dataCrawler.crawlData(newSchema, columnNew.getName(), tableName, columnNew.getType());
				dataComparer.newColumnData(listOfnewData, tableName);
			}
		}
	}

	/**
	 * �berpr�fung und korrektur der Datentypgr��e
	 * 
	 * @param allColumnOld
	 * @param allColumnNew
	 * @param tableName
	 * @throws IOException 
	 */
	protected void wrongDatatypSize(List<Column> allColumnOld, List<Column> allColumnNew, String tableName) throws IOException {
		for (Column columnOld : allColumnOld) {
			for (Column columnNew : allColumnNew) {
				if (StringUtils.equals(columnNew.getName(), columnOld.getName())) {
					if (StringUtils.equals(columnNew.getType(), columnOld.getType())
							&& (columnNew.getSize() != columnOld.getSize())) {
						sentStmt.modifyDatasize(tableName, columnNew.getName(), columnNew.getType(),
								columnNew.getSize());
						break;
					}
					break;
				}
			}
		}
	}

	/**
	 * �berpr�fen und korrektur des Feldes Nullable
	 * 
	 * @param allColumnsOld
	 * @param allColumnsNew
	 * @param tableName
	 * @throws IOException 
	 */
	protected void nullable(List<Column> allColumnsOld, List<Column> allColumnsNew, String tableName) throws IOException {

		for (Column columnOld : allColumnsOld) {
			for (Column columnNew : allColumnsNew) {
				if (StringUtils.equals(columnNew.getName(), columnOld.getName())) {
					if (columnNew.isNullable() != columnOld.isNullable()) {
						sentStmt.modifyNullable(tableName, columnNew.getName(), columnNew.getType(),
								columnNew.isNullable(), columnNew.getSize());
						break;
					}
					break;
				}
			}
		}
	}

}
