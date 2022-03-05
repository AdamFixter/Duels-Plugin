package me.itscomits.files.SQLFile;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.itscomits.ArenaPvP;
import me.itscomits.arena.category.Category;
import me.itscomits.files.SQLFile.database.Database;
import me.itscomits.files.SQLFile.database.querys.Query;
import me.itscomits.kits.KitsManager;
import me.itscomits.user.CategoryStats;
import me.itscomits.user.User;

public class PlayerData extends Database {
	public enum Table {
		OVERALL("Overall");
		private String tableName;

		private Table(String tableName) {
			this.tableName = tableName;
		}

		public String getTableName() {
			return this.tableName;
		}

	}

	public PlayerData(ArenaPvP plugin) {
		super(plugin, Databases.PLAYER_DATA);
		this.createTable(Table.OVERALL.getTableName()) // (uuid, tWins, tGames, tDeaths, tKills)
				.setPrimaryKey("UserID").addColumn("UserID", "VARCHAR(36)").addColumn("Games", "INT(1000000000)")
				.addColumn("Wins", "INT(1000000000)").addColumn("Kills", "INT(1000000000)")
				.addColumn("Deaths", "INT(1000000000)").build();

		for (Category category : Category.values()) {
			this.createTable(category.name()).setPrimaryKey("UserID").addColumn("UserID", "VARCHAR(36)")
					.addColumn("Winstreak", "INT(1000000000)").addColumn("Wins", "INT(1000000000)")
					.addColumn("Kills", "INT(1000000000)").addColumn("Deaths", "INT(1000000000)")
					.addColumn("Games", "INT(1000000000)").addColumn("Contents", "TEXT").build();
		}

	}
	
