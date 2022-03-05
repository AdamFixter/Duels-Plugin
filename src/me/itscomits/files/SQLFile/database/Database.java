package me.itscomits.files.SQLFile.database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import me.itscomits.ArenaPvP;
import me.itscomits.files.SQLFile.database.errors.Error;
import me.itscomits.files.SQLFile.database.tables.Table;
import me.itscomits.files.SQLFile.Databases;

public class Database {

	protected ArenaPvP plugin;
	private Connection connection;
	private String fileName;
	private Databases databaseType;

	public Database(ArenaPvP plugin, Databases database) {
		this.plugin = plugin;
		this.fileName = database.getFileName();
	}

	
	
	//Create table and columns
	public Table createTable(String tableName) {
		Table table = new Table(this, tableName);
		return table;
	}
	
	// Get Connection
	public Connection getSQLConnection() {
		File dataFolder = new File(this.plugin.getDataFolder(), this.fileName + ".db");
		if (!dataFolder.exists()) {
			try {
				dataFolder.createNewFile();
			} catch (IOException e) {
				plugin.getLogger().log(Level.SEVERE, "File write error: " + this.fileName + ".db");
			}
		}
		try {
			if (connection != null && !connection.isClosed()) {
				return connection;
			}
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
			return connection;
		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, "SQLite exception on initialize", ex);
		} catch (ClassNotFoundException ex) {
			plugin.getLogger().log(Level.SEVERE, "You need the SQLite JBDC library. Google it. Put it in /lib folder.");
		}
		return null;
	}

	// Close Database
	public void close(PreparedStatement ps, ResultSet rs) {
		try {
			if (ps != null)
				ps.close();
			if (rs != null)
				rs.close();
		} catch (SQLException ex) {
			Error.close(plugin, ex);
		}
	}
	
	//Load new table into the database
    public void loadTable(Table table) {
        connection = getSQLConnection();
        try {
            Statement s = connection.createStatement();
            s.executeUpdate(table.getTableCreation());
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initialize(table.getTableName());
    }
    
	// Initialize Connection with table
	public void initialize(String table) {
		connection = getSQLConnection();
		try {
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE UserID = ?");
			ResultSet rs = ps.executeQuery();
			close(ps, rs);

		} catch (SQLException ex) {
			plugin.getLogger().log(Level.SEVERE, "Unable to retreive connection", ex);
		}
	}
	public Connection getConnection() {
		return this.connection;
	}
	public String getFileName() {
		return this.fileName;
	}
	public Databases getDatabaseType() {
		return this.databaseType;
	}
}
