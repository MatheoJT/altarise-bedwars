package net.altarise.bw.game.inventory.inventories.shop;

import net.altarise.api.utils.ItemBuilder;
import net.altarise.bw.game.inventory.AbstractInventory;
import net.altarise.bw.game.inventory.inventories.EShop;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

public class Potions extends AbstractInventory {

    public Potions(Player player) {
        super("Potions");
        setDefaultBuyableMenu();
        useRandom(player);
        setItem(4, new ItemBuilder(Material.POTION).displayname("§6§lPotions").lore("", "§7Boutique des potions").flag(ItemFlag.HIDE_ATTRIBUTES).build());

        for(EShop item : EShop.values()) {
            if(item.getInventoryType().equals(InventoryBuilder.InventoryType.POTIONS)) {
                setBuyableItem(item.getSlot(), item.getDisplayName(), item.getReward(), item.getCost(), item.getPrice());
            }
        }

    }


}
