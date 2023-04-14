package com.KOBAJIbCKUI.ShootingBattles.commands;

import com.KOBAJIbCKUI.ShootingBattles.lobby.LobbyStatus;
import com.KOBAJIbCKUI.ShootingBattles.lobby.Lobby;
import com.KOBAJIbCKUI.ShootingBattles.battle.ShootingBattle;
import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import com.KOBAJIbCKUI.ShootingBattles.lobby.ShootingMap;
import com.KOBAJIbCKUI.ShootingBattles.managers.LobbiesManager;
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

            LobbiesManager lobbiesManager = shootingGames.getLobbiesManager();
            Lobby foundLobby = lobbiesManager.findLobby(player);
            if (foundLobby == null) {
                sender.sendMessage("You are not a member of any lobby");
                return true;
            }

            if (foundLobby.getStatus() != LobbyStatus.READY) {
                sender.sendMessage("Lobby " + foundLobby.getName() + " is in status " + foundLobby.getStatus().getName());
                return true;
            }

            ShootingMap shootingMap;
            try {
                shootingMap = foundLobby.getShootingMaps().stream().filter(o -> o.getName().equals(args[0])).findFirst().get();
            } catch (NoSuchElementException e) {
                sender.sendMessage("Map " + args[0] + " doesn't exist in lobby " + foundLobby.getName());
                return true;
            }

            if (foundLobby.playersQuantity() < foundLobby.getMinPlayers()) {
                sender.sendMessage("Too few players in lobby to begin battle. Min number is " + foundLobby.getMinPlayers());
                return true;
            }

            if (foundLobby.playersQuantity() > foundLobby.getMaxPlayers()) {
                sender.sendMessage("Too many players in lobby to begin battle. Max number is " + foundLobby.getMaxPlayers());
            }

            if (foundLobby.getBoard().areInOneLobbyBattleTeam(foundLobby.getPlayers())) {
                sender.sendMessage("Unable to start battle because all players are in one team");
                return true;
            }

            if (foundLobby.isTeleportToGulag() && foundLobby.getGulagMap() == null) {
                sender.sendMessage("Gulag map is not set");
                return true;
            }

            if (foundLobby.isTeleportToGulag() && (foundLobby.playersQuantity() > shootingMap.spawnPointsQuantity())) {
                sender.sendMessage("Too few spawn points on map " + args[0] + " to begin battle");
                return true;
            }

            if (foundLobby.playersQuantity() > foundLobby.getGulagMap().spawnPointsQuantity()) {
                sender.sendMessage("Too few spawn points on gulag map to begin battle");
                return true;
            }

            ShootingBattle shootingBattle = new ShootingBattle(shootingGames, foundLobby, shootingMap);

            for (UUID uuid : foundLobby.getPlayers()) {
                shootingBattle.getBattlePlayerData().joinBattle(uuid);
            }

            shootingBattle.startCountdown();

            sender.sendMessage("Battle in lobby " + foundLobby.getName() + " on map " + shootingMap.getName() + " successfully started");
            return true;
        }
        return false;
    }
}
