package me.itscomits.utils;

import me.itscomits.files.SQLFile.database.Database;

public class DatabaseData {
	private Database database;
	public DatabaseData(Database database) {
		this.database = database;
	}
	public Database getDatabase() {
		return this.database;
	}
}
