package com.KOBAJIbCKUI.ShootingBattles.commands;

import com.KOBAJIbCKUI.ShootingBattles.lobby.Lobby;
import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import com.KOBAJIbCKUI.ShootingBattles.lobby.ShootingMap;
import com.KOBAJIbCKUI.ShootingBattles.managers.LobbiesManager;
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

        LobbiesManager lobbiesManager = shootingGames.getLobbiesManager();
        Lobby foundLobby = lobbiesManager.findLobby(player);
        if (foundLobby == null) {
            sender.sendMessage("You are not a member of any lobby");
            return true;
        }

        if (foundLobby.getShootingMaps().isEmpty()) {
            sender.sendMessage("Maps list is empty");
            return true;
        }

        sender.sendMessage("Maps list:");
        for (ShootingMap shootingMap : foundLobby.getShootingMaps()) {
            sender.sendMessage("-" + shootingMap.getName());
        }

        if (foundLobby.getGulagMap() == null) {
            sender.sendMessage("No gulag map");
        } else {
            sender.sendMessage("Gulag map is " + foundLobby.getGulagMap().getName());
        }
        return true;
    }
}
