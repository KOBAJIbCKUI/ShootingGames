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
import org.bukkit.scoreboard.Team;

public class JoinTeamCommandExecutor implements CommandExecutor {
    ShootingGames shootingGames;

    public JoinTeamCommandExecutor(ShootingGames shootingGames) {
        this.shootingGames = shootingGames;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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

            if (foundLobby == null) {
                sender.sendMessage("You are not a member of any lobby");
                return true;
            }

            Board board = foundLobby.getBoard();

            Team foundLobbyTeam = board.findLobbyTeam(args[0]);
            if (foundLobbyTeam == null) {
                sender.sendMessage("Team " + args[0] + " doesn't exist");
                return true;
            }

            if (foundLobbyTeam.getName().equals(board.findLobbyTeam(player).getName())) {
                sender.sendMessage("You are already a member of team " + args[0]);
                return true;
            }

            board.leaveLobbyTeam(player, board.findLobbyTeam(player).getName());
            board.joinLobbyTeam(player, args[0]);
            sender.sendMessage("You successfully joined " + args[0] + "'s team");
            return true;
        }
        return false;
    }
}
