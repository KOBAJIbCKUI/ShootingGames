package com.KOBAJIbCKUI.ShootingGames.commands;

import com.KOBAJIbCKUI.ShootingGames.ShootingGames;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ChangeSaveTechCommandExecutor implements CommandExecutor {
    ShootingGames shootingGames;

    public ChangeSaveTechCommandExecutor(ShootingGames shootingGames) {
        this.shootingGames = shootingGames;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length == 1) {
            switch (args[0]) {
                case "default":
                    ShootingGames.serializationTechnology = "default";
                    break;
                case "xml":
                    ShootingGames.serializationTechnology = "xml";
                    break;
                default:
                    sender.sendMessage("Unable to change save technology");
                    return true;
            }
            shootingGames.config.set("save_technology", ShootingGames.serializationTechnology);
            shootingGames.saveConfig();
            shootingGames.saveLobbies(ShootingGames.SAVE_LOBBY_PATH);
            sender.sendMessage("Save technology changed to " + args[0]);
            return true;
        }
        return false;
    }
}
