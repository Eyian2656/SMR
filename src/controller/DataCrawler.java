package controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import model.Data;

/**
 * Diese Klasse dient zur Erstellung eine Liste welche ein mapping enthält. Die
 * Liste enthält alle Daten einer Tabelle. Sie wird gemappt und besser
 * navigieren zu können und um später die zu änderende Stelle zu identifizieren.
 * 
 * @author Dev
 *
 */
public class DataCrawler {
	DateFormat df = new SimpleDateFormat("dd-mmm-yyyy");

	public List<Data> crawlData(Connection conn, String columnName, String tablename, String datatype)
			throws Exception {
		List<Data> dataInsideTable = new ArrayList<Data>();
		Statement stmt = conn.createStatement();
		String sqlStmt = "SELECT NR , " + columnName + " FROM " + tablename;
		try {
			ResultSet rs = stmt.executeQuery(sqlStmt);

			while (rs.next()) {
				Data tableData = new Data();
				tableData.setNr(rs.getBigDecimal(1).intValueExact());

				// Überprüfung des -auf der DB hinterlegten- Datentyps um diese
				// als String in ein Objekt zu parsen.
				if (StringUtils.equals(datatype, "NUMBER")) {
					tableData.setValue(String.valueOf(rs.getInt(2)));
				}
				if (StringUtils.equals(datatype, "VARCHAR2")) {
					tableData.setValue(rs.getString(2));
				}
				if (StringUtils.equals(datatype, "DATE")) {
					if (rs.getDate(2) == null) {
						tableData.setValue("null");
					} else {
						tableData.setValue(df.format(rs.getDate(2)));
					}
				}
				tableData.setColumnName(columnName);
				dataInsideTable.add(tableData);
			}
			return dataInsideTable;
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (Exception e) {
				throw e;
			};
		}
	}

}
