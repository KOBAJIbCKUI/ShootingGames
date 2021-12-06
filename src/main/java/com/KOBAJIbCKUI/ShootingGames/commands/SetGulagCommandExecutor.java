package com.KOBAJIbCKUI.ShootingGames.commands;

import com.KOBAJIbCKUI.ShootingGames.lobby.Lobby;
import com.KOBAJIbCKUI.ShootingGames.ShootingGames;
import com.KOBAJIbCKUI.ShootingGames.lobby.ShootingMap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.NoSuchElementException;

public class SetGulagCommandExecutor implements CommandExecutor {
    ShootingGames shootingGames;

    public SetGulagCommandExecutor(ShootingGames shootingGames) {
        this.shootingGames = shootingGames;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length == 1) {

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

            if (foundLobby.isInBattle) {
                sender.sendMessage("Lobby " + foundLobby.getName() + " is in battle");
                return true;
            }

            ShootingMap shootingMap;
            try {
                shootingMap = foundLobby.getShootingMaps().stream().filter(o -> o.getName().equals(args[0])).findFirst().get();
            } catch (NoSuchElementException e) {
                sender.sendMessage("Map " + args[0] + " doesn't exist in lobby " + foundLobby.getName());
                return true;
            }

            foundLobby.setGulagMap(shootingMap);
            sender.sendMessage("Gulag successfully set");
            shootingGames.saveLobbies(ShootingGames.SAVE_LOBBY_PATH);
            return true;
        }
        return false;
    }
}
