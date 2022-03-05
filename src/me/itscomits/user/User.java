package me.itscomits.user;

import java.util.HashMap;

import com.google.gson.Gson;

import java.util.UUID;

import me.itscomits.arena.Arena;
import me.itscomits.arena.category.Category;
import me.itscomits.arena.statistics.GameStats;

public class User {
	private boolean updated = false;
	private final UUID uuid;
	private int totalGamesPlayed = 0;
	private int totalWins = 0;
	private int totalKills = 0;
	private int totalDeaths = 0;
	// Category Statistics
	private HashMap<Category, CategoryStats> categoryStatistics = new HashMap<Category, CategoryStats>();
	
	public User(UUID uuid) {
		this.uuid = uuid;
		for (Category category : Category.values()) {
			categoryStatistics.put(category, new CategoryStats());
		}
	}
	public void addGame(Arena a, boolean win) {
		if (!(updated)) {
			updated = true;
		}
		if (win) {
			this.totalWins++;
		}
		totalGamesPlayed++;
		GameStats gameStats = a.getGameStats(this.uuid);
		this.addTotalKills(gameStats.getKills());
		this.addTotalDeaths(gameStats.getDeaths());
		this.categoryStatistics.get(a.getMode().getCategory()).addGame(gameStats.getKills(), gameStats.getDeaths(), win);
	}
	
	public UUID getUUID() {
		return this.uuid;
	}
	public CategoryStats getCategoryStatistics(Category category) {
		return this.categoryStatistics.get(category);
	}
	public String getCategoryStatisticsJSON(Category category) {
		Gson gson = new Gson();
		return gson.toJson(this.categoryStatistics.get(category));
	}
	public int getTotalGamesPlayed() {
		return this.totalGamesPlayed;
	}
	public int getTotalWins() {
		return this.totalWins;
	}
	public int getTotalKills() {
		return this.totalKills;
	}
	public int getTotalDeaths() {
		return this.totalDeaths;
	}
	
	public void addTotalKills(int kills) {
		this.totalKills = this.totalKills + kills;
	}
	public void addTotalDeaths(int deaths) {
		this.totalDeaths = this.totalDeaths + totalDeaths;
	}
	public void addTotalWins(int wins) {
		this.totalWins = this.totalWins + wins;
	}
	public void addTotalGames(int gamesPlayed) {
		this.totalGamesPlayed = this.totalGamesPlayed + gamesPlayed;
	}
	public boolean hasUpdated() {
		return this.updated;
	}
	
	public void setTotalKills(int totalKills) {
		this.totalKills = totalKills;
	}
	public void setTotalDeaths(int totalDeaths) {
		this.totalDeaths = totalDeaths;
	}
	public void setTotalWins(int totalWins) {
		this.totalWins = totalWins;
	}
	public void setTotalGames(int totalGamesPlayed) {
		this.totalGamesPlayed = totalGamesPlayed;
	}
	public void setUpdated(boolean updated) {
		this.updated = updated;
	}
	

}
