package net.altarise.bw.impl;

import net.altarise.bw.Bedwars;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class TeamPropertiesImpl extends net.altarise.gameapi.engine.properties.TeamProperties<TeamImpl> {
    private static final Map<String, TeamImpl> teamsByName = new HashMap<>();

    public TeamPropertiesImpl() {
        for (TeamImpl team : Bedwars.INSTANCE().getGameProperties().getTeams()) {
            teamsByName.put(team.getName(), team);
        }
    }

    public static TeamImpl getTeamsByName(String name) {
        return teamsByName.get(name);
    }

    @Override
    public boolean useTeams() {
        return true;
    }

    @Override
    public int getMaxPlayerPerTeam() {
        return Bedwars.INSTANCE().getGameProperties().getMaxPlayersPerTeam();
    }

    @Override
    public TeamImpl getTeamPlayer(Player player) {

        return Bedwars.INSTANCE().getGameProperties().getTeams().stream().filter(team -> team.getPlayers().contains(player)).findFirst().orElse(null);
    }


    @Override
    public Collection<TeamImpl> getTeams() {
        return Bedwars.INSTANCE().getGameProperties().getTeams();
    }

    public TeamImpl getClosestTeam(Player player) {
        TeamImpl team = null;
        double distance = Double.MAX_VALUE;
        for (TeamImpl team1 : Bedwars.INSTANCE().getGameProperties().getTeams()) {
            if (team1.getPlayers().contains(player)) continue;
            double distance1 = player.getLocation().distance(team1.getSpawnLocation());
            if (distance1 < distance) {
                distance = distance1;
                team = team1;
            }
        }
        return team;
    }
}
