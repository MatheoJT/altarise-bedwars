package net.altarise.bw.game.inventory.inventories.shop;

import net.altarise.api.utils.ItemBuilder;
import net.altarise.bw.Bedwars;
import net.altarise.bw.game.inventory.AbstractInventory;
import net.altarise.bw.game.inventory.inventories.EShop;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class Tools extends AbstractInventory {

    public Tools(Player player) {
        super("Tools");
        setDefaultBuyableMenu();
        useRandom(player);
        setItem(4, new ItemBuilder(Material.IRON_PICKAXE).displayname("§6§lOutils").lore("", "§7Boutique d'outils").flag(ItemFlag.HIDE_ATTRIBUTES).build());

        for (EShop item : EShop.values()) {
            if (item.getInventoryType().equals(InventoryBuilder.InventoryType.TOOL)) {
                new BuyTools(player, item, this, item.getSlot());
            }
        }
    }

    public static class BuyTools {

        public BuyTools(Player player, EShop item, AbstractInventory inventory, int slot) {
            if(item.getReward().getType().equals(Material.SHEARS)) {
                if (Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).hasShears()) {
                    inventory.setItem(slot, new ItemBuilder(EShop.SHEARS.getReward()).displayname("§aPOSSEDE §e" + EShop.SHEARS.getDisplayName()).build());
                } else {

                    inventory.setUpgradableItem(player, slot, new ItemBuilder(EShop.SHEARS.getReward()).displayname(EShop.SHEARS.getDisplayName()).build(), EShop.SHEARS.getCost(), EShop.SHEARS.getPrice(), event -> {

                        if (inventory.transaction(player, EShop.SHEARS.getCost(), EShop.SHEARS.getPrice())) {
                            Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).addShears();
                            player.getInventory().addItem(new ItemStack(EShop.SHEARS.getReward()));
                            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
                            inventory.updateItem(slot, new ItemBuilder(EShop.SHEARS.getReward()).displayname("§aPOSSEDE §e" + EShop.SHEARS.getDisplayName()).build());
                            new Tools(player).open(player);
                            player.updateInventory();
                        }
                    });
                }
            }

            //
            if(item.getReward().getType().equals(Material.WOOD_PICKAXE) || item.getReward().getType().equals(Material.STONE_PICKAXE) || item.getReward().getType().equals(Material.IRON_PICKAXE) || item.getReward().getType().equals(Material.DIAMOND_PICKAXE)) {
                if (Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).getPickaxeLvl() > 3) {
                    inventory.setItem(slot, new ItemBuilder(EShop.DIAMOND_PICKAXE.getReward()).displayname("§aPOSSEDE §e" + EShop.DIAMOND_PICKAXE.getDisplayName()).build());
                } else {

                    EShop pickaxe;

                    switch (Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).getPickaxeLvl()) {
                        case 0:
                            pickaxe = EShop.WOODEN_PICKAXE;
                            break;
                        case 1:
                            pickaxe = EShop.STONE_PICKAXE;
                            break;
                        case 2:
                            pickaxe = EShop.IRON_PICKAXE;
                            break;
                        default:
                            pickaxe = EShop.DIAMOND_PICKAXE;
                            break;
                    }

                    inventory.setUpgradableItem(player, slot, new ItemBuilder(pickaxe.getReward()).displayname(pickaxe.getDisplayName()).build(), pickaxe.getCost(), pickaxe.getPrice(), event -> {
                        if (inventory.transaction(player, pickaxe.getCost(), pickaxe.getPrice())) {
                            Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).addPickaxeLvl();
                            removePickaxe(player);
                            player.getInventory().addItem(new ItemStack(pickaxe.getReward().getType()));
                            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
                            inventory.updateItem(slot, Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).getPickaxeLvl() > 3 ? new ItemBuilder(EShop.DIAMOND_PICKAXE.getReward()).displayname("§aPOSSEDE §e" + EShop.DIAMOND_PICKAXE.getDisplayName()).build() : new ItemBuilder(pickaxe.getReward()).displayname(pickaxe.getDisplayName()).build());
                            new Tools(player).open(player);
                            player.updateInventory();
                        }
                    });

                }
            }

            if(item.getReward().getType().equals(Material.WOOD_AXE) || item.getReward().getType().equals(Material.STONE_AXE) || item.getReward().getType().equals(Material.IRON_AXE) || item.getReward().getType().equals(Material.DIAMOND_AXE)) {
                if (Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).getAxeLvl() > 3) {
                    inventory.setItem(slot, new ItemBuilder(EShop.DIAMOND_AXE.getReward()).displayname("§aPOSSEDE §e" + EShop.DIAMOND_AXE.getDisplayName()).build());
                } else {

                    EShop axe;

                    switch (Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).getAxeLvl()) {
                        case 0:
                            axe = EShop.WOODEN_AXE;
                            break;
                        case 1:
                            axe = EShop.STONE_AXE;
                            break;
                        case 2:
                            axe = EShop.IRON_AXE;
                            break;
                        default:
                            axe = EShop.DIAMOND_AXE;
                            break;
                    }

                    inventory.setUpgradableItem(player, slot, new ItemBuilder(axe.getReward()).displayname(axe.getDisplayName()).build(), axe.getCost(), axe.getPrice(), event -> {
                        if (inventory.transaction(player, axe.getCost(), axe.getPrice())) {
                            Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).addAxeLvl();
                            removeAxe(player);
                            player.getInventory().addItem(new ItemStack(axe.getReward().getType()));
                            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
                            inventory.updateItem(slot, Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).getAxeLvl() > 3 ? new ItemBuilder(EShop.DIAMOND_AXE.getReward()).displayname("§aPOSSEDE §e" + EShop.DIAMOND_AXE.getDisplayName()).build() : new ItemBuilder(axe.getReward()).displayname(axe.getDisplayName()).build());
                            new Tools(player).open(player);
                            player.updateInventory();
                        }
                    });

                }
            }





        }

        private void removePickaxe(Player player) {
            player.getInventory().remove(Material.WOOD_PICKAXE);
            player.getInventory().remove(Material.STONE_PICKAXE);
            player.getInventory().remove(Material.IRON_PICKAXE);
        }

        private void removeAxe(Player player) {
            player.getInventory().remove(Material.WOOD_AXE);
            player.getInventory().remove(Material.STONE_AXE);
            player.getInventory().remove(Material.IRON_AXE);
        }
    }


}
