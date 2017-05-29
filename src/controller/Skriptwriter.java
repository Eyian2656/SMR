package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
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
	 * steht eine SQL Anweisung.
	 * 
	 * @param scriptCmd
	 *            Erhält die SQL-Anweisung aus SQLStatements Klasse.
	 * @throws IOException
	 */
	public void writeScript(String scriptCmd, String targetSchemaName) throws IOException {
		FileWriter outputStream = new FileWriter(outputFile, true);
		BufferedWriter bw = new BufferedWriter(outputStream);

		if (!metaInfoExists) {

			// Schickt das Erstellungsdatum und die Nutzungsinformationen an den
			// Skriptwriter. Diese Informationen werden im Skriptwriter an die
			// erste Stelle geschrieben.
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			String time = timestamp.toString();
			String manual = " Führen Sie das skript gegen das Zielschema: " + targetSchemaName + " aus.";
			String info = time + manual;
			bw.write(info);

			metaInfoExists = true;
		}

		String n = System.getProperty("line.separator");

		bw.flush();
		for (int i = 0; i < 100; i++) {
			bw.write(n);
		}
		bw.write(scriptCmd);
		bw.close();
	}

	/**
	 * Schreibt das Erstellungsdatum und Nutzungsinformationen in das Skript
	 * 
	 * @param info
	 *            Enthält Timestap und Nutgunsinformationen
	 * @throws IOException
	 */
	public void writeInfo(String info) throws IOException {

		RandomAccessFile f = new RandomAccessFile(outputFile, "rw");
		f.seek(0);
		f.write(info.getBytes());
		f.close();
	}

}