package net.altarise.bw.game.inventory;

import net.altarise.api.utils.ItemBuilder;
import net.altarise.bw.Bedwars;
import net.altarise.bw.impl.TeamImpl;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class TrackerChooser extends AbstractInventory{

    private final Player player;

    public TrackerChooser(Player player) {
        super("Tracker",9*3);

        this.player = player;
        HashMap<Player, TeamImpl> trackedPlayers = Bedwars.INSTANCE().getGameCoherence().trackers;
        String currentTrackedTeam = trackedPlayers.containsKey(player) ? trackedPlayers.get(player).getChatColor() + trackedPlayers.get(player).getName() : "§cAucune";
        setItem(4, new ItemBuilder(new ItemStack(Material.COMPASS)).displayname("§aTraqueur").lore("", "§7Choisissez une équipe à traquer.", "", "§cSi vous traquez déjà une équipe,", "§celle sera remplacée", "", "§eVous traquez l'équipe: " + currentTrackedTeam).build());
        for (int i = 9; i < 18; i++) setItem(i, new ItemBuilder(new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15)).displayname(" ").build());

        int teamCount = Bedwars.INSTANCE().getGameProperties().getTeams().size();
        int i = 18 + (9 - teamCount) / 2;

        for (TeamImpl team : Bedwars.INSTANCE().getGameProperties().getTeams()) {
            if (!team.getPlayers().contains(player)) {
                String current = Bedwars.INSTANCE().getGameCoherence().trackers.containsKey(player) && Bedwars.INSTANCE().getGameCoherence().trackers.get(player) == team ? "§aACTIVE " : "";
                setItem(i, new ItemBuilder(new ItemStack(Material.WOOL, 1, team.getByteColor())).displayname(current + team.getChatColor() + team.getName()).lore("", "§ePrix: §22 émeraudes").build(), event -> buy(team));
                i++;
            }
        }
    }

    private void buy(TeamImpl team) {
        if((!Bedwars.INSTANCE().getTeamProperties().getTeams().contains(team)) || !player.getInventory().contains(Material.EMERALD, 2) || Bedwars.INSTANCE().getGameCoherence().trackers.containsKey(player) && Bedwars.INSTANCE().getGameCoherence().trackers.get(player) == team) {
            player.playSound(player.getLocation(), Sound.NOTE_BASS, 1, 1);
            return;
        }
        player.closeInventory();
        player.getInventory().removeItem(new ItemStack(Material.EMERALD, 2));
        Bedwars.INSTANCE().getGameCoherence().trackers.remove(player);
        Bedwars.INSTANCE().getGameCoherence().trackers.put(player, team);
    }

}
