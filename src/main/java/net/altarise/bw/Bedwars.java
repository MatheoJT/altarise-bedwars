package net.altarise.bw;

import net.altarise.bean.service.data.statistic.StatisticManager;
import net.altarise.bw.commands.CommandBTP;
import net.altarise.bw.commands.CommandBoooom;
import net.altarise.bw.commands.CommandEvents;
import net.altarise.bw.game.GameManager;
import net.altarise.bw.game.events.EventManager;
import net.altarise.bw.game.inventory.AbstractInventory;
import net.altarise.bw.game.listeners.ListenersManager;
import net.altarise.bw.game.schedulers.GameScheduler;
import net.altarise.bw.game.schedulers.GeneratorScheduler;
import net.altarise.bw.game.schedulers.SchedulerProperties;
import net.altarise.bw.impl.GamePlayerImpl;
import net.altarise.bw.impl.GamePropertiesImpl;
import net.altarise.bw.impl.TeamPropertiesImpl;
import net.altarise.gameapi.engine.Game;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;

public class Bedwars extends Game<GamePlayerImpl> {

    private static Bedwars instance;

    private GamePropertiesImpl gameProperties;
    private GameManager gameManager;
    private TeamPropertiesImpl teamProperties;
    private EventManager eventManager;
    private GeneratorScheduler generatorScheduler;
    private GameScheduler gameScheduler;
    private SchedulerProperties schedulerProperties;
    private StatisticManager statisticManager;



    @Override
    public void onEnable() {
        super.onEnable();
        
        saveDefaultConfig();

        instance = this;
        statisticManager = this.getAltariseAPI().getDataProvider().getStatisticManager();
        schedulerProperties = new SchedulerProperties();
        gameScheduler = new GameScheduler();
        generatorScheduler = new GeneratorScheduler();
        gameManager = new GameManager();
        gameProperties = new GamePropertiesImpl();
        teamProperties = new TeamPropertiesImpl();
        eventManager = new EventManager();

        gameManager.loadNPCs();
        gameManager.loadHolograms();

        new ListenersManager(this);
        Bukkit.getPluginManager().registerEvents(new AbstractInventory(), this);

        Bukkit.getWorld("world").setTime(1000);
        Bukkit.getWorld("world").setGameRuleValue("doMobSpawning", String.valueOf(false));
        Bukkit.getWorld("world").setGameRuleValue("doDaylightCycle", String.valueOf(false));
        Bukkit.getWorld("world").setGameRuleValue("doMobLoot", String.valueOf(true));
        Bukkit.getWorld("world").setGameRuleValue("doEntityDrops", String.valueOf(true));
        Bukkit.getWorld("world").setGameRuleValue("doFireTick", String.valueOf(true));
        Bukkit.getWorld("world").setGameRuleValue("mobGriefing", String.valueOf(true));
        Bukkit.getWorld("world").setGameRuleValue("tntExplodes", String.valueOf(true));
        Bukkit.getWorld("world").setPVP(false);
        Bukkit.getWorld("world").setDifficulty(Difficulty.PEACEFUL);

        getCommand("boooom").setExecutor(new CommandBoooom());
        getCommand("events").setExecutor(new CommandEvents());
        getCommand("btp").setExecutor(new CommandBTP());

        this.getGameAPI().registerNewGame(this);
    }

    @Override
    public GamePropertiesImpl getGameProperties() {
        return gameProperties;
    }

    @Override
    public GameManager getGameCoherence() {
        return gameManager;
    }

    public TeamPropertiesImpl getTeamProperties() {
        return teamProperties;
    }

    public static Bedwars INSTANCE() {
        return instance;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public GeneratorScheduler getGeneratorScheduler() {
        return generatorScheduler;
    }

    public GameScheduler getGameScheduler() {
        return gameScheduler;
    }

    public SchedulerProperties getSchedulerProperties() {
        return schedulerProperties;
    }

    public StatisticManager getStatisticManager() {
        return statisticManager;
    }
}
