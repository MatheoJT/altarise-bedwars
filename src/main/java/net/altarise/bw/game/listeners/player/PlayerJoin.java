package net.altarise.bw.game.listeners.player;

import net.altarise.bw.Bedwars;
import net.altarise.bw.impl.GamePlayerImpl;
import net.altarise.gameapi.basic.GameState;
import net.altarise.gameapi.event.player.GamePlayerConnectEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(GamePlayerConnectEvent event) {
        if(Bedwars.INSTANCE().getGameAPI().getGameState().equals(GameState.WAITING) || Bedwars.INSTANCE().getGameAPI().getGameState().equals(GameState.STARTING)) {
            loadLocations();
            Bedwars.INSTANCE().getGameStatistics().put(event.getPlayer().getUniqueId(), new GamePlayerImpl(event.getPlayer()));
            addHealthBar(event.getPlayer());
        } else {event.getPlayer().teleport(Bedwars.INSTANCE().getGameProperties().getMiddle());}
    }

    @EventHandler
    public void onBasicJoin(PlayerJoinEvent event) {
        Bedwars.INSTANCE().getGameCoherence().npcs.forEach(npc -> {
            npc.addReceiver(event.getPlayer());
            npc.show(event.getPlayer());
        });
        Bedwars.INSTANCE().getGameCoherence().holograms.forEach(hologram -> hologram.addReceiver(event.getPlayer()));

    }

    private void loadLocations() {
        if(!Bedwars.INSTANCE().getGameProperties().getSpawn().getChunk().isLoaded()) Bedwars.INSTANCE().getGameProperties().getSpawn().getChunk().load(true);
        Bedwars.INSTANCE().getGameProperties().getGenerators().stream().filter(generator -> !generator.getLocation().getChunk().isLoaded()).forEach(generator -> generator.getLocation().getChunk().load(true));
        Bedwars.INSTANCE().getGameProperties().getTeams().stream().filter(team -> !team.getLocation().getChunk().isLoaded()).forEach(team -> team.getLocation().getChunk().load(true));
    }

    private void addHealthBar(Player player) {
        final ScoreboardManager manager = Bukkit.getScoreboardManager();
        final Scoreboard scoreboard = manager.getNewScoreboard();
        final Objective objective = scoreboard.registerNewObjective("health", "health");
        objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
        objective.setDisplayName("§c❤");
        player.setScoreboard(scoreboard);
    }

}
