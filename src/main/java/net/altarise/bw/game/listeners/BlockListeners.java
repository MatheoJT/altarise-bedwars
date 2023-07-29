package net.altarise.bw.game.listeners;

import net.altarise.api.utils.LocationUtils;
import net.altarise.api.utils.title.Titles;
import net.altarise.bw.Bedwars;
import net.altarise.bw.game.Generator;
import net.altarise.bw.game.events.PowerDestruct;
import net.altarise.bw.impl.TeamImpl;
import net.altarise.gameapi.basic.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Objects;

public class BlockListeners implements Listener {


    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (Bedwars.INSTANCE().getGameAPI().getGameState().equals(GameState.WAITING)) event.setCancelled(true);
        event.getBlock().setMetadata("breakable", new FixedMetadataValue(Bedwars.INSTANCE(), true));
        PowerDestruct.blocks.add(event.getBlock());

        if(event.getBlock().getLocation().getBlockY() == 181) {
            event.setCancelled(true);
            return;
        }

        for (Generator generator : Bedwars.INSTANCE().getGameProperties().getGenerators()) {
            if (LocationUtils.getAroundLocations(generator.getLocation(), 3).contains(event.getBlock().getLocation())) {
                event.setCancelled(true);
                return;
            }
        }


        for (TeamImpl team : Bedwars.INSTANCE().getGameProperties().getTeams()) {
            if(team.getProtectedArea().contains(event.getBlock().getLocation()) || LocationUtils.getAroundLocations(team.getGeneratorLocation(), 2).contains(event.getBlock().getLocation()) || LocationUtils.getAroundLocations(team.getShopLocation(), 1).contains(event.getBlock().getLocation()) || LocationUtils.getAroundLocations(team.getUpgradeLocation(), 1).contains(event.getBlock().getLocation())) {
                event.setCancelled(true);
                return;
            }
        }

        if(event.getBlock().getType().equals(Material.TNT)) {
            event.setCancelled(true);
            Objects.requireNonNull(event.getBlock().getLocation().getWorld()).spawn(event.getBlock().getLocation().add(0.5, 0, 0.5), TNTPrimed.class).setFuseTicks(60);
            event.getPlayer().getInventory().removeItem(new ItemStack(Material.TNT, 1));
            event.getPlayer().updateInventory();
        }

    }


    @EventHandler
    public void onGlassExplode(EntityExplodeEvent event) {
            for (Block block : new ArrayList<>(event.blockList())) {
                int protectedBlocks = 0;

                for(BlockFace face : new BlockFace[] {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST}) {
                    if(block.getRelative(face).getType().toString().contains("GLASS")) {
                        protectedBlocks++;
                    }
                }

                if(block.getType().toString().contains("GLASS")) {
                    event.blockList().remove(block);
                }

                if (!block.hasMetadata("breakable")  || block.getType().toString().contains("BED") || protectedBlocks > 1) {
                    event.blockList().remove(block);
                }
            }

    }
    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        for (Block block : new ArrayList<>(event.blockList())) {
            int protectedBlocks = 0;

            for(BlockFace face : new BlockFace[] {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST}) {
                if(block.getRelative(face).getType().toString().contains("GLASS")) {
                    protectedBlocks++;
                }
            }

            if(block.getType().toString().contains("GLASS")) {
                event.blockList().remove(block);
            }


            if (!block.hasMetadata("breakable") || block.getType().toString().contains("BED") ||  protectedBlocks > 1) {
                event.blockList().remove(block);
            }
        }
    }

    @EventHandler
    public void onBlockDamage(BlockBreakEvent event) {
        if (Bedwars.INSTANCE().getGameAPI().getGameState().equals(GameState.WAITING)) event.setCancelled(true);
        if (Bedwars.INSTANCE().getGameAPI().getGameState().equals(GameState.STARTING)) event.setCancelled(true);

        for (TeamImpl team : Bedwars.INSTANCE().getGameProperties().getTeams()) {
            if (team.getBedLocations().contains(event.getBlock().getLocation())) {
                event.setCancelled(true);
                if (team.isBedDestroyed()) return;
                if (team.getPlayers().contains(event.getPlayer())) return;
                team.setBedDestroyed();
                team.getBedLocations().forEach(location -> location.getBlock().setType(Material.AIR));
                Bedwars.INSTANCE().getGamePlayer(event.getPlayer().getUniqueId()).addDestroyedBed();
                Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 1));
                team.getPlayers().forEach(player -> Titles.sendTitle(player, 20, 40, 20, "§cVotre lit a été détruit !", "§7Vous ne pouvez plus réapparaître"));
                Bukkit.broadcastMessage("§3§lBEDWARS §f▪ Le lit de l'équipe " + team.getChatColor() + team.getName() + " §fa été détruit par " + Bedwars.INSTANCE().getTeamProperties().getTeamPlayer(event.getPlayer()).getChatColor() + event.getPlayer().getName() + " §f!");
                return;
            } else {
                if(LocationUtils.getAroundLocations(team.getBedLocations().get(0), 20).contains(event.getBlock().getLocation())) {
                    if(team.hasMinerTraps()) {
                        if (!team.getPlayers().contains(event.getPlayer())) {
                            Bedwars.INSTANCE().getGamePlayers().forEach(player -> {
                                if (LocationUtils.getAroundLocations(team.getBedLocations().get(0), 20).contains(player.getLocation().getBlock().getLocation())) {
                                    if (!team.getPlayers().contains(player)) {
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20 * 15, 1));
                                    }
                                }
                            });
                            team.setMinerTraps(false);
                        }
                    }
                }
            }

            if (team.getProtectedArea().contains(event.getBlock().getLocation())) event.setCancelled(true);
            if (LocationUtils.getAroundLocations(team.getGeneratorLocation(), 2).contains(event.getBlock().getLocation())) event.setCancelled(true);
            if (LocationUtils.getAroundLocations(team.getShopLocation(), 1).contains(event.getBlock().getLocation())) event.setCancelled(true);
            if (LocationUtils.getAroundLocations(team.getUpgradeLocation(), 1).contains(event.getBlock().getLocation())) event.setCancelled(true);
        }

        if (!event.getBlock().hasMetadata("breakable")) event.setCancelled(true);

    }



}
