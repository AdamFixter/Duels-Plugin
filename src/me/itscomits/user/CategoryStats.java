package me.itscomits.user;

import org.bukkit.inventory.ItemStack;

public class CategoryStats {
	
	private boolean updated = false;
	private int gamesPlayed = 0;
	private int kills = 0;
	private int deaths = 0;
	private int wins = 0;
	private int winStreak = 0;
	private ItemStack[] contents;
	
	public CategoryStats() {}
	
	public void addGame(int kills, int deaths, boolean win) {
		if (!(updated)) {
			updated = true;
		}
		gamesPlayed++;
		this.addDeaths(deaths);
		this.addKills(kills);
		if (win) {
			this.wins++;
			this.winStreak = this.winStreak++;
		} else if (this.winStreak > 0){
			this.winStreak = 0;
		}
	}
	
	public int getGamesPlayed() {
		return this.gamesPlayed;
	}
	public int getKills() {
		return this.kills;
	}
	public int getDeaths() {
		return this.deaths;
	}
	public int getWins() {
		return this.wins;
	}
	public int getWinstreak() {
		return this.winStreak;
	}
	public int getLosses() {
		return this.gamesPlayed - this.wins;
	}
	public ItemStack[] getContents() {
		return this.contents;
	}
	public boolean hasUpdated() {
		return this.updated;
	}
	
	public void addGamesPlayed(int gamesPlayed) {
		this.gamesPlayed = this.gamesPlayed + gamesPlayed;
	}
	public void addKills(int kills) {
		this.kills = this.kills + kills;
	}
	public void addDeaths(int deaths) {
		this.deaths = this.deaths + deaths;
	}
	public void addWins(int wins) {
		this.wins = this.wins + wins;
	}
	
	public void setGamesPlayed(int gamesPlayed) {
		this.gamesPlayed = gamesPlayed;
	}
	public void setKills(int kills) {
		this.kills = kills;
	}
	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}
	public void setWins(int wins) {
		this.wins = wins;
	}
	public void setContents(ItemStack[] contents) {
		this.contents = contents;
	}
	public void setWinstreak(int winstreak) {
		this.winStreak = winstreak;
	}
	public void setUpdated(boolean updated) {
		this.updated = updated;
	}
}
