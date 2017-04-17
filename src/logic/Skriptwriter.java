package logic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Skriptwriter {
	private File outputFile;

	public Skriptwriter(File outputFile) {
		this.outputFile = outputFile;
	}

	/**
	 * Funktion um ein String in ein File zu schreiben. Das n�tige String ist
	 * ein SQL-Statement und kommt aus der SQLStatements Klasse. Im Statement
	 * steht eine SQL Anweisung.
	 * 
	 * @param scriptCmd
	 */
	public void writeScript(String scriptCmd) {
		try {
			FileWriter outputStream = new FileWriter(outputFile, true);
			BufferedWriter bw = new BufferedWriter(outputStream);
			String n = System.getProperty("line.separator");

			bw.flush();
			bw.write(n);
			bw.write("------");
			bw.write(scriptCmd);
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}