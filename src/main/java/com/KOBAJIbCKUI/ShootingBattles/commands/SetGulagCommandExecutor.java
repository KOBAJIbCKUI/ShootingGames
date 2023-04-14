package com.KOBAJIbCKUI.ShootingBattles.commands;

import com.KOBAJIbCKUI.ShootingBattles.lobby.LobbyStatus;
import com.KOBAJIbCKUI.ShootingBattles.lobby.Lobby;
import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import com.KOBAJIbCKUI.ShootingBattles.lobby.ShootingMap;
import com.KOBAJIbCKUI.ShootingBattles.managers.LobbiesManager;
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

            LobbiesManager lobbiesManager = shootingGames.getLobbiesManager();
            Lobby foundLobby = lobbiesManager.findLobby(player);
            if (foundLobby == null) {
                sender.sendMessage("You are not a member of any lobby");
                return true;
            }

            if (!foundLobby.getOwner().equals(player.getUniqueId())) {
                sender.sendMessage("You are not owner of this lobby");
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

            foundLobby.setGulagMap(shootingMap);

            sender.sendMessage("Gulag map successfully set");
            shootingGames.lobbiesConfig().saveLobbiesData();
            return true;
        }
        return false;
    }
}
