package logic;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import model.Data;

public class DataComparer {
	SQLStatements sentStmt = new SQLStatements();
	
	/**
	 * Funktion um inkongruente Daten von bestehenden Spalten zu korrigieren.
	 * @param oldDataList
	 * @param newDataList
	 * @param tableName
	 */
	public void compareData(List<Data> oldDataList, List<Data> newDataList, String tableName) {
		for (Data oldData : oldDataList ) {
			for (Data newData : newDataList) {
				if (oldData.getNr() == newData.getNr() && !StringUtils.equals(oldData.getValue(), newData.getValue())) {
					sentStmt.updateData(tableName, newData.getColumnName() , newData.getValue(), oldData.getNr() );
				}	
			}
		}
	}

	/**
	 * Funktion um  Daten einer kürzlich erstellten Spalte hizuzufügen..
	 */
	public void newColumnData(List<Data> newDataList, String tableName) {
		for (Data newData : newDataList) {
			sentStmt.updateData(tableName, newData.getColumnName() , newData.getValue(), newData.getNr() );
		}
	}
}
