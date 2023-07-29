package net.altarise.bw.commands;

import net.altarise.bw.Bedwars;
import net.altarise.bw.game.events.IEvent;
import net.altarise.bw.game.events.PowerDestruct;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandEvents implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) commandSender;

        if(!player.hasPermission("bw.events")) {
            player.sendMessage("You don't have permission to use this command!");
            return true;
        }

        if(strings.length == 0) {
            player.sendMessage("Usage: /events <[event]/random>");
            return true;
        }

        if(strings[0].equalsIgnoreCase("random")) {
            Bedwars.INSTANCE().getEventManager().playEvent(Bedwars.INSTANCE().getEventManager().getEvents().stream().findAny().isPresent() ? Bedwars.INSTANCE().getEventManager().getEvents().stream().findAny().get() : new PowerDestruct());
        } else {

            for(IEvent event : Bedwars.INSTANCE().getEventManager().getEvents()) {
                if(event.getID().equalsIgnoreCase(strings[0])) {
                    Bedwars.INSTANCE().getEventManager().playEvent(event);
                    return true;
                } else {
                    player.sendMessage("§cEvent not found!");
                    for(IEvent event1 : Bedwars.INSTANCE().getEventManager().getEvents()) {
                        player.sendMessage("§e- §f" + event1.getID());
                    }
                }
            }

        }


        return false;
    }
}
