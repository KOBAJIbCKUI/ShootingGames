package com.KOBAJIbCKUI.ShootingBattles.commands;

import com.KOBAJIbCKUI.ShootingBattles.lobby.Lobby;
import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CreateLobbyCommandExecutor implements CommandExecutor {
    private ShootingGames shootingGames;

    public CreateLobbyCommandExecutor(ShootingGames shootingGames) {
        this.shootingGames = shootingGames;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if (args.length == 1) {
            if (shootingGames.lobbiesListWrapper.lobbies.stream().noneMatch(o -> o.getName().equals(args[0]))) {
                shootingGames.lobbiesListWrapper.lobbies.add(new Lobby(args[0]));
                sender.sendMessage("Lobby " + args[0] + " successfully created");
                shootingGames.saveLobbies(ShootingGames.SAVE_LOBBY_PATH);
            } else {
                sender.sendMessage("Lobby " + args[0] + " already exists");
            }
            return true;
        }

        return false;
    }
}
