package com.KOBAJIbCKUI.ShootingGames;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleSonarCommandExecutor implements CommandExecutor {
    ShootingGames shootingGames;

    public ToggleSonarCommandExecutor(ShootingGames shootingGames) {
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
        if (Sonar.players.containsKey(player)) {
            if ((idToCancel = Sonar.players.get(player)) != null) {
                Bukkit.getScheduler().cancelTask(idToCancel);
                Sonar.players.put(player, null);
                sender.sendMessage("S disabled");
            } else {
                sender.sendMessage("S enabled");
                Sonar.players.put(player, Bukkit.getScheduler().scheduleSyncRepeatingTask(shootingGames, new Sonar(player), 100, 100));
            }
        } else {
            sender.sendMessage("S enabled");
            Sonar.players.put(player, Bukkit.getScheduler().scheduleSyncRepeatingTask(shootingGames, new Sonar(player), 100, 100));
        }

        return true;
    }
}
