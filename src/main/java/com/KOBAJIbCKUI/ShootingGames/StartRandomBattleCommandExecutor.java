package com.KOBAJIbCKUI.ShootingGames;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
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
            sender.sendMessage("Lobby " + foundLobby.getName() + " is in battle");
            return true;
        }

        if (foundLobby.playersQuantity() < 2) {
            sender.sendMessage("Too few players in lobby " + foundLobby.getName() + " to begin battle");
            return true;
        }

        if (foundLobby.getGulagMap() == null) {
            sender.sendMessage("Gulag map is not set");
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

        if (playersForBattle.size() > foundLobby.getGulagMap().spawnPointsQuantity()) {
            sender.sendMessage("Too few spawn points on gulag map to begin battle");
            return true;
        }

        List<ShootingMap> suitableMaps = new ArrayList<>();
        for (ShootingMap shootingMap : foundLobby.getShootingMaps()) {
            if (!shootingMap.equals(foundLobby.getGulagMap()) && shootingMap.getSpawnPoints().size() >= playersForBattle.size()) {
                suitableMaps.add(shootingMap);
            }
        }

        if (suitableMaps.isEmpty()) {
            sender.sendMessage("No suitable maps to begin battle");
            return true;
        }

        ShootingBattle.createBattle(shootingGames, foundLobby, playersForBattle, suitableMaps.get((int) (Math.random() * suitableMaps.size())));
        sender.sendMessage("Battle in lobby " + foundLobby.getName() + " on random map successfully started");
        return true;
    }
}
