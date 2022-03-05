package me.itscomits.files.SQLFile.database.querys;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InsertQuery extends Query {

    private boolean firstValue;
    private final List<Object> values;
	private String sql;
    
    public InsertQuery(final Connection connection, final String sql) {
        super(connection);
        this.sql = sql;
        this.firstValue = true;
        this.values = new ArrayList<Object>();
    } 
    
    public InsertQuery insert(final String insert) {
        this.sql = String.valueOf(this.sql) + insert + ", ";
        return this;
    }
    
    public InsertQuery value(final Object value) {
        this.values.add(value);
        this.sql = this.sql.substring(0, this.sql.length() - 1);
        if (this.firstValue) {
            this.sql = this.sql.substring(0, this.sql.length() - 1);
            this.sql = String.valueOf(this.sql) + ") VALUES (?)";
            this.firstValue = false;
        }
        else {
            this.sql = String.valueOf(this.sql) + ", ?)";
        }
        return this;
    }
    
    public void execute() {
        try {
            if (this.connection == null) {
                System.out.println("No Connection !");
            }
            if (this.sql == null) {
                System.out.println("Request is null !");
            }
            final PreparedStatement prest = this.connection.prepareStatement(this.sql);
            int i = 1;
            for (final Object object : this.values) {
                prest.setObject(i, object);
                ++i;
            }
            prest.executeUpdate();
            prest.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
