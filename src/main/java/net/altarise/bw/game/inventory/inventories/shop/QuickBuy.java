package net.altarise.bw.game.inventory.inventories.shop;

import net.altarise.api.utils.ItemBuilder;
import net.altarise.bw.Bedwars;
import net.altarise.bw.game.inventory.AbstractInventory;
import net.altarise.bw.game.inventory.inventories.EShop;
import net.altarise.bw.impl.GamePlayerImpl;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class QuickBuy extends AbstractInventory {

    public QuickBuy(final Player player) {
        super("QuickBuy");
        setDefaultBuyableMenu();
        useRandom(player);
        setItem(4, new ItemBuilder(Material.NETHER_STAR).displayname("§6§lAchats Rapides").lore("", "§7Boutique achats rapide").flag(ItemFlag.HIDE_ATTRIBUTES).build());

        if (Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).getTempData() != null
                && Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).getTempData().containsKey("q-inv")) {
            final GamePlayerImpl gamePlayer = Bedwars.INSTANCE().getGamePlayer(player.getUniqueId());
            final Map<Integer, String> slots = InventoryBuilder.stringToSlots(gamePlayer.getTempData().get("q-inv"));
            slots.forEach((slot, value) -> {
                if (value.equals("EMPTY")) {
                    setEmptySlot(slot, player);
                } else {
                    final EShop item = EShop.valueOf(value);
                    addItemToInventory(slot, player, item);
                }
            });
        } else {
            HashMap<Integer, String> map = new HashMap<>();
            for (int i = 20; i <= 42; i += 9) {
                setEmptySlot(i, player);
                map.put(i, "EMPTY");
                setEmptySlot(i + 1, player);
                map.put(i + 1, "EMPTY");
                setEmptySlot(i + 2, player);
                map.put(i + 2, "EMPTY");
                setEmptySlot(i + 3, player);
                map.put(i + 3, "EMPTY");
                setEmptySlot(i + 4, player);
                map.put(i + 4, "EMPTY");
                Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).getTempData().put("q-inv", InventoryBuilder.slotsToString(map));
            }
        }
    }

    private void setEmptySlot(final int slot, final Player player) {
        final ItemStack empty = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 14);
        final ItemMeta meta = empty.getItemMeta();
        meta.setDisplayName("§c§lVide");
        meta.setLore(Arrays.asList("", "§eChanger cette emplacement: §6§lClick"));
        empty.setItemMeta(meta);

        setItem(slot, empty, event -> new QuickBuyChooser(event.getRawSlot(), player).open(player));
    }

    @SuppressWarnings("all")
    private void addItemToInventory(final int slot, final Player player, final EShop item) {
        switch (item.getInventoryType()) {
            case ARMOR:
                new Armors.BuyArmor(player, item, this, slot);
                break;
            case TOOL:
                new Tools.BuyTools(player, item, this, slot);
                break;
            default:
                final ItemStack reward = item == EShop.WOOL
                        ? new ItemStack(item.getReward().getType(), item.getReward().getAmount(), Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getByteColor())
                        : new ItemStack(item.getReward().getType(), item.getReward().getAmount());
                final ItemStack itemStack = reward;
                final ItemMeta meta = itemStack.getItemMeta();
                meta.setDisplayName("§f" + itemStack.getAmount() + " §e" + item.getDisplayName());
                meta.setLore(Arrays.asList("§7Prix: " + transcript(item.getCost(), item.getPrice()), "", "§eChanger cette emplacement: §6§lShift-Click"));

                itemStack.setItemMeta(meta);
                setItem(slot, itemStack, event -> {
                    if (event.isShiftClick()) {
                        player.closeInventory();
                        new QuickBuyChooser(event.getRawSlot(), (Player) event.getWhoClicked()).open(player);
                    } else if (transaction((Player) event.getWhoClicked(), item.getCost(), item.getPrice())) {
                        event.getWhoClicked().getInventory().addItem(new ItemBuilder(reward).unbreakable(true).build());
                    }
                });
        }
    }









}
