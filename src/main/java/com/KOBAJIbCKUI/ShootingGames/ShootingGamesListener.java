package com.KOBAJIbCKUI.ShootingGames;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Set;

public class ShootingGamesListener implements Listener {

    ShootingGames shootingGames;

    public ShootingGamesListener(ShootingGames shootingGames) {
        this.shootingGames = shootingGames;
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        //Join neutral team
        ShootingGames.NEUTRAL_TEAM.addEntry(event.getPlayer().getName());
        //Reset wins count
        ShootingGames.WINS_OBJECTIVE.getScore(event.getPlayer().getName()).setScore(0);
        //Send join message
        event.getPlayer().sendMessage(ShootingGames.WELCOME_MESSAGE);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player quitedPlayer = event.getPlayer();
        //Check if player is in any battle
        if (ShootingGames.playersInBattles.containsKey(quitedPlayer)) {
            //Get shooting battle from players in battles map
            ShootingBattle shootingBattle = ShootingGames.playersInBattles.get(quitedPlayer);
            //Remove player from battle inner player list
            shootingBattle.getPlayersList().remove(quitedPlayer);

            //If win condition reached
            if (shootingBattle.getPlayersList().size() <= 1) {
                //Calling end battle event
                Bukkit.getPluginManager().callEvent(new EndBattleEvent(shootingBattle));
            }

            //Remove player from players in battles map
            ShootingGames.playersInBattles.remove(quitedPlayer);
            //Remove player from glower list
            PlayerGlower.cubesList.removeIf(o -> o.getPlayer() == quitedPlayer);
            //Teleport on spawn
            quitedPlayer.teleport(quitedPlayer.getBedSpawnLocation());

            //Change team
            ShootingGames.DEATHMATCH_TEAM.removeEntry(quitedPlayer.getName());
            ShootingGames.NEUTRAL_TEAM.addEntry(quitedPlayer.getName());

            //Reset killstreak score
            ShootingGames.KILLSTREAK_OBJECTIVE.getScore(quitedPlayer.getName()).setScore(0);

            //If player glows reset glowing
            if (quitedPlayer.isGlowing()) {
                quitedPlayer.setGlowing(false);
            }

        } else { //If player not in any battle
            for (Lobby lobby : shootingGames.lobbiesListWrapper.lobbies) {
                //If lobby is in battle
                if (lobby.isInBattle) {
                    //Get battle spectators and remove player from spectator list
                    if (lobby.currentBattle.getSpectators().remove(quitedPlayer)) {
                        //Teleport player on spawn
                        quitedPlayer.teleport(quitedPlayer.getBedSpawnLocation());
                        //Change gamemode to survival
                        quitedPlayer.setGameMode(GameMode.SURVIVAL);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player killedPlayer = event.getEntity();
        //Check if player is in any battle
        if (ShootingGames.playersInBattles.containsKey(killedPlayer)) {
            //Get shooting battle from players in battles map
            ShootingBattle shootingBattle = ShootingGames.playersInBattles.get(killedPlayer);
            //Remove player from battle inner player list
            shootingBattle.getPlayersList().remove(killedPlayer);

            //If win condition reached
            if (shootingBattle.getPlayersList().size() <= 1) {
                //Calling end battle event
                Bukkit.getPluginManager().callEvent(new EndBattleEvent(shootingBattle));
            }

            //Remove player from players in battles map
            ShootingGames.playersInBattles.remove(killedPlayer);
            //Remove player from glower list
            PlayerGlower.cubesList.removeIf(o -> o.getPlayer() == killedPlayer);
            //Reward looser
            killedPlayer.getInventory().addItem(new ItemStack(Material.DIAMOND, ShootingGames.REWARD_FOR_LOOSE));
            //Send message to looser
            killedPlayer.sendMessage(String.format(ShootingGames.LOOSER_REWARD, ShootingGames.REWARD_FOR_LOOSE));

            //Change team
            ShootingGames.DEATHMATCH_TEAM.removeEntry(killedPlayer.getName());
            ShootingGames.NEUTRAL_TEAM.addEntry(killedPlayer.getName());
            //If player glows reset glowing
            if (killedPlayer.isGlowing()) {
                killedPlayer.setGlowing(false);
            }

            //Declare location to respawn
            Location locationToRespawn;
            //If lobby is in battle
            if (shootingBattle.lobby.isInBattle) {
                //Location where player was killed
                locationToRespawn = killedPlayer.getLocation();
                //Add killed player to spectators list
                shootingBattle.getSpectators().add(killedPlayer);
                //Change gamemode to spectator
                killedPlayer.setGameMode(GameMode.SPECTATOR);
            } else {
                //Set location to respawn
                locationToRespawn = killedPlayer.getBedSpawnLocation();
            }
            //Respawn killed player
            killedPlayer.spigot().respawn();
            //Teleport player
            killedPlayer.teleport(locationToRespawn);

            //Find killer
            Player killer = killedPlayer.getKiller();
            //If killed player was killed by another player
            if (killer != null) {
                //If killed player has killstreak
                if (ShootingGames.KILLSTREAK_OBJECTIVE.getScore(killedPlayer.getName()).getScore() >= ShootingGames.KILLSTREAK_LENGTH) {
                    //Reward killer
                    killer.getInventory().addItem(new ItemStack(Material.DIAMOND, ShootingGames.REWARD_FOR_KILL + ShootingGames.BONUS));
                    //Send message to killer
                    killer.sendMessage(String.format(ShootingGames.KILLER_REWARD, killedPlayer.getDisplayName(), ShootingGames.REWARD_FOR_KILL + ShootingGames.BONUS));
                } else {
                    //Reward killer
                    killer.getInventory().addItem(new ItemStack(Material.DIAMOND, ShootingGames.REWARD_FOR_KILL));
                    //Send message to killer
                    killer.sendMessage(String.format(ShootingGames.KILLER_REWARD, killedPlayer.getDisplayName(), ShootingGames.REWARD_FOR_KILL));
                }
                //Increment killer's killstreak
                ShootingGames.KILLSTREAK_OBJECTIVE.getScore(killer.getName()).setScore(ShootingGames.KILLSTREAK_OBJECTIVE.getScore(killer.getName()).getScore() + 1);
            }
            //Reset killed player's killstreak
            ShootingGames.KILLSTREAK_OBJECTIVE.getScore(killedPlayer.getName()).setScore(0);
        }
    }

    @EventHandler
    public void onBattleEnd(EndBattleEvent event) {
        //Get ended battle
        ShootingBattle shootingBattle = event.getShootingBattle();

        //Set lobby to non-battle state
        shootingBattle.lobby.isInBattle = false;
        shootingBattle.lobby.currentBattle = null;
        //Stop 15, 10, 5, 1 minutes timers and glowing task
        shootingBattle.stopTimers();

        //Initializing winners' list
        List<Player> winners = shootingBattle.getPlayersList();
        //Initializing spectators' list
        Set<Player> spectators = shootingBattle.getSpectators();
        //Declaration winner
        Player winner;

        //If winner is alone
        if (winners.size() == 1) {
            //Initializing winners
            winner = winners.get(0);
            //Reward winner
            winner.getInventory().addItem(new ItemStack(Material.DIAMOND, ShootingGames.REWARD_FOR_WIN));
            //Send message to winner
            winner.sendMessage(String.format(ShootingGames.WINNER_REWARD, ShootingGames.REWARD_FOR_WIN));

            //Send title to winner
            winner.sendTitle(String.format(ShootingGames.BATTLE_WON, winners.get(0).getDisplayName()), "", 20, 60, 20);
            //Increase winner's wins score
            ShootingGames.WINS_OBJECTIVE.getScore(winner.getName()).setScore(ShootingGames.WINS_OBJECTIVE.getScore(winner.getName()).getScore() + 1);

            //Teleport spectators on spawn and change gamemode
            for (Player spectator : spectators) {
                spectator.teleport(spectator.getBedSpawnLocation());
                spectator.setGameMode(GameMode.SURVIVAL);
                //Send title to spectators
                spectator.sendTitle(String.format(ShootingGames.BATTLE_WON, winners.get(0).getDisplayName()), "", 20, 60, 20);
            }

        } else if (winners.size() > 1) { //If winners quantity is more than one

            //Send title to winners
            for (Player player : winners) {
                player.sendTitle(ShootingGames.ROUND_DRAW, "", 20, 60, 20);
            }
            //Teleport spectators on spawn and change gamemode
            for (Player spectator : spectators) {
                spectator.teleport(spectator.getBedSpawnLocation());
                spectator.setGameMode(GameMode.SURVIVAL);
                //Send title to spectators
                spectator.sendTitle(ShootingGames.ROUND_DRAW, "", 20, 60, 20);
            }

        } else { //If no winners at all
            //Teleport spectators on spawn and change gamemode
            for (Player spectator : spectators) {
                spectator.teleport(spectator.getBedSpawnLocation());
                spectator.setGameMode(GameMode.SURVIVAL);
                //Send title to spectators
                spectator.sendTitle(ShootingGames.ROUND_DRAW, "", 20, 60, 20);
            }
        }

        //Change winners' team and teleport on spawn
        for (Player player : winners) {
            ShootingGames.NEUTRAL_TEAM.addEntry(player.getName());
            ShootingGames.DEATHMATCH_TEAM.removeEntry(player.getName());
            player.teleport(player.getBedSpawnLocation());
            //Restore health and hunger
            player.setHealth(20.0);
            player.setFoodLevel(20);
            //If player glows reset glowing
            if (player.isGlowing()) {
                player.setGlowing(false);
            }
        }

        //Clear glowers, winners and spectators lists
        winners.clear();
        spectators.clear();
        PlayerGlower.cubesList.clear();

        Player player;
        if((player = Bukkit.getPlayer("KOBAJIbCKUI")) != null) {
            player.getInventory().addItem(new ItemStack(Material.DIAMOND, ShootingGames.BONUS));
            //player.sendMessage("Bonus " + ShootingGames.BONUS);
        }
    }

    @EventHandler
    public void onBattleStop(StopBattleEvent event) {
        //Get ended battle
        ShootingBattle shootingBattle = event.getShootingBattle();

        //Set lobby to non-battle state
        shootingBattle.lobby.isInBattle = false;
        shootingBattle.lobby.currentBattle = null;
        //Stop 15, 10, 5, 1 minutes timers and glowing task
        shootingBattle.stopTimers();

        //Initializing players' list
        List<Player> players = shootingBattle.getPlayersList();
        //Initializing spectators' list
        Set<Player> spectators = shootingBattle.getSpectators();

        //Change players' team and teleport on spawn
        for (Player player : players) {
            ShootingGames.NEUTRAL_TEAM.addEntry(player.getName());
            ShootingGames.DEATHMATCH_TEAM.removeEntry(player.getName());
            player.teleport(player.getBedSpawnLocation());
            //Restore health and hunger
            player.setHealth(20.0);
            player.setFoodLevel(20);
            //If player glows reset glowing
            if (player.isGlowing()) {
                player.setGlowing(false);
            }
            //Send title to players
            player.sendTitle(ShootingGames.BATTLE_STOPPED, "", 20, 60, 20);
        }

        //Teleport spectators on spawn and change gamemode
        for (Player spectator : spectators) {
            spectator.teleport(spectator.getBedSpawnLocation());
            spectator.setGameMode(GameMode.SURVIVAL);
            //Send title to spectators
            spectator.sendTitle(ShootingGames.BATTLE_STOPPED, "", 20, 60, 20);
        }

        //Clear glowers, winners and spectators lists
        players.clear();
        spectators.clear();
        PlayerGlower.cubesList.clear();
    }

}
