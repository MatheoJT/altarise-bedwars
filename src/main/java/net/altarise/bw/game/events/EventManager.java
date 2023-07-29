package net.altarise.bw.game.events;

import net.altarise.api.utils.title.Titles;
import net.altarise.bw.Bedwars;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;

public class EventManager {

    private final Collection<IEvent> events = new ArrayList<>();

    public EventManager() {
           events.add(new PowerDestruct());
    }

    public void playEvent(IEvent event) {

        Bedwars.INSTANCE().getGamePlayers().forEach(player -> {
            Titles.sendTitle(player, 20, 20*3, 20, "§c§lEVENT", "§f§k§lIIIIIIIIIIII");
            player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1, 1);
        });

        new BukkitRunnable() {
            @Override
            public void run() {
                Bedwars.INSTANCE().getGamePlayers().forEach(player -> {
                    Titles.sendTitle(player, 20, 20*3, 20, "§c§lEVENT", "§f§l" + event.getName());
                    player.playSound(player.getLocation(), Sound.SPLASH, 1, 1);
                });
                event.play();
            }
        }.runTaskLater(net.altarise.bw.Bedwars.INSTANCE(), 20 * 5);

    }

    public Collection<IEvent> getEvents() {
        return events;
    }


}
