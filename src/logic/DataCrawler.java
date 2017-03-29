package logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import model.Column;
import model.Row;

public class DataCrawler {

	public List<Row> crawlData(Connection conn, String tableName, List<Column> columnMetaData) throws SQLException {
		System.out.println("Crawling the data for the table " + tableName);
		Statement stmt = conn.createStatement();
		String sql = "SELECT * FROM " + tableName;
		ResultSet rs = stmt.executeQuery(sql);
		List<Row> datasInsideTable = new ArrayList<Row>();

		while (rs.next()) {
			Row r = new Row();
			for (Column column : columnMetaData) {
				if (StringUtils.equals(column.getType(), "NUMBER")) {
					r.addRow(column.getName(), rs.getInt(column.getName()));
				}
				if (StringUtils.equals(column.getType(), "VARCHAR2")) {
					r.addRow(column.getName(), rs.getString(column.getName()));
				}
				if (StringUtils.equals(column.getType(), "DATE")) {
					r.addRow(column.getName(), rs.getDate(column.getName()));
				}
			}
			datasInsideTable.add(r);
		}
		return datasInsideTable;
	}

}
