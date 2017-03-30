package model;

import java.util.HashMap;
import java.util.Map;

public class Data {
	
	private Map<String, Object> rows;

	public Map<String, Object> getRows() {
		return rows;
	}

	public void setRows(Map<String, Object> rows) {
		this.rows = rows;
	}
	
	public void addRow(String key, Object value) {
		if (this.rows == null) {
			this.rows = new HashMap<String, Object>();
		}
		rows.put(key, value);
	}

	public Object getValue(String columnName){
		return this.rows.get(columnName);
	}
	
}
