package controller;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import model.Data;

/**
 * Der DataComparer funktioniert ähnlich wie der TableComparer, hat aber weniger
 * Anweisungen. Hier werden die Daten einer Tabelle auf Inkongruenz geprüft.
 * Wenn differrenzen identifiziert werden, wird über die Klasse SQLStatements
 * die nötigen Anweisungen in das Updateskript geschrieben.
 * 
 * DataCrawler formatierte Objekte, welche die Funktionen compareData() und newColumnData() vearbeiten. 
 * @author Ian Noack
 *
 */
public class DataComparer {
	private SQLStatements sentStmt;

	public DataComparer(SQLStatements sentStmt) {
		this.sentStmt = sentStmt;
	}

	/**
	 * Die Funktion compareData() vergleicht alle Daten einer Tabelle und bei differenzen schickt sie die zu korrigierende Stelle an die Klasse SQLStatements. 
	 * 
	 * @param targetDataList Liste der Ziel Werte.
	 * @param sourceDataList Liste der Quelle Werte.
	 * @param tableName Tabellenname der momentan geprüft wird
	 * @throws IOException
	 */
	public void compareData(List<Data> targetDataList, List<Data> sourceDataList, String tableName) throws IOException {
		for (Data targetData : targetDataList) {
			for (Data sourceData : sourceDataList) {
				if (targetData.getNr() == sourceData.getNr()
						&& !StringUtils.equals(targetData.getValue(), sourceData.getValue())) {
					sentStmt.updateData(tableName, sourceData.getColumnName(), sourceData.getValue(),
							targetData.getNr());
				}
			}
		}
	}

	/**
	 * Die Funktion newColumnData() wird nur aufgerufen wenn eine neue Spalte hinzukommt. Dann müssen keine Daten verglichen werden, sondern nur Daten von Quelle Schema auf das Ziel Schema kopiert werden.
	 * 
	 * @throws IOException
	 */
	public void newColumnData(List<Data> targetDataList, String tableName) throws IOException {
		for (Data targetData : targetDataList) {
			sentStmt.updateData(tableName, targetData.getColumnName(), targetData.getValue(), targetData.getNr());
		}
	}
}
