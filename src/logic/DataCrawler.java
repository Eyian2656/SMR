package logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Column;
import model.Data;

/**
 * Diese Klasse dient zur Erstellung eine Liste welche ein mapping enthält. Die Liste enthält alle Daten einer Tabelle.
 * Sie wird gemappt und besser navigieren zu können und um später die zu änderende Stelle zu identifizieren.
 * @author Dev
 *
 */
public class DataCrawler {
	
	public List<Data> crawlData(Connection conn, List<String> allColumnNames, String tablename, List<Column> columnMetaData) throws SQLException{
		List<Data> dataInsideTable = new ArrayList<Data>();
		Statement stmt = conn.createStatement();
		String sqlStmt = createSQLStmt(allColumnNames, tablename);
		ResultSet rs = stmt.executeQuery(sqlStmt);
		
		while(rs.next()){
			
			Data tableData = new Data();
			tableData.addRow(key, value);
				
			}
		}
		
		
		return dataInsideTable;
	}

	/**
	 * Hier wird ein String erstellt für das SQL Statement, es sollen nicht alle Spalten gezogen werden sondern nur die
	 * die auch in dem neuen Schema enthalten sind.
	 * @param allColumnNames
	 * @param tablename
	 * @return
	 */
	public String createSQLStmt(List<String> allColumnNames, String tablename){
		String columnNameString = "";
		String sqlStmt;
		int i = 0;
		
		for(String name : allColumnNames){	
			columnNameString = columnNameString + name;
			if( i < allColumnNames.size()-1){
				columnNameString = columnNameString + " , ";
			}
			i++;
		}
	
		sqlStmt = "SELECT " + columnNameString + " FROM " + tablename;
		
		return sqlStmt;
	}
}
