package net.altarise.bw.impl;

import net.altarise.bw.Bedwars;
import net.altarise.bw.game.schedulers.SchedulerProperties;
import net.altarise.gameapi.scoreboard.GameScoreboard;
import net.altarise.gameapi.utils.TimerPrettyPrinter;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class PlayingScoreboard extends GameScoreboard {

   private final Player player;



    public PlayingScoreboard(Player player) {
        this.player = player;

    }


    @SuppressWarnings("all")
    @Override
    public Collection<String> getLines() {

        SchedulerProperties properties = Bedwars.INSTANCE().getSchedulerProperties();

        int            size   = Bedwars.INSTANCE().getGameProperties().getTeams().size();
        List<TeamImpl> first  = new ArrayList<>(Bedwars.INSTANCE().getGameProperties().getTeams().subList(0, (size + 1) / 2));
        List<TeamImpl> second = new ArrayList<>(Bedwars.INSTANCE().getGameProperties().getTeams().subList((size + 1) / 2, size));

        List<String> teams = new ArrayList<>();

        for (TeamImpl team : first) {
            teams.add(team.getChatColor() + team.getIcon() + " ");

        }

        String f = teams.toString().replace("[", " ").replace("]", "").replace(",", "");;
        teams.clear();

        for (TeamImpl team : second) {
            teams.add(team.getChatColor() + team.getIcon() + " ");
        }

        String s = teams.toString().replace("[", " ").replace("]", "").replace(",", "");;
        teams.clear();



        String d = properties.getDiamondLvl() == 3 ? "III: §cMAX" : properties.getRomanNumber(properties.getDiamondLvl() + 1) + ": " + DurationFormatUtils.formatDuration(properties.getDiamondBetweenLvl()*1000, "§emm'§7:'§ess");
        String e = properties.getEmeraldLvl() == 3 ? "III: §cMAX" : properties.getRomanNumber(properties.getEmeraldLvl() + 1) + ": " + DurationFormatUtils.formatDuration(properties.getEmeraldBetweenLvl()*1000, "§emm'§7:'§ess");




        return Arrays.asList("§3❯§f❯ §3§lInformations",
                " §b▪ §fTemps " + new TimerPrettyPrinter().format(properties.GAME_TIMER, ChatColor.YELLOW, ChatColor.GRAY),
                " §b▪ §fLits détruits: §e" + Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).getDestroyedBed(),
                " §b▪ §fFinal kills: §e" + Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).getFinalKills(),
                " §b▪ §fKills: §e" + Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).getKills(),
                "",
                "§3❯§f❯ §3§lÉquipes",
                f,
                s,
                "",
                "§3❯§f❯ §3§lAméliorations",
                "§fDiamant " + d,
                "§fÉmeraude " + e
        );
    }


    @Override
    public void onUpdate() {

    }
}
