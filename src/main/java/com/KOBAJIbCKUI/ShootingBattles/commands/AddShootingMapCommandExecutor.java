package com.KOBAJIbCKUI.ShootingBattles.commands;

import com.KOBAJIbCKUI.ShootingBattles.lobby.Lobby;
import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import com.KOBAJIbCKUI.ShootingBattles.lobby.ShootingMap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AddShootingMapCommandExecutor implements CommandExecutor {
    ShootingGames shootingGames;

    public AddShootingMapCommandExecutor(ShootingGames shootingGames) {
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

            if (foundLobby.getShootingMaps().stream().anyMatch(o -> o.getName().equals(args[0]))) {
                sender.sendMessage("Map " + args[0] + " already exists in lobby " + foundLobby.getName());
                return true;
            }

            if (foundLobby.addShootingMap(new ShootingMap(args[0]))) {
                sender.sendMessage("Map " + args[0] + " successfully added to lobby " + foundLobby.getName());
                shootingGames.saveLobbies(ShootingGames.SAVE_LOBBY_PATH);
            } else {
                sender.sendMessage("Map " + args[0] + " was NOT added to lobby " + foundLobby.getName());
            }
            return true;
        }
        return false;
    }
}
