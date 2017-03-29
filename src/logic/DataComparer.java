package logic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import model.Column;
import model.Data;

public class DataComparer {

	public List<Column> crawlData(Connection conn1, Connection conn2, Data data, String tableName) throws SQLException {

		Statement smt1 = conn1.createStatement();
		Statement smt2 = conn2.createStatement();
		for (String name : data.getColumnName()) {
			
			String sql = " Select * from " + tableName;
			ResultSet oldData = smt1.executeQuery(sql);
			ResultSet newData = smt2.executeQuery(sql);
			int x = 1;
			while (oldData.next() && newData.next()) {
				if(!StringUtils.equals(oldData.getString(name.toString()) , newData.getString(name.toString()))){
				System.out.println("Old: " + x + " Name: " + name + " Data " + oldData.getString(name.toString()));
				System.out.println("New: " + x + " Name: " + name + " Data " + newData.getString(name.toString()));
				x++;}
			}
		}
		return null;
	}

	int rowNumber;

}
