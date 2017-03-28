import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import logic.SchemaComparator;
import logic.SchemaCrawler;
import model.Column;


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
		toBeCheckedTable.add("KANTENTYP");
		toBeCheckedTable.add("KANTENTYP2ATTRIBUT");
		toBeCheckedTable.add("KANTENTYP2PANEL");
		toBeCheckedTable.add("KANTENTYP2ABATTRIBUT");
		toBeCheckedTable.add("KNOTENTYP");
		toBeCheckedTable.add("KNOTENTYP2ATTRIBUT");
		toBeCheckedTable.add("KNOTENTYP2PANEL");
		toBeCheckedTable.add("KANTENTYP2ABATTRIBUT");
		toBeCheckedTable.add("PANEL");
		toBeCheckedTable.add("PANEL2ATTRIBUT");
		toBeCheckedTable.add("PANEL2PANEL");
		toBeCheckedTable.add("PANEL2ABATTRIBUT");
		toBeCheckedTable.add("TABELLENATTRIBUT");

		try {
			for (String string : toBeCheckedTable) {
				System.out.println("Now checking table " + string);
				// Crawl the column and convert to a list of column object
				List<Column> attributColumn = crawler.crawlColumns(conn1, string);
				List<Column> attributColumn2 = crawler.crawlColumns(conn2, string);

		 comparator.differColumn(attributColumn, attributColumn2);
				System.out.println("=============================");
			}

			conn1.close();
			conn2.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
