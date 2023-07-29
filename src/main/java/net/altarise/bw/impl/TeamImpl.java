package net.altarise.bw.impl;

import net.altarise.api.utils.ColorUtils;
import net.altarise.api.utils.LocationUtils;
import net.altarise.api.utils.title.Titles;
import net.altarise.bw.Bedwars;
import net.altarise.gameapi.engine.properties.Team;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;
@SuppressWarnings("unused")
public class TeamImpl extends Team {

    private int forgeLevel;
    public int goldBetween, ironBetween;
    private final Location generatorLocation;
    private final Location shopLocation;
    private final Location upgradeLocation;

    private final List<Location> bedLocations;
    private final byte byteColor;
    private final Location spawnLocation;
    private final Location protectedArea1;
    private final Location protectedArea2;
    private boolean bedDestroyed = false;
    private boolean eliminated = false;


    private boolean sharpened = false;
    private int armorLevel = 0;
    private int minerLevel = 0;

    private boolean minerTraps = false;
    private boolean alarmTraps = false;
    private boolean regeneration = false;

    public int goldCound, ironCount;






    public TeamImpl(String id, String name, ChatColor chatColor, byte byteColor, Location spawnLocation, Location generatorLocation, Location shopLocation, Location upgradeLocation, Location protectedArea1, Location protectedArea2, List<Location> bedLocations) {
        super(id, name, chatColor, ColorUtils.chatColorToColor(chatColor), spawnLocation);
        forgeLevel = 1;

        this.generatorLocation = generatorLocation;
        this.shopLocation = shopLocation;
        this.upgradeLocation = upgradeLocation;
        this.bedLocations = bedLocations;
        this.byteColor = byteColor;
        this.spawnLocation = spawnLocation;
        this.protectedArea1 = protectedArea1;
        this.protectedArea2 = protectedArea2;
        this.goldCound = 0;
        this.ironCount = 0;


    }


    public boolean isBedDestroyed() {
        return bedDestroyed;
    }

    public void setBedDestroyed() {
        this.bedDestroyed = true;
        getPlayers().forEach(player -> {
            Titles.sendTitle(player, 10, 40, 10, "§cVotre lit à été détruit!", "§7Vous ne pouvez plus réapparaitre !");
            player.playSound(player.getLocation(), Sound.ZOMBIE_INFECT, 1, 1);
        });
    }

    public boolean isEliminated() {
        return eliminated;
    }

    public void setEliminated() {
        if(eliminated) return;
        eliminated = true;
        Bedwars.INSTANCE().getGamePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.ZOMBIE_METAL, 1, 1));
    }
    public int getForgeLevel() {
        return forgeLevel;
    }


    public void addForgeLevel() {
        forgeLevel++;
    }


    public Location getGeneratorLocation() {
        return generatorLocation;
    }

    public void setIronBetween(int ironBetween) {
        this.ironBetween = ironBetween;
    }

    public void setGoldBetween(int goldBetween) {
        this.goldBetween = goldBetween;
    }

    public boolean hasMinerTraps() {
        return minerTraps;
    }

    public void setMinerTraps(boolean minerTraps) {
        if(!minerTraps && this.minerTraps) {
            getPlayers().forEach(player -> {
                player.sendMessage("§cLe piège §bMining Fatigue §ca été déclenché !");
                player.playSound(player.getLocation(), Sound.ZOMBIE_METAL, 1, 1);
            });
        }
        this.minerTraps = minerTraps;
    }

    public void setRegeneration(boolean regeneration) {
        this.regeneration = regeneration;
    }

    public boolean hasRegeneration() {
        return regeneration;
    }

    public boolean hasAlarmTraps() {
        return alarmTraps;
    }

    public void setAlarmTraps(boolean alarmTraps) {
        if(!alarmTraps && this.alarmTraps) {
            getPlayers().forEach(player -> {
                player.sendMessage("§cLe piège §bAlarme §ca été déclenché !");
                player.playSound(player.getLocation(), Sound.ZOMBIE_INFECT, 1, 1);
            });
        }
        this.alarmTraps = alarmTraps;
    }

    public Location getShopLocation() {
        return shopLocation;
    }

    public Location getUpgradeLocation() {
        return upgradeLocation;
    }






    public String getIcon() {
        if(bedDestroyed || eliminated) return getNumber();
        return "⬛";
    }

    private String getNumber() {
        if(eliminated) return "✘";
        if(getPlayers().size() == 9) return "➒";
        if(getPlayers().size() == 8) return "➑";
        if(getPlayers().size() == 7) return "➐";
        if(getPlayers().size() == 6) return "➏";
        if(getPlayers().size() == 5) return "➎";
        if(getPlayers().size() == 4 ) return "➍";
        if(getPlayers().size() == 3 ) return "➌";
        if(getPlayers().size() == 2 ) return "➋";
        if(getPlayers().size() == 1 ) return "➊";
        return "✘";
    }
    public List<Location> getBedLocations() {
        return bedLocations;
    }

    public byte getByteColor() {
        return byteColor;
    }

    public Location getProtectedArea1() {
        return protectedArea1;
    }

    public Location getProtectedArea2() {
        return protectedArea2;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    @Override
    public Location getLocation() {
        return super.getLocation();
    }

    @Override
    public ChatColor getChatColor() {
        return super.getChatColor();
    }

    @Override
    public String getId() {
        return super.getId();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public Color getColor() {
        return super.getColor();
    }

    public List<Location> getProtectedArea() {
        return LocationUtils.getCuboidLocations(protectedArea1, protectedArea2);
    }

    @Override
    public net.altarise.api.minecraft.scoreboard.nametag.Team getTeam() {
        return super.getTeam();
    }

    @Override
    public Collection<Player> getPlayers() {
        return super.getPlayers();
    }


    public int getArmorLevel() {
        return armorLevel;
    }

    public void addArmorLevel() {
     if(armorLevel != 4) armorLevel++;
    }

    public int getMinerLevel() {
        return minerLevel;
    }

    public void addMinerLevel() {
        if(minerLevel != 2) minerLevel++;
    }

    public boolean hasSharpened() {
        return sharpened;
    }

    public void setSharpened() {
        this.sharpened = true;
    }
}
