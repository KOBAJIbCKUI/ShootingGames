package com.KOBAJIbCKUI.ShootingBattles.commands;

import com.KOBAJIbCKUI.ShootingBattles.lobby.Lobby;
import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.NoSuchElementException;

public class JoinLobbyCommandExecutor implements CommandExecutor {

    ShootingGames shootingGames;

    public JoinLobbyCommandExecutor(ShootingGames shootingGames) {
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

            if (ShootingGames.playersInBattles.containsKey(player)) {
                sender.sendMessage("You can't do this being in battle");
                return true;
            }

            Lobby foundLobby = null;
            for (Lobby lobby : shootingGames.lobbiesListWrapper.lobbies) {
                if (lobby.getPlayers().contains(player.getUniqueId())) {
                    foundLobby = lobby;
                    break;

                }
            }

            if (foundLobby != null) {
                sender.sendMessage("You cannot join another lobby, before you leave lobby " + foundLobby.getName());
                return true;
            }

            Lobby lobby;
            try {
                lobby = shootingGames.lobbiesListWrapper.lobbies.stream().filter(o -> o.getName().equals(args[0])).findFirst().get();
            } catch (NoSuchElementException e) {
                sender.sendMessage("Lobby " + args[0] + " doesn't exist");
                return true;
            }

            if (lobby.addPlayer(player)) {
                sender.sendMessage("You successfully joined lobby " + lobby.getName());
                shootingGames.saveLobbies(ShootingGames.SAVE_LOBBY_PATH);
            } else {
                sender.sendMessage("You are already in lobby " + lobby.getName());
            }
            return true;
        }
        return false;
    }
}
