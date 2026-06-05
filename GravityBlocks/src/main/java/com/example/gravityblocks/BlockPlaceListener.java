package com.example.gravityblocks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.util.Vector;

public class BlockPlaceListener implements Listener {

    private final GravityBlocks plugin;

    public BlockPlaceListener(GravityBlocks plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        // このプレイヤーが重力モードでなければ何もしない
        if (!plugin.isGravityEnabled(player)) {
            return;
        }

        Block placedBlock = event.getBlockPlaced();
        Material material = placedBlock.getType();

        // 設置をキャンセルして代わりにFallingBlockエンティティを生成
        event.setCancelled(true);

        // 設置した位置の少し上からFallingBlockを生成（自然な落下に見せるため）
        Location spawnLoc = placedBlock.getLocation().add(0.5, 0.0, 0.5);

        // FallingBlockを生成
        FallingBlock fallingBlock = player.getWorld().spawnFallingBlock(spawnLoc, material.createBlockData());

        // 設定: 着地後にブロックになる、アイテムドロップしない
        fallingBlock.setDropItem(false);
        fallingBlock.setShouldAutoExpire(false);

        // ダメージ無効化（エンティティを傷つけない）
        fallingBlock.setHurtEntities(false);

        // 初速度ゼロ（真下に落下）
        fallingBlock.setVelocity(new Vector(0, 0, 0));

        // プレイヤーのインベントリからブロックを1つ消費
        // （イベントキャンセルしているため手動で消費）
        consumeBlockFromHand(player);
    }

    /**
     * プレイヤーのメインハンドのアイテムを1つ消費する。
     * クリエイティブモードでは消費しない。
     */
    private void consumeBlockFromHand(Player player) {
        if (player.getGameMode() == org.bukkit.GameMode.CREATIVE) {
            return;
        }
        org.bukkit.inventory.ItemStack item = player.getInventory().getItemInMainHand();
        if (item != null && item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            player.getInventory().setItemInMainHand(null);
        }
    }
}
