package me.itscomits.files.SQLFile.database.tables;

import me.itscomits.arena.category.Category;
import me.itscomits.files.SQLFile.database.Database;

public class Table  {

	private Database database;

	protected String tableName = "";
	private String columns = "";
	private String primaryKey = "";
	private String tableCreation = "";


	public Table(Database database, String tableName) {
		this.database = database;
		this.tableName = tableName;
	}
	
	public Table setPrimaryKey(String primaryKey) {
		this.primaryKey = "(`" + primaryKey + "`)";
		return this;
	}

	public Table addColumn(String name, String dataInfo) {
		//System.out.print("Adding the column " + name + " [" + dataInfo + "]");
		if (this.columns.equals("")) {
			this.columns = "`" + name + "` " + dataInfo + ",";
		} else {
			this.columns = this.columns + "`" + name + "` " + dataInfo + ",";
		}
		return this;
	}
	public Table addCategorys(String dataInfo) {
		for (Category category : Category.values()) {
			if (this.columns.equals("")) {
				this.columns = "`" + category.name() + "` " + dataInfo + ",";
			} else {
				this.columns = this.columns + "`" + category.name() + "` " + dataInfo + ",";
			}
		}
		return this;
	}

	public Database build() {
		this.tableCreation = "CREATE TABLE IF NOT EXISTS " + this.tableName + " (" + this.columns + "PRIMARY KEY "
				+ this.primaryKey + ");";
		this.database.loadTable(this);
		return this.database;
	}
	
	public String getTableName() {
		return this.tableName;
	}
	public String getColumns() {
		return this.columns;
	}
	public String getPrimaryKey() {
		return this.primaryKey;
	}
	public String getTableCreation() {
		return this.tableCreation;
	}

}