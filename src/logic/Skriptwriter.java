package logic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Skriptwriter {

	public Skriptwriter() {
	}

	/**
	 * Funktion die überprüft ob ein Updateskript im ausgewählten Pfad
	 * existiert. Wenn nicht wird ein Updateskript erstellt.
	 * 
	 * @throws IOException
	 */
	public void findScript() throws IOException {
		Path path = Paths.get("C:/Users/Dev/Abschlussprüfung Ian/file.sql");
		if (Files.notExists(path)) {
			this.createScript();
		}
	}

	/**
	 * Funkion um das Updateskript zu erstellen.
	 * 
	 * @throws IOException
	 */
	public void createScript() throws IOException {
		File file = new File("C:/Users/Dev/Abschlussprüfung Ian/file.sql");
		file.createNewFile();
		System.out.println("File wurde erstellt");
	}

	/**
	 * Funktion um ein String in ein File zu schreiben. Das nötige String ist
	 * ein SQL-Statement und kommt aus der SQLStatements Klasse. Im Statement
	 * steht eine SQL Anweisung.
	 * 
	 * @param scriptCmd
	 */
	public void writeScript(String scriptCmd) {
		try {
			FileWriter outputStream = new FileWriter("C:/Users/Dev/Abschlussprüfung Ian/file.sql", true);
			BufferedWriter bw = new BufferedWriter(outputStream);
			String n = System.getProperty("line.separator");

			bw.flush();
			bw.write(n);
			bw.write(scriptCmd);
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}