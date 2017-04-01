package logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import model.Data;

/**
 * Diese Klasse dient zur Erstellung eine Liste welche ein mapping enthält. Die Liste enthält alle Daten einer Tabelle.
 * Sie wird gemappt und besser navigieren zu können und um später die zu änderende Stelle zu identifizieren.
 * @author Dev
 *
 */
public class DataCrawler {
	DateFormat df = new SimpleDateFormat("dd-mmm-yyyy");
	
	public List<Data> crawlData(Connection conn, String columnName, String tablename, String datatype) throws SQLException{
		List<Data> dataInsideTable = new ArrayList<Data>();
		Statement stmt = conn.createStatement();
		String sqlStmt = "SELECT NR , " + columnName + " FROM " + tablename;
		ResultSet rs = stmt.executeQuery(sqlStmt);
		
		
		
		while(rs.next()){
			//System.out.println(rs.getInt(1) + " , " + rs.getString(2));
			Data tableData = new Data();
			tableData.setNr( rs.getBigDecimal(1).intValueExact());

			//Überprüfung des Datentyps 
			if (StringUtils.equals(datatype, "NUMBER")) {
				tableData.setValue( String.valueOf(rs.getInt(2)));
			}
			if (StringUtils.equals(datatype, "VARCHAR2")) {
				tableData.setValue( rs.getString(2));
			}
			if (StringUtils.equals(datatype, "DATE")) {
				tableData.setValue( df.format(rs.getDate(2)));
			}	
				tableData.setColumnName(columnName);
			System.out.println(tableData.getColumnName() + " = " +  tableData.getNr() + " = " + tableData.getValue());
				dataInsideTable.add(tableData);	
	}
		return dataInsideTable;
	}

}
