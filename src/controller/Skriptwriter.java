package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

/**
 * Erstellt ein Updateskript und schreibt SQL Statements hinein. Die SQL
 * Statements erhält das Programm über die Klasse SQLStatments.
 * 
 * @author Ian Noack
 *
 */
public class Skriptwriter {
	private File outputFile;
	private boolean metaInfoExists;

	public Skriptwriter(File outputFile) {
		this.outputFile = outputFile;
		this.metaInfoExists = false;
	}

	/**
	 * Funktion um ein String in ein File zu schreiben. Das nötige String ist
	 * ein SQL-Statement und kommt aus der SQLStatements Klasse. Im Statement
	 * steht eine SQL Anweisung. Desweiteren erhält das Skript am Anfang
	 * Informationen zum Erstellungszeitraum und zur Nutzung.
	 * 
	 * @param scriptCmd Enthält die SQL-Anweisung aus SQLStatements Klasse.
	 * @param targetSchemaName  Enthält den Namen des Zielschemas.
	 * @throws IOException
	 */
	public void writeScript(String scriptCmd, String targetSchemaName) throws IOException {
		FileWriter outputStream = new FileWriter(outputFile, true);
		BufferedWriter bw = new BufferedWriter(outputStream);

		if (!metaInfoExists && targetSchemaName != null) {

			// Erstellungsdatum und die Nutzungsinformationen an die
			// erste Stelle geschrieben. Das schreiben der Informationen ist
			// einmalig.
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			String time = timestamp.toString();
			String manual = " Führen Sie das Skript gegen das Zielschema: " + targetSchemaName + " aus.";
			String info = time + manual;
			bw.write(info);

			metaInfoExists = true;
		}

		String n = System.getProperty("line.separator");
		bw.flush();
		bw.write(n);
		bw.write(scriptCmd);
		bw.close();
	}
}