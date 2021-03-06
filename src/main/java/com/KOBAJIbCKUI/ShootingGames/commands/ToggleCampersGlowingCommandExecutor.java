package com.KOBAJIbCKUI.ShootingGames.commands;

import com.KOBAJIbCKUI.ShootingGames.ShootingGames;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ToggleCampersGlowingCommandExecutor implements CommandExecutor {
    ShootingGames shootingGames;

    public ToggleCampersGlowingCommandExecutor(ShootingGames shootingGames) {
        this.shootingGames = shootingGames;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length == 0) {
            if (ShootingGames.CAMPERS_GLOWING) {
                ShootingGames.CAMPERS_GLOWING = false;
                shootingGames.config.set("campers_glowing", false);
                sender.sendMessage("Campers glowing disabled");
            } else {
                ShootingGames.CAMPERS_GLOWING = true;
                shootingGames.config.set("campers_glowing", true);
                sender.sendMessage("Campers glowing enabled");
            }
            shootingGames.saveConfig();
            return true;
        }
        return false;
    }
}
