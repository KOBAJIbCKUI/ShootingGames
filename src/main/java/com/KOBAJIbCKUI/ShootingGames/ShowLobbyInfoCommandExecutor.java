package com.KOBAJIbCKUI.ShootingGames;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.NoSuchElementException;
import java.util.UUID;

public class ShowLobbyInfoCommandExecutor implements CommandExecutor {
    ShootingGames shootingGames;

    public ShowLobbyInfoCommandExecutor(ShootingGames shootingGames) {
        this.shootingGames = shootingGames;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length == 1) {
            Lobby foundLobby;
            try {
                foundLobby = shootingGames.lobbiesListWrapper.lobbies.stream().filter(o -> o.getName().equals(args[0])).findFirst().get();
            } catch (NoSuchElementException e) {
                sender.sendMessage("Lobby " + args[0] + " doesn't exist");
                return true;
            }

            if (foundLobby.isInBattle) {
                sender.sendMessage("Lobby is in battle");
            } else {
                sender.sendMessage("Lobby is standing by");
            }

            sender.sendMessage("Players in lobby: ");
            if (foundLobby.playersQuantity() == 0) {
                sender.sendMessage(" No players");
            } else {
                for (UUID uuid : foundLobby.getPlayers()) {
                    Player player;
                    OfflinePlayer offlinePlayer;
                    if ((player = Bukkit.getPlayer(uuid)) == null) {
                        offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                        sender.sendMessage(" " + offlinePlayer.getName() + " - offline");
                    } else {
                        sender.sendMessage(" " + player.getDisplayName() + " - online");
                    }
                }
            }

            sender.sendMessage("Maps in lobby: ");
            if (foundLobby.shootingMapsQuantity() == 0) {
                sender.sendMessage(" No maps");
            } else {
                for (ShootingMap map : foundLobby.getShootingMaps()) {
                    sender.sendMessage(" " + map.getName());
                }
            }
            return true;
        }
        return false;
    }
}
