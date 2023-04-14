package com.KOBAJIbCKUI.ShootingBattles;

import com.KOBAJIbCKUI.ShootingBattles.commands.*;
import com.KOBAJIbCKUI.ShootingBattles.data.Config;
import com.KOBAJIbCKUI.ShootingBattles.data.LobbiesConfig;
import com.KOBAJIbCKUI.ShootingBattles.data.MainBoard;
import com.KOBAJIbCKUI.ShootingBattles.listeners.*;
import com.KOBAJIbCKUI.ShootingBattles.lobby.Lobby;
import com.KOBAJIbCKUI.ShootingBattles.managers.*;
import com.KOBAJIbCKUI.ShootingBattles.util.Util;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.UUID;


public class ShootingGames extends JavaPlugin {
    //Plugin reference
    private static ShootingGames shootingGames;

    //Config declaration
    private Config config;

    //Lobbies config declaration
    private LobbiesConfig lobbiesConfig;

    //Lobbies manager declaration
    private LobbiesManager lobbiesManager;

    //Battles manager declaration
    private PlayersManager playersManager;

    //Main scoreboard declaration
    private MainBoard mainBoard;

    @Override
    public void onEnable() {

        if (!Util.isRunningMinecraftVersion(1, 12)) {
            if (!Util.isRunningMinecraftVersion(1, 12, 2)) {
                Util.warning("Shooting fights supports only versions above 1.12.2+!");
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
        }
        if (getDescription().getVersion().contains("Alpha")) {
            Util.log("YOU ARE RUNNING AN ALPHA VERSION, so may appear a lot of issues and bugs during use!");
        }
        loadPlugin();

    }

    public void loadPlugin() {
        //Initializing plugin reference
        shootingGames = this;

        //Config initializing
        config = new Config(this);

        //Create battles manager
        playersManager = new PlayersManager();

        //Loading lobbies to lobbies list
        lobbiesManager = new LobbiesManager();

        //Main board initialization
        mainBoard = new MainBoard(this);

        //Lobbies config initialization
        lobbiesConfig = new LobbiesConfig(this);

        initCommands();
        registerListeners();
        Util.log("ShootingGames has been enabled");
    }

    private void registerListeners() {

        //Adding events listeners
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new DeathListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
        getServer().getPluginManager().registerEvents(new StartBattleListener(), this);
        getServer().getPluginManager().registerEvents(new StartCountdownListener(), this);
        getServer().getPluginManager().registerEvents(new StartGulagListener(), this);
        getServer().getPluginManager().registerEvents(new ShowNameTagsListener(), this);
        getServer().getPluginManager().registerEvents(new StopBattleListener(), this);
        getServer().getPluginManager().registerEvents(new EndBattleListener(), this);
        getServer().getPluginManager().registerEvents(new MoveListener(), this);
    }

    private void initCommands() {

        //Binding command name with command executors
        getCommand("createLobby").setExecutor(new CreateLobbyCommandExecutor(this));
        getCommand("removeLobby").setExecutor(new RemoveLobbyCommandExecutor(this));
        getCommand("showLobbies").setExecutor(new ShowLobbiesCommandExecutor(this));
        getCommand("addMap").setExecutor(new AddShootingMapCommandExecutor(this));
        getCommand("removeMap").setExecutor(new RemoveShootingMapCommandExecutor(this));
        getCommand("addSpawnPoint").setExecutor(new AddSpawnPointCommandExecutor(this));
        getCommand("removeSpawnPoint").setExecutor(new RemoveSpawnPointCommandExecutor(this));
        getCommand("joinLobby").setExecutor(new JoinLobbyCommandExecutor(this));
        getCommand("leaveLobby").setExecutor(new LeaveLobbyCommandExecutor(this));
        getCommand("showLobbyMaps").setExecutor(new ShowLobbyMapsCommandExecutor(this));
        getCommand("showLobbyPlayers").setExecutor(new ShowLobbyPlayersCommandExecutor(this));
        getCommand("spectateBattle").setExecutor(new SpectateBattleCommandExecutor(this));
        getCommand("stopSpectateBattle").setExecutor(new StopSpectateBattleCommandExecutor(this));
        getCommand("startBattle").setExecutor(new StartBattleCommandExecutor(this));
        getCommand("startRandomBattle").setExecutor(new StartRandomBattleCommandExecutor(this));
        getCommand("showMapSpawnPoints").setExecutor(new ShowMapSpawnPointsCommandExecutor(this));
        getCommand("setGulag").setExecutor(new SetGulagCommandExecutor(this));
        getCommand("showLobbyInfo").setExecutor(new ShowLobbyInfoCommandExecutor(this));
        getCommand("toggleH").setExecutor(new ToggleHealCommandExecutor(this));
        getCommand("toggleS").setExecutor(new ToggleSonarCommandExecutor(this));
        getCommand("toggleCampersGlowing").setExecutor(new ToggleCampersGlowingCommandExecutor(this));
        getCommand("stopBattle").setExecutor(new StopBattleCommandExecutor(this));
        getCommand("joinTeam").setExecutor(new JoinTeamCommandExecutor(this));
        getCommand("leaveTeam").setExecutor(new LeaveTeamCommandExecutor(this));
        getCommand("setShowTagsTime").setExecutor(new SetShowTagsTimeCommandExecutor(this));
        getCommand("setTimeToEnd").setExecutor(new SetTimeToEndCommandExecutor(this));
    }

    @Override
    public void onDisable() {
        unloadPlugin();
    }

    public void unloadPlugin() {
        //Saving lobbies list
        lobbiesConfig.saveLobbiesData();
        stopAll();

        shootingGames = null;
        lobbiesManager = null;
        playersManager = null;
        config = null;

        HandlerList.unregisterAll(this);

        Util.log("ShootingGames has been disabled");
    }

    private void stopAll() {
        for (Lobby lobby : lobbiesManager.getLobbies()) {
            if (lobby.getCurrentBattle() != null) {
                lobby.getCurrentBattle().stopBattle();
            }
            for (UUID uuid : new HashSet<>(lobby.getPlayers())) {
                lobby.removePlayer(Bukkit.getPlayer(uuid));
            }
        }
    }

    public Config config() {
        return config;
    }

    public LobbiesConfig lobbiesConfig() {
        return lobbiesConfig;
    }

    public static ShootingGames getPlugin() {
        return shootingGames;
    }

    public LobbiesManager getLobbiesManager() {
        return this.lobbiesManager;
    }

    public PlayersManager getPlayersManager() {
        return this.playersManager;
    }

    public MainBoard getMainBoard() {
        return this.mainBoard;
    }

}
