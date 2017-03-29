package model;

import java.util.HashMap;
import java.util.Map;

public class Row {
	private int id;
	private Map<String, Object> rows;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public Object getValue(String columnName) {
		return this.rows.get(columnName);
	}

}
