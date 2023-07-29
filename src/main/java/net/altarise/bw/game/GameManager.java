package net.altarise.bw.game;

import net.altarise.api.AltariseAPI;
import net.altarise.api.minecraft.hologram.Hologram;
import net.altarise.api.minecraft.npc.NPC;
import net.altarise.api.minecraft.npc.Type;
import net.altarise.api.minecraft.npc.callback.Action;
import net.altarise.api.minecraft.npc.type.NPCFakePlayer;
import net.altarise.api.minecraft.scoreboard.Scoreboard;
import net.altarise.api.text.Component;
import net.altarise.api.utils.ItemBuilder;
import net.altarise.api.utils.PlayerUtils;
import net.altarise.bw.Bedwars;
import net.altarise.bw.game.inventory.inventories.shop.QuickBuy;
import net.altarise.bw.game.inventory.inventories.upgrade.Enchants;
import net.altarise.bw.impl.PlayingScoreboard;
import net.altarise.bw.impl.SpecScoreboard;
import net.altarise.bw.impl.TeamImpl;
import net.altarise.gameapi.basic.GameState;
import net.altarise.gameapi.engine.coherence.GameCoherence;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

@SuppressWarnings("all")
public class GameManager implements GameCoherence {

    private BukkitTask generatorRunnable;


    public final List<NPC> npcs = new ArrayList<>();
    public final List<Hologram> holograms = new ArrayList<>();
    public final HashMap<Player, TeamImpl> trackers = new HashMap<>();



    @Override
    public void onGameStart() {

        for(Player player : Bedwars.INSTANCE().getGamePlayers()) {
            PlayerUtils.cleanPlayer(player, GameMode.SURVIVAL);
            Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getLocation().getChunk().load(true);
            player.teleport(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getLocation());
            reloadPlayer(player);
            AltariseAPI.get().getMinecraftProvider().getScoreboardManager().setPlayerScoreboard(player, getPlayingScoreboard(player));
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
        }

            Bukkit.getWorld("world").setDifficulty(Difficulty.HARD);
            Bukkit.getWorld("world").setPVP(true);
            win();
            startEngine();

    }



    public void reloadPlayer(Player player) {
        player.getInventory().addItem(new ItemBuilder(Material.WOOD_SWORD).unbreakable(true).build());
        player.getInventory().setItem(17, new ItemBuilder(Material.COMPASS).displayname("§aTraqueur").lore("", "§7Cliquez ici pour traquer", "§7une équipe").build());
        switch (Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).getArmorLvl()) {

            case 2:
                player.getInventory().setArmorContents(new ItemStack[]{new ItemBuilder(Material.CHAINMAIL_BOOTS).unbreakable(true).build(), new ItemBuilder(Material.LEATHER_LEGGINGS).unbreakable(true).color(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getColor()).build(), new ItemBuilder(Material.CHAINMAIL_CHESTPLATE).unbreakable(true).build(), new ItemBuilder(Material.LEATHER_HELMET).unbreakable(true).color(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getColor()).build()});
                break;
            case 3:
                player.getInventory().setArmorContents(new ItemStack[]{new ItemBuilder(Material.IRON_BOOTS).unbreakable(true).build(), new ItemBuilder(Material.LEATHER_LEGGINGS).unbreakable(true).color(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getColor()).build(), new ItemBuilder(Material.IRON_CHESTPLATE).unbreakable(true).build(), new ItemBuilder(Material.LEATHER_HELMET).unbreakable(true).color(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getColor()).build()});
                break;
            case 4:
                player.getInventory().setArmorContents(new ItemStack[]{new ItemBuilder(Material.DIAMOND_BOOTS).unbreakable(true).build(), new ItemBuilder(Material.LEATHER_LEGGINGS).unbreakable(true).color(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getColor()).build(), new ItemBuilder(Material.DIAMOND_CHESTPLATE).unbreakable(true).build(), new ItemBuilder(Material.LEATHER_HELMET).unbreakable(true).color(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getColor()).build()});
                break;
            default:
                player.getInventory().setArmorContents(new ItemStack[]{new ItemBuilder(Material.LEATHER_BOOTS).unbreakable(true).color(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getColor()).build(), new ItemBuilder(Material.LEATHER_LEGGINGS).unbreakable(true).color(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getColor()).build(), new ItemBuilder(Material.LEATHER_CHESTPLATE).unbreakable(true).color(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getColor()).build(), new ItemBuilder(Material.LEATHER_HELMET).unbreakable(true).color(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getColor()).build()});
                break;
        }

