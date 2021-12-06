package com.KOBAJIbCKUI.ShootingGames.commands;

import com.KOBAJIbCKUI.ShootingGames.lobby.Lobby;
import com.KOBAJIbCKUI.ShootingGames.lobby.PlayerBattleSpawner;
import com.KOBAJIbCKUI.ShootingGames.ShootingGames;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpectateBattleCommandExecutor implements CommandExecutor {
    ShootingGames shootingGames;

    public SpectateBattleCommandExecutor(ShootingGames shootingGames) {
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

        if (ShootingGames.playersInBattles.containsKey(player)) {
            sender.sendMessage("You are cannot do this in battle");
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

        if (!foundLobby.isInBattle) {
            sender.sendMessage("There is no battle in lobby " + foundLobby.getName());
            return true;
        }

        if (foundLobby.currentBattle.getSpectators().add(player)) {
            player.setGameMode(GameMode.SPECTATOR);
            PlayerBattleSpawner.spawnSpectator(player, foundLobby.currentBattle.shootingMap.getSpawnPoints());
            sender.sendMessage("Begin spectating");
        } else {
            sender.sendMessage("You are already spectating");
        }
        return true;
    }
}
