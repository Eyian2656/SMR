package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Erstellt und schreibt SQL Statements in das Updateskript
 * 
 * @author Ian
 *
 */
public class Skriptwriter {
	private File outputFile;

	public Skriptwriter(File outputFile) {
		this.outputFile = outputFile;
	}

	/**
	 * Funktion um ein String in ein File zu schreiben. Das nötige String ist
	 * ein SQL-Statement und kommt aus der SQLStatements Klasse. Im Statement
	 * steht eine SQL Anweisung.
	 * 
	 * @param scriptCmd
	 * @throws IOException
	 */
	public void writeScript(String scriptCmd) throws IOException {
		FileWriter outputStream = new FileWriter(outputFile, true);
		BufferedWriter bw = new BufferedWriter(outputStream);
		String n = System.getProperty("line.separator");

		bw.flush();
		bw.write(n);
		bw.write(scriptCmd);
		bw.close();
	}

}