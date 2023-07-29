package net.altarise.bw.game.listeners.player;

import net.altarise.api.utils.ItemBuilder;
import net.altarise.api.utils.PlayerUtils;
import net.altarise.api.utils.reflection.Reflection;
import net.altarise.api.utils.title.Titles;
import net.altarise.bw.Bedwars;
import net.altarise.bw.impl.TeamImpl;
import net.altarise.gameapi.basic.GameState;
import net.altarise.gameapi.event.player.PlayerDamagePlayerEvent;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class PlayerListener implements Listener {

    private final Scoreboard scoreboard;
    private final Team team;

    public PlayerListener() {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        team = scoreboard.registerNewTeam("hide_nametag");
        team.setNameTagVisibility(NameTagVisibility.NEVER);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(Bedwars.INSTANCE().getGameAPI().getGameState().equals(GameState.WAITING) || Bedwars.INSTANCE().getGameAPI().getGameState().equals(GameState.STARTING)) return;
        if(!(event.getEntity() instanceof Player)) return;
        if(!Bedwars.INSTANCE().getGamePlayers().contains((Player) event.getEntity())) return;


        if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL)

                || event.getCause().equals(EntityDamageEvent.DamageCause.MAGIC)
                || event.getCause().equals(EntityDamageEvent.DamageCause.FIRE)
                || event.getCause().equals(EntityDamageEvent.DamageCause.FIRE_TICK)
                || event.getCause().equals(EntityDamageEvent.DamageCause.LAVA)
                || event.getCause().equals(EntityDamageEvent.DamageCause.LIGHTNING)
                || event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)
                || event.getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION)
                || event.getCause().equals(EntityDamageEvent.DamageCause.VOID)
                || event.getCause().equals(EntityDamageEvent.DamageCause.WITHER)
                || event.getCause().equals(EntityDamageEvent.DamageCause.CONTACT)
                || event.getCause().equals(EntityDamageEvent.DamageCause.DROWNING)
                || event.getCause().equals(EntityDamageEvent.DamageCause.FALLING_BLOCK)) {

            Player player = (Player) event.getEntity();

            if(player.getHealth() - event.getFinalDamage() > 0) return;
            event.setCancelled(true);
            if(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).isBedDestroyed()) {
                Bedwars.INSTANCE().getGameAPI().getSpectatorManager().addSpectator(player);
                Bukkit.broadcastMessage(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getChatColor() + player.getName() + " §7à été éliminé !");
                Bedwars.INSTANCE().getGameCoherence().playerFinalKill(player, player);
                TeamImpl playingTeam = Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player);
                playingTeam.getPlayers().remove(player);
                TeamImpl team = Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player);
                team.getPlayers().remove(player);
                player.teleport(Bedwars.INSTANCE().getGameProperties().getMiddle());
                Bedwars.INSTANCE().getGameCoherence().win();
            }else {
                respawn(player);
                Bedwars.INSTANCE().getGamePlayers().forEach(players -> players.sendMessage(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getChatColor() + player.getName() + " §7a été tué"));
            }

        }
    }


    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof TNTPrimed) {
            TNTPrimed tnt = (TNTPrimed) event.getDamager();
            LivingEntity damaged = (LivingEntity) event.getEntity();
            Vector distance = damaged.getLocation().subtract(0, 0.5, 0).toVector().subtract(tnt.getLocation().toVector());
            Vector direction = distance.clone().normalize();
            double force = ((9) / (3 + distance.length()));
            Vector resultingForce = direction.clone().multiply(force);
            resultingForce.setY(resultingForce.getY() / (distance.length() + 2));
            damaged.setVelocity(resultingForce);
            event.setDamage(0);
            event.setCancelled(true);
        }

        if(event.getDamager() instanceof Fireball) event.setCancelled(true);
    }


    @EventHandler
    public void onPlayerDamagePlayer(PlayerDamagePlayerEvent event) {
        if(Bedwars.INSTANCE().getGameAPI().getGameState().equals(GameState.WAITING)) return;
        if(Bedwars.INSTANCE().getGameAPI().getGameState().equals(GameState.STARTING)) return;
        if(Bedwars.INSTANCE().getGameAPI().getGameState().equals(GameState.ENDING)) return;
        if(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(event.getPlayer()).equals(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(event.getVictim()))){
            event.setCancelled(true);
            return;
        }

        if(event.getVictim().hasMetadata("respawn")) {
            event.setCancelled(true);
            return;
        }

        if(event.getVictim().getHealth() - event.getDamage() <= 0) {
            event.setCancelled(true);
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.FALL_BIG, 1, 1);
            Bedwars.INSTANCE().getGamePlayer(event.getPlayer().getUniqueId()).addKill();
            if(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(event.getVictim()).isBedDestroyed()) {
                Bedwars.INSTANCE().getGameAPI().getSpectatorManager().addSpectator(event.getVictim());
                Bukkit.broadcastMessage(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(event.getVictim()).getChatColor() + event.getVictim().getName() + " §7a été éliminé par " + Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(event.getPlayer()).getChatColor() + event.getPlayer().getName());
                Bedwars.INSTANCE().getGameCoherence().playerFinalKill(event.getVictim(), event.getPlayer());
                Bedwars.INSTANCE().getGamePlayer(event.getPlayer().getUniqueId()).addFinalKill();
                TeamImpl playingTeam = Bedwars.INSTANCE().getGameProperties().getPlayerPlayingTeam(event.getVictim());
                playingTeam.getPlayers().remove(event.getVictim());
                TeamImpl team = Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(event.getVictim());
                event.getVictim().teleport(Bedwars.INSTANCE().getGameProperties().getMiddle());
                team.getPlayers().remove(event.getVictim());
                Bedwars.INSTANCE().getGamePlayer(event.getVictim().getUniqueId()).setLose(true);
                Bedwars.INSTANCE().getGameCoherence().win();
            }else{
                respawn(event.getVictim());
                Bedwars.INSTANCE().getGamePlayers().forEach(player -> player.sendMessage(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(event.getVictim()).getChatColor() + event.getVictim().getName() + " §7a été tué par " + Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(event.getPlayer()).getChatColor() + event.getPlayer().getName()));
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (Bedwars.INSTANCE().getGameAPI().getGameState().equals(GameState.WAITING)) return;
        if (Bedwars.INSTANCE().getGameAPI().getGameState().equals(GameState.STARTING)) return;
        if (Bedwars.INSTANCE().getGameAPI().getGameState().equals(GameState.ENDING)) return;
        respawn(event.getEntity().getPlayer());
    }


    @EventHandler
    public void onItemDurabilityDamage(PlayerItemDamageEvent event) {
        if (Bedwars.INSTANCE().getGameAPI().getGameState().equals(GameState.WAITING)) return;
        if (Bedwars.INSTANCE().getGameAPI().getGameState().equals(GameState.STARTING)) return;
        if (Bedwars.INSTANCE().getGameAPI().getGameState().equals(GameState.ENDING)) return;
        event.setCancelled(true);
    }




    @EventHandler
    public void onFood(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }


    public static void respawn(Player player) {
        if (Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getTeam() == null) return;
        if (Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).isBedDestroyed()) {
            player.teleport(Bedwars.INSTANCE().getGameProperties().getMiddle());
            Bedwars.INSTANCE().getGameCoherence().win();
            return;
        }


        Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).removeAxeLvl();
        Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).removePickaxeLvl();

        if(Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).getAxeLvl() <= 1) player.getInventory().addItem( new ItemBuilder(Material.WOOD_AXE).unbreakable(true).build());
        if(Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).getAxeLvl() == 2) player.getInventory().addItem( new ItemBuilder(Material.IRON_AXE).unbreakable(true).build());
        if(Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).getAxeLvl() >= 3) player.getInventory().addItem( new ItemBuilder(Material.DIAMOND_AXE).unbreakable(true).build());

        if(Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).getPickaxeLvl() <= 1) player.getInventory().addItem( new ItemBuilder(Material.WOOD_PICKAXE).unbreakable(true).build());
        if(Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).getPickaxeLvl() == 2) player.getInventory().addItem( new ItemBuilder(Material.IRON_PICKAXE).unbreakable(true).build());
        if(Bedwars.INSTANCE().getGamePlayer(player.getUniqueId()).getPickaxeLvl() >= 3) player.getInventory().addItem( new ItemBuilder(Material.DIAMOND_PICKAXE).unbreakable(true).build());

        Bedwars.INSTANCE().getGameCoherence().reloadPlayer(player);

        PlayerUtils.cleanPlayer(player, GameMode.SPECTATOR);
        player.teleport(Bedwars.INSTANCE().getGameProperties().getMiddle());
        player.setAllowFlight(true);
        player.setFlying(true);

        player.playSound(player.getLocation(), Sound.FALL_BIG, 1, 1);

        new BukkitRunnable() {
            int i = 5;
            @Override
            public void run() {

                i--;
                Titles.sendTitle(player, 0, 21, 0, "§cRespawn dans " + i, null);
                if (i == 0) {
                    player.teleport(Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(player).getSpawnLocation());
                    PlayerUtils.cleanPlayer(player, GameMode.SURVIVAL);
                    Bedwars.INSTANCE().getGameCoherence().reloadPlayer(player);
                    player.setFlying(false);
                    player.setAllowFlight(false);
                    player.setMetadata("respawn", new FixedMetadataValue(Bedwars.INSTANCE(), true));

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.removeMetadata("respawn", Bedwars.INSTANCE());
                        }
                    }.runTaskLater(Bedwars.INSTANCE(), 5*20);

                    cancel();

                }

            }
        }.runTaskTimer(Bedwars.INSTANCE(), 0, 20);
    }

    private final HashMap<Player, BukkitTask> schedulerHashMap = new HashMap<>();

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        if(Bedwars.INSTANCE().getGameAPI().getGameState().equals(GameState.WAITING) || Bedwars.INSTANCE().getGameAPI().getGameState().equals(GameState.STARTING)) return;
        if(event.getItem().getType().name().contains("POTION")) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Bedwars.INSTANCE(), () -> event.getPlayer().getInventory().removeItem(new ItemStack(Material.GLASS_BOTTLE, 1)));
            new BukkitRunnable() {
                @Override
                public void run() {
                    for(PotionEffect potionEffect : event.getPlayer().getActivePotionEffects()) {
                        if(potionEffect.toString().contains("INVISIBILITY")) {
                            int duration = potionEffect.getDuration();
                            Bukkit.getOnlinePlayers().forEach(player -> hideArmor(event.getPlayer(), player));
                            if(schedulerHashMap.containsKey(event.getPlayer())) {
                                schedulerHashMap.get(event.getPlayer()).cancel();
                                schedulerHashMap.remove(event.getPlayer());
                            }
                            schedulerHashMap.put(event.getPlayer(), Bukkit.getScheduler().runTaskLater(Bedwars.INSTANCE(), () -> Bukkit.getOnlinePlayers().forEach(player -> showArmor(event.getPlayer(), player)), duration));
                        }
                    }
                }
            }.runTaskLater(Bedwars.INSTANCE(), 5L);
        }
    }

    private void showArmor(Player player, Player others) {
        if(player != others) {
            schedulerHashMap.remove(player);
            EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
            PacketPlayOutEntityEquipment hand = new PacketPlayOutEntityEquipment(entityPlayer.getId(), 0, entityPlayer.inventory.getItemInHand());
            PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment(entityPlayer.getId(), 4, entityPlayer.inventory.getArmorContents()[3]);
            PacketPlayOutEntityEquipment chest = new PacketPlayOutEntityEquipment(entityPlayer.getId(), 3, entityPlayer.inventory.getArmorContents()[2]);
            PacketPlayOutEntityEquipment pants = new PacketPlayOutEntityEquipment(entityPlayer.getId(), 2, entityPlayer.inventory.getArmorContents()[1]);
            PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment(entityPlayer.getId(), 1, entityPlayer.inventory.getArmorContents()[0]);
            Reflection.sendPacket(others, hand);
            Reflection.sendPacket(others, helmet);
            Reflection.sendPacket(others, chest);
            Reflection.sendPacket(others, pants);
            Reflection.sendPacket(others, boots);
            team.removePlayer(player);
            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }
    }

    private void hideArmor(Player player, Player others) {
        if (player != others) {
            PacketPlayOutEntityEquipment hand = new PacketPlayOutEntityEquipment(player.getEntityId(), 0, CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.AIR)));
            PacketPlayOutEntityEquipment helmet = new PacketPlayOutEntityEquipment(player.getEntityId(), 1, CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.AIR)));
            PacketPlayOutEntityEquipment chest = new PacketPlayOutEntityEquipment(player.getEntityId(), 2, CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.AIR)));
            PacketPlayOutEntityEquipment pants = new PacketPlayOutEntityEquipment(player.getEntityId(), 3, CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.AIR)));
            PacketPlayOutEntityEquipment boots = new PacketPlayOutEntityEquipment(player.getEntityId(), 4, CraftItemStack.asNMSCopy(new ItemStack(org.bukkit.Material.AIR)));
            Reflection.sendPacket(others, hand);
            Reflection.sendPacket(others, helmet);
            Reflection.sendPacket(others, chest);
            Reflection.sendPacket(others, pants);
            Reflection.sendPacket(others, boots);
            team.addPlayer(player);
            player.setScoreboard(scoreboard);
        }
    }


}
