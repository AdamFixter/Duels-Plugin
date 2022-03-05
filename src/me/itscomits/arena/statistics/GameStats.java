package me.itscomits.arena.statistics;

import java.text.DecimalFormat;

public class GameStats {

	private int kills, deaths, damageDealt, meleeAttempts, meleeHits, bowShots, bowHits, healthRegenerated = 0;

	public GameStats() {}

	private String getAccuracy(int total, int attempts) {
		if (total == 0) {
			return "N/A";
		} else {
			DecimalFormat df = new DecimalFormat("#.#");
			df.format(100 / (total / attempts));
			return df.toString();
		}
	}

	public void addKills(int kills) {
		this.kills = this.kills + kills;
	}

	public void addDeaths(int deaths) {
		this.deaths = this.deaths + deaths;
	}

	public void addDamageDealt(int damage) {
		this.damageDealt = this.damageDealt + damageDealt;
	}

	public void addMeleeAttempts(int meleeAttempt) {
		this.meleeAttempts = this.meleeAttempts + this.meleeAttempts;
	}

	public void addMeleeHits(int meleeHit) {
		this.meleeHits = this.meleeHits + meleeHits;
	}

	public void addBowShots(int bowShot) {
		this.bowShots = this.bowShots + bowShot;
	}

	public void addBowHits(int bowHit) {
		this.bowHits = this.bowHits + bowHit;
	}

	public void addHealthRegenerate(int healthRenerated) {
		this.healthRegenerated = this.healthRegenerated + healthRegenerated;
	}

	public int getKills() {
		return this.kills;
	}

	public int getDeaths() {
		return this.deaths;
	}

	public int getDamageDealt() {
		return this.damageDealt;
	}

	public String getMeleeAccuracy() {
		return getAccuracy(this.meleeAttempts, this.meleeHits);
	}

	public String getBowAccuracy() {
		return getAccuracy(this.bowShots, this.bowHits);
	}

	public int getHealthRegeneration() {
		return this.healthRegenerated;
	}

	public int getMeleeAttempts() {
		return this.meleeAttempts;
	}
}
