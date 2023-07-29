package net.altarise.bw.game.inventory.inventories.upgrade;

import net.altarise.api.utils.ItemBuilder;
import net.altarise.bw.Bedwars;
import net.altarise.bw.game.ModeProperties;
import net.altarise.bw.game.inventory.AbstractInventory;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Forge extends AbstractInventory {

    public Forge(Player player) {
        super("Forge", 9*5);
        setDefaultUpgradeMenu();

        setItem(4, new ItemBuilder(Material.ANVIL).displayname("§6§lForge").lore("", "§7Amélioration de la forge").build());

        final ModeProperties.Section upgrades = Bedwars.INSTANCE().getGameProperties().getModeProperties().getSection("upgrades");
        final int forgePrice1 = Integer.parseInt(upgrades.getValue("FORGE-2"));
        final int forgePrice2 = Integer.parseInt(upgrades.getValue("FORGE-3"));
        final int forgePrice3 = Integer.parseInt(upgrades.getValue("FORGE-4"));


        setItem(20, new ItemBuilder(Material.CLAY_BRICK).displayname(transcriptGetter(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getForgeLevel() > 1) + "§6Forge Niveau §e2").lore("", "§7Prix: " + transcript(Material.DIAMOND, forgePrice1)).build(), event -> {
            if(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer((Player) event.getWhoClicked()).getForgeLevel() <= 1) {
                if(transaction(player, Material.DIAMOND, forgePrice1)) {
                    Bedwars.INSTANCE().getTeamProperties().getTeamPlayer((Player) event.getWhoClicked()).addForgeLevel();
                    ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.ANVIL_LAND, 1, 1);
                    updateItem(20, new ItemBuilder(Material.CLAY_BRICK).displayname(transcriptGetter(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getForgeLevel() > 1) + "§6Forge Niveau §e2").lore("", "§7Prix: " + transcript(Material.DIAMOND, forgePrice1)).build());
                    player.updateInventory();
                    new Forge(player).open(player);
                    player.updateInventory();
                    for (Player teamPlayer : Bedwars.INSTANCE().getTeamProperties().getTeamPlayer((Player) event.getWhoClicked()).getPlayers()) {
                        teamPlayer.sendMessage("§6" + event.getWhoClicked().getName() + " §7a amélioré la forge au niveau §b" + Bedwars.INSTANCE().getTeamProperties().getTeamPlayer((Player) event.getWhoClicked()).getForgeLevel());
                    }
                }
            }else ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.NOTE_BASS, 1, 1);
        });

        setItem(22, new ItemBuilder(Material.GOLD_INGOT).displayname(transcriptGetter(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getForgeLevel() > 2) + "§6Forge Niveau §e3").lore("§7Prix: " + transcript(Material.DIAMOND, forgePrice2)).build(), event -> {
            if(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer((Player) event.getWhoClicked()).getForgeLevel() == 2) {
                if(transaction(player, Material.DIAMOND, forgePrice2)) {
                    Bedwars.INSTANCE().getTeamProperties().getTeamPlayer((Player) event.getWhoClicked()).addForgeLevel();
                    ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.ANVIL_LAND, 1, 1);
                    updateItem(22, new ItemBuilder(Material.GOLD_INGOT).displayname(transcriptGetter(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getForgeLevel() > 2) + "§6Forge Niveau §e3").lore("§7Prix: " + transcript(Material.DIAMOND, forgePrice2)).build());
                    player.updateInventory();
                    new Forge(player).open(player);
                    player.updateInventory();
                    for (Player teamPlayer : Bedwars.INSTANCE().getTeamProperties().getTeamPlayer((Player) event.getWhoClicked()).getPlayers()) {
                        teamPlayer.sendMessage("§6" + event.getWhoClicked().getName() + " §7a amélioré la forge au niveau §b" + Bedwars.INSTANCE().getTeamProperties().getTeamPlayer((Player) event.getWhoClicked()).getForgeLevel());
                    }
                }
            }else ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.NOTE_BASS, 1, 1);
        });

        setItem(24, new ItemBuilder(Material.DIAMOND).displayname(transcriptGetter(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getForgeLevel() >= 3) + "§6Forge Niveau §e4").lore("", "§7Prix: " + transcript(Material.DIAMOND, forgePrice3)).build(), event -> {
            if(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer((Player) event.getWhoClicked()).getForgeLevel() == 3) {
                if(transaction(player, Material.DIAMOND, forgePrice3)) {
                    Bedwars.INSTANCE().getTeamProperties().getTeamPlayer((Player) event.getWhoClicked()).addForgeLevel();
                    ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.ANVIL_LAND, 1, 1);
                    updateItem(24, new ItemBuilder(Material.DIAMOND).displayname(transcriptGetter(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getForgeLevel() >= 3) + "§6Forge Niveau §e4").lore("", "§7Prix: " + transcript(Material.DIAMOND, forgePrice3)).build());
                    player.updateInventory();
                    new Forge(player).open(player);
                    player.updateInventory();
                    for (Player teamPlayer : Bedwars.INSTANCE().getTeamProperties().getTeamPlayer((Player) event.getWhoClicked()).getPlayers()) {
                        teamPlayer.sendMessage("§6" + event.getWhoClicked().getName() + " §7a amélioré la forge au niveau §b" + Bedwars.INSTANCE().getTeamProperties().getTeamPlayer((Player) event.getWhoClicked()).getForgeLevel());
                    }
                }
            }else ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.NOTE_BASS, 1, 1);
        });

    }

}

