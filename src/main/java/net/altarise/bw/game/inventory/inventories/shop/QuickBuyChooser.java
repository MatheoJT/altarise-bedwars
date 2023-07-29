package net.altarise.bw.game.inventory.inventories.shop;

import net.altarise.api.utils.ItemBuilder;
import net.altarise.bw.Bedwars;
import net.altarise.bw.game.inventory.AbstractInventory;
import net.altarise.bw.game.inventory.inventories.EShop;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.HashMap;

@SuppressWarnings("unused")
public class QuickBuyChooser extends AbstractInventory {


    private final Player player;
    private final int slot;
    public QuickBuyChooser(int slot, Player player) {
        super("QuickBuyChooser");
        this.player = player;
        this.slot = slot;

        setItems(0, 8, new ItemBuilder(Material.STAINED_GLASS_PANE).displayname("").color(Color.GRAY).build());
        setItem(4, new ItemBuilder(Material.NETHER_STAR).displayname("§6§lAchats Rapides").lore("", "§7Cliquez sur un item pour l'attribuer").flag(ItemFlag.HIDE_ATTRIBUTES).build());
        setItem(0, new ItemBuilder(Material.ARROW).displayname("§6§lRetour").lore("", "§7Cliquez pour retourner en arrière").flag(ItemFlag.HIDE_ATTRIBUTES).build(), event -> new QuickBuy(player).open(player));
        int i = 9;

        for(EShop eShop : EShop.values()) {
                     if(i+1>=9*6) return;
                     if(eShop != EShop.DIAMOND_BOOTS && eShop != EShop.DIAMOND_ICON && eShop != EShop.CHAINMAIL_BOOTS && eShop != EShop.CHAINMAIL_ICON && eShop != EShop.IRON_BOOTS && eShop != EShop.IRON_ICON) {
                         setItemChooser(i, eShop);
                         i++;
                     }
        }
    }

    public void setItemChooser(int slot, EShop eShop) {
        setItem(slot, new ItemBuilder(eShop.getReward().getType()).displayname("§f" + eShop.getReward().getAmount() + " §e" + eShop.getDisplayName()).build(), event -> {
           // PlayerStatistic playerStatistic = AltariseAPI.get().getDataProvider().getStatisticManager().getPlayerStatistic(event.getWhoClicked().getUniqueId().toString(), BasicServerType.BED_WARS.toServerType(Bedwars.INSTANCE().getAltariseAPI())).orElse(null);
          //  assert playerStatistic != null;
            HashMap<Integer, String> slots = InventoryBuilder.stringToSlots(Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).getTempData().get("q-inv"));
            if(slots.get(this.slot) != null) {
                slots.remove(this.slot);
            }
            slots.put(this.slot, eShop.toString());
            Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).getTempData().put("q-inv", InventoryBuilder.slotsToString(slots));
            //AltariseAPI.get().getDataProvider().getStatisticManager().saveStatistic(player.getUniqueId().toString(), playerStatistic);
            player.closeInventory();
            new QuickBuy(player).open(player);
        });
    }


}
