package com.KOBAJIbCKUI.ShootingBattles.commands;

import com.KOBAJIbCKUI.ShootingBattles.lobby.LobbyStatus;
import com.KOBAJIbCKUI.ShootingBattles.lobby.Lobby;
import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import com.KOBAJIbCKUI.ShootingBattles.managers.LobbiesManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveLobbyCommandExecutor implements CommandExecutor {
    private ShootingGames shootingGames;

    public RemoveLobbyCommandExecutor(ShootingGames shootingGames) {
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
            Lobby foundLobby;

            if ((foundLobby = lobbiesManager.findLobby(args[0])) == null) {
                sender.sendMessage("Lobby " + args[0] + " doesn't exist");
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

            if (lobbiesManager.removeLobby(foundLobby)) {
                sender.sendMessage("Lobby " + foundLobby.getName() + " successfully deleted");
                shootingGames.lobbiesConfig().saveLobbiesData();
            } else {
                sender.sendMessage("Lobby " + foundLobby.getName() + " doesn't exist");
            }
            return true;
        }
        return false;
    }
}
