package com.KOBAJIbCKUI.ShootingBattles.commands;

import com.KOBAJIbCKUI.ShootingBattles.lobby.Lobby;
import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
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

        if (foundLobby.playersQuantity() == 0) {
            sender.sendMessage("There are no players in lobby " + foundLobby.getName());
        } else {
            sender.sendMessage("Players in lobby " + foundLobby.getName() + ":");
            for (UUID uuid : foundLobby.getPlayers()) {
                Player lobbyPlayer;
                OfflinePlayer offlineLobbyPlayer;
                if ((lobbyPlayer = Bukkit.getPlayer(uuid)) == null) {
                    offlineLobbyPlayer = Bukkit.getOfflinePlayer(uuid);
                    sender.sendMessage(" " + offlineLobbyPlayer.getName() + " - offline");
                } else {
                    sender.sendMessage( " " + lobbyPlayer.getDisplayName() + " - online");
                }
            }
        }
        return true;

    }
}
