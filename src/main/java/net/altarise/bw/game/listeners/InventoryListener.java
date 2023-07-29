package net.altarise.bw.game.listeners;

import net.altarise.bw.Bedwars;
import net.altarise.bw.game.inventory.TrackerChooser;
import net.altarise.gameapi.basic.GameState;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class InventoryListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (Bedwars.INSTANCE().getGameAPI().getGameState().equals(GameState.WAITING) || Bedwars.INSTANCE().getGameAPI().getGameState().equals(GameState.STARTING)) return;
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (event.getSlotType().equals(InventoryType.SlotType.ARMOR)) event.setCancelled(true);
        if (event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY) && (event.getCurrentItem().getType().equals(Material.COMPASS) || event.getCurrentItem().getType().equals(Material.WOOD_SWORD) || event.getCurrentItem().getType().equals(Material.WOOD_AXE) || event.getCurrentItem().getType().equals(Material.STONE_AXE) || event.getCurrentItem().getType().equals(Material.IRON_AXE) || event.getCurrentItem().getType().equals(Material.GOLD_AXE) || event.getCurrentItem().getType().equals(Material.DIAMOND_AXE) || event.getCurrentItem().getType().equals(Material.WOOD_PICKAXE) || event.getCurrentItem().getType().equals(Material.STONE_PICKAXE) || event.getCurrentItem().getType().equals(Material.IRON_PICKAXE) || event.getCurrentItem().getType().equals(Material.GOLD_PICKAXE) || event.getCurrentItem().getType().equals(Material.DIAMOND_PICKAXE) || event.getCurrentItem().getType().equals(Material.SHEARS))) {
            event.setCancelled(true);
        }
        if(event.getCurrentItem() != null && event.getCurrentItem().getType().equals(Material.COMPASS)) {
            event.setCancelled(true);
            new TrackerChooser((Player) event.getWhoClicked()).open((Player) event.getWhoClicked());
        }
        Bedwars.INSTANCE().getGameCoherence().checkPlayerHasSword((Player) event.getWhoClicked());

    }
}
