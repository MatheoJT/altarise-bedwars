package net.altarise.bw.game.inventory.inventories.shop;

import net.altarise.api.utils.ItemBuilder;
import net.altarise.bw.game.inventory.AbstractInventory;
import net.altarise.bw.game.inventory.inventories.EShop;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

public class Utilities extends AbstractInventory {

    public Utilities(Player player) {
        super("Utilities");
        setDefaultBuyableMenu();
        useRandom(player);
        setItem(4, new ItemBuilder(Material.TNT).displayname("§6§lUtilitaires").lore("", "§7Boutique d'utilitaires").flag(ItemFlag.HIDE_ATTRIBUTES).build());

        for(EShop item : EShop.values()) {
            if(item.getInventoryType().equals(InventoryBuilder.InventoryType.UTILITY)) {
                setBuyableItem(item.getSlot(), item.getDisplayName(), item.getReward(), item.getCost(), item.getPrice());
            }
        }
    }


}
