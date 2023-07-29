package net.altarise.bw.impl;

import net.altarise.api.network.BasicServerType;
import net.altarise.bean.model.data.statistic.PlayerStatistic;
import net.altarise.bw.Bedwars;
import net.altarise.gameapi.engine.GamePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("unused")
public class GamePlayerImpl extends GamePlayer {

    private int armorLvl;
    private int pickaxeLvl;
    private int axeLvl;
    private final Player player;
    private int destroyedBed;
    private int kills;
    private int finalKills;

    private boolean shears;
    private boolean regeneration;

    private final HashMap<String, String> gameData = new HashMap<>();



    public GamePlayerImpl(Player player) {
        super(player);
        this.player = player;
        this.armorLvl = 1;
        this.pickaxeLvl = 0;
        this.axeLvl = 0;
        this.destroyedBed = 0;
        this.kills = 0;
        this.finalKills = 0;
        this.regeneration = false;
        this.shears = false;
    }
    public int getKills() {
        return kills;
    }

    public int getFinalKills() {
        return finalKills;
    }

    public void addFinalKill() {
        this.finalKills++;
    }

    public void addKill() {
        kills++;
    }

    public void setArmorLvl(int armorLvl) {
        this.armorLvl = armorLvl;
    }

    public int getArmorLvl() {
        return armorLvl;
    }

    public boolean hasShears() {
        return shears;
    }

    public void addShears() {
        this.shears = true;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public Map<String, String> getGameData(Map<String, String> map) {
        return null;
    }

    public int getAxeLvl() {
        return axeLvl;
    }

    public int getPickaxeLvl() {
        return pickaxeLvl;
    }

    public void addArmorLvl() {

        if(armorLvl <4) this.armorLvl++;
    }

    public void addPickaxeLvl() {
        this.pickaxeLvl++;
    }

    public void addAxeLvl() {
         this.axeLvl++;
    }

    public void removeArmorLvl() {
        if(armorLvl > 0) this.armorLvl--;
    }

    public int getDestroyedBed() {
        return destroyedBed;
    }

    public void addDestroyedBed() {
        this.destroyedBed++;
    }

    public void removePickaxeLvl() {
        if(pickaxeLvl > 0) this.pickaxeLvl--;
    }

    public void removeAxeLvl() {
       if(axeLvl > 0) this.axeLvl--;
    }


    public PlayerStatistic getStatistic() {
        Optional<PlayerStatistic> statistic = Bedwars.INSTANCE().getStatisticManager().getPlayerStatistic(player.getUniqueId().toString(), BasicServerType.BED_WARS.toServerType(Bedwars.INSTANCE().getAltariseAPI()));
        assert statistic.isPresent();
        return statistic.get();
    }

    public void setRegeneration(boolean regeneration) {
        this.regeneration = regeneration;
    }

    public boolean hasRegeneration() {
        return regeneration;
    }

    public Map<String, String> getTempData() {
        return gameData;
    }
}
