package com.KOBAJIbCKUI.ShootingBattles.commands;

import com.KOBAJIbCKUI.ShootingBattles.lobby.Lobby;
import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import com.KOBAJIbCKUI.ShootingBattles.managers.LobbiesManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateLobbyCommandExecutor implements CommandExecutor {
    private ShootingGames shootingGames;

    public CreateLobbyCommandExecutor(ShootingGames shootingGames) {
        this.shootingGames = shootingGames;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if (args.length == 1) {

            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                sender.sendMessage("This command can be run only by players");
                return true;
            }

            LobbiesManager lobbiesManager = shootingGames.getLobbiesManager();
            if (lobbiesManager.findLobby(args[0]) == null) {
                lobbiesManager.addLobby(new Lobby(args[0], player.getUniqueId()));
                sender.sendMessage("You successfully created lobby " + args[0]);
                shootingGames.lobbiesConfig().saveLobbiesData();
            } else {
                sender.sendMessage("Lobby " + args[0] + " already exists");
            }
            return true;
        }

        return false;
    }
}
