package logic;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import model.Column;
import model.Data;

public class DataComparer {

	
	public void compareData(List<Data> oldDataList, List<Data> newDataList) {

		for (Data oldData : oldDataList) {
			for(Data newData : newDataList){
				if(!oldData.getValue().equals(newData.getValue())){
					System.out.println("Update Tablename SET "+ oldData.getColumnName() + " = " + newData.getValue() + " WHERE NR = " + oldData.getNr() );
				}
				break;
					
			}
			}
	}

//	// Vergleichen basiert auf spalten name
//	public void compareByColumnName(Data oldData, Data newData, Column column) {
//		String columnName = column.getName();
//		String columnType = column.getType();
//
//		if (StringUtils.equals(columnType, "VARCHAR2")) {
//			String oldValue = (String) oldData.getValue(columnName);
//			String newValue = (String) newData.getValue(columnName);
//			if (!StringUtils.equals(oldValue, newValue)) {
//				System.out.println("STRING DIFF-Daten aus row nr " + oldData.getValue("NR")
//						+ " hat unterschiedliche Werte. Old hat " + oldValue + " und die neue hat " + newValue);
//			}
//		}
//
//		if (StringUtils.equals(columnType, "NUMBER")) {
//			int oldValue = (Integer) oldData.getValue(columnName);
//			int newValue = (Integer) newData.getValue(columnName);
//			if (oldValue != newValue) {
//				System.out.println("INT DIFF-Daten aus row nr " + oldData.getValue("NR")
//						+ " hat unterschiedliche Werte. Old hat " + oldValue + " und die neue hat " + newValue);
//			}
//		}
//
//		if (StringUtils.equals(columnType, "DATE")) {
//			Date oldValue = (Date) oldData.getValue(columnName);
//			Date newValue = (Date) newData.getValue(columnName);
//
//			if (oldValue != null && newValue != null) {
//				if (!oldValue.equals(newValue)) {
//					System.out.println("DATE DIFF-Daten aus row nr " + oldData.getValue("NR")
//							+ " hat unterschiedliche Werte. Old hat " + oldValue + " und die neue hat " + newValue);
//				}
//			}
//		}
//
//	}
//
//	// Finde row basiert auf NR-Spalte
//	public Data findRow(List<Data> dataRows, int nr) {
//		for (Data dataRow : dataRows) {
//			if (dataRow != null) {
//				int value = (Integer) dataRow.getValue("NR");
//				if (value == nr) {
//					return dataRow;
//				}
//			}
//		}
//		return null;
//	}
}
