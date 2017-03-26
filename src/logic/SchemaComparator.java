package logic;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.Diff;

import model.Column;
import model.Difference;

public class SchemaComparator {

	// Iterate
	public List<Difference> differColumn(List<Column> colsOld, List<Column> colsNew) throws SQLException {
		List<Difference> diffs = new ArrayList<Difference>();

		Difference a = new Difference();
		
		// Check the size
		if (colsOld.size() != colsNew.size()) {
			a.setTyp("SIZE_OF_COLUMN");
			System.out.println("Different size of column");
		}

		// Check the same type & size
		for (Column column : colsOld) {
			Column toBeCompared = findByName(colsNew, column.getName());
			// Wenn die column nicht gefunden in der neuen Tabelle
			if (toBeCompared == null) {
				Difference diff = new Difference();
				System.out.println("Column " + column.getName() + " could not be found in the second table");
				System.out.println("Now looking for the nearest similarity");
				Column similarColumn = findTheLowestDifference(colsNew, column.getName());
				if (similarColumn != null) {
					// TODO Check if the column is in the old table.
					Column similarColumnInOldTable = findByName(colsOld, similarColumn.getName());
					if (similarColumnInOldTable != null) {
						System.out.println("It is possible that the column is dropped");
					} else {
						System.out.println("Found similar column with name " + similarColumn.getName());
					}
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
		return diffs;
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
