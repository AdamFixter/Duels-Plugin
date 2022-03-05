package me.itscomits.utils;

public class Time {
	public static String formatTime(int time) {
		int minutes = time / 60;
		int seconds = time % 60;
		String disMinu = (minutes < 10 ? "0" : "") + minutes;
		String disSec = (seconds < 10 ? "0" : "") + seconds;
		return disMinu + ":" + disSec;
	}
}
