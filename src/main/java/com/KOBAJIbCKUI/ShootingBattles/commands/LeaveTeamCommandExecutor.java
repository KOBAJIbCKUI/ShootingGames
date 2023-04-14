package com.KOBAJIbCKUI.ShootingBattles.commands;

import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import com.KOBAJIbCKUI.ShootingBattles.data.Board;
import com.KOBAJIbCKUI.ShootingBattles.lobby.Lobby;
import com.KOBAJIbCKUI.ShootingBattles.managers.LobbiesManager;
import com.KOBAJIbCKUI.ShootingBattles.managers.PlayersManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveTeamCommandExecutor implements CommandExecutor {
    ShootingGames shootingGames;

    public LeaveTeamCommandExecutor(ShootingGames shootingGames) {
        this.shootingGames = shootingGames;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {

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

            Board board = foundLobby.getBoard();
            String teamName = board.findLobbyTeam(player).getName();

            if (teamName.equals(player.getName())) {
                sender.sendMessage("You cannot leave this team because you are leader");
                return true;
            }

            board.leaveLobbyTeam(player, teamName);
            board.joinLobbyTeam(player, board.createLobbyTeam(player.getName()).getName());
            sender.sendMessage("You successfully left " + teamName + "'s team");

            return true;
        }
        return false;
    }
}
