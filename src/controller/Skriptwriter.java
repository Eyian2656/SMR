package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Erstellt ein Updateskript und schreibt SQL Statements hinein. 
 * Die SQL Statements erhält das Programm über die Klasse SQLStatments.
 * 
 * @author Ian Noack 
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