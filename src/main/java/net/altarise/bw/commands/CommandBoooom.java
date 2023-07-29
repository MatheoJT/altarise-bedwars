package net.altarise.bw.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandBoooom implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!commandSender.hasPermission("altarise.bw.boooom")) return true;
        if(strings.length > 0) {
            if(Bukkit.getPlayer(strings[0]) != null) {
                launchPlayer(Bukkit.getPlayer(strings[0]));
            }else {
                commandSender.sendMessage("§cPlayer not found");
            }
            return false;
        }
        Bukkit.getOnlinePlayers().forEach(this::launchPlayer);
        commandSender.sendMessage("§aLaunched all players");

        return false;
    }

    private void launchPlayer(Player player) {
        player.getWorld().strikeLightningEffect(player.getLocation());
        player.teleport(player.getLocation().add(0, 30, 0));
        player.setVelocity(player.getLocation().getDirection().multiply(0.5).setY(1.2D));
    }



}
