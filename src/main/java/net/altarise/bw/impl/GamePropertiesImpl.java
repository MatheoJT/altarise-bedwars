package net.altarise.bw.impl;

import com.mojang.authlib.GameProfile;
import net.altarise.api.utils.LocationUtils;
import net.altarise.api.utils.profile.GameProfileUtils;
import net.altarise.bw.Bedwars;
import net.altarise.bw.game.Generator;
import net.altarise.bw.game.ModeProperties;
import net.altarise.gameapi.engine.properties.GameProperties;
import net.altarise.gameapi.engine.properties.Team;
import net.altarise.gameapi.engine.properties.TeamProperties;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@SuppressWarnings("unused")
public class GamePropertiesImpl implements GameProperties {



    private final int maxPlayersPerTeam;
    private final int maxPlayers;
    private final int minPlayersStart = 2;
    private final int startTimer;
    private final Location spawn;
    private final Location middle;

    private final int goldLimit, ironLimit, diamondLimit, emeraldLimit, netherStarLimit;

    private final List<TeamImpl> teams = new ArrayList<>();
    private final List<Generator> generators = new ArrayList<>();

    private final List<TeamImpl> playingTeams = new ArrayList<>();
    private final List<TeamImpl> staticTeams = new ArrayList<>();
    private final ModeProperties modeProperties;


    public GamePropertiesImpl() {
        final ConfigurationSection properties = Bedwars.INSTANCE().getConfig().getConfigurationSection("properties");
        startTimer = properties.getInt("start-timer");
        spawn = LocationUtils.str2loc(properties.getString("spawn"));
        middle = LocationUtils.str2loc(properties.getString("middle"));


        final ConfigurationSection teamProperties = Bedwars.INSTANCE().getConfig().getConfigurationSection("teams");
        teamProperties.getKeys(false).forEach(s -> {
            final ConfigurationSection teamSection = teamProperties.getConfigurationSection(s);
            final String name = teamSection.getString("name");
            final ChatColor chatColor = ChatColor.getByChar(String.valueOf(teamSection.getString("color")));
            final byte byteColor = (byte) teamSection.getInt("byte-color");
            final Location spawnLocation = LocationUtils.str2loc(teamSection.getString("spawn"));
            final Location generatorLocation = LocationUtils.str2loc(teamSection.getString("generator"));
            final Location shopLocation = LocationUtils.str2loc(teamSection.getString("shop"));
            final Location upgradeLocation = LocationUtils.str2loc(teamSection.getString("upgrade"));
            final Location protectedArea1 = LocationUtils.str2loc(teamSection.getString("protected-area-1"));
            final Location protectedArea2 = LocationUtils.str2loc(teamSection.getString("protected-area-2"));
            final List<Location> bedLocation = teamSection.getStringList("bed-locations").stream().map(LocationUtils::str2loc).collect(Collectors.toList());
            TeamImpl team = new TeamImpl(s, name, chatColor, byteColor, spawnLocation, generatorLocation,shopLocation, upgradeLocation, protectedArea1, protectedArea2, bedLocation);
            teams.add(team);
            playingTeams.add(team);
            staticTeams.add(team);
        });

        this.modeProperties = new ModeProperties(Bedwars.INSTANCE().getAltariseAPI().getServerType().getId());
        ModeProperties.Section generators = modeProperties.getSection("generators");

        this.goldLimit = Integer.parseInt(generators.getValue("gold-limit"));
        this.ironLimit = Integer.parseInt(generators.getValue("iron-limit"));
        this.diamondLimit = Integer.parseInt(generators.getValue("diamond-limit"));
        this.emeraldLimit = Integer.parseInt(generators.getValue("emerald-limit"));
        this.netherStarLimit = Integer.parseInt(generators.getValue("netherstar-limit"));



        final int ironBetween1 = Integer.parseInt(generators.getValue("iron-between-1"));
        final int ironBetween2 = Integer.parseInt(generators.getValue("iron-between-2"));
        final int ironBetween3 = Integer.parseInt(generators.getValue("iron-between-3"));
        final int ironBetween4 = Integer.parseInt(generators.getValue("iron-between-4"));

        final int goldBetween1 = Integer.parseInt(generators.getValue("gold-between-1"));
        final int goldBetween2 = Integer.parseInt(generators.getValue("gold-between-2"));
        final int goldBetween3 = Integer.parseInt(generators.getValue("gold-between-3"));
        final int goldBetween4 = Integer.parseInt(generators.getValue("gold-between-4"));

        final int diamondBetween1 = Integer.parseInt(generators.getValue("diamond-between-1"));
        final int diamondBetween2 = Integer.parseInt(generators.getValue("diamond-between-2"));
        final int diamondBetween3 = Integer.parseInt(generators.getValue("diamond-between-3"));
        final int emeraldBetween1 = Integer.parseInt(generators.getValue("emerald-between-1"));
        final int emeraldBetween2 = Integer.parseInt(generators.getValue("emerald-between-2"));
        final int emeraldBetween3 = Integer.parseInt(generators.getValue("emerald-between-3"));
        final int diamondUpgrade1 = Integer.parseInt(generators.getValue("diamond-upgrade-1"));
        final int diamondUpgrade2 = Integer.parseInt(generators.getValue("diamond-upgrade-2"));
        final int emeraldUpgrade1 = Integer.parseInt(generators.getValue("emerald-upgrade-1"));
        final int emeraldUpgrade2 = Integer.parseInt(generators.getValue("emerald-upgrade-2"));

        final ConfigurationSection generatorProperties = Bedwars.INSTANCE().getConfig().getConfigurationSection("generators");
        generatorProperties.getStringList("diamond-locations").stream().map(LocationUtils::str2loc).forEach(location -> this.generators.add(new Generator(Generator.GeneratorType.DIAMOND, location)));
        generatorProperties.getStringList("emerald-locations").stream().map(LocationUtils::str2loc).forEach(location -> this.generators.add(new Generator(Generator.GeneratorType.EMERALD, location)));

        Bedwars.INSTANCE().getSchedulerProperties().register(ironBetween1, ironBetween2, ironBetween3, ironBetween4, goldBetween1, goldBetween2, goldBetween3, goldBetween4, diamondBetween1, diamondBetween2, diamondBetween3, diamondUpgrade1, diamondUpgrade2, emeraldBetween1, emeraldBetween2, emeraldBetween3, emeraldUpgrade1, emeraldUpgrade2);

        maxPlayers = modeProperties.getMaxPlayers();
        maxPlayersPerTeam = maxPlayers/4;
       //todo minPlayersStart = maxPlayers/2;
    }

