package net.altarise.bw.game.schedulers;

import net.altarise.api.utils.LocationUtils;
import net.altarise.api.utils.title.Titles;
import net.altarise.bw.Bedwars;
import net.altarise.bw.game.Generator;
import net.altarise.bw.impl.TeamImpl;
import net.altarise.gameapi.utils.TimerPrettyPrinter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

public class GameScheduler extends BukkitRunnable {

    private final SchedulerProperties properties = Bedwars.INSTANCE().getSchedulerProperties();
    @Override
    public void run() {

        if(properties.getDiamondLvl() < 3) properties.setDiamondBetweenLvl(properties.getDiamondBetweenLvl()-1);
        if(properties.getEmeraldLvl() < 3) properties.setEmeraldBetweenLvl(properties.getEmeraldBetweenLvl()-1);

        Bedwars.INSTANCE().getSchedulerProperties().setDiamondBetween(Bedwars.INSTANCE().getSchedulerProperties().getDiamondBetween()-1);
        Bedwars.INSTANCE().getSchedulerProperties().setEmeraldBetween(Bedwars.INSTANCE().getSchedulerProperties().getEmeraldBetween()-1);

        properties.GAME_TIMER++;

        runAlarm();
        runRegen();

        Bedwars.INSTANCE().getGamePlayers().forEach(player -> Bedwars.INSTANCE().getGameCoherence().checkPlayerHasSword(player));
        Bedwars.INSTANCE().getGamePlayers().forEach(player -> Bedwars.INSTANCE().getGameCoherence().enchant(player));
        Bedwars.INSTANCE().getGameCoherence().win();

        if(Bedwars.INSTANCE().getSchedulerProperties().getDiamondBetween() <= 0) {
            Bedwars.INSTANCE().getSchedulerProperties().setDiamondBetween(Bedwars.INSTANCE().getSchedulerProperties().getDiamondForLevel());
            Bedwars.INSTANCE().getGameProperties().getGenerators().stream().filter(generator -> generator.getType() == Generator.GeneratorType.DIAMOND).forEach(generator -> {
                if(generator.count < Bedwars.INSTANCE().getGameProperties().getDiamondLimit())  {
                    properties.dropItems(generator.getLocation(), Material.DIAMOND);
                    generator.count++;
                }
            });


            if(properties.getDiamondGenerated() == 5) {
                properties.setDiamondGenerated(1);
                Bedwars.INSTANCE().getGameProperties().getGenerators().stream().filter(generator -> generator.getType() == Generator.GeneratorType.DIAMOND).forEach(generator -> {
                    if(generator.starCount < Bedwars.INSTANCE().getGameProperties().getNetherStarLimit()) {
                        properties.dropItems(generator.getLocation(), Material.NETHER_STAR);
                        generator.starCount++;
                    }
                });
            }
            properties.setDiamondGenerated(properties.getDiamondGenerated()+1);
        }

        if(properties.getEmeraldBetween() <= 0) {
            properties.setEmeraldBetween(properties.getEmeraldForLevel());
            Bedwars.INSTANCE().getGameProperties().getGenerators().stream().filter(generator -> generator.getType() == Generator.GeneratorType.EMERALD).forEach(generator -> {
                if(generator.count < Bedwars.INSTANCE().getGameProperties().getEmeraldLimit()) {
                    properties.dropItems(generator.getLocation(), Material.EMERALD);
                    generator.count++;
                }
            });
        }


        if(properties.getDiamondBetweenLvl() <= 0) {
            properties.setDiamondBetweenLvl(properties.getDiamondUpgradeForLevel());
            properties.setDiamondLvl(properties.getDiamondLvl()+1);
            Bukkit.getConsoleSender().sendMessage("Diamond generator upgraded to level " + properties.getDiamondLvl());
            Bukkit.broadcastMessage("§3§lBEDWARS §f▪ Les générateurs de §bdiamants §font été améliorés au niveau §e" + properties.getRomanNumber(properties.getDiamondLvl()));
        }


        if(properties.getEmeraldBetweenLvl() <= 0) {
            properties.setEmeraldBetweenLvl(properties.getEmeraldUpgradeForLevel());
            properties.setEmeraldLvl(properties.getEmeraldLvl()+1);
            Bukkit.getConsoleSender().sendMessage("Emerald generator upgraded to level " + properties.getEmeraldLvl());
            Bukkit.broadcastMessage("§3§lBEDWARS §f▪ Les générateurs d'§aémeraudes §font été améliorés au niveau §e" + properties.getRomanNumber(properties.getEmeraldLvl()));
        }

        for(String key : properties.getUpdaters().keySet()) {
            for(Consumer<String> consumer : properties.getUpdaters().get(key)) {
                if(key.equals("diamondLineOne")) consumer.accept("§fSpawn dans " + TimerPrettyPrinter.format(properties.getDiamondBetween(), ChatColor.YELLOW, ChatColor.GRAY));
                if(key.equals("diamondLineTwo")) consumer.accept("§fUpgrade dans " + (properties.getDiamondLvl() == 3 ? "§cMAX" : TimerPrettyPrinter.format(properties.getDiamondBetweenLvl(), ChatColor.YELLOW, ChatColor.GRAY)));
                if(key.equals("diamondLineThree")) consumer.accept("§b§lDIAMANT §8[§9" + properties.getRomanNumber(properties.getDiamondLvl()) + "§8]");
                if(key.equals("emeraldLineOne")) consumer.accept("§fSpawn dans " + TimerPrettyPrinter.format(properties.getEmeraldBetween(), ChatColor.YELLOW, ChatColor.GRAY));
                if(key.equals("emeraldLineTwo")) consumer.accept("§fUpgrade dans " + (properties.getEmeraldLvl() == 3 ? "§cMAX" : TimerPrettyPrinter.format(properties.getEmeraldBetweenLvl(), ChatColor.YELLOW, ChatColor.GRAY)));
                if(key.equals("emeraldLineThree")) consumer.accept("§a§lÉMERAUDE §8[§9" + properties.getRomanNumber(properties.getEmeraldLvl()) + "§8]");
            }
        }



    }

