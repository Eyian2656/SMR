import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import logic.DataCrawler;
import logic.TableComparer;
import logic.TableCrawler;
import model.Column;


public class TestDB {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AccessV1DB.getInstance().connect();
		AccessV2DB.getInstance().connect();

		Connection conn1 = AccessV1DB.getInstance().getConnection();
		Connection conn2 = AccessV2DB.getInstance().getConnection();

		TableCrawler tableCrawler = new TableCrawler();
		TableComparer tableComparer = new TableComparer();
		List<String> allTableNames= new ArrayList<String>();
		//DataComparer dataComparer = new DataComparer();
		DataCrawler dataCrawler = new DataCrawler();
		
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
			// Hier werden alle Tabellen durchiteriert um diese als string an die einzelnen Methoden zu übergeben
			for (String string : toBeCheckedTable) {
				System.out.println("Now checking table " + string);
				
				// mittels TableCrawler werden die Metadaten aus einer Tabelle geyogen in in ein Objekt gespeicher welches
				// dann verglichen werden kann um die unterschiede festzustellen
				List<Column> oldColumn = tableCrawler.crawlColumns(conn1, string);
				List<Column> newColumn = tableCrawler.crawlColumns(conn2, string);
				allTableNames = tableComparer.differColumn(oldColumn, newColumn, string);
				System.out.println("=============================");
				
				dataCrawler.crawlData(conn1, allTableNames, string, oldColumn);
			
			}

			conn1.close();
			conn2.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

}
