package com.KOBAJIbCKUI.ShootingBattles.commands;

import com.KOBAJIbCKUI.ShootingBattles.lobby.Lobby;
import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import com.KOBAJIbCKUI.ShootingBattles.managers.LobbiesManager;
import com.KOBAJIbCKUI.ShootingBattles.managers.PlayersManager;
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

        PlayersManager playersManager = shootingGames.getPlayersManager();
        if (playersManager.hasData(player)) {
            sender.sendMessage("You cannot do this being in battle");
            return true;
        }

        LobbiesManager lobbiesManager = shootingGames.getLobbiesManager();
        Lobby foundLobby = lobbiesManager.findLobby(player);

        if (foundLobby == null) {
            sender.sendMessage("You are not a member of any lobby");
            return true;
        }

        if (foundLobby.removePlayer(player)) {
            sender.sendMessage("You successfully left lobby " + foundLobby.getName());
        } else {
            sender.sendMessage("Unable to leave lobby " + foundLobby.getName());
        }
        return true;
    }
}
