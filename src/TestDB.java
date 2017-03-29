import java.awt.Robot;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import logic.SchemaComparator;
import logic.SchemaCrawler;
import model.Column;
import model.Difference;
import model.Row;

public class TestDB {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AccessV1DB.getInstance().connect();
		AccessV2DB.getInstance().connect();

		Connection conn1 = AccessV1DB.getInstance().getConnection();
		Connection conn2 = AccessV2DB.getInstance().getConnection();

		SchemaCrawler crawler = new SchemaCrawler();
		SchemaComparator comparator = new SchemaComparator();

		List<String> toBeCheckedTable = new ArrayList<String>();
		toBeCheckedTable.add("ATTRIBUT");
		// toBeCheckedTable.add("KANTENTYP");
		// toBeCheckedTable.add("KANTENTYP2ATTRIBUT");
		// toBeCheckedTable.add("KANTENTYP2PANEL");
		// toBeCheckedTable.add("KANTENTYP2ABATTRIBUT");
		// toBeCheckedTable.add("KNOTENTYP");
		// toBeCheckedTable.add("KNOTENTYP2ATTRIBUT");
		// toBeCheckedTable.add("KNOTENTYP2PANEL");
		// toBeCheckedTable.add("KANTENTYP2ABATTRIBUT");
		// toBeCheckedTable.add("PANEL");
		// toBeCheckedTable.add("PANEL2ATTRIBUT");
		// toBeCheckedTable.add("PANEL2PANEL");
		// toBeCheckedTable.add("PANEL2ABATTRIBUT");

		try {
			for (String string : toBeCheckedTable) {
				System.out.println("Now checking table " + string);
				// Crawl the column and convert to a list of column object
				List<Column> attributColumn = crawler.crawlColumns(conn1, string);
				List<Column> attributColumn2 = crawler.crawlColumns(conn2, string);

				Statement stmt = conn1.createStatement();
				String sql = "SELECT * FROM " + string;
				ResultSet rs = stmt.executeQuery(sql);
				List<Row> datasInsideTable = new ArrayList<Row>();

				while (rs.next()) {
					Row r = new Row();
					for (Column column : attributColumn) {
						Map<String, Object> map = new HashMap<String, Object>();
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

				System.out.println("Total row " + datasInsideTable.size());

				// for (Row row : datasInsideTable) {
				// System.out.println(row.getValue("NAME"));
				// }

				// comparator.missingColumn(attributColumn, attributColumn2);
				// comparator.unwantedColumn(attributColumn, attributColumn2);
				List<Difference> differColumn = comparator.differColumn(attributColumn, attributColumn2);
				System.out.println("=============================");
			}

			conn1.close();
			conn2.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
