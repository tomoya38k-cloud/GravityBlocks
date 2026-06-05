package com.example.gravityblocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class BlockBreakListener implements Listener {

    private final GravityBlocks plugin;
    private static final int MAX_COLLAPSE = 64;

    public BlockBreakListener(GravityBlocks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Block broken = event.getBlock();
        new BukkitRunnable() {
            @Override
            public void run() {
                collapseAbove(broken.getLocation());
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

            final int delay = count * 1;
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
