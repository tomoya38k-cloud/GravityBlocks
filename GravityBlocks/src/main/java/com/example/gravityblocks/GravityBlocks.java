package com.example.gravityblocks;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GravityBlocks extends JavaPlugin {

    // 重力モードが有効なプレイヤーのUUID一覧
    private final Set<UUID> gravityPlayers = new HashSet<>();

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new BlockPlaceListener(this), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(this), this);
        getLogger().info("GravityBlocks が有効化されました！");
    }

    @Override
    public void onDisable() {
        getLogger().info("GravityBlocks が無効化されました。");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("このコマンドはプレイヤーのみ使用できます。");
            return true;
        }

        Player player = (Player) sender;
        String cmd = command.getName().toLowerCase();

        if (cmd.equals("gravity")) {
            if (args.length == 0) {
                // トグル
                toggleGravity(player);
            } else {
                switch (args[0].toLowerCase()) {
                    case "on":
                        enableGravity(player);
                        break;
                    case "off":
                        disableGravity(player);
                        break;
                    default:
                        player.sendMessage(ChatColor.RED + "使い方: /gravity [on|off]");
                }
            }
            return true;
        }
        return false;
    }

    private void toggleGravity(Player player) {
        if (isGravityEnabled(player)) {
            disableGravity(player);
        } else {
            enableGravity(player);
        }
    }

    private void enableGravity(Player player) {
        gravityPlayers.add(player.getUniqueId());
        player.sendMessage(ChatColor.GREEN + "✔ 重力モード " + ChatColor.BOLD + "ON" +
                ChatColor.GREEN + " — 設置したブロックが落下して積み上がります！");
    }

    private void disableGravity(Player player) {
        gravityPlayers.remove(player.getUniqueId());
        player.sendMessage(ChatColor.YELLOW + "✘ 重力モード " + ChatColor.BOLD + "OFF" +
                ChatColor.YELLOW + " — 通常の設置に戻りました。");
    }

    public boolean isGravityEnabled(Player player) {
        return gravityPlayers.contains(player.getUniqueId());
    }
}
