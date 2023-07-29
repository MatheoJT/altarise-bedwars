package net.altarise.bw.game.inventory.inventories.shop;

import net.altarise.api.utils.ItemBuilder;
import net.altarise.bw.Bedwars;
import net.altarise.bw.game.inventory.AbstractInventory;
import net.altarise.bw.game.inventory.inventories.EShop;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class Blocks extends AbstractInventory {


    public Blocks(Player player) {
        super("Blocks");
        setDefaultBuyableMenu();
        useRandom(player);
        setItem(4, new ItemBuilder(Material.STAINED_CLAY).displayname("§6§lBlocks").lore("", "§7Boutique des blocks").flag(ItemFlag.HIDE_ATTRIBUTES).build());

        for(EShop item : EShop.values()) {
            if(item.getInventoryType().equals(InventoryBuilder.InventoryType.BLOCK)) {
                if(item.getReward().getType().equals(Material.WOOL)) {
                    ItemStack reward  = new ItemStack(item.getReward().getType(), item.getReward().getAmount(), Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getByteColor());
                    setBuyableItem(item.getSlot(), item.getDisplayName(), reward, item.getCost(), item.getPrice());
                } else {
                    setBuyableItem(item.getSlot(), item.getDisplayName(), item.getReward(), item.getCost(), item.getPrice());
                }
            }
        }




    }




}
