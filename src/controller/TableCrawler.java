package controller;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Column;

/**
 * Die Klasse Tablecrawler hat nur eine Funktion und damit auch nur eine Methode. 
 * Sie dient zur erstellen einer Liste mit dem Typen Column. Der Datentyp Column beschreibt 
 * die Struktur einer Spalte.
 * @author Ian Noack
 *
 */
public class TableCrawler {

	public List<Column> crawlColumns(Connection conn, String tableName) throws SQLException {
		DatabaseMetaData meta = conn.getMetaData();
		ResultSet rsColumns = meta.getColumns(null, null, tableName, null);
		List<Column> result = new ArrayList<Column>();

		while (rsColumns.next()) {
			String name = rsColumns.getString("COLUMN_NAME");
			String type = rsColumns.getString("TYPE_NAME");
			int size = rsColumns.getInt("COLUMN_SIZE");
			int nullable = rsColumns.getInt("NULLABLE");
			boolean isNullable = false;
			if (nullable == DatabaseMetaData.columnNullable) {
				isNullable = true;
			}

			Column a = new Column();
			a.setName(name);
			a.setType(type);
			a.setSize(size);
			a.setNullable(isNullable);

			result.add(a);
		}

		return result;
	}

}
