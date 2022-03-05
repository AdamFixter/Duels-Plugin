package me.itscomits.files.SQLFile.database.errors;
import java.util.logging.Level;

import me.itscomits.ArenaPvP;

public class Error {
    public static void execute(ArenaPvP plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
    }
    public static void close(ArenaPvP plugin, Exception ex){
        plugin.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
    }
}