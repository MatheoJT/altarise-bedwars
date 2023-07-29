package net.altarise.bw.game.schedulers;

import net.altarise.api.text.update.Updatable;
import net.altarise.bw.Bedwars;
import net.altarise.bw.impl.TeamImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class SchedulerProperties {


    public int GAME_TIMER = 0;
    private int ironBetween1, ironBetween2, ironBetween3, ironBetween4, goldBetween1, goldBetween2, goldBetween3, goldBetween4, diamondBetween1, diamondBetween2, diamondBetween3, diamondUpgrade1, diamondUpgrade2, emeraldBetween1, emeraldBetween2, emeraldBetween3, emeraldUpgrade1, emeraldUpgrade2;
    private int  diamondBetween, emeraldBetween, diamondLvl, emeraldLvl, diamondBetweenLvl, emeraldBetweenLvl, diamondGenerated;

    private final Map<String, Collection<Consumer<String>>> updaters = new HashMap<>();
    private final Updatable diamondLineOne = new Updatable() {@Override public void setOnUpdate(Consumer<String> consumer) { updaters.computeIfAbsent("diamondLineOne", k -> new ArrayList<>()).add(consumer);}@Override public String defaultValue() {return "Default";}};
    private final Updatable diamondLineTwo = new Updatable() {@Override public void setOnUpdate(Consumer<String> consumer) { updaters.computeIfAbsent("diamondLineTwo", k -> new ArrayList<>()).add(consumer);}@Override public String defaultValue() {return "Default";}};
    private final Updatable diamondLineThree = new Updatable() {@Override public void setOnUpdate(Consumer<String> consumer) { updaters.computeIfAbsent("diamondLineThree", k -> new ArrayList<>()).add(consumer);}@Override public String defaultValue() {return "Default";}};
    private final Updatable emeraldLineOne = new Updatable() {@Override public void setOnUpdate(Consumer<String> consumer) { updaters.computeIfAbsent("emeraldLineOne", k -> new ArrayList<>()).add(consumer);}@Override public String defaultValue() {return "Default";}};
    private final Updatable emeraldLineTwo = new Updatable() {@Override public void setOnUpdate(Consumer<String> consumer) { updaters.computeIfAbsent("emeraldLineTwo", k -> new ArrayList<>()).add(consumer);}@Override public String defaultValue() {return "Default";}};
    private final Updatable emeraldLineThree = new Updatable() {@Override public void setOnUpdate(Consumer<String> consumer) { updaters.computeIfAbsent("emeraldLineThree", k -> new ArrayList<>()).add(consumer);}@Override public String defaultValue() {return "Default";}};
    private BukkitTask startupTask;
    private boolean startupBoolean = false;

    public void register(int ironBetween1, int ironBetween2, int ironBetween3, int ironBetween4, int goldBetween1, int goldBetween2, int goldBetween3, int goldBetween4, int diamondBetween1, int diamondBetween2, int diamondBetween3, int diamondUpgrade1, int diamondUpgrade2, int emeraldBetween1, int emeraldBetween2, int emeraldBetween3, int emeraldUpgrade1, int emeraldUpgrade2) {
        this.ironBetween1 = ironBetween1;
        this.ironBetween2 = ironBetween2;
        this.ironBetween3 = ironBetween3;
        this.ironBetween4 = ironBetween4;
        this.goldBetween1 = goldBetween1;
        this.goldBetween2 = goldBetween2;
        this.goldBetween3 = goldBetween3;
        this.goldBetween4 = goldBetween4;
        this.diamondBetween1 = diamondBetween1;
        this.diamondBetween2 = diamondBetween2;
        this.diamondBetween3 = diamondBetween3;
        this.diamondUpgrade1 = diamondUpgrade1;
        this.diamondUpgrade2 = diamondUpgrade2;
        this.emeraldBetween1 = emeraldBetween1;
        this.emeraldBetween2 = emeraldBetween2;
        this.emeraldBetween3 = emeraldBetween3;
        this.emeraldUpgrade1 = emeraldUpgrade1;
        this.emeraldUpgrade2 = emeraldUpgrade2;

        this.diamondBetween = diamondBetween1;
        this.emeraldBetween = emeraldBetween1;
        this.diamondLvl = 1;
        this.emeraldLvl = 1;
        this.diamondGenerated = 1;

        this.diamondBetweenLvl = getDiamondUpgradeForLevel();
        this.emeraldBetweenLvl = getEmeraldUpgradeForLevel();
    }

    public int getDiamondUpgradeForLevel() {
        if (diamondLvl == 1) {
            return diamondUpgrade1;
        }
        return diamondUpgrade2;
    }

    public int getEmeraldUpgradeForLevel() {
        if (emeraldLvl == 1) {
            return emeraldUpgrade1;
        }
        return emeraldUpgrade2;
    }

    public String getRomanNumber(long i) {

        if(i==0) {return "0";}
        else if(i==1) {return "I";}
        else if(i==2) {return "II";}
        else if(i==3) {return "III";}
        else if(i==4) {return "IV";}
        else if(i==5) {return "V";}
        else return "VI";
    }

    public int getDiamondForLevel() {
        switch (diamondLvl) {
            case 1: return diamondBetween1;
            case 2: return diamondBetween2;
            default: return diamondBetween3;
        }
    }

    public int getDiamondLvl() {
        return diamondLvl;
    }

    public int getEmeraldLvl() {
        return emeraldLvl;
    }



    public int getEmeraldForLevel() {
        switch (emeraldLvl) {
            case 1: return emeraldBetween1;
            case 2: return emeraldBetween2;
            default: return emeraldBetween3;
        }
    }

    private void runStartup() {
        if(!startupBoolean) {
            if(startupTask == null) startupTask = Bukkit.getScheduler().runTaskLater(Bedwars.INSTANCE(), () -> {startupBoolean = true;}, 20*2);
        }
    }


    public int getIronForLevel(TeamImpl team) {
        if(!startupBoolean) {
            runStartup();
            return 2;
        }
        switch (team.getForgeLevel()) {
            case 1: return ironBetween1;
            case 2: return ironBetween2;
            case 3: return ironBetween3;
            default: return ironBetween4;
        }
    }

    public int getGoldForLevel(TeamImpl team) {
        if(!startupBoolean) {
            runStartup();
            return 2;
        }
        switch (team.getForgeLevel()) {
            case 1: return goldBetween1;
            case 2: return goldBetween2;
            case 3: return goldBetween3;
            default: return goldBetween4;
        }
    }


    public int getIronBetween1() {
        return ironBetween1;
    }

    public int getIronBetween2() {
        return ironBetween2;
    }

    public int getIronBetween3() {
        return ironBetween3;
    }

    public int getGoldBetween1() {
        return goldBetween1;
    }

    public int getGoldBetween2() {
        return goldBetween2;
    }

    public int getGoldBetween3() {
        return goldBetween3;
    }

    public int getDiamondBetween1() {
        return diamondBetween1;
    }

    public int getDiamondBetween2() {
        return diamondBetween2;
    }

    public int getDiamondBetween3() {
        return diamondBetween3;
    }

    public int getDiamondUpgrade1() {
        return diamondUpgrade1;
    }

    public int getDiamondUpgrade2() {
        return diamondUpgrade2;
    }

    public int getEmeraldBetween1() {
        return emeraldBetween1;
    }

    public int getEmeraldBetween2() {
        return emeraldBetween2;
    }

    public int getEmeraldBetween3() {
        return emeraldBetween3;
    }
    public void dropItems(Location location, Material material) {
        if(!location.getChunk().isLoaded()) location.getChunk().load(true);
        Item item = location.getWorld().dropItemNaturally(location, new ItemStack(material));
        item.setPickupDelay(0);
        item.setVelocity(item.getVelocity().multiply(1.0));
    }

    public int getEmeraldUpgrade1() {
        return emeraldUpgrade1;
    }

    public int getEmeraldUpgrade2() {
        return emeraldUpgrade2;
    }

    public int getDiamondBetween() {
        return diamondBetween;
    }

    public int getEmeraldBetween() {
        return emeraldBetween;
    }

    public int getDiamondBetweenLvl() {
        return diamondBetweenLvl;
    }

    public int getEmeraldBetweenLvl() {
        return emeraldBetweenLvl;
    }

    public int getDiamondGenerated() {
        return diamondGenerated;
    }

    public Map<String, Collection<Consumer<String>>> getUpdaters() {
        return updaters;
    }

    public Updatable getDiamondLineOne() {
        return diamondLineOne;
    }

    public Updatable getDiamondLineTwo() {
        return diamondLineTwo;
    }

    public Updatable getDiamondLineThree() {
        return diamondLineThree;
    }

    public Updatable getEmeraldLineOne() {
        return emeraldLineOne;
    }

    public Updatable getEmeraldLineTwo() {
        return emeraldLineTwo;
    }

    public Updatable getEmeraldLineThree() {
        return emeraldLineThree;
    }

    public void setDiamondBetween(int diamondBetween) {
        this.diamondBetween = diamondBetween;
    }

    public void setEmeraldBetween(int emeraldBetween) {
        this.emeraldBetween = emeraldBetween;
    }

    public void setDiamondBetweenLvl(int diamondBetweenLvl) {
        this.diamondBetweenLvl = diamondBetweenLvl;
    }

    public void setEmeraldBetweenLvl(int emeraldBetweenLvl) {
        this.emeraldBetweenLvl = emeraldBetweenLvl;
    }

    public void setDiamondGenerated(int diamondGenerated) {
        this.diamondGenerated = diamondGenerated;
    }

    public void setDiamondLvl(int diamondLvl) {
        this.diamondLvl = diamondLvl;
    }

    public void setEmeraldLvl(int emeraldLvl) {
        this.emeraldLvl = emeraldLvl;
    }
}
