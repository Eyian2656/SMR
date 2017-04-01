package logic;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import model.Data;

public class DataComparer {

	public void compareData(List<Data> oldDataList, List<Data> newDataList, String tableName) {

		
		
		for (Data oldData : oldDataList ) {
			for (Data newData : newDataList) {
				
				if (oldData.getNr() == newData.getNr() && !StringUtils.equals(oldData.getValue(), newData.getValue())) {
					System.out.println("Update "+ tableName + " SET " +
					oldData.getColumnName() + " = " + newData.getValue()
					+ " WHERE NR = " + oldData.getNr());
					
				}
				
			}
			
		}
		System.out.println(">>>>>>>>>>>>>>>>>>>>><<<<<<<<<<<<<<<<<<<");
	}

	/**
	 * Hier werden die Daten einer kürzlich erstellten Reihe hizugefügt.
	 */
	public void newColumnData(List<Data> newDataList, String tableName) {
		for (Data newData : newDataList) {
			System.out.println("INSERT INTO " + tableName + " (" + newData.getColumnName() + ") VALUES " + " ( "
					+ newData.getValue() + " );");
		}
	}
}
