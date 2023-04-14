package com.KOBAJIbCKUI.ShootingBattles.commands;

import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import com.KOBAJIbCKUI.ShootingBattles.buffers.Sonar;
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
        if (Sonar.sonars.containsKey(player)) {
            Sonar.sonars.get(player).stop();
            Sonar.sonars.remove(player);
            sender.sendMessage("S disabled");
        } else {
            Sonar.sonars.put(player, new Sonar(player));
            sender.sendMessage("S enabled");
        }

        return true;
    }
}
