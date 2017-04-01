package logic;

import java.util.List;

import model.Data;

public class DataComparer {

	public void compareData(List<Data> oldDataList, List<Data> newDataList, String tableName) {

		
		
		for (Data oldData : oldDataList ) {
			for (Data newData : newDataList) {
				System.out.println(oldData.getValue() + " ** "+ newData.getValue() + " ** "+ oldData.getColumnName() + " ** "+  newData.getColumnName());
		
				if (!oldData.getValue().equals(newData.getValue())) {
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
