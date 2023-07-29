package net.altarise.bw.game.listeners.player;

import net.altarise.bw.Bedwars;
import net.altarise.gameapi.basic.GameState;
import net.altarise.gameapi.event.player.GamePlayerDisconnectEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerLeave implements Listener {

    @EventHandler
    public void onDisconnect(GamePlayerDisconnectEvent event) {
        Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(event.getPlayer()).getPlayers().remove(event.getPlayer());
        Bedwars.INSTANCE().getGameStatistics().remove(event.getPlayer().getUniqueId());
        if (Bedwars.INSTANCE().getGameAPI().getGameState().equals(GameState.WAITING)) return;
        if (Bedwars.INSTANCE().getGameAPI().getGameState().equals(GameState.STARTING)) return;
        if (Bedwars.INSTANCE().getGameAPI().getGameState().equals(GameState.ENDING)) return;
        Bedwars.INSTANCE().getGameCoherence().win();
    }


}
