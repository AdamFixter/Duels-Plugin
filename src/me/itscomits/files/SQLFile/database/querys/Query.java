package me.itscomits.files.SQLFile.database.querys;

import java.sql.Connection;

public class Query {
	protected Connection connection;
	protected String sql;
	
	public Query(Connection connection) {
		this.connection = connection;
	}
	
	public DeleteQuery delete(String tableName) {
		return new DeleteQuery(this.connection, "DELETE FROM " + tableName);
	}
    public CreateQuery create(String tableName) {
        return new CreateQuery(this.connection, "CREATE TABLE IF NOT EXISTS " + tableName + " (");
    }
    
    public UpdateQuery update(String tableName) {
        return new UpdateQuery(this.connection, "UPDATE " + tableName + " SET");
    }
    
    public InsertQuery insert(String tableName) {
        return new InsertQuery(this.connection, "INSERT INTO " + tableName + " (");
    }
    public SelectQuery select(String tableName, String selection) {
        return new SelectQuery(this.connection, "SELECT " + selection + " FROM " + tableName);
    }
    
	
	public Connection getConnection() {
		return this.connection;
	}
	public String getQuery() {
		return this.sql;
	}

}
