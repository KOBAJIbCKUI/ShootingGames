package com.KOBAJIbCKUI.ShootingBattles.commands;

import com.KOBAJIbCKUI.ShootingBattles.lobby.Lobby;
import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveLobbyCommandExecutor implements CommandExecutor {
    ShootingGames shootingGames;

    public LeaveLobbyCommandExecutor(ShootingGames shootingGames) {
        this.shootingGames = shootingGames;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

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

        if (foundLobby == null) {
            sender.sendMessage("You are not a member of any lobby");
            return true;
        }

        if (foundLobby.removePlayer(player)) {
            sender.sendMessage("You successfully left lobby " + foundLobby.getName());
            shootingGames.saveLobbies(ShootingGames.SAVE_LOBBY_PATH);
        } else {
            sender.sendMessage("Unable to leave lobby " + foundLobby.getName());
        }
        return true;
    }
}
