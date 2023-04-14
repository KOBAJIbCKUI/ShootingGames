package com.KOBAJIbCKUI.ShootingBattles.commands;

import com.KOBAJIbCKUI.ShootingBattles.lobby.Lobby;
import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import com.KOBAJIbCKUI.ShootingBattles.lobby.ShootingMap;
import com.KOBAJIbCKUI.ShootingBattles.managers.LobbiesManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class ShowLobbyInfoCommandExecutor implements CommandExecutor {
    ShootingGames shootingGames;

    public ShowLobbyInfoCommandExecutor(ShootingGames shootingGames) {
        this.shootingGames = shootingGames;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length == 1) {
            LobbiesManager lobbiesManager = shootingGames.getLobbiesManager();
            Lobby foundLobby;
            if ((foundLobby = lobbiesManager.findLobby(args[0])) == null) {
                sender.sendMessage("Lobby " + args[0] + " doesn't exist");
                return true;
            }


            sender.sendMessage("Owner: " + Bukkit.getPlayer(foundLobby.getOwner()).getName());
            sender.sendMessage("Status: " + foundLobby.getStatus().getName());
            if (foundLobby.playersQuantity() == 0) {
                sender.sendMessage("No players in lobby");
            } else {
                sender.sendMessage("Players in lobby: ");
                for (UUID uuid : foundLobby.getPlayers()) {
                    sender.sendMessage(" " + Bukkit.getPlayer(uuid).getDisplayName());
                }
            }

            if (foundLobby.shootingMapsQuantity() == 0) {
                sender.sendMessage("No maps in lobby");
            } else {
                sender.sendMessage("Maps in lobby: ");
                for (ShootingMap map : foundLobby.getShootingMaps()) {
                    sender.sendMessage(" " + map.getName());
                }
            }

            if (foundLobby.getGulagMap() == null) {
                sender.sendMessage("No gulag map");
            } else {
                sender.sendMessage("Gulag map is " + foundLobby.getGulagMap().getName());
            }
            return true;
        }
        return false;
    }
}
