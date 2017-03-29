import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import logic.DataComparer;
import logic.SchemaCrawler;
import logic.TableComparer;
import model.Column;
import model.Data;


public class TestDB {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AccessV1DB.getInstance().connect();
		AccessV2DB.getInstance().connect();

		Connection conn1 = AccessV1DB.getInstance().getConnection();
		Connection conn2 = AccessV2DB.getInstance().getConnection();

		SchemaCrawler crawler = new SchemaCrawler();
		TableComparer comparator = new TableComparer();
		Data datamodell= new Data();
		DataComparer comparer = new DataComparer();
		
		List<String> toBeCheckedTable = new ArrayList<String>();
//		toBeCheckedTable.add("ATTRIBUT");
//		toBeCheckedTable.add("KANTENTYP");
//		toBeCheckedTable.add("KANTENTYP2ATTRIBUT");
//		toBeCheckedTable.add("KANTENTYP2PANEL");
//		toBeCheckedTable.add("KANTENTYP2ABATTRIBUT");
//		toBeCheckedTable.add("KNOTENTYP");
//		toBeCheckedTable.add("KNOTENTYP2ATTRIBUT");
//		toBeCheckedTable.add("KNOTENTYP2PANEL");
//		toBeCheckedTable.add("KANTENTYP2ABATTRIBUT");
		toBeCheckedTable.add("PANEL");
//		toBeCheckedTable.add("PANEL2ATTRIBUT");
//		toBeCheckedTable.add("PANEL2PANEL");
//		toBeCheckedTable.add("PANEL2ABATTRIBUT");
//		toBeCheckedTable.add("TABELLENATTRIBUT");

		try {
			for (String string : toBeCheckedTable) {
				System.out.println("Now checking table " + string);
				// Crawl the column and convert to a list of column object
				List<Column> attributColumn = crawler.crawlColumns(conn1, string);
				List<Column> attributColumn2 = crawler.crawlColumns(conn2, string);
				datamodell = comparator.differColumn(attributColumn, attributColumn2, conn1, string);
				System.out.println("=============================");
				
				comparer.crawlData(conn1, conn2, datamodell, string);
			}

			conn1.close();
			conn2.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
