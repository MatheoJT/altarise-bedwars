package net.altarise.bw.game.inventory.inventories.shop;

import net.altarise.api.utils.ItemBuilder;
import net.altarise.bw.Bedwars;
import net.altarise.bw.game.inventory.AbstractInventory;
import net.altarise.bw.game.inventory.inventories.EShop;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

public class Armors extends AbstractInventory {

    public Armors(Player player) {
        super("Armors");
        setDefaultBuyableMenu();
        useRandom(player);
        setItem(4, new ItemBuilder(Material.IRON_CHESTPLATE).displayname("§6§lArmures").lore("", "§7Boutique d'armures").flag(ItemFlag.HIDE_ATTRIBUTES).build());

        for (EShop item : EShop.values()) {
            if(item.getInventoryType().equals(InventoryBuilder.InventoryType.ARMOR)) {
                new BuyArmor(player, item, this, item.getSlot());
            }
        }
    }

    public static class BuyArmor {

        public BuyArmor(Player player, EShop item, AbstractInventory inventory, int slot) {

            if (item.getReward().getType().equals(Material.CHAINMAIL_CHESTPLATE) || item.getReward().getType().equals(Material.PRISMARINE_CRYSTALS) || item.getReward().getType().equals(Material.CHAINMAIL_BOOTS)) {
                inventory.setUpgradableItem(player, slot, new ItemBuilder(item.getReward()).displayname(inventory.transcriptGetter(Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).getArmorLvl() > 1) + item.getDisplayName()).build(), item.getCost(), item.getPrice(), event -> {
                    if (Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).getArmorLvl() <= 1) {
                        if(inventory.transaction(player, item.getCost(), item.getPrice())) {
                            Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).setArmorLvl(2);
                            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
                            player.getInventory().setChestplate(new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).unbreakable(true).build());
                            player.getInventory().setBoots(new ItemBuilder(Material.CHAINMAIL_BOOTS).unbreakable(true).build());
                            inventory.updateItem(slot, new ItemBuilder(item.getReward()).displayname(inventory.transcriptGetter(Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).getArmorLvl() > 1) + item.getDisplayName()).build());
                            player.updateInventory();
                        }
                    } else player.playSound(player.getLocation(), Sound.NOTE_BASS, 1, 1);
                });

            } else if (item.getReward().getType().equals(Material.IRON_CHESTPLATE) || item.getReward().getType().equals(Material.IRON_INGOT) || item.getReward().getType().equals(Material.IRON_BOOTS)) {
                inventory.setUpgradableItem(player, slot, new ItemBuilder(item.getReward()).displayname(inventory.transcriptGetter(Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).getArmorLvl() > 2) + item.getDisplayName()).build(), item.getCost(), item.getPrice(), event -> {
                    if (Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).getArmorLvl() <= 2) {
                        if(inventory.transaction(player, item.getCost(), item.getPrice())) {
                            Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).setArmorLvl(3);
                            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
                            player.getInventory().setChestplate(new ItemBuilder(Material.IRON_CHESTPLATE).unbreakable(true).build());
                            player.getInventory().setBoots(new ItemBuilder(Material.IRON_BOOTS).unbreakable(true).build());
                            inventory.updateItem(slot, new ItemBuilder(item.getReward()).displayname(inventory.transcriptGetter(Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).getArmorLvl() > 2) + item.getDisplayName()).build());
                            player.updateInventory();
                        }
                    } else player.playSound(player.getLocation(), Sound.NOTE_BASS, 1, 1);

                });

            } else if (item.getReward().getType().equals(Material.DIAMOND_CHESTPLATE) || item.getReward().getType().equals(Material.DIAMOND) || item.getReward().getType().equals(Material.DIAMOND_BOOTS)) {

                if(Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).getArmorLvl() >= 4) {
                    inventory.setItem(item.getSlot(), new ItemBuilder(item.getReward()).displayname("§aPOSSEDE " + item.getDisplayName()).build(), true);
                }else {

                    inventory.setUpgradableItem(player, slot, new ItemBuilder(item.getReward()).displayname(item.getDisplayName()).build(), item.getCost(), item.getPrice(), event -> {

                        if (inventory.transaction(player, item.getCost(), item.getPrice())) {
                            Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).setArmorLvl(4);
                            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
                            player.getInventory().setChestplate(new ItemBuilder(Material.DIAMOND_CHESTPLATE).unbreakable(true).build());
                            player.getInventory().setBoots(new ItemBuilder(Material.DIAMOND_BOOTS).unbreakable(true).build());
                            inventory.updateItem(slot, new ItemBuilder(item.getReward()).displayname("§aPOSSEDE " + item.getDisplayName()).build());
                            player.updateInventory();
                        }
                    });
                }
            }

        }

    }

}
