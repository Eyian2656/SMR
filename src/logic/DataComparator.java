package logic;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import model.Column;
import model.Row;

public class DataComparator {

	public void differData(List<Row> oldDatas, List<Row> newDatas, List<Column> oldColumn, List<Column> newColumn) {
		for (Row oldData : oldDatas) {
			Row newData = findRow(newDatas, (Integer) oldData.getValue("NR"));
			// Die daten würde gefunden, für jede spalten würde die rows
			// gecheckt
			if (newData != null) {
				for (Column column : oldColumn) {
					compareByColumnName(oldData, newData, column);
				}
			} else {
				System.out
						.println("The row with nr" + (Integer) oldData.getValue("NR") + " is not found in the new DB");
			}
		}
	}

	// Vergleichen basiert auf spalten name
	public void compareByColumnName(Row oldData, Row newData, Column column) {
		String columnName = column.getName();
		String columnType = column.getType();
		
		if (StringUtils.equals(columnType, "VARCHAR2")) {
			String oldValue = (String) oldData.getValue(columnName);
			String newValue = (String) newData.getValue(columnName);
			if (!StringUtils.equals(oldValue, newValue)) {
				System.out
						.println("STRING DIFF-Daten aus row nr " + oldData.getValue("NR") + " hat unterschiedliche Werte. Old hat "
								+ oldValue + " und die neue hat " + newValue);
			}
		}

		if (StringUtils.equals(columnType, "NUMBER")) {
			int oldValue = (Integer) oldData.getValue(columnName);
			int newValue = (Integer) newData.getValue(columnName);
			if (oldValue != newValue) {
				System.out
						.println("INT DIFF-Daten aus row nr " + oldData.getValue("NR") + " hat unterschiedliche Werte. Old hat "
								+ oldValue + " und die neue hat " + newValue);
			}
		}
		
		
	}

	// Finde row basiert auf NR-Spalte
	public Row findRow(List<Row> rows, int nr) {
		for (Row row : rows) {
			if (row != null) {
				int value = (Integer) row.getValue("NR");
				if (value == nr) {
					return row;
				}
			}
		}
		return null;
	}

	public String getColumnTypeByName(List<Column> columns, String name) {
		for (Column column : columns) {
			if (StringUtils.equals(column.getName(), name)) {
				return column.getType();
			}
		}
		return null;
	}
}
