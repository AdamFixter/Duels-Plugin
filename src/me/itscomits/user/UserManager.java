package me.itscomits.user;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import me.itscomits.files.SQLFile.PlayerData.Execute;
import me.itscomits.files.SQLFile.PlayerData.Table;
import me.itscomits.files.SQLFile.database.DatabaseManager;

public class UserManager {
	

	public UserManager() {}
	
	private static UserManager um = new UserManager();
	public static UserManager getManager() {
		return um;
	}
	
	private List<User> users = new ArrayList<User>();

	private boolean userExists(Player p) {
		for (User user : users) {
			if (user.getUUID().equals(p.getUniqueId())) {
				return true;
			}
		}
		return false;
	}
	private boolean userExists(User user) {
		for (User users : users) {
			if (users.equals(user)) {
				return true;
			}
		}
		return false;
	}
	public User getUser(Player p) {
		for (User user : users) {
			if (user.getUUID().equals(p.getUniqueId())) {
				return user;
			}
		}
		return null;
	}

	public void addUser(Player p) {
		if (!userExists(p)) { //Has not joined after restart
			p.sendMessage("Loading stats..");
			DatabaseManager dm = DatabaseManager.getManager();
			User user = new User(p.getUniqueId());
			
			if (dm.getPlayerData().containsUser(p.getUniqueId().toString(), Table.OVERALL.getTableName())) { //Has joined before restart
				p.sendMessage("Getting your stats from the database");
				dm.getPlayerData().execute(Execute.USER_GET, user, null);
			} else {
				p.sendMessage("Play a game to be added to the database");
			}
			// add the user to the list. 
			users.add(user); 
		}
	}
	
	
	public void resetUser(User user) {
		if (userExists(user)) {
			UUID uuid = user.getUUID();
			users.remove(user);
			new User(uuid);
			//Update to database
		}
	}
	
	public void saveUser(User user) {
		DatabaseManager.getManager().getPlayerData().execute(Execute.USER_SAVE, user, null);
	}
}
