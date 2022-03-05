package me.itscomits.files.SQLFile.database.querys;

import java.sql.Connection;
import java.sql.SQLException;

import java.sql.Statement;

public class CreateQuery extends Query {
    private boolean firstValue;
    private String sql;
    
    public CreateQuery(final Connection connection, final String sql) {
        super(connection);
    	this.sql = sql;
        this.firstValue = true;
    }
    
    public CreateQuery create(final String value) {
        if (!this.firstValue) {
            this.sql = this.sql.substring(0, this.sql.length() - 1);
            this.sql = String.valueOf(this.sql) + ", ";
        }
        else {
            this.firstValue = false;
        }
        this.sql = String.valueOf(this.sql) + value + ")";
        return this;
    }
    
    public void execute() {
        try {
            final Statement statement = this.connection.createStatement();
            statement.execute(this.sql);
            statement.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
