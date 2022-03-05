package me.itscomits.files.SQLFile;

public enum Databases {
	PLAYER_DATA("PlayerData");
	
	private String fileName;
	private Databases(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFileName() {
		return this.fileName;
	}
	
}
