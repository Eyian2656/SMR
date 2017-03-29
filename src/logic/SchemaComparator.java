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
	public List<Difference> differColumn(List<Column> columnOld, List<Column> colsNew) throws SQLException {
		List<Difference> diffs = new ArrayList<Difference>();
		return diffs;
	}

	// Difference a = new Difference();

	// // Check the same type & size
	//
	// // Wenn die column nicht gefunden in der neuen Tabelle
	// if (toBeCompared == null) {
	// Difference diff = new Difference();
	// System.out.println("Column " + column.getName() + " could not be found in
	// the second table");
	// System.out.println("Now looking for the nearest similarity");
	// Column similarColumn = findTheLowestDifference(colsNew,
	// column.getName());
	// if (similarColumn != null) {
	// // TODO Check if the column is in the old table.
	// Column similarColumnInOldTable = findByName(columnOld,
	// similarColumn.getName());
	// if (similarColumnInOldTable != null) {
	// System.out.println("It is possible that the column is dropped");
	// } else {
	// System.out.println("Found similar column with name " +
	// similarColumn.getName());
	// }
	// }
	// } else {
	// if (!StringUtils.equals(column.getType(), toBeCompared.getType())) {
	// System.out.println(column.getName() + " has a different type now");
	// } else if (column.getSize() != toBeCompared.getSize()) {
	// System.out.println(column.getName() + " has a different size now. It
	// changed from "
	// + column.getSize() + " to " + toBeCompared.getSize());
	// } else if (column.isNullable() != toBeCompared.isNullable()) {
	// System.out.println(column.getName() + " has a different nullable now. It
	// changed from "
	// + column.isNullable() + " to " + toBeCompared.isNullable());
	// }
	// }
	// }
	// return diffs;
	// }

	/**
	 * Diese Funktion �berpr�ft ob eine Spalte zuviel ist und l�scht dieses
	 * anschlie�end
	 * 
	 * @param columnOld
	 * @param columnNew
	 */
	public void unwantedColumn(List<Column> columnOld, List<Column> columnNew) {
		boolean columnNotThere = false;

		for (Column columnNameOld : columnOld) {
			for (Column columnNameNew : columnNew) {
				if (StringUtils.equals(columnNameNew.getName(), columnNameOld.getName())) {
					columnNotThere = false;
					break;
				} else {
					columnNotThere = true;
				}
			}
			if (columnNotThere == true) {
				// hier kommt die Funktion zum l�schen des Namens oder eine
				// �bergabe des Columns als String um diese ins Skript zu
				// schreiben
				System.out.println("Die zu l�schende Spalte ist: " + columnNameOld.getName());
			}
		}
	}

	public void missingColumn(List<Column> columnOld, List<Column> columnNew) {
		boolean columnNotThere = false;

		for (Column columnNameNew : columnNew) {

			for (Column columnNameOld : columnOld) {
				if (StringUtils.equals(columnNameNew.getName(), columnNameOld.getName())) {
					columnNotThere = false;
					break;
				} else {
					columnNotThere = true;
				}
			}
			if (columnNotThere == true) {
				// hier kommt die Funktion zum hinzuf�gen des Namens oder eine
				// �bergabe des Columns als String um diese ins Skript zu
				// schreiben
				System.out.println("Die zu a Spalte ist: " + columnNameNew.getName());
			}
		}
	}

}