    public GameProfile getShopProfile() {
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "Shop");
        //todo editable texture
        String NPCShopTexture = "ewogICJ0aW1lc3RhbXAiIDogMTYxODIxNTU4MTc1OCwKICAicHJvZmlsZUlkIiA6ICJlZDUzZGQ4MTRmOWQ0YTNjYjRlYjY1MWRjYmE3N2U2NiIsCiAgInByb2ZpbGVOYW1lIiA6ICI0MTQxNDE0MWgiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTA0NGNiODMyZDg3Y2RlNmFmNDJhMGRlNDdiYzg1YTY3YzdkNGU1OWEyZDc0NjY2MTc2ZDFjYTQxYWJkMGEyZCIKICAgIH0KICB9Cn0=";
        String NPCShopSignature = "T5QGS3fQ9wWvsjmD6l9b/nZMkfOfYW1X3c1xvDdZQ5WHvPmew//3Q86+yfgQqIjPvEcXiDilr71p3WDrz/itsLb5mf9wLU5P4X18x5c6bmmv49TDLUCH5mEIUXu1jiQ8Kog/vzZNGZAAxadTGQPJ7BdII/+OpHDLS+WiCPRMnjCs/1h5RTE7I1OOPQnsh+yk+gOpaxCxgVFMLnMqNnL3mJP05qajHI6OKKXnyyXPwV0xxA3XT2WPbtCPsux3CjNCPP7fA1mYL4dPtdTaju9kP+6jeuf0IkS0jZ31bHKx324cM/W4xiSbR/2OSyYepHdS7TxWPZIYpkMPbaHMLXao7Ok209LD7p3GWZ5RDNvnZTcvGlF10wKoHJ9xy7lHoSfy4NfRAD3doATK5meRo7/JQCCo8M8Mw6dnBvYC9bcb3zCrvTkwQz2dfjkHvmH/QcWkJS5iqYCS6Uk67PJsFtYxa5a9ZBiZGUVxhprrB0hoZem0vfsnzGgzbwjpw0VxDSN1ndXSIJZ4yXB2KI58NE0HMjkVL9OcmOItoS4fqLqdo7CqrntdHsRcDZ7lSaCFVphBMsJI3AbrWAyIM54N9SSMJgpQkrbJ1tWhO1jp8mTXGqW1YlbmCEFS+LRR6sk/F3YK6FtSucJlhlrOdeKGHVaESWLVFzTMBgfVS3TfKSxSRI8=";
        GameProfileUtils.changeProfileSkin(gameProfile, NPCShopTexture, NPCShopSignature);
        return gameProfile;
    }

    public GameProfile getUpgradeProfile() {
        GameProfile gameProfile = new GameProfile(UUID.fromString("37925f06-1c5a-4be4-a8da-dc218e40e651"), "Upgrade");
        //todo editable texture
        String NPCUpgradeTexture = "ewogICJ0aW1lc3RhbXAiIDogMTYzMzg4NTA0MzEyNCwKICAicHJvZmlsZUlkIiA6ICIwNjNhMTc2Y2RkMTU0ODRiYjU1MjRhNjQyMGM1YjdhNCIsCiAgInByb2ZpbGVOYW1lIiA6ICJkYXZpcGF0dXJ5IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzRlNWRjYzY0OWYwZDU5OTAxNTQzZWRiYjNlYzRkMmViNzU0ZmVkMzllYmFjODRjZjViYWE5MGY5NDc1NmFiMGUiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==";
        String NPCUpgradeSignature = "lhMwi08niId3pU0n/gc1YdS+qcIM8sgL+63KQOuDhmMNaOtKx4QwV+ghXQnksLxGqjuqUVXICPzD0rK3dToU5Q9RkG5jQv/QYPhp9P5RSZN46x+UU8JTgpUsFMLffzWu2iXoO3CfW4AWId8nY2uwLViNEhHtYuIa7Xl1tptZGP/kdmCEIGRZLHXi0olIGWkQ17Dqot8zJGLqEfiZh5aDEJsDmKUngFFeIbNgi/YAg5V+cH19vK41Ax6ZvTHVKaiqb8JuEBJA+/8NOsTEvj+Y+c7TfrVF86+ioJS5OVDnLLkr4HFP5F0+5hldabrb9IoHpHghiNfH2guOY3sw5SbAEUgA/QV0kLDgsEaZTEKJWK+W9f6OVy7BFOMMtrx9XhbtV/uIj//J9t2QPX2UXit+k7+B1JlSNh4RDOCyRMUCysG0//lsyvJe1B3b0oLhMXg3DIn3SmHc2eMnnLUm/xYqBLtEQJcZM+BzNr0kDItLAMtmok2id/kEj9zEQjYBbEa4sCArqicVnk7vUx89Mpa/W81KQibn82EeT/2GYKrjXKw/k6io+Pmxt6UGuLuC4IISF9E1AODLUNUSKBPODQbfQCwUBoFK/xjx8l0y22iOW3zVgvngNROLcULgexznIO3SGxh7fizBCtZPCWsgtehaeHrp/oV9vNL5v7ytgarBWTY=";
        GameProfileUtils.changeProfileSkin(gameProfile, NPCUpgradeTexture, NPCUpgradeSignature);
        return gameProfile;
    }

    public TeamImpl getPlayerPlayingTeam(Player player) {
        for (TeamImpl team : playingTeams) {
            if (team.getPlayers().contains(player)) {
                return team;
            }
        }
        return null;
    }

    public int getDiamondLimit() {
        return diamondLimit;
    }

    public int getEmeraldLimit() {
        return emeraldLimit;
    }

    public int getIronLimit() {
        return ironLimit;
    }



    public int getGoldLimit() {
        return goldLimit;
    }

    public int getNetherStarLimit() {
        return netherStarLimit;
    }

    public List<TeamImpl> getPlayingTeams() {
        return playingTeams;
    }

    public List<TeamImpl> getTeams() {
        return teams;
    }

    public int getMaxPlayersPerTeam() {
        return maxPlayersPerTeam;
    }

    public Location getMiddle() {
        return middle;
    }

    public List<TeamImpl> getStaticTeams() {
        return staticTeams;
    }

    @Override
    public int getMaxPlayerCount() {
        return maxPlayers;
    }

    @Override
    public int getMinPlayerToStart() {
        return minPlayersStart;
    }

    @Override
    public int getStartTimer() {
        return startTimer;
    }

    public ModeProperties getModeProperties() {
        return modeProperties;
    }

    @Override
    public Location getSpawn() {
        return spawn;
    }

    @Override
    public boolean useWaitingRoom() {
        return true;
    }

    @Nullable
    @Override
    public TeamProperties<? extends Team> getTeamProperties() {
        return new TeamPropertiesImpl();
    }

    public List<Generator> getGenerators() {
        return generators;
    }
}
