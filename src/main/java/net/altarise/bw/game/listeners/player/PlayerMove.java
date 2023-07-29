package net.altarise.bw.game.listeners.player;

import net.altarise.bw.Bedwars;
import net.altarise.bw.impl.TeamImpl;
import net.altarise.gameapi.basic.GameState;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements org.bukkit.event.Listener {


    @EventHandler
    public void onVoid(PlayerMoveEvent event) {
        if (Bedwars.INSTANCE().getGameAPI().getGameState().equals(GameState.WAITING) || Bedwars.INSTANCE().getGameAPI().getGameState().equals(GameState.STARTING)) return;
        if(!Bedwars.INSTANCE().getGamePlayers().contains(event.getPlayer())) return;
        if (event.getPlayer().getLocation().getBlockY() <= 0) {
            if(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(event.getPlayer()).isBedDestroyed()) {
                    Bedwars.INSTANCE().getGameAPI().getSpectatorManager().addSpectator(event.getPlayer());
                    Bukkit.broadcastMessage(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(event.getPlayer()).getChatColor() + event.getPlayer().getName() + " §7a été éliminé par le vide");
                    Bedwars.INSTANCE().getGameCoherence().playerFinalKill(event.getPlayer(), event.getPlayer());
                    TeamImpl playingTeam = Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(event.getPlayer());
                    playingTeam.getPlayers().remove(event.getPlayer());
                    TeamImpl team = Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(event.getPlayer());
                    team.getPlayers().remove(event.getPlayer());
                    Bedwars.INSTANCE().getGameCoherence().win();
                }else {
                    PlayerListener.respawn(event.getPlayer());
                    Bedwars.INSTANCE().getGamePlayers().forEach(player -> player.sendMessage(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(event.getPlayer()).getChatColor() + event.getPlayer().getName() + " §7a été tué par le vide"));
                }

            }
    }



}
