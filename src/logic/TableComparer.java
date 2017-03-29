package logic;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import model.Column;
import model.Data;
import model.Difference;

public class TableComparer {

	public String tablename; 
	
	// Iterate
	public Data differColumn(List<Column> columnOld, List<Column> columnNew, Connection conn, String tableName) throws SQLException {

		tablename = tableName;
		Data datamodel = new Data();
		
		datamodel = unwantedColumn(columnOld, columnNew);
//		missingColumn(columnOld, columnNew);
//		wrongDatatypSize(columnOld, columnNew);
//		nullable(columnOld, columnNew);
		
		
		return datamodel;
	}
	
/**
 * Diese Funktion überprüft ob eine Spalte zuviel ist und löscht dieses anschließend
 * @param columnOld
 * @param columnNew
 * @throws SQLException 
 */
	protected Data unwantedColumn(List<Column> columnOld, List<Column> columnNew) {
		boolean columnNotThere = false;
		Data datamodel = new Data();
		List<String> holder = new ArrayList<String>();	

		for (Column columnNameOld : columnOld) {
			for (Column columnNameNew : columnNew) {
			
				holder.add(columnNameNew.getName());
				datamodel.setColumnName(holder);
				
				if (StringUtils.equals(columnNameNew.getName(), columnNameOld.getName())) {
					columnNotThere = false;					
					break;
				} else {
					columnNotThere = true;
				}
			}
			if (columnNotThere == true) {
				// hier kommt die Funktion zum löschen des Namens oder eine Übergabe des Columns als String um diese ins Skript zu schreiben
				System.out.println("Die zu löschende Spalte ist: " + columnNameOld.getName());
			}
		}

		return datamodel;
	}

	
	
//	protected void infos4DataComp(List<Column> columnNew, Connection conn, String tableName ) throws SQLException {
//		Data datamodel = new Data();
//		DataComparer comparer = new DataComparer();
//		List<String> holder = new ArrayList<String>();	
//			for (Column columnNameNew : columnNew) {
//				holder.add(columnNameNew.getName());
//				datamodel.setColumnName(holder);
//			}
//			comparer.crawlData(conn, datamodel, tablename);
//		}
//	
	
	
	
	
	
	protected void missingColumn(List<Column> columnOld, List<Column> columnNew) {
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
				// hier kommt die Funktion zum hinzufügen des Namens oder eine
				// Übergabe des Columns als String um diese ins Skript zu
				// schreiben
				System.out.println("Die anzuhängende Spalte ist: " + columnNameNew.getName()+ " Mit dem Datentyp " + columnNameNew.getType() + " " + columnNameNew.getSize());
			}
		}
	}
	//TODO hier muss nur die Größe veränder werden und NICHT der Datentyp noch anpassen
	protected void wrongDatatypSize(List<Column> columnOld, List<Column> columnNew) {
		for (Column columnNameOld : columnOld) {
			for (Column columnNameNew : columnNew) {
				if (StringUtils.equals(columnNameNew.getName(), columnNameOld.getName())) {
					if (!StringUtils.equals(columnNameNew.getType(), columnNameOld.getType()) || (columnNameNew.getSize()!= columnNameOld.getSize())) {
						// hier muss der columnName geholt werden und an der
						// stelle den Datatyp geändert werden müssen
						System.out.println("Typ von: " + columnNameOld.getName() + " muss zu  " + columnNameNew.getType() + " mit der Größe " +columnNameNew.getSize()
								+ " geändert werden");
						break;
					}
					break;
				}
			}

		}
	}

	protected void nullable(List<Column> columnOld, List<Column> columnNew) {

		for (Column columnNameOld : columnOld) {
			for (Column columnNameNew : columnNew) {
				if (StringUtils.equals(columnNameNew.getName(), columnNameOld.getName())) {
					if (columnNameNew.isNullable() != columnNameOld.isNullable()) {
						// der boolean check muss richtige gesetzt werden je nach dem soll die funktion nullable oder nciht nullabel sein
						System.out.println("Typ von: " + columnNameNew.getName() + " muss zu "+ columnNameNew.isNullable() + " geändert werden ");
						break;
					}
					break;
				}
			}

		}
	}
}
	
	
	


//Difference a = new Difference();	

//// Check the same type & size
//
//	// Wenn die column nicht gefunden in der neuen Tabelle
//	if (toBeCompared == null) {
//		Difference diff = new Difference();
//		System.out.println("Column " + column.getName() + " could not be found in the second table");
//		System.out.println("Now looking for the nearest similarity");
//		Column similarColumn = findTheLowestDifference(colsNew, column.getName());
//		if (similarColumn != null) {
//			// TODO Check if the column is in the old table.
//			Column similarColumnInOldTable = findByName(columnOld, similarColumn.getName());
//			if (similarColumnInOldTable != null) {
//				System.out.println("It is possible that the column is dropped");
//			} else {
//				System.out.println("Found similar column with name " + similarColumn.getName());
//			}
//		}
//	} else {
//		if (!StringUtils.equals(column.getType(), toBeCompared.getType())) {
//			System.out.println(column.getName() + " has a different type now");
//		} else if (column.getSize() != toBeCompared.getSize()) {
//			System.out.println(column.getName() + " has a different size now. It changed from "
//					+ column.getSize() + " to " + toBeCompared.getSize());
//		} else if (column.isNullable() != toBeCompared.isNullable()) {
//			System.out.println(column.getName() + " has a different nullable now. It changed from "
//					+ column.isNullable() + " to " + toBeCompared.isNullable());
//		}
//	}
//}
//return diffs;
//}