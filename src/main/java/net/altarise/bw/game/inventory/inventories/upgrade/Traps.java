package net.altarise.bw.game.inventory.inventories.upgrade;

import net.altarise.api.utils.ItemBuilder;
import net.altarise.bw.Bedwars;
import net.altarise.bw.game.inventory.AbstractInventory;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Traps extends AbstractInventory {

    public Traps(Player player) {
        super("Traps", 9*5);
        setDefaultUpgradeMenu();


        setItem(4, new ItemBuilder(Material.TRIPWIRE_HOOK).displayname("§6§lPièges").lore("", "§7Achat de pièges").build());



        setItem(20, new ItemBuilder(Material.WOOD_PICKAXE).displayname(transcriptGetter(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).hasMinerTraps()) + "§6Mining Fatigue").lore("", "§7Prix: " + transcript(Material.DIAMOND, 2)).build(), event -> {
            if(!Bedwars.INSTANCE().getTeamProperties().getTeamPlayer((Player) event.getWhoClicked()).hasMinerTraps()) {
                if(transaction(player, Material.DIAMOND, 2)) {
                    Bedwars.INSTANCE().getTeamProperties().getTeamPlayer((Player) event.getWhoClicked()).setMinerTraps(true);
                    ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.ANVIL_LAND, 1, 1);
                    updateItem(20, new ItemBuilder(Material.WOOD_PICKAXE).displayname(transcriptGetter(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).hasMinerTraps()) + "§6Mining Fatigue").lore("", "§7Prix: " + transcript(Material.DIAMOND, 2)).build());
                    player.updateInventory();
                    new Traps(player).open(player);
                    player.updateInventory();
                    for (Player teamPlayer : Bedwars.INSTANCE().getTeamProperties().getTeamPlayer((Player) event.getWhoClicked()).getPlayers()) {
                        teamPlayer.sendMessage("§6" + event.getWhoClicked().getName() + " §7à acheter le piège §bMining Fatigue");
                    }
                }
            }else ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.NOTE_BASS, 1, 1);
        });




        setItem(22, new ItemBuilder(Material.REDSTONE_TORCH_ON).displayname(transcriptGetter(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).hasAlarmTraps()) + "§6Alarme").lore("", "§7Prix: " + transcript(Material.DIAMOND, 2)).build(), event -> {
            if(!Bedwars.INSTANCE().getTeamProperties().getTeamPlayer((Player) event.getWhoClicked()).hasAlarmTraps()) {
                if(transaction(player, Material.DIAMOND, 2)) {
                    Bedwars.INSTANCE().getTeamProperties().getTeamPlayer((Player) event.getWhoClicked()).setAlarmTraps(true);
                    ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.ANVIL_LAND, 1, 1);
                    updateItem(22, new ItemBuilder(Material.REDSTONE_TORCH_ON).displayname(transcriptGetter(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).hasAlarmTraps()) + "§6Alarme").lore("", "§7Prix: " + transcript(Material.DIAMOND, 2)).build());
                    player.updateInventory();
                    new Traps(player).open(player);
                    player.updateInventory();
                    for (Player teamPlayer : Bedwars.INSTANCE().getTeamProperties().getTeamPlayer((Player) event.getWhoClicked()).getPlayers()) {
                        teamPlayer.sendMessage("§6" + event.getWhoClicked().getName() + " §7à acheter le piège §bAlarme");
                    }
                }
            }else ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.NOTE_BASS, 1, 1);
        });





        setItem(24, new ItemBuilder(Material.BEACON).displayname(transcriptGetter(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).hasRegeneration()) + "§6Régénération").lore("", "§7Prix: " + transcript(Material.DIAMOND, 2)).build(), event -> {
            if(!Bedwars.INSTANCE().getTeamProperties().getTeamPlayer((Player) event.getWhoClicked()).hasRegeneration()) {
                if(transaction(player, Material.DIAMOND, 2)) {
                    Bedwars.INSTANCE().getTeamProperties().getTeamPlayer((Player) event.getWhoClicked()).setRegeneration(true);
                    ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.ANVIL_LAND, 1, 1);
                    updateItem(24, new ItemBuilder(Material.BEACON).displayname(transcriptGetter(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).hasRegeneration()) + "§6Régénération").lore("", "§7Prix: " + transcript(Material.DIAMOND, 2)).build());
                    player.updateInventory();
                    new Traps(player).open(player);
                    player.updateInventory();
                    for (Player teamPlayer : Bedwars.INSTANCE().getTeamProperties().getTeamPlayer((Player) event.getWhoClicked()).getPlayers()) {
                        teamPlayer.sendMessage("§6" + event.getWhoClicked().getName() + " §7à acheter §bRégénération");
                    }
                }
            }else ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.NOTE_BASS, 1, 1);
        });






    }


}
