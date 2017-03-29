package logic;
import logic.Skriptwriter;

public class SQLStatements {
	//TODO hier muss noch size
	public void insert(String tableName , String columnName , String datatype){
		String statement = ("ALTER TABLE " + tableName + " ADD " + columnName + datatype);
	}

	public void drop(String tableName , String columnName){
		String statement = ("ALTER TABLE "+ tableName + " DROP COLUMN " + columnName);
	}
	//TODO hier muss noch size
	public void modifyDatatyp(String tableName , String columnName, String datatype){
		String statement = ("ALTER TABLE "+ tableName + " MODIFY " + columnName + datatype);
	}
	
	public void modifyNullable(String tableName , String columnName, String datatyp, Boolean bool){
		if(bool = true){
		String statement = ("ALTER TABLE "+ tableName + " MODIFY " + columnName + datatyp +" null");
		}else{
		String statement = ("ALTER TABLE "+ tableName + " MODIFY " + columnName + datatyp +" not null");
		}
	}
	


}
