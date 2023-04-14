package com.KOBAJIbCKUI.ShootingBattles.commands;

import com.KOBAJIbCKUI.ShootingBattles.lobby.Lobby;
import com.KOBAJIbCKUI.ShootingBattles.lobby.ShootingBattle;
import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import com.KOBAJIbCKUI.ShootingBattles.lobby.ShootingMap;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class StartBattleCommandExecutor implements CommandExecutor {

    ShootingGames shootingGames;

    public StartBattleCommandExecutor(ShootingGames shootingGames) {
        this.shootingGames = shootingGames;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length == 1 || args.length == 2) {

            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }

            if (player == null && args.length == 2) {
                player = Bukkit.getPlayer(args[1]);
            }

            if (player == null){
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

            if (foundLobby.isInBattle) {
                sender.sendMessage("Lobby " + foundLobby.getName() + " is already in battle");
                return true;
            }

            ShootingMap shootingMap;
            try {
                shootingMap = foundLobby.getShootingMaps().stream().filter(o -> o.getName().equals(args[0])).findFirst().get();
            } catch (NoSuchElementException e) {
                sender.sendMessage("Map " + args[0] + " doesn't exist in lobby " + foundLobby.getName());
                return true;
            }

            if (foundLobby.getGulagMap() == null) {
                sender.sendMessage("Gulag map is not set");
                return true;
            }

            if (foundLobby.playersQuantity() < 2) {
                sender.sendMessage("Too few players in lobby " + foundLobby.getName() + " to begin battle");
                return true;
            }

            List<Player> playersForBattle = new ArrayList<>();
            Player playerToAdd;
            for (UUID playerID : foundLobby.getPlayers()) {
                playerToAdd = Bukkit.getPlayer(playerID);
                if (playerToAdd != null) {
                    playersForBattle.add(playerToAdd);
                }
            }

            if (playersForBattle.size() < 2) {
                sender.sendMessage("Too few online players in lobby " + foundLobby.getName() + " to begin battle");
                return true;
            }

            if (playersForBattle.size() > shootingMap.spawnPointsQuantity()) {
                sender.sendMessage("Too few spawn points on map " + args[0] + " to begin battle");
                return true;
            }

            if (playersForBattle.size() > foundLobby.getGulagMap().spawnPointsQuantity()) {
                sender.sendMessage("Too few spawn points on gulag map to begin battle");
                return true;
            }

            ShootingBattle.createBattle(shootingGames, foundLobby, playersForBattle, shootingMap);

            sender.sendMessage("Battle in lobby " + foundLobby.getName() + " on map " + shootingMap.getName() + " successfully started");
            return true;
        }
        return false;
    }
}
