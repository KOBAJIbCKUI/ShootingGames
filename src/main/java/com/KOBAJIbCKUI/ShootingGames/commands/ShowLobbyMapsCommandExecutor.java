package com.KOBAJIbCKUI.ShootingGames.commands;

import com.KOBAJIbCKUI.ShootingGames.lobby.Lobby;
import com.KOBAJIbCKUI.ShootingGames.ShootingGames;
import com.KOBAJIbCKUI.ShootingGames.lobby.ShootingMap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ShowLobbyMapsCommandExecutor implements CommandExecutor {
    ShootingGames shootingGames;

    public ShowLobbyMapsCommandExecutor(ShootingGames shootingGames) {
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

        Lobby foundLobby = null;
        for (Lobby lobby : shootingGames.lobbiesListWrapper.lobbies) {
            if (lobby.getPlayers().contains(player.getUniqueId())) {
                foundLobby = lobby;
                break;

            }
        }

        if (foundLobby == null) {
            sender.sendMessage("You are not a member of any lobby");
            return true;
        }

        if (foundLobby.getShootingMaps().isEmpty()) {
            sender.sendMessage("Map list in lobby " + foundLobby.getName() + " is empty");
        }
        sender.sendMessage("Map list in lobby " + foundLobby.getName() + ":");
        for (ShootingMap shootingMap : foundLobby.getShootingMaps()) {
            sender.sendMessage("-" + shootingMap.getName());
        }
        return true;
    }
}
