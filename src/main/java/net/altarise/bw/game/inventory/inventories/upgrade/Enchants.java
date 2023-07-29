package net.altarise.bw.game.inventory.inventories.upgrade;

import net.altarise.api.utils.ItemBuilder;
import net.altarise.bw.Bedwars;
import net.altarise.bw.game.ModeProperties;
import net.altarise.bw.game.inventory.AbstractInventory;
import net.altarise.bw.impl.TeamImpl;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Enchants extends AbstractInventory {

    public Enchants(Player player) {
        super("Enchants", 9*5);
        setDefaultUpgradeMenu();

        setItem(4, new ItemBuilder(Material.ENCHANTMENT_TABLE).displayname("§6§lEnchantements").lore("", "§7Amélioration de stuff de team").build());


        final ModeProperties.Section upgrades = Bedwars.INSTANCE().getGameProperties().getModeProperties().getSection("upgrades");
        final int sharpnessPrice = Integer.parseInt(upgrades.getValue("SHARPNESS"));
        final int protectionPrice1 = Integer.parseInt(upgrades.getValue("PROTECTION-1"));
        final int protectionPrice2 = Integer.parseInt(upgrades.getValue("PROTECTION-2"));
        final int protectionPrice3 = Integer.parseInt(upgrades.getValue("PROTECTION-3"));
        final int protectionPrice4 = Integer.parseInt(upgrades.getValue("PROTECTION-4"));
        final int hastePrice1 = Integer.parseInt(upgrades.getValue("HASTE-1"));
        final int hastePrice2 = Integer.parseInt(upgrades.getValue("HASTE-2"));


        setItem(20, new ItemBuilder(Material.IRON_SWORD).displayname(transcriptGetter(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).hasSharpened()) + "§6Sharpness").lore("", "§7Prix: " + transcript(Material.DIAMOND, sharpnessPrice)).build(), event -> {
            if(!Bedwars.INSTANCE().getTeamProperties().getTeamPlayer((Player) event.getWhoClicked()).hasSharpened()) {
                if(transaction(player, Material.DIAMOND, sharpnessPrice)) {
                    Bedwars.INSTANCE().getTeamProperties().getTeamPlayer((Player) event.getWhoClicked()).setSharpened();
                    ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.ANVIL_LAND, 1, 1);
                    updateItem(20, new ItemBuilder(Material.IRON_SWORD).displayname(transcriptGetter(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).hasSharpened()) + "§6Sharpness").lore("", "§7Prix: " + transcript(Material.DIAMOND, sharpnessPrice)).build());
                    enchants();
                    player.updateInventory();
                    new Enchants(player).open(player);
                    player.updateInventory();
                    for (Player teamPlayer : Bedwars.INSTANCE().getTeamProperties().getTeamPlayer((Player) event.getWhoClicked()).getPlayers()) {
                        teamPlayer.sendMessage("§6" + event.getWhoClicked().getName() + " §7à acheté(e) l'amélioration §bSharpness");
                    }
                }
            }else ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.NOTE_BASS, 1, 1);
        });


        Material protectionIcon;
        int protectionPrice;

        switch (Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getArmorLevel()) {
            case 1:
                protectionIcon = Material.CHAINMAIL_CHESTPLATE;
                protectionPrice = protectionPrice4;
                break;
            case 2:
                protectionIcon = Material.IRON_CHESTPLATE;
                protectionPrice = protectionPrice3;
                break;
            case 3:
                protectionIcon = Material.GOLD_CHESTPLATE;
                protectionPrice = protectionPrice2;
                break;

            default:
                protectionIcon = Material.LEATHER_CHESTPLATE;
                protectionPrice = protectionPrice1;
                break;
        }


        setItem(22, new ItemBuilder(protectionIcon).displayname(transcriptGetter(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getArmorLevel() > 3) + "§6Protection §e" + Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getArmorLevel()).lore("", "§7Prix: " + transcript(Material.DIAMOND, protectionPrice)).build(), event -> {
            if(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer((Player) event.getWhoClicked()).getArmorLevel() <= 3) {
                if(transaction(player, Material.DIAMOND, protectionPrice)) {
                    Bedwars.INSTANCE().getTeamProperties().getTeamPlayer((Player) event.getWhoClicked()).addArmorLevel();
                    ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.ANVIL_LAND, 1, 1);
                    updateItem(22, new ItemBuilder(protectionIcon).displayname(transcriptGetter(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getArmorLevel() > 3) + "§6Protection §e" + Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getArmorLevel()).lore("", "§7Prix: " + transcript(Material.DIAMOND, protectionPrice)).build());
                    enchants();
                    player.updateInventory();
                    new Enchants(player).open(player);
                    player.updateInventory();
                    for (Player teamPlayer : Bedwars.INSTANCE().getTeamProperties().getTeamPlayer((Player) event.getWhoClicked()).getPlayers()) {
                        teamPlayer.sendMessage("§6" + event.getWhoClicked().getName() + " §7à acheté(e) l'enchantement §bProtection " + Bedwars.INSTANCE().getTeamProperties().getTeamPlayer((Player) event.getWhoClicked()).getArmorLevel());
                    }
                }
            }else ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.NOTE_BASS, 1, 1);
        });




        Material minerIcon;
        int minerPrice;

        if (Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getMinerLevel() == 1) {
            minerIcon = Material.IRON_PICKAXE;
            minerPrice = hastePrice2;
        } else {
            minerIcon = Material.WOOD_PICKAXE;
            minerPrice = hastePrice1;
        }

        setItem(24, new ItemBuilder(minerIcon).displayname(transcriptGetter(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getMinerLevel() > 1) + "§6Efficacité §e" + Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getArmorLevel()).lore("", "§7Prix: " + transcript(Material.DIAMOND, minerPrice)).build(), event -> {
            if(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer((Player) event.getWhoClicked()).getMinerLevel() <= 1) {
                if(transaction(player, Material.DIAMOND, minerPrice)) {
                    Bedwars.INSTANCE().getTeamProperties().getTeamPlayer((Player) event.getWhoClicked()).addMinerLevel();
                    ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.ANVIL_LAND, 1, 1);
                    updateItem(24, new ItemBuilder(minerIcon).displayname(transcriptGetter(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getMinerLevel() > 1) + "§6Efficacité §e" + Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getArmorLevel()).lore("", "§7Prix: " + transcript(Material.DIAMOND, minerPrice)).build());
                    enchants();
                    player.updateInventory();
                    new Enchants(player).open(player);
                    player.updateInventory();
                    for (Player teamPlayer : Bedwars.INSTANCE().getTeamProperties().getTeamPlayer((Player) event.getWhoClicked()).getPlayers()) {
                        teamPlayer.sendMessage("§6" + event.getWhoClicked().getName() + " §7à acheté(e) l'enchantement  §befficacité " + Bedwars.INSTANCE().getTeamProperties().getTeamPlayer((Player) event.getWhoClicked()).getMinerLevel());
                    }
                }
            }else ((Player) event.getWhoClicked()).playSound(event.getWhoClicked().getLocation(), Sound.NOTE_BASS, 1, 1);
        });

    }




    private void enchants() {

        for(TeamImpl team : Bedwars.INSTANCE().getGameProperties().getPlayingTeams()) {
            if(team.hasSharpened()) {
                for(Player player : team.getPlayers()) {
                    for(ItemStack item : player.getInventory().getContents()) {
                        if(item != null && (item.getType().equals(Material.STONE_SWORD) || item.getType().equals(Material.IRON_SWORD) || item.getType().equals(Material.WOOD_SWORD) || item.getType().equals(Material.DIAMOND_SWORD))) {
                            if(!item.getEnchantments().containsKey(Enchantment.DAMAGE_ALL)) item.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                        }
                    }
                }
            }

            if(team.getArmorLevel() > 0) {
                for(Player player : team.getPlayers()) {
                    for(ItemStack item : player.getInventory().getArmorContents()) {
                        if(item != null && (item.getType().equals(Material.LEATHER_HELMET) || item.getType().equals(Material.LEATHER_CHESTPLATE) || item.getType().equals(Material.LEATHER_LEGGINGS) || item.getType().equals(Material.LEATHER_BOOTS) || item.getType().equals(Material.CHAINMAIL_HELMET) || item.getType().equals(Material.CHAINMAIL_CHESTPLATE) || item.getType().equals(Material.CHAINMAIL_LEGGINGS) || item.getType().equals(Material.CHAINMAIL_BOOTS) || item.getType().equals(Material.IRON_HELMET) || item.getType().equals(Material.IRON_CHESTPLATE) || item.getType().equals(Material.IRON_LEGGINGS) || item.getType().equals(Material.IRON_BOOTS) || item.getType().equals(Material.GOLD_HELMET) || item.getType().equals(Material.GOLD_CHESTPLATE) || item.getType().equals(Material.GOLD_LEGGINGS) || item.getType().equals(Material.GOLD_BOOTS) || item.getType().equals(Material.DIAMOND_HELMET) || item.getType().equals(Material.DIAMOND_CHESTPLATE) || item.getType().equals(Material.DIAMOND_LEGGINGS) || item.getType().equals(Material.DIAMOND_BOOTS))) {
                            if(!item.getEnchantments().containsKey(Enchantment.PROTECTION_ENVIRONMENTAL) || item.getEnchantments().get(Enchantment.PROTECTION_ENVIRONMENTAL) != team.getArmorLevel()) item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, team.getArmorLevel());
                        }
                    }
                }
            }

            if(team.getMinerLevel() > 0) {
                for(Player player : team.getPlayers()) {
                    for(ItemStack item : player.getInventory().getContents()) {
                        if(item != null && (item.getType().equals(Material.IRON_PICKAXE) || item.getType().equals(Material.DIAMOND_PICKAXE) || item.getType().equals(Material.STONE_PICKAXE) || item.getType().equals(Material.WOOD_PICKAXE) || item.getType().equals(Material.WOOD_AXE) || item.getType().equals(Material.STONE_AXE) || item.getType().equals(Material.IRON_AXE) || item.getType().equals(Material.DIAMOND_AXE))) {
                            if(!item.getEnchantments().containsKey(Enchantment.DIG_SPEED)) item.addEnchantment(Enchantment.DIG_SPEED, team.getMinerLevel());
                        }
                    }
                }
            }
        }

    }




}

