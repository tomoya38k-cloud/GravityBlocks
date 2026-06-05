package com.example.gravityblocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

public class BlockBreakListener implements Listener {

    private final GravityBlocks plugin;
    private static final int MAX_COLLAPSE = 64;

    public BlockBreakListener(GravityBlocks plugin) {
        this.plugin = plugin;
    }

    // 通常のブロック破壊（プレイヤー・ピストンなど）
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onBlockBreak(BlockBreakEvent event) {
        scheduleCollapse(event.getBlock().getLocation());
    }

    // TNTなどエンティティによる爆発
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onEntityExplode(EntityExplodeEvent event) {
        for (Block block : event.blockList()) {
            scheduleCollapse(block.getLocation());
        }
    }

    // ブロック自体の爆発（床置きTNTなど）
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
    public void onBlockExplode(BlockExplodeEvent event) {
        for (Block block : event.blockList()) {
            scheduleCollapse(block.getLocation());
        }
    }

    private void scheduleCollapse(Location loc) {
        new BukkitRunnable() {
            @Override
            public void run() {
                collapseAbove(loc);
            }
        }.runTaskLater(plugin, 1L);
    }

    private void collapseAbove(Location brokenLoc) {
        int count = 0;
        Location check = brokenLoc.clone().add(0, 1, 0);

        while (count < MAX_COLLAPSE) {
            Block above = check.getBlock();
            Material mat = above.getType();

            if (mat == Material.AIR || mat == Material.CAVE_AIR || mat == Material.VOID_AIR
                    || mat == Material.WATER || mat == Material.LAVA || !mat.isSolid()) {
                break;
            }

            final org.bukkit.block.data.BlockData data = above.getBlockData().clone();
            final Location spawnLoc = above.getLocation().add(0.5, 0.0, 0.5);
            above.setType(Material.AIR);

            final int delay = count;
            new BukkitRunnable() {
                @Override
                public void run() {
                    FallingBlock fb = spawnLoc.getWorld().spawnFallingBlock(spawnLoc, data);
                    fb.setDropItem(false);
                    fb.setHurtEntities(false);
                    fb.setVelocity(new Vector(0, 0, 0));
                }
            }.runTaskLater(plugin, delay);

            count++;
            check.add(0, 1, 0);
        }
    }
}
