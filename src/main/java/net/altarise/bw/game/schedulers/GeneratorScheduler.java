package net.altarise.bw.game.schedulers;

import net.altarise.api.utils.LocationUtils;
import net.altarise.api.utils.title.ActionBar;
import net.altarise.bw.Bedwars;
import net.altarise.bw.impl.TeamImpl;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class GeneratorScheduler extends BukkitRunnable {

    private final SchedulerProperties properties = Bedwars.INSTANCE().getSchedulerProperties();




    @Override
    public void run() {

        trackerRunner();

        for(TeamImpl team : Bedwars.INSTANCE().getGameProperties().getStaticTeams()) {
            team.ironBetween++;
            team.goldBetween++;
                if(team.ironBetween >= properties.getIronForLevel(team)) {
                    team.ironBetween = 0;
                    if(team.ironCount < Bedwars.INSTANCE().getGameProperties().getIronLimit()) {
                        properties.dropItems(team.getGeneratorLocation(), Material.IRON_INGOT);
                        team.ironCount++;
                    }
                }

                if(team.goldBetween >= properties.getGoldForLevel(team)) {
                    team.goldBetween = 0;
                    if(team.goldCound < Bedwars.INSTANCE().getGameProperties().getGoldLimit()) {
                        properties.dropItems(team.getGeneratorLocation(), Material.GOLD_INGOT);
                        team.goldCound++;
                    }
                }
            }
        }



    private void trackerRunner() {
        for(Player player : Bedwars.INSTANCE().getGameCoherence().trackers.keySet()) {
            if(Bedwars.INSTANCE().getGameCoherence().trackers.get(player) != null && !Bedwars.INSTANCE().getGameCoherence().trackers.get(player).getPlayers().isEmpty()) {
            List<String> trackerMessage = new ArrayList<>();
            for(Player tracked : Bedwars.INSTANCE().getGameCoherence().trackers.get(player).getPlayers()) {
                TeamImpl team = Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(tracked);
                trackerMessage.add(team.getChatColor() + tracked.getName() + " §f" + LocationUtils.getDirection(player, tracked.getLocation()));
            }
            new ActionBar(String.join(" §7| ", trackerMessage)).sendToPlayer(player);
            }
        }
    }

}
