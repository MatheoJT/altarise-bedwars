package net.altarise.bw.commands;

import net.altarise.bw.Bedwars;
import net.altarise.bw.impl.TeamImpl;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class CommandBTP implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(!commandSender.hasPermission("altarise.bw.btp")) return true;

        if(strings.length == 0) {
            commandSender.sendMessage("§cUsage: /btp <teamid>");
            return true;
        }

        Optional<TeamImpl> team = Bedwars.INSTANCE().getGameProperties().getStaticTeams().stream().filter(team1 -> team1.getId().equals(strings[0])).findFirst();

        if(!team.isPresent()) {
            commandSender.sendMessage("§cTeam not found!");
            for (TeamImpl team1 : Bedwars.INSTANCE().getGameProperties().getStaticTeams()) {
                commandSender.sendMessage("§e- §f" + team1.getId());
            }
            return true;
        }

        commandSender.sendMessage("§aTeleporting to team " + team.get().getChatColor() + team.get().getName() + "§a...");

        Player player = (Player) commandSender;
        player.teleport(team.get().getSpawnLocation());

        return false;
    }
}
