package net.altarise.bw.game.listeners;

import net.altarise.api.utils.LocationUtils;
import net.altarise.bw.Bedwars;
import net.altarise.bw.game.Generator;
import net.altarise.bw.impl.TeamImpl;
import net.altarise.gameapi.basic.GameState;
import net.minecraft.server.v1_8_R3.EntityFireball;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftFireball;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class ItemsListeners implements Listener {
    @EventHandler
    public void onItemDespawn(ItemDespawnEvent event) {
        event.setCancelled(true);
    }


    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        if (event.getEntity().getItemStack().getType().equals(Material.BED)) event.setCancelled(true);
    }


    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if(event.getItemDrop().getItemStack().getType().equals(Material.COMPASS)) event.setCancelled(true);
        if(event.getItemDrop().getItemStack().getType().toString().contains("AXE")) event.setCancelled(true);
        if(event.getItemDrop().getItemStack().getType().toString().contains("PICKAXE")) event.setCancelled(true);
        if(event.getItemDrop().getItemStack().getType().toString().contains("HELMET")) event.setCancelled(true);
        if(event.getItemDrop().getItemStack().getType().toString().contains("CHESTPLATE")) event.setCancelled(true);
        if(event.getItemDrop().getItemStack().getType().toString().contains("LEGGINGS")) event.setCancelled(true);
        if(event.getItemDrop().getItemStack().getType().toString().contains("BOOTS")) event.setCancelled(true);
        if (event.getItemDrop().getItemStack().getType().equals(Material.WOOD_SWORD)) event.getItemDrop().remove();
    }


    @EventHandler
    public void onPlayerReceiveItem(PlayerPickupItemEvent event) {
        if (Bedwars.INSTANCE().getGameAPI().getGameState().equals(GameState.WAITING)) return;
        for (Generator generator : Bedwars.INSTANCE().getGameProperties().getGenerators()) {
            if (generator.getType().equals(Generator.GeneratorType.DIAMOND)) {
                if (LocationUtils.getAroundLocations(generator.getLocation(), 3).contains(event.getItem().getLocation().getBlock().getLocation())) {
                    if (event.getItem().getItemStack().getType().equals(Material.DIAMOND))
                        generator.count -= event.getItem().getItemStack().getAmount();
                    if (event.getItem().getItemStack().getType().equals(Material.NETHER_STAR))
                        generator.starCount -= event.getItem().getItemStack().getAmount();
                }
            }
            if (generator.getType().equals(Generator.GeneratorType.EMERALD)) {
                if (LocationUtils.getAroundLocations(generator.getLocation(), 2).contains(event.getItem().getLocation().getBlock().getLocation())) {
                    if (event.getItem().getItemStack().getType().equals(Material.EMERALD))
                        generator.count -= event.getItem().getItemStack().getAmount();
                }
            }
        }
        for (TeamImpl team : Bedwars.INSTANCE().getGameProperties().getStaticTeams()) {
            if (LocationUtils.getAroundLocations(team.getGeneratorLocation(), 22).contains(event.getItem().getLocation().getBlock().getLocation())) {
                if (event.getItem().getItemStack().getType().equals(Material.GOLD_INGOT))
                    team.goldCound -= event.getItem().getItemStack().getAmount();
                if (event.getItem().getItemStack().getType().equals(Material.IRON_INGOT))
                    team.ironCount -= event.getItem().getItemStack().getAmount();
            }
        }
    }

    private final List<Player> fireballCooldown = new ArrayList<>();

    @EventHandler
    public void onInterract(PlayerInteractEvent event) {

        if (Bedwars.INSTANCE().getGameAPI().getGameState().equals(GameState.WAITING) || Bedwars.INSTANCE().getGameAPI().getGameState().equals(GameState.STARTING)) {
            event.setCancelled(true);
            return;
        }
        if (event.getPlayer() == null) return;



        if(event.getPlayer().getItemInHand().getType().equals(Material.FIREBALL)) {
            event.setCancelled(true);
            if(!fireballCooldown.contains(event.getPlayer())) {
            Fireball fireball = event.getPlayer().launchProjectile(Fireball.class);
            Vector direction = event.getPlayer().getEyeLocation().getDirection();
            fireball.setCustomName("Fireball");
            fireball.setCustomNameVisible(false);
            fireball = setFireballDirection(fireball, direction);
            fireball.setVelocity(fireball.getDirection());
            fireball.setIsIncendiary(false);
            fireball.setYield(3);
            event.setCancelled(true);
            event.getPlayer().getInventory().removeItem(new ItemStack(Material.FIREBALL, 1));
            fireballCooldown.add(event.getPlayer());
            Bukkit.getScheduler().runTaskLater(Bedwars.INSTANCE(), () -> fireballCooldown.remove(event.getPlayer()), 20 * 3);
        }
        }


        if (event.getClickedBlock() == null) return;
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (event.getPlayer().isSneaking()) return;
            if (event.getClickedBlock().getType().equals(Material.BED_BLOCK)) event.setCancelled(true);
            if (event.getClickedBlock().getType().equals(Material.BED)) event.setCancelled(true);

            if (event.getClickedBlock().getType().equals(Material.CHEST)) {
                for (TeamImpl team : Bedwars.INSTANCE().getGameProperties().getPlayingTeams()) {
                    if (team.getProtectedArea().contains(event.getClickedBlock().getLocation()) && !team.getPlayers().contains(event.getPlayer())) {
                        event.setCancelled(true);
                        return;
                    }

                }
            }
        }
    }

    private Fireball setFireballDirection(Fireball fireball, Vector direction) {
        EntityFireball entityFireball = ((CraftFireball) fireball).getHandle();
        entityFireball.dirX = direction.getX() * 0.1D;
        entityFireball.dirY = direction.getY() * 0.1D;
        entityFireball.dirZ = direction.getZ() * 0.1D;
        return (Fireball) entityFireball.getBukkitEntity();
    }


    @EventHandler
    public void onFireballHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Fireball) {
            if (event.getEntity().getCustomName().equals("Fireball")) {
                for (Entity entity : Bukkit.getWorld("world").getNearbyEntities(event.getEntity().getLocation(), 3, 3, 3)) {
                    if(entity instanceof Player) {
                        Player player = (Player) entity;
                        if(Bedwars.INSTANCE().getGamePlayers().contains(player)) {
                            player.damage(0);

                            Vector playerVector = player.getLocation().toVector();
                            Vector normalizedVector = (event.getEntity().getLocation().toVector()).subtract(playerVector).normalize();
                            Vector horizontalVector = normalizedVector.multiply(-1.0);
                            double y = normalizedVector.getY();
                            if(y < 0 ) y += 1.5;
                            if(y <= 0.5) {
                                y = 0.65*1.5;
                            } else {
                                y = y*0.65*1.5;
                            }
                            player.setVelocity(horizontalVector.setY(y));
                        }
                    }
                }
            }
        }
    }

}
