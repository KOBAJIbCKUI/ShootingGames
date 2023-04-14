package com.KOBAJIbCKUI.ShootingBattles.commands;

import com.KOBAJIbCKUI.ShootingBattles.lobby.LobbyStatus;
import com.KOBAJIbCKUI.ShootingBattles.battle.ShootingBattle;
import com.KOBAJIbCKUI.ShootingBattles.lobby.Lobby;
import com.KOBAJIbCKUI.ShootingBattles.managers.LobbiesManager;
import com.KOBAJIbCKUI.ShootingBattles.managers.PlayersManager;
import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpectateBattleCommandExecutor implements CommandExecutor {
    ShootingGames shootingGames;

    public SpectateBattleCommandExecutor(ShootingGames shootingGames) {
        this.shootingGames = shootingGames;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }

        if (player == null && args.length == 1) {
            player = Bukkit.getPlayer(args[0]);
        }

        if (player == null) {
            sender.sendMessage("This command can be run only by players");
            return true;
        }

        PlayersManager playersManager = shootingGames.getPlayersManager();
        if (playersManager.hasPlayerData(player)) {
            sender.sendMessage("You cannot do this being in battle");
            return true;
        }

        LobbiesManager lobbiesManager = shootingGames.getLobbiesManager();
        Lobby foundLobby = lobbiesManager.findLobby(player);
        if (foundLobby == null) {
            sender.sendMessage("You are not a member of any lobby");
            return true;
        }

        if (!(foundLobby.getStatus() == LobbyStatus.RUNNING || foundLobby.getStatus() == LobbyStatus.COUNTDOWN)) {
            sender.sendMessage("Nothing to spectate");
            return true;
        }

        if (playersManager.hasSpectatorData(player)) {
            sender.sendMessage("You are already spectating");
            return true;
        }

        ShootingBattle shootingBattle = foundLobby.getCurrentBattle();
        shootingBattle.getBattlePlayerData().spectateBattle(player);
        sender.sendMessage("Begin spectating");
        return true;
    }
}
