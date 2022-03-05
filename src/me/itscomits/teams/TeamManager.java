package me.itscomits.teams;

import java.util.ArrayList;
import java.util.List;

import me.itscomits.ArenaPvP;
import me.itscomits.arena.Arena;

public class TeamManager {
	@SuppressWarnings("unused")
	private ArenaPvP plugin;

	public TeamManager() {
	}

	private static TeamManager tm = new TeamManager();

	public static TeamManager getManager() {
		return tm;
	}

	List<Team> teams = new ArrayList<Team>();

	public List<Team> getTeams() {
		return teams;
	}

	public Team getTeam(Arena a) {
		for (Team team : teams) {
			if (team.getArena().equals(a)) {
				return team;
			} else {
				Team t = new Team(a);
				return t;
			}
		}
		return null;
	}

	public void addTeams(Arena a) {
		if (!teams.contains(a)) {
			Team t = new Team(a);
			teams.add(t);
		}
	}

	public void removeTeams(Arena a) {
		if (teams.contains(a)) {
			teams.remove(a);
		}
	}
}
