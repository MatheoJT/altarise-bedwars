package net.altarise.bw.game.events;

import net.altarise.bw.Bedwars;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class PowerDestruct implements IEvent {


    public static final List<Block> blocks = new ArrayList<>();

    @Override
    public String getName() {
        return "BLOCK D'AIR WOOOOW";
    }

    @Override
    public String getID() {
        return "destruct";
    }

    @Override
    public void play() {

        blocks.removeIf(Objects::isNull);

        for(int i = 0; i < 30; i++) {
            if(blocks.size() == 0) return;
            Block randomBlock = blocks.get(new Random().nextInt(blocks.size()));
            randomBlock.breakNaturally();
            blocks.remove(randomBlock);
            Bukkit.getWorld("world").playSound(randomBlock.getLocation(), Sound.SPLASH, 1, 1);
            Bukkit.getWorld("world").strikeLightningEffect(randomBlock.getLocation());
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for(int i = 0; i < 30; i++) {
                    if (blocks.size() == 0) {
                        cancel();
                        return;
                    }
                    Block randomBlock = blocks.get(new Random().nextInt(blocks.size()));
                    randomBlock.breakNaturally();
                    blocks.remove(randomBlock);
                    Bukkit.getWorld("world").playSound(randomBlock.getLocation(), Sound.SPLASH, 1, 1);
                    Bukkit.getWorld("world").strikeLightningEffect(randomBlock.getLocation());
                }

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for(int i = 0; i < 30; i++) {
                            if (blocks.size() == 0) {
                                cancel();
                                return;
                            }
                            Block randomBlock = blocks.get(new Random().nextInt(blocks.size()));
                            randomBlock.breakNaturally();
                            blocks.remove(randomBlock);
                            Bukkit.getWorld("world").playSound(randomBlock.getLocation(), Sound.SPLASH, 1, 1);
                            Bukkit.getWorld("world").strikeLightningEffect(randomBlock.getLocation());
                        }
                    }
                }.runTaskLater(Bedwars.INSTANCE(), 20 * 20);

            }
        }.runTaskLater(Bedwars.INSTANCE(), 20 * 20);


    }


}
