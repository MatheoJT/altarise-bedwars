package net.altarise.bw.game.inventory.inventories.shop;

import net.altarise.api.utils.ItemBuilder;
import net.altarise.bw.Bedwars;
import net.altarise.bw.game.inventory.AbstractInventory;
import net.altarise.bw.game.inventory.inventories.EShop;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

public class Weapons extends AbstractInventory {

    public Weapons(Player player) {
        super("Weapons");
        setDefaultBuyableMenu();
        useRandom(player);
        setItem(4, new ItemBuilder(Material.IRON_SWORD).displayname("§6§lArmes").lore("", "§7Boutique d'armes").flag(ItemFlag.HIDE_ATTRIBUTES).build());
        for(EShop item : EShop.values()) {
            if(item.getInventoryType().equals(InventoryBuilder.InventoryType.WEAPON)) {
                setBuyableItem(item.getSlot(), item.getDisplayName(), item.getReward(), item.getCost(), item.getPrice(), event -> {
                    if(item == EShop.IRON_SWORD || item == EShop.STONE_SWORD || item == EShop.DIAMOND_SWORD) {
                        if (player.getInventory().contains(Material.WOOD_SWORD))
                            player.getInventory().remove(Material.WOOD_SWORD);
                    }
                        Bedwars.INSTANCE().getGameCoherence().enchant(player);

                });
            }
        }
    }

//    private int getAmount(Player arg0, ItemStack arg1) {
//        if (arg1 == null) return 0; int amount = 0; for (int i = 0; i < 36; i++) {
//            ItemStack slot = arg0.getInventory().getItem(i); if (slot == null || !slot.isSimilar(arg1)) continue;
//            amount += slot.getAmount();
//        } return amount;
//    }

}
