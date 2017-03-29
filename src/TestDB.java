import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import logic.DataComparator;
import logic.DataCrawler;
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

		Connection oldConn = AccessV1DB.getInstance().getConnection();
		Connection newConn = AccessV2DB.getInstance().getConnection();

		SchemaCrawler crawler = new SchemaCrawler();
		DataCrawler dataCrawler = new DataCrawler();

		SchemaComparator comparator = new SchemaComparator();
		DataComparator dataComparator = new DataComparator();

		List<String> toBeCheckedTable = new ArrayList<String>();
		// toBeCheckedTable.add("ATTRIBUT");
		// toBeCheckedTable.add("KANTENTYP");
		// toBeCheckedTable.add("KANTENTYP2ATTRIBUT");
		// toBeCheckedTable.add("KANTENTYP2PANEL");
		// toBeCheckedTable.add("KANTENTYP2ABATTRIBUT");
		toBeCheckedTable.add("KNOTENTYP");
		// toBeCheckedTable.add("KNOTENTYP2ATTRIBUT");
		// toBeCheckedTable.add("KNOTENTYP2PANEL");
		// toBeCheckedTable.add("KNOTENTYP2ABATTRIBUT");
		// toBeCheckedTable.add("PANEL");
		// toBeCheckedTable.add("PANEL2ATTRIBUT");
		// toBeCheckedTable.add("PANEL2PANEL");
		// toBeCheckedTable.add("PANEL2ABATTRIBUT");

		try {
			for (String tableName : toBeCheckedTable) {
				System.out.println("Now checking table " + tableName);
				// Crawl the column and convert to a list of column object
				List<Column> oldColumn = crawler.crawlColumns(oldConn, tableName);
				List<Column> newColumn = crawler.crawlColumns(newConn, tableName);

				List<Row> oldDatas = dataCrawler.crawlData(oldConn, tableName, oldColumn);
				List<Row> newDatas = dataCrawler.crawlData(newConn, tableName, newColumn);

				dataComparator.differData(oldDatas, newDatas, oldColumn, newColumn);

				// comparator.missingColumn(attributColumn, attributColumn2);
				// comparator.unwantedColumn(attributColumn, attributColumn2);

				System.out.println("=============================");
			}

			oldConn.close();
			newConn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
