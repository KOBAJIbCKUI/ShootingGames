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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class StartRandomBattleCommandExecutor implements CommandExecutor {
    ShootingGames shootingGames;

    public StartRandomBattleCommandExecutor(ShootingGames shootingGames) {
        this.shootingGames = shootingGames;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }

        if (player == null && args.length == 1) {
            player = Bukkit.getPlayer(args[0]);
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

        if (foundLobby.isTeleportToGulag() && (foundLobby.playersQuantity() > foundLobby.getGulagMap().spawnPointsQuantity())) {
            sender.sendMessage("Too few spawn points on gulag map to begin battle");
            return true;
        }

        List<ShootingMap> suitableMaps = new ArrayList<>();
        for (ShootingMap shootingMap : foundLobby.getShootingMaps()) {
            if (!shootingMap.getName().equals(foundLobby.getGulagMap().getName()) && shootingMap.getSpawnPoints().size() >= foundLobby.playersQuantity()) {
                suitableMaps.add(shootingMap);
            }
        }

        if (suitableMaps.isEmpty()) {
            sender.sendMessage("No suitable maps to begin battle");
            return true;
        }

        Collections.shuffle(suitableMaps);
        ShootingBattle shootingBattle = new ShootingBattle(shootingGames, foundLobby, suitableMaps.get(0));

        for (UUID uuid : foundLobby.getPlayers()) {
            shootingBattle.getBattlePlayerData().joinBattle(uuid);
        }

        shootingBattle.startCountdown();

        sender.sendMessage("Battle in lobby " + foundLobby.getName() + " on random map successfully started");
        return true;
    }
}
