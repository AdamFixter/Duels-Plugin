package me.itscomits.files.SQLFile.database.querys;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SelectQuery extends Query {
    private boolean and;
    private PreparedStatement prest;
    private final List<Object> values;
    private String sql;
    
    public SelectQuery(final Connection connection, final String sql) {
        super(connection);
        this.sql = sql;
        this.and = false;
        this.values = new ArrayList<Object>();
    }
    
    public SelectQuery where(final String key, final Object value) {
        if (this.and) {
            this.sql = String.valueOf(this.sql) + " AND";
        }
        else {
            this.sql = String.valueOf(this.sql) + " WHERE";
        }
        this.sql = String.valueOf(this.sql) + " " + key + "=";
        this.values.add(value);
        this.sql = String.valueOf(this.sql) + "?";
        this.and = true;
        return this;
    }
    
    public ResultSet execute() {
        try {
            this.prest = this.connection.prepareStatement(this.sql);
            int i = 1;
            for (final Object object : this.values) {
                this.prest.setObject(i, object);
                ++i;
            }
            return this.prest.executeQuery();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void close() {
        try {
            this.prest.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
