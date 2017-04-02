package logic;
import logic.Skriptwriter;

public class SQLStatements {
	Skriptwriter scriptwriter = new Skriptwriter();
	//TODO hier muss noch size
	public void insert(String tableName , String columnName , String datatype, int datasize){
		String statement = ("ALTER TABLE " + tableName + " ADD " + columnName + " " + datatype + " " + datasize);
		scriptwriter.writeScript(statement);
		
	}

	public void drop(String tableName , String columnName){
		String statement = ("ALTER TABLE "+ tableName + " DROP COLUMN " + columnName);
		scriptwriter.writeScript(statement);
		
	}
	//TODO hier muss noch size
	public void modifyDatasize(String tableName , String columnName, String datatype, int datasize){
		String statement = ("ALTER TABLE "+ tableName + " MODIFY (" + columnName +" "+ datatype +" ("+ datasize + "));");
		scriptwriter.writeScript(statement);
	}
	
	public void modifyNullable(String tableName , String columnName, String datatyp, Boolean bool){
		if(bool = true){
		String statement = ("ALTER TABLE "+ tableName + " MODIFY " + columnName + datatyp +" null");
		scriptwriter.writeScript(statement);
		}else{
		String statement = ("ALTER TABLE "+ tableName + " MODIFY " + columnName + datatyp +" not null");
		scriptwriter.writeScript(statement);
		}
	}
	


}
