package logic;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import model.Column;

public class SchemaComparator {

	// Iterate
	public void differColumn(List<Column> cols1, List<Column> cols2) throws SQLException {
		// Check the size
		if (cols1.size() != cols2.size()) {
			System.out.println("Different size of column");
		}

		// Check the same type & size
		for (Column column : cols1) {
			Column toBeCompared = findByName(cols2, column.getName());
			if (toBeCompared == null) {
				System.out.println("Column " + column.getName() + " could not be found in the second table");
				System.out.println("Now looking for the nearest similarity");
				Column similarColumn = findTheLowestDifference(cols2, column.getName());
				if (similarColumn != null) {
					// TODO Check if the column is in the old table.
					System.out.println("Found similar column with name " + similarColumn.getName());
				}
			} else {
				if (!StringUtils.equals(column.getType(), toBeCompared.getType())) {
					System.out.println(column.getName() + " has a different type now");
				} else if (column.getSize() != toBeCompared.getSize()) {
					System.out.println(column.getName() + " has a different size now. It changed from "
							+ column.getSize() + " to " + toBeCompared.getSize());
				} else if (column.isNullable() != toBeCompared.isNullable()) {
					System.out.println(column.getName() + " has a different nullable now. It changed from "
							+ column.isNullable() + " to " + toBeCompared.isNullable());
				}
			}
		}
	}

	protected Column findByName(List<Column> columns, String columnName) {
		for (Column cols : columns) {
			if (StringUtils.equals(cols.getName(), columnName)) {
				return cols;
			}
		}
		return null;
	}

	protected Column findTheLowestDifference(List<Column> columns, String columnName) {
		double distance = 0;
		Column theNearest = null;
		for (Column cols : columns) {
			double jaroWinklerDistance = StringUtils.getJaroWinklerDistance(cols.getName(), columnName);
			if (jaroWinklerDistance >= distance) {
				distance = jaroWinklerDistance;
				theNearest = cols;
			}
		}
		return theNearest;
	}
}
