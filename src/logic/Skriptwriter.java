package logic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Skriptwriter {




	public Skriptwriter(){
		
	}
	//TODO die Syso wegmachen , else statment l�schen 
	public void findSkript() throws IOException{
		Path path = Paths.get("C:/Users/Ian/Test/file.sql");
		if(Files.notExists(path)){
			this.createScript();
		}
		else{
			System.out.println("File exestiert schon");
		}
	}
	
	public void createScript() throws IOException {
		File file = new File("C:/Users/Ian/Test/file.sql");
		file.createNewFile();
		System.out.println("File wurde erstellt");
	}
	
	
	public void writeScript(String skriptCmd) {
		try {
			FileWriter outputStream = new FileWriter("C:/Users/Ian/Test/file.sql", true);
			BufferedWriter bw = new BufferedWriter(outputStream);
			String n = System.getProperty("line.separator");
			
			bw.flush();
			bw.write(n);
			bw.write(skriptCmd);
			bw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

}

}