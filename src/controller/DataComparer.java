package controller;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import model.Data;

/**
 * Vergleicht die Daten einer Tabelle vom Ziel und Quelle Schema.
 * @author Ian
 *
 */
public class DataComparer {
	private SQLStatements sentStmt;

	public DataComparer(SQLStatements sentStmt) {
		this.sentStmt = sentStmt;
	}

	/**
	 * Funktion um inkongruente Daten von bestehenden Spalten zu korrigieren.
	 * 
	 * @param targetDataList
	 * @param sourceDataList
	 * @param tableName
	 * @throws IOException 
	 */
	public void compareData(List<Data> targetDataList, List<Data> sourceDataList, String tableName) throws IOException {
		for (Data targetData : targetDataList) {
			for (Data sourceData : sourceDataList) {
				if (targetData.getNr() == sourceData.getNr() && !StringUtils.equals(targetData.getValue(), sourceData.getValue())) {
					sentStmt.updateData(tableName, sourceData.getColumnName(), sourceData.getValue(), targetData.getNr());
				}
			}
		}
	}

	/**
	 * Funktion um Daten einer kürzlich erstellten Spalte hizuzufügen..
	 * @throws IOException 
	 */
	public void newColumnData(List<Data> targetDataList, String tableName) throws IOException {
		for (Data targetData : targetDataList) {
			sentStmt.updateData(tableName, targetData.getColumnName(), targetData.getValue(), targetData.getNr());
		}
	}
}
