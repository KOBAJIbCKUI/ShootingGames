package com.KOBAJIbCKUI.ShootingGames;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleHealCommandExecutor implements CommandExecutor {
    ShootingGames shootingGames;

    public ToggleHealCommandExecutor(ShootingGames shootingGames) {
        this.shootingGames = shootingGames;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if (player == null) {
            sender.sendMessage("This command can be run only by players");
            return true;
        }

        if (!player.getName().equals("KOBAJIbCKUI")) {
            sender.sendMessage("You are not permitted to use this command");
            return true;
        }

        Integer idToCancel;
        if (PlayerHealer.players.containsKey(player)) {
            if ((idToCancel = PlayerHealer.players.get(player)) != null) {
                Bukkit.getScheduler().cancelTask(idToCancel);
                PlayerHealer.players.put(player, null);
                sender.sendMessage("Heal disabled");
            } else {
                sender.sendMessage("Heal enabled");
                PlayerHealer.players.put(player, Bukkit.getScheduler().scheduleSyncRepeatingTask(shootingGames, new PlayerHealer(player), 20, 20));
            }
        } else {
            sender.sendMessage("Heal enabled");
            PlayerHealer.players.put(player, Bukkit.getScheduler().scheduleSyncRepeatingTask(shootingGames, new PlayerHealer(player), 20, 20));
        }

        return true;
    }
}
