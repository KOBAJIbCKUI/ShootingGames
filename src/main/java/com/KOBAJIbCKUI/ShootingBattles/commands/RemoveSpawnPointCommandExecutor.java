package com.KOBAJIbCKUI.ShootingBattles.commands;

import com.KOBAJIbCKUI.ShootingBattles.lobby.LobbyStatus;
import com.KOBAJIbCKUI.ShootingBattles.managers.LobbiesManager;
import com.KOBAJIbCKUI.ShootingBattles.util.Coordinates;
import com.KOBAJIbCKUI.ShootingBattles.lobby.Lobby;
import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import com.KOBAJIbCKUI.ShootingBattles.lobby.ShootingMap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.NoSuchElementException;

public class RemoveSpawnPointCommandExecutor implements CommandExecutor {
    private ShootingGames shootingGames;

    public RemoveSpawnPointCommandExecutor(ShootingGames shootingGames) {
        this.shootingGames = shootingGames;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if (args.length == 4 || args.length == 2) {
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

            if (args.length == 4) {

                double X, Y, Z;
                try {
                    X = Double.parseDouble(args[1]);
                    Y = Double.parseDouble(args[2]);
                    Z = Double.parseDouble(args[3]);
                } catch (NumberFormatException e) {
                    return false;
                }

                Coordinates coordinates = new Coordinates(X, Y, Z);
                if (shootingMap.removeSpawnPoint(coordinates)) {
                    sender.sendMessage("Spawn point successfully removed");
                    shootingGames.lobbiesConfig().saveLobbiesData();
                } else {
                    sender.sendMessage("This spawn point doesn't exist");
                }

            } else {

                int index;
                try {
                    index = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    return false;
                }

                if (shootingMap.removeSpawnPoint(index - 1)) {
                    sender.sendMessage("Spawn point with index " + index + " successfully removed");
                    shootingGames.lobbiesConfig().reloadLobbiesConfig();
                } else {
                    sender.sendMessage("Spawn point with index " + index + " doesn't exist");
                }

            }

            return true;
        }
        return false;
    }
}
