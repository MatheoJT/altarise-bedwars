package net.altarise.bw.game.listeners;

import net.altarise.bw.game.listeners.player.PlayerJoin;
import net.altarise.bw.game.listeners.player.PlayerLeave;
import net.altarise.bw.game.listeners.player.PlayerListener;
import net.altarise.bw.game.listeners.player.PlayerMove;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class ListenersManager {

    public ListenersManager(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(new BlockListeners(), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerJoin(), plugin);
        Bukkit.getPluginManager().registerEvents(new ItemsListeners(), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerLeave(), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerMove(), plugin);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new WorldListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), plugin);
    }

}
