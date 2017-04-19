package controller;

import java.io.File;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import model.Data;

/**
 * Vergleicht die Daten einer Tabelle von den zwei Schemas.
 * @author Ian
 *
 */
public class DataComparer {
	private SQLStatements sentStmt;

	public DataComparer(File outputFile) {
		this.sentStmt = new SQLStatements(outputFile);
	}

	public DataComparer(SQLStatements sentStmt) {
		this.sentStmt = sentStmt;
	}

	/**
	 * Funktion um inkongruente Daten von bestehenden Spalten zu korrigieren.
	 * 
	 * @param oldDataList
	 * @param newDataList
	 * @param tableName
	 */
	public void compareData(List<Data> oldDataList, List<Data> newDataList, String tableName) {
		for (Data oldData : oldDataList) {
			for (Data newData : newDataList) {
				if (oldData.getNr() == newData.getNr() && !StringUtils.equals(oldData.getValue(), newData.getValue())) {
					sentStmt.updateData(tableName, newData.getColumnName(), newData.getValue(), oldData.getNr());
				}
			}
		}
	}

	/**
	 * Funktion um Daten einer kürzlich erstellten Spalte hizuzufügen..
	 */
	public void newColumnData(List<Data> newDataList, String tableName) {
		for (Data newData : newDataList) {
			sentStmt.updateData(tableName, newData.getColumnName(), newData.getValue(), newData.getNr());
		}
	}
}
