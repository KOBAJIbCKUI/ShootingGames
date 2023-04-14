package com.KOBAJIbCKUI.ShootingBattles.commands;

import com.KOBAJIbCKUI.ShootingBattles.lobby.Lobby;
import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import com.KOBAJIbCKUI.ShootingBattles.managers.LobbiesManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;


public class ShowLobbyPlayersCommandExecutor implements CommandExecutor {
    ShootingGames shootingGames;

    public ShowLobbyPlayersCommandExecutor(ShootingGames shootingGames) {
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

        LobbiesManager lobbiesManager = shootingGames.getLobbiesManager();
        Lobby foundLobby = lobbiesManager.findLobby(player);
        if (foundLobby == null) {
            sender.sendMessage("You are not a member of any lobby");
            return true;
        }

        if (foundLobby.playersQuantity() == 0) {
            sender.sendMessage("No players in lobby");
        } else {
            sender.sendMessage("Players in lobby: ");
            for (UUID uuid : foundLobby.getPlayers()) {
                sender.sendMessage(" " + Bukkit.getPlayer(uuid).getDisplayName());
            }
        }
        return true;

    }
}
