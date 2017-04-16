package controller;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import logic.TableComparer;
import logic.TableCrawler;
import model.Column;
import view.ChooserView;

public class ChooserController {
	private JFrame chooserView;
	private static ChooserController chooserController;
	private Connection oldSchema;
	private Connection newSchema;

	public ChooserController() {
		this.chooserView = new ChooserView();
	}

	public static ChooserController getInstance() {
		if (chooserController == null) {
			chooserController = new ChooserController();
		}
		return chooserController;
	}

	/**
	 * Startet das Abbilden der View
	 * @param newSchema 
	 * @param oldSchema 
	 */
	public void showChooserView(Connection oldSchema, Connection newSchema) {
		this.oldSchema = oldSchema;
		this.newSchema = newSchema;
		chooserView.setVisible(true);
	}
	
	public void hideChooserView() {
		chooserView.setVisible(false);
	}

	public void navigateToMainController() {
		this.hideChooserView();
		MainController.getInstance().showMainView();
	}
	
	public void execute(File file){

		TableCrawler tableCrawler = new TableCrawler();
		TableComparer tableComparer = new TableComparer(file);

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
			// Hier werden alle Tabellen durchiteriert um diese als string an
			// die einzelnen Methoden zu übergeben
			for (String string : toBeCheckedTable) {
				System.out.println("Now checking table " + string);

				// mittels TableCrawler werden die Metadaten aus einer Tabelle
				// gezogen in in ein Objekt gespeicher welches
				// dann verglichen werden kann um die unterschiede festzustellen
				List<Column> oldColumn = tableCrawler.crawlColumns(oldSchema, string);
				List<Column> newColumn = tableCrawler.crawlColumns(newSchema, string);
				tableComparer.differColumn(oldColumn, newColumn, string, oldSchema, newSchema);

				System.out.println("=============================");
			}

			oldSchema.close();
			newSchema.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}
	
}