    public void runAlarm() {
        for(TeamImpl team : Bedwars.INSTANCE().getTeamProperties().getTeams()) {
            if(!team.hasAlarmTraps()) continue;
            for(Player player : Bedwars.INSTANCE().getGamePlayers()) {
                if(LocationUtils.getAroundLocations(team.getBedLocations().get(0), 20).contains(player.getLocation().getBlock().getLocation())) {
                    if(!team.getPlayers().contains(player)) {
                        sendAlarm(player, team);
                    }
                }
            }
        }
    }

    public void runRegen() {
        for(TeamImpl team : Bedwars.INSTANCE().getTeamProperties().getTeams()) {
            if(!team.hasRegeneration()) continue;
            for(Player player : team.getPlayers()) {
                sendRegeneration(player, LocationUtils.getAroundLocations(team.getBedLocations().get(0), 20).contains(player.getLocation().getBlock().getLocation()));
            }
        }
    }

    private void sendRegeneration(Player player, boolean isInArea) {

            if(isInArea) {
                if(!Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).hasRegeneration()) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1));
                    Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).setRegeneration(true);
                }
            }


        if(!isInArea) {
                if(Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).hasRegeneration()) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1));
                    Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).setRegeneration(false);
                }
        }
    }

    private void sendAlarm(Player player, TeamImpl team) {

        team.setAlarmTraps(false);
        team.getPlayers().forEach(players -> players.sendMessage("§3§lBEDWARS §f▪ L'alarme à été déclenchée par " + Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getChatColor() + player.getName()));
        team.getPlayers().forEach(players -> Titles.sendTitle(players, 20, 40, 20, "§cL'alarme à été déclenchée", "§7par " + Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getChatColor() + player.getName()));


        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                team.getPlayers().forEach(player -> player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1));
                Bedwars.INSTANCE().getGamePlayers().forEach(player -> {
                    if(LocationUtils.getAroundLocations(team.getBedLocations().get(0), 10).contains(player.getLocation().getBlock().getLocation()) && !team.getPlayers().contains(player)) {
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, 1);
                    }
                });

                if(i == 10) {
                    cancel();

                }

                i++;
            }
        }.runTaskTimer(Bedwars.INSTANCE(), 0, 20);
    }


}