	//TODO: Make methods:
	//			containsData(table, column, value)
	//			getData(table, column, user) returns resultset. So can do rs.get()
	
	
    public ResultSet getData(String uuid, String tableName, String columnName) {
        ResultSet rs = null;
        try {
            DatabaseMetaData dmeta = this.getConnection().getMetaData();
			rs = dmeta.getColumns(null, null, tableName, columnName);
            if (rs.next()) {
                final ResultSet res = new Query(this.getConnection()).select(tableName, "UserID").where("UserID", uuid).execute();
                res.first();
                return res;
            }
        }
        catch (SQLException e) {
            return rs;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                }
                catch (SQLException ex) {}
            }
        }
		return rs;
    }
    public String getString(String uuid, String tableName, String columnName) {
        ResultSet rs = null;
        try {
            DatabaseMetaData dmeta = this.getConnection().getMetaData();
			rs = dmeta.getColumns(null, null, tableName, columnName);
            if (rs.next()) {
                final ResultSet res = new Query(this.getConnection()).select(tableName, "UserID").where("UserID", uuid).execute();
                res.next();
                return res.getString(columnName);
            }
        } catch (SQLException e) { // Data does not exist
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                }
                catch (SQLException ex) {}
            }
        }
		return "";
    }
    public int getInt(String uuid, String tableName, String columnName) {
        ResultSet rs = null;
        try {
            DatabaseMetaData dmeta = this.getConnection().getMetaData();
			rs = dmeta.getColumns(null, null, tableName, columnName);
            if (rs.next()) {
                final ResultSet res = new Query(this.getConnection()).select(tableName, "UserID").where("UserID", uuid).execute();
                res.next();
                return res.getInt(columnName);
            }
        } catch (SQLException e) {
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                }
                catch (SQLException ex) {}
            }
        }
		return 0;
    }
	public boolean containsData(String tableName, String columnName, String uuid) {
		ResultSet rs = null;
		try {
			DatabaseMetaData dmeta = this.getConnection().getMetaData();
			rs = dmeta.getColumns(null, null, tableName, columnName);
			if (rs.next() && rs != null) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
		
	}
	
	public boolean containsUser(String uuid, String table) {
		System.out.print("Contains User");
		if (this.getConnection() == null) {
			return false;
		}
		try {
			if (new Query(this.getConnection()).select(table, "UserID").where("UserID", uuid).execute().next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
		
	}
	
	public void execute(Execute execute, User user, Category category) {
		if (execute == Execute.USER_SAVE) {
			String uuid = user.getUUID().toString();
			if (user.hasUpdated()) {
				new Query(this.getConnection()).update(Table.OVERALL.getTableName())
						.set("Games", user.getTotalGamesPlayed()).set("Wins", user.getTotalWins())
						.set("Kills", user.getTotalKills()).set("Deaths", user.getTotalDeaths()).execute();
				user.setUpdated(false);
			}

			for (Category categorys : Category.values()) {
				if (containsUser(uuid, category.name())) {
					CategoryStats cs = user.getCategoryStatistics(categorys);
					if (cs.hasUpdated()) {
						new Query(this.getConnection()).update(categorys.name()).set("Winstreak", cs.getWinstreak())
								.set("Wins", cs.getWins()).set("Kills", cs.getKills()).set("Deaths", cs.getDeaths())
								.set("Games", cs.getGamesPlayed())
								.set("Contents", KitsManager.getManager().serialize(cs.getContents()))
								.where("UserID", uuid).execute();
						cs.setUpdated(false);
					}
				}
			}
		}
		if (execute == Execute.USER_CREATE) {
			String uuid = user.getUUID().toString();
			if (!containsUser(uuid, Table.OVERALL.getTableName())) {
				new Query(this.getConnection()).insert(Table.OVERALL.getTableName())
						.insert("UserID, Games, Wins, Kills, Deaths").value(uuid).value(user.getTotalGamesPlayed())
						.value(user.getTotalWins()).value(user.getTotalKills()).value(user.getTotalDeaths()).execute();
			}
			if (!containsUser(uuid, category.name())) {
				CategoryStats cs = user.getCategoryStatistics(category);
				new Query(this.getConnection()).insert(category.name())
						.insert("UserID, Winstreak, Wins, Kills, Deaths, Games, Contents").value(uuid)
						.value(cs.getWinstreak()).value(cs.getWins()).value(cs.getKills()).value(cs.getDeaths())
						.value(cs.getGamesPlayed()).value(null).execute();
			}

		}
		if (execute == Execute.USER_REMOVE) { //
			String uuid = user.getUUID().toString();
			if (category == null) { // Delete all
				new Query(this.getConnection()).delete(Table.OVERALL.getTableName()).where("UserID", uuid).execute();
				for (Category categorys : Category.values()) {
					new Query(this.getConnection()).delete(categorys.name()).where("UserID", uuid).execute();
				}
			} else { // Delete Category
				new Query(this.getConnection()).delete(category.name()).where("UserID", uuid).execute();
			}
		}

		if (execute == Execute.USER_GET) {
			/*try {

					for (Categorys categorys : Categorys.values()) {
						if (containsUser(uuid, "UserID", categorys)) {
							ResultSet crs = new Query(this.getConnection()).select(categorys.name(), "UserID").where("UserID", uuid).execute();
							CategoryStats cs = user.getCategoryStatistics(categorys);
							if (crs.next()) {
							try {
								cs.setContents(KitsManager.getManager().deserialize(crs.getString("Contents")));
							} catch (IOException e) {
								e.printStackTrace();
							}
							//Winstreak, Wins, Kills, Deaths, GamesPlayed, Contents"
							p.sendMessage(categorys.name() + ": ");
							p.sendMessage("Deaths: " + crs.getInt("Deaths"));
							p.sendMessage("Games: " + crs.getInt("Games"));
							p.sendMessage("Kills: " + crs.getInt("Kills"));
							p.sendMessage("Wins: " + crs.getInt("Wins"));
							p.sendMessage("Winstreak: " + crs.getInt("Winstreak"));
							cs.setDeaths(crs.getInt("Deaths"));
							cs.setGamesPlayed(crs.getInt("Games")); 
							cs.setKills(crs.getInt("Kills"));
							cs.setWins(crs.getInt("Wins"));
							cs.setWinstreak(crs.getInt("Winstreak"));
							}
						}
						
					}
				} catch (SQLException e) {
					e.printStackTrace();
				} try {
                    rs.close();
                } catch (SQLException ex) {}*/
			String uuid = user.getUUID().toString();
			Player p = Bukkit.getPlayer(user.getUUID());
			p.sendMessage("Contains user: " + containsUser(uuid, Table.OVERALL.getTableName()));
			if (containsUser(uuid, Table.OVERALL.getTableName())) {
				String table = Table.OVERALL.getTableName();
					p.sendMessage("Winstreak: " + Integer.toString(getInt(uuid, table, "Winstreak")));
					p.sendMessage("Wins: " + Integer.toString(getInt(uuid, table, "Wins")));
					p.sendMessage("Kills: " + Integer.toString(getInt(uuid, table, "Kills")));
					p.sendMessage("Deaths: " + Integer.toString(getInt(uuid, table, "Deaths")));
					p.sendMessage("Games: " + Integer.toString(getInt(uuid, table, "Games")));
			}
		}
	}

	public enum Execute {
		USER_SAVE, USER_CREATE, USER_REMOVE, USER_GET;
	}

}