     switch (Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).getAxeLvl()) {
            case 1:
                player.getInventory().addItem(new ItemBuilder(Material.STONE_AXE).unbreakable(true).build());
                break;
            case 2:
                player.getInventory().addItem(new ItemBuilder(Material.IRON_AXE).unbreakable(true).build());
                break;
            case 3:
                player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_AXE).unbreakable(true).build());
                break;
     }

     switch (Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).getPickaxeLvl()) {
            case 1:
                player.getInventory().addItem(new ItemBuilder(Material.STONE_PICKAXE).unbreakable(true).build());
                break;
            case 2:
                player.getInventory().addItem(new ItemBuilder(Material.IRON_PICKAXE).unbreakable(true).build());
                break;
            case 3:
                player.getInventory().addItem(new ItemBuilder(Material.DIAMOND_PICKAXE).unbreakable(true).build());
                break;
     }

     if(Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).hasShears()) {
         player.getInventory().addItem(new ItemBuilder(Material.SHEARS).unbreakable(true).build());
     }

     enchant(player);

    }

    private enum SwordValues { WOOD_SWORD, STONE_SWORD, IRON_SWORD, DIAMOND_SWORD, GOLD_SWORD }

    public void checkPlayerHasSword(Player player) {
        if(Bedwars.INSTANCE().getGameAPI().getGameState().equals(GameState.WAITING) || Bedwars.INSTANCE().getGameAPI().getGameState().equals(GameState.STARTING)) return;
        if (!Bedwars.INSTANCE().getGamePlayers().contains(player))
            return;

        if (hasBetterSword(player)) {
            removeWoodSword(player);
        } else {
            ensureOneWoodSword(player);
        }
    }

    private boolean hasBetterSword(Player player) {
        for (SwordValues swordValue : SwordValues.values()) {
            if (swordValue == SwordValues.WOOD_SWORD)
                continue;
            if (player.getInventory().contains(Material.valueOf(swordValue.toString()))) {
                return true;
            }
        }
        return false;
    }

    private void removeWoodSword(Player player) {
        player.getInventory().remove(Material.WOOD_SWORD);
    }

    private void ensureOneWoodSword(Player player) {
        if (player.getInventory().all(Material.WOOD_SWORD).size() > 1) {
            player.getInventory().removeItem(new ItemStack(Material.WOOD_SWORD, player.getInventory().all(Material.WOOD_SWORD).size() - 1));
        } else if (!player.getInventory().contains(Material.WOOD_SWORD)) {
            player.getInventory().addItem(new ItemBuilder(Material.WOOD_SWORD).unbreakable(true).build());
        }
    }

    public void enchant(Player player) {
        if(!Bedwars.INSTANCE().getGamePlayers().contains(player)) return;
        final TeamImpl team = Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player);

        if(team.hasSharpened()) {
            for(ItemStack item : player.getInventory().getContents()) {
                if(item != null && (item.getType().equals(Material.STONE_SWORD) || item.getType().equals(Material.IRON_SWORD) || item.getType().equals(Material.WOOD_SWORD) || item.getType().equals(Material.DIAMOND_SWORD))) {
                    if(!item.getEnchantments().containsKey(Enchantment.DAMAGE_ALL)) item.addEnchantment(Enchantment.DAMAGE_ALL, 1);
                }
            }
        }

        if(team.getArmorLevel() > 0) {
            for(ItemStack item : player.getInventory().getArmorContents()) {
                if(item != null && (item.getType().equals(Material.LEATHER_HELMET) || item.getType().equals(Material.LEATHER_CHESTPLATE) || item.getType().equals(Material.LEATHER_LEGGINGS) || item.getType().equals(Material.LEATHER_BOOTS) || item.getType().equals(Material.CHAINMAIL_HELMET) || item.getType().equals(Material.CHAINMAIL_CHESTPLATE) || item.getType().equals(Material.CHAINMAIL_LEGGINGS) || item.getType().equals(Material.CHAINMAIL_BOOTS) || item.getType().equals(Material.IRON_HELMET) || item.getType().equals(Material.IRON_CHESTPLATE) || item.getType().equals(Material.IRON_LEGGINGS) || item.getType().equals(Material.IRON_BOOTS) || item.getType().equals(Material.GOLD_HELMET) || item.getType().equals(Material.GOLD_CHESTPLATE) || item.getType().equals(Material.GOLD_LEGGINGS) || item.getType().equals(Material.GOLD_BOOTS) || item.getType().equals(Material.DIAMOND_HELMET) || item.getType().equals(Material.DIAMOND_CHESTPLATE) || item.getType().equals(Material.DIAMOND_LEGGINGS) || item.getType().equals(Material.DIAMOND_BOOTS))) {
                    if(!item.getEnchantments().containsKey(Enchantment.PROTECTION_ENVIRONMENTAL) || item.getEnchantments().get(Enchantment.PROTECTION_ENVIRONMENTAL) != team.getArmorLevel()) item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, team.getArmorLevel());
                }
            }
        }

        if(team.getMinerLevel() > 0) {
            for(ItemStack item : player.getInventory().getContents()) {
                if(item != null && (item.getType().equals(Material.IRON_PICKAXE) || item.getType().equals(Material.DIAMOND_PICKAXE) || item.getType().equals(Material.STONE_PICKAXE) || item.getType().equals(Material.WOOD_PICKAXE) || item.getType().equals(Material.WOOD_AXE) || item.getType().equals(Material.STONE_AXE) || item.getType().equals(Material.IRON_AXE) || item.getType().equals(Material.DIAMOND_AXE))) {
                    if(!item.getEnchantments().containsKey(Enchantment.DIG_SPEED)) item.addEnchantment(Enchantment.DIG_SPEED, team.getMinerLevel());
                }
            }
        }

    }



    public void loadNPCs() {

        for (TeamImpl team : Bedwars.INSTANCE().getGameProperties().getTeams()) {

         NPC shop = AltariseAPI.get().getMinecraftProvider().getNPCManager().create(Type.FAKE_PLAYER, "shop" + team.getByteColor() , Component.text(team.getChatColor() + "Boutique"), team.getShopLocation().add(team.getShopLocation().getBlockX() > 0 ? 0.5 : -0.5, 0.0, team.getShopLocation().getBlockZ() > 0 ? 0.5 : -0.5));
            shop.listen(Action.RIGHT_CLICK, (player, entity) -> new QuickBuy(player).open(player)).listen(Action.LEFT_CLICK, (player, entity) ->  new QuickBuy(player).open(player));
         NPCFakePlayer fakeShop = (NPCFakePlayer) shop;
            String NPCShopTexture = "ewogICJ0aW1lc3RhbXAiIDogMTYxODIxNTU4MTc1OCwKICAicHJvZmlsZUlkIiA6ICJlZDUzZGQ4MTRmOWQ0YTNjYjRlYjY1MWRjYmE3N2U2NiIsCiAgInByb2ZpbGVOYW1lIiA6ICI0MTQxNDE0MWgiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTA0NGNiODMyZDg3Y2RlNmFmNDJhMGRlNDdiYzg1YTY3YzdkNGU1OWEyZDc0NjY2MTc2ZDFjYTQxYWJkMGEyZCIKICAgIH0KICB9Cn0=";
            String NPCShopSignature = "T5QGS3fQ9wWvsjmD6l9b/nZMkfOfYW1X3c1xvDdZQ5WHvPmew//3Q86+yfgQqIjPvEcXiDilr71p3WDrz/itsLb5mf9wLU5P4X18x5c6bmmv49TDLUCH5mEIUXu1jiQ8Kog/vzZNGZAAxadTGQPJ7BdII/+OpHDLS+WiCPRMnjCs/1h5RTE7I1OOPQnsh+yk+gOpaxCxgVFMLnMqNnL3mJP05qajHI6OKKXnyyXPwV0xxA3XT2WPbtCPsux3CjNCPP7fA1mYL4dPtdTaju9kP+6jeuf0IkS0jZ31bHKx324cM/W4xiSbR/2OSyYepHdS7TxWPZIYpkMPbaHMLXao7Ok209LD7p3GWZ5RDNvnZTcvGlF10wKoHJ9xy7lHoSfy4NfRAD3doATK5meRo7/JQCCo8M8Mw6dnBvYC9bcb3zCrvTkwQz2dfjkHvmH/QcWkJS5iqYCS6Uk67PJsFtYxa5a9ZBiZGUVxhprrB0hoZem0vfsnzGgzbwjpw0VxDSN1ndXSIJZ4yXB2KI58NE0HMjkVL9OcmOItoS4fqLqdo7CqrntdHsRcDZ7lSaCFVphBMsJI3AbrWAyIM54N9SSMJgpQkrbJ1tWhO1jp8mTXGqW1YlbmCEFS+LRR6sk/F3YK6FtSucJlhlrOdeKGHVaESWLVFzTMBgfVS3TfKSxSRI8=";
            fakeShop.updateSkin(NPCShopTexture, NPCShopSignature);
            fakeShop.setNameVisible(true);
            fakeShop.removeFromTabList();

         NPC upgrade = AltariseAPI.get().getMinecraftProvider().getNPCManager().create(Type.FAKE_PLAYER,"upgrade" + team.getByteColor() , Component.text(team.getChatColor() + "Amélioration"), team.getUpgradeLocation().add(team.getUpgradeLocation().getBlockX() > 0 ? 0.5 : -0.5, 0.0, team.getUpgradeLocation().getBlockZ() > 0 ? 0.5 : -0.5));
            upgrade.listen(Action.RIGHT_CLICK, (player, entity) -> new Enchants(player).open(player)).listen(Action.LEFT_CLICK, (player, entity) ->  new Enchants(player).open(player));
            NPCFakePlayer fakeUpgrade = (NPCFakePlayer) upgrade;
            String NPCUpgradeTexture = "ewogICJ0aW1lc3RhbXAiIDogMTYzMzg4NTA0MzEyNCwKICAicHJvZmlsZUlkIiA6ICIwNjNhMTc2Y2RkMTU0ODRiYjU1MjRhNjQyMGM1YjdhNCIsCiAgInByb2ZpbGVOYW1lIiA6ICJkYXZpcGF0dXJ5IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzRlNWRjYzY0OWYwZDU5OTAxNTQzZWRiYjNlYzRkMmViNzU0ZmVkMzllYmFjODRjZjViYWE5MGY5NDc1NmFiMGUiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==";
            String NPCUpgradeSignature = "lhMwi08niId3pU0n/gc1YdS+qcIM8sgL+63KQOuDhmMNaOtKx4QwV+ghXQnksLxGqjuqUVXICPzD0rK3dToU5Q9RkG5jQv/QYPhp9P5RSZN46x+UU8JTgpUsFMLffzWu2iXoO3CfW4AWId8nY2uwLViNEhHtYuIa7Xl1tptZGP/kdmCEIGRZLHXi0olIGWkQ17Dqot8zJGLqEfiZh5aDEJsDmKUngFFeIbNgi/YAg5V+cH19vK41Ax6ZvTHVKaiqb8JuEBJA+/8NOsTEvj+Y+c7TfrVF86+ioJS5OVDnLLkr4HFP5F0+5hldabrb9IoHpHghiNfH2guOY3sw5SbAEUgA/QV0kLDgsEaZTEKJWK+W9f6OVy7BFOMMtrx9XhbtV/uIj//J9t2QPX2UXit+k7+B1JlSNh4RDOCyRMUCysG0//lsyvJe1B3b0oLhMXg3DIn3SmHc2eMnnLUm/xYqBLtEQJcZM+BzNr0kDItLAMtmok2id/kEj9zEQjYBbEa4sCArqicVnk7vUx89Mpa/W81KQibn82EeT/2GYKrjXKw/k6io+Pmxt6UGuLuC4IISF9E1AODLUNUSKBPODQbfQCwUBoFK/xjx8l0y22iOW3zVgvngNROLcULgexznIO3SGxh7fizBCtZPCWsgtehaeHrp/oV9vNL5v7ytgarBWTY=";
            fakeUpgrade.updateSkin(NPCUpgradeTexture, NPCUpgradeSignature);
            fakeUpgrade.setNameVisible(true);
            fakeUpgrade.removeFromTabList();

            npcs.addAll(Arrays.asList(shop, upgrade));

        }
    }




    private boolean sent = false;
    public void win() {

        for(Iterator<TeamImpl> teams = Bedwars.INSTANCE().getGameProperties().getPlayingTeams().iterator(); teams.hasNext();) {
            TeamImpl team = teams.next();
            if(team.getPlayers().size() == 0) {
                team.setEliminated();
                team.getBedLocations().forEach(location -> location.getBlock().setType(Material.AIR));
                teams.remove();
            }

        }


        if(Bedwars.INSTANCE().getGameProperties().getPlayingTeams().size() == 1) {
            if (!sent) {
                final TeamImpl team = Bedwars.INSTANCE().getGameProperties().getPlayingTeams().get(0);
                Bedwars.INSTANCE().getGameAPI().getGameManager().setTeamWinner(team.getPlayers(), team.getChatColor() + StringUtils.capitalize(team.getName()));
                Bedwars.INSTANCE().getGameAPI().getGameManager().setWinners(Bedwars.INSTANCE().getGameProperties().getPlayingTeams().get(0).getPlayers());
                Bedwars.INSTANCE().getGameProperties().getPlayingTeams().get(0).getPlayers().forEach(player -> sendEndMessage(player, true));
                stopEngine();
                sent = true;
            }
        }
    }

    public void playerFinalKill(Player player, Player killer) {
        if(killer == null ) Bukkit.broadcastMessage("§cFINAL KILL " + Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getChatColor() + player.getName() + "§f est mort !");
        if(killer != null ) Bukkit.broadcastMessage("§cFINAL KILL " + Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getChatColor() + player.getName() + " §fà été tué par " + Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(killer).getChatColor() + killer.getName() + "§f !");
        sendEndMessage(player, false);
    }


    public void sendEndMessage(Player player, boolean win) {
        String s = win ? "§aGAGNÉ" : "§cPERDU";

        TextComponent str = new TextComponent("    ");
        TextComponent space = new TextComponent("         ");

        TextComponent REPLAY = new TextComponent("REJOUER"); REPLAY.setBold(true);
        REPLAY.setColor(net.md_5.bungee.api.ChatColor.GREEN);
        REPLAY.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Cliquez §epour rejouer !").create()));
        REPLAY.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/re"));

        TextComponent LEAVE = new TextComponent("QUITTER"); LEAVE.setBold(true);
        LEAVE.setColor(net.md_5.bungee.api.ChatColor.RED);
        LEAVE.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§6Cliquez §epour retourner au lobby !").create()));
        LEAVE.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lobby"));

        str.addExtra(REPLAY); str.addExtra(space); str.addExtra(LEAVE);

        player.sendMessage(" ");
        player.sendMessage("       §fVous avez " + s + " §f!");
        player.sendMessage(" ");
        player.sendMessage("         §b◢ §f§lStatistiques");
        player.sendMessage("         §b» §6" + Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).getDestroyedBed() + "§7 Lit(s) détruit(s)");
        player.sendMessage("         §b» §6" + Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).getFinalKills() + "§7 Final kill(s)");
        player.sendMessage("         §b» §6" + Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).getKills() + "§7 Kill(s)");
        player.sendMessage(" ");
        player.sendMessage(" ");
        player.spigot().sendMessage(str);
        player.sendMessage(" ");
    }


    public void startEngine() {
        Bedwars.INSTANCE().getGeneratorScheduler().runTaskTimer(Bedwars.INSTANCE(), 10, 1);
        Bedwars.INSTANCE().getGameScheduler().runTaskTimer(Bedwars.INSTANCE(), 5, 20);

    }
    public void stopEngine() {
        Bedwars.INSTANCE().getGameScheduler().cancel();
        Bedwars.INSTANCE().getGeneratorScheduler().cancel();

    }
    public void loadHolograms() {
        for (Generator generator : Bedwars.INSTANCE().getGameProperties().getGenerators()) {
            if(generator.getType().equals(Generator.GeneratorType.DIAMOND)) {
                Hologram hologram = AltariseAPI.get().getMinecraftProvider().getHologramManager().createHologram(generator.getLocation().clone().add(0,-1,0), Component.updatable(Bedwars.INSTANCE().getSchedulerProperties().getDiamondLineTwo()), Component.updatable(Bedwars.INSTANCE().getSchedulerProperties().getDiamondLineOne()), Component.updatable(Bedwars.INSTANCE().getSchedulerProperties().getDiamondLineThree()));
                Bukkit.getOnlinePlayers().forEach(hologram::addReceiver);
                holograms.add(hologram);
            }

            if(generator.getType().equals(Generator.GeneratorType.EMERALD)) {
                Hologram hologram = AltariseAPI.get().getMinecraftProvider().getHologramManager().createHologram(generator.getLocation().clone().add(0, -1, 0), Component.updatable(Bedwars.INSTANCE().getSchedulerProperties().getEmeraldLineTwo()), Component.updatable(Bedwars.INSTANCE().getSchedulerProperties().getEmeraldLineOne()), Component.updatable(Bedwars.INSTANCE().getSchedulerProperties().getEmeraldLineThree()));
                Bukkit.getOnlinePlayers().forEach(hologram::addReceiver);
                holograms.add(hologram);
            }

        }
    }




    @Override
    public Scoreboard getPlayingScoreboard(Player player) {
        return new PlayingScoreboard(player);
    }

    @Override
    public Scoreboard getSpectatorScoreboard(Player player) {
        return new SpecScoreboard();
    }



}
