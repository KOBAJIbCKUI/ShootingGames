package com.KOBAJIbCKUI.ShootingBattles.commands;

import com.KOBAJIbCKUI.ShootingBattles.lobby.Lobby;
import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import com.KOBAJIbCKUI.ShootingBattles.managers.LobbiesManager;
import com.KOBAJIbCKUI.ShootingBattles.managers.PlayersManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

            PlayersManager playersManager = shootingGames.getPlayersManager();
            if (playersManager.hasData(player)) {
                sender.sendMessage("You cannot do this being in battle");
                return true;
            }

            LobbiesManager lobbiesManager = shootingGames.getLobbiesManager();
            Lobby foundLobby = lobbiesManager.findLobby(player);

            if (foundLobby != null) {
                if (foundLobby.getName().equals(args[0])) {
                    sender.sendMessage("You are already in lobby " + foundLobby.getName());
                } else {
                    sender.sendMessage("You cannot join another lobby, before you leave lobby " + foundLobby.getName());
                }
                return true;
            }

            if ((foundLobby = lobbiesManager.findLobby(args[0])) == null) {
                sender.sendMessage("Lobby " + args[0] + " doesn't exist");
                return true;
            }

            if (foundLobby.addPlayer(player)) {
                sender.sendMessage("You successfully joined lobby " + foundLobby.getName());
            } else {
                sender.sendMessage("You are already in lobby " + foundLobby.getName());
            }
            return true;
        }
        return false;
    }
}
