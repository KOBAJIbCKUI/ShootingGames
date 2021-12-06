package com.KOBAJIbCKUI.ShootingGames;

import com.KOBAJIbCKUI.ShootingGames.commands.*;
import com.KOBAJIbCKUI.ShootingGames.listeners.ShootingGamesListener;
import com.KOBAJIbCKUI.ShootingGames.lobby.LobbiesListWrapper;
import com.KOBAJIbCKUI.ShootingGames.lobby.ShootingBattle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

public class ShootingGames extends JavaPlugin {
    //Path to file with lobbies list
    public static final String SAVE_LOBBY_PATH = "plugins\\ShootingGames\\lobbies";

    //Logger initialization
    Logger log = getLogger();

    //Command executors declaration
    private CommandExecutor
            createLobbyCommandExecutor,
            removeLobbyCommandExecutor,
            showLobbiesCommandExecutor,
            addShootingMapCommandExecutor,
            removeShootingMapCommandExecutor,
            addSpawnPointCommandExecutor,
            removeSpawnPointCommandExecutor,
            joinLobbyCommandExecutor,
            leaveLobbyCommandExecutor,
            showLobbyMapsCommandExecutor,
            showLobbyPlayersCommandExecutor,
            spectateBattleCommandExecutor,
            stopSpectateBattleCommandExecutor,
            startBattleCommandExecutor,
            startRandomBattleCommandExecutor,
            showMapSpawnPointsCommandExecutor,
            setGulagCommandExecutor,
            showLobbyInfoCommandExecutor,
            toggleHealCommandExecutor,
            toggleSonarCommandExecutor,
            toggleCampersGlowingCommandExecutor,
            stopBattleCommandExecutor,
            changeSaveTechCommandExecutor;

    //File configuration declaration
    public FileConfiguration config;

    //Variables from config
    public static int REWARD_FOR_WIN;
    public static int REWARD_FOR_LOOSE;
    public static int REWARD_FOR_KILL;
    public static int BONUS;
    public static int KILLSTREAK_LENGTH;
    public static boolean CAMPERS_GLOWING;
    public static String serializationTechnology;

    //Strings answers
    public static final String BATTLE_HAS_BEGUN = "" + ChatColor.RED + ChatColor.BOLD + "Battle has begun!";
    public static final String MINUTES_TO_GULAG = "" + ChatColor.RED + ChatColor.BOLD + "You have 15 minutes before GULAG";
    public static final String BATTLE_WON = "" + ChatColor.GREEN + ChatColor.BOLD + "%s won the battle!";
    public static final String ROUND_DRAW = "" + ChatColor.GRAY + ChatColor.BOLD + "Battle draw!";
    public static final String BATTLE_STOPPED = "" + ChatColor.BLUE + ChatColor.BOLD + "Battle stopped!";
    public static final String KILLER_REWARD = "" + ChatColor.GOLD + ChatColor.BOLD + "You've killed %s and got " + ChatColor.AQUA + ChatColor.BOLD + "%d diamonds";
    public static final String WINNER_REWARD = "" + ChatColor.GOLD + ChatColor.BOLD + "You've won the battle and got " + ChatColor.AQUA + ChatColor.BOLD + "%d diamonds";
    public static final String LOOSER_REWARD = "" + ChatColor.GOLD + ChatColor.BOLD + "You've lost the battle and got " + ChatColor.AQUA + ChatColor.BOLD + "%d diamonds";
    public static final String BATTLE_ENDS_IN_10 = "" + ChatColor.GOLD + ChatColor.BOLD + "Battle ends in " + ChatColor.RED + ChatColor.BOLD + "10 minutes!";
    public static final String BATTLE_ENDS_IN_5 = "" + ChatColor.GOLD + ChatColor.BOLD + "Battle ends in " + ChatColor.RED + ChatColor.BOLD + "5 minutes!";
    public static final String BATTLE_ENDS_IN_1 = "" + ChatColor.GOLD + ChatColor.BOLD + "Battle ends in " + ChatColor.RED + ChatColor.BOLD + "1 minute!";
    public static final String WELCOME_TO_GULAG = "" + ChatColor.RED + ChatColor.BOLD + "Welcome to GULAG,";
    public static final String TRAITORS = "" + ChatColor.RED + ChatColor.BOLD + "traitors to Motherland!";
    public static final String NAME_TAG_VISIBLE = "" + ChatColor.GOLD + ChatColor.BOLD + "Now name tags are " + ChatColor.RED + ChatColor.BOLD + "visible!";
    public static final String WELCOME_MESSAGE = "" + ChatColor.GREEN + "Welcome to ShootingGames server! BTW Sanya loh.";

    //Teams declaration
    public static Team NEUTRAL_TEAM;
    public static Team DEATHMATCH_TEAM;

    //Objectives declaration
    public static Objective WINS_OBJECTIVE;
    public static Objective KILLSTREAK_OBJECTIVE;

    //Lobbies list declaration
    public LobbiesListWrapper lobbiesListWrapper;

    //Players in battles map initialization
    //Key = player, value = shooting battle, where player takes part
    public static Map<Player, ShootingBattle> playersInBattles = new HashMap<>();

    @Override
    public void onEnable() {

        log.info("It's really working!");

        //config initialization
        config = getConfig();

        //Rewards from config
        REWARD_FOR_WIN = config.getInt("rewards.win_reward",8);
        REWARD_FOR_LOOSE = config.getInt("rewards.loose_reward", 3);
        REWARD_FOR_KILL = config.getInt("rewards.kill_reward", 5);
        BONUS = config.getInt("rewards.bonus", 5);
        KILLSTREAK_LENGTH = config.getInt("killstreak_length", 3);
        CAMPERS_GLOWING = config.getBoolean("campers_glowing", false);
        serializationTechnology =  config.getString("save_technology", "default");

        //Saving rewards values to config
        config.set("rewards.win_reward", REWARD_FOR_WIN);
        config.set("rewards.loose_reward", REWARD_FOR_LOOSE);
        config.set("rewards.kill_reward", REWARD_FOR_KILL);
        config.set("rewards.bonus", BONUS);
        config.set("killstreak_length", KILLSTREAK_LENGTH);
        config.set("campers_glowing", CAMPERS_GLOWING);
        config.set("save_technology", serializationTechnology);
        saveConfig();

        //Loading lobbies to lobbies list
        lobbiesListWrapper = loadLobbies(SAVE_LOBBY_PATH);

        //Get main scoreboard
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = scoreboardManager.getMainScoreboard();

        //Initializing teams
        if ((NEUTRAL_TEAM = scoreboard.getTeam("Neutral")) == null) {
            NEUTRAL_TEAM = scoreboard.registerNewTeam("Neutral");
        }
        if ((DEATHMATCH_TEAM = scoreboard.getTeam("Singles")) == null) {
            DEATHMATCH_TEAM = scoreboard.registerNewTeam("Singles");
        }
        //Set teams options
        NEUTRAL_TEAM.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
        DEATHMATCH_TEAM.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OTHER_TEAMS);
        NEUTRAL_TEAM.setAllowFriendlyFire(false);
        DEATHMATCH_TEAM.setAllowFriendlyFire(true);

        //Initializing objectives
        if ((WINS_OBJECTIVE = scoreboard.getObjective("Wins")) == null) {
            WINS_OBJECTIVE = scoreboard.registerNewObjective("Wins", "dummy");
        }
        if ((KILLSTREAK_OBJECTIVE = scoreboard.getObjective("Killstreak")) == null) {
            KILLSTREAK_OBJECTIVE = scoreboard.registerNewObjective("Killstreak", "dummy");
        }

        //Set display slot
        WINS_OBJECTIVE.setDisplaySlot(DisplaySlot.SIDEBAR);
        KILLSTREAK_OBJECTIVE.setDisplaySlot(DisplaySlot.BELOW_NAME);

        //Adding events listener
        getServer().getPluginManager().registerEvents(new ShootingGamesListener(this), this);

        //Initializing command executors
        createLobbyCommandExecutor = new CreateLobbyCommandExecutor(this);
        removeLobbyCommandExecutor = new RemoveLobbyCommandExecutor(this);
        showLobbiesCommandExecutor = new ShowLobbiesCommandExecutor(this);
        addShootingMapCommandExecutor = new AddShootingMapCommandExecutor(this);
        removeShootingMapCommandExecutor = new RemoveShootingMapCommandExecutor(this);
        addSpawnPointCommandExecutor = new AddSpawnPointCommandExecutor(this);
        removeSpawnPointCommandExecutor = new RemoveSpawnPointCommandExecutor(this);
        joinLobbyCommandExecutor = new JoinLobbyCommandExecutor(this);
        leaveLobbyCommandExecutor = new LeaveLobbyCommandExecutor(this);
        showLobbyMapsCommandExecutor = new ShowLobbyMapsCommandExecutor(this);
        showLobbyPlayersCommandExecutor = new ShowLobbyPlayersCommandExecutor(this);
        spectateBattleCommandExecutor = new SpectateBattleCommandExecutor(this);
        stopSpectateBattleCommandExecutor = new StopSpectateBattleCommandExecutor(this);
        startBattleCommandExecutor = new StartBattleCommandExecutor(this);
        startRandomBattleCommandExecutor = new StartRandomBattleCommandExecutor(this);
        showMapSpawnPointsCommandExecutor = new ShowMapSpawnPointsCommandExecutor(this);
        setGulagCommandExecutor = new SetGulagCommandExecutor(this);
        showLobbyInfoCommandExecutor = new ShowLobbyInfoCommandExecutor(this);
        toggleHealCommandExecutor = new ToggleHealCommandExecutor(this);
        toggleSonarCommandExecutor = new ToggleSonarCommandExecutor(this);
        toggleCampersGlowingCommandExecutor = new ToggleCampersGlowingCommandExecutor(this);
        stopBattleCommandExecutor = new StopBattleCommandExecutor(this);
        changeSaveTechCommandExecutor = new ChangeSaveTechCommandExecutor(this);

        //Binding command name with command executors
        getCommand("createLobby").setExecutor(createLobbyCommandExecutor);
        getCommand("removeLobby").setExecutor(removeLobbyCommandExecutor);
        getCommand("showLobbies").setExecutor(showLobbiesCommandExecutor);
        getCommand("addMap").setExecutor(addShootingMapCommandExecutor);
        getCommand("removeMap").setExecutor(removeShootingMapCommandExecutor);
        getCommand("addSpawnPoint").setExecutor(addSpawnPointCommandExecutor);
        getCommand("removeSpawnPoint").setExecutor(removeSpawnPointCommandExecutor);
        getCommand("joinLobby").setExecutor(joinLobbyCommandExecutor);
        getCommand("leaveLobby").setExecutor(leaveLobbyCommandExecutor);
        getCommand("showLobbyMaps").setExecutor(showLobbyMapsCommandExecutor);
        getCommand("showLobbyPlayers").setExecutor(showLobbyPlayersCommandExecutor);
        getCommand("spectateBattle").setExecutor(spectateBattleCommandExecutor);
        getCommand("stopSpectateBattle").setExecutor(stopSpectateBattleCommandExecutor);
        getCommand("startBattle").setExecutor(startBattleCommandExecutor);
        getCommand("startRandomBattle").setExecutor(startRandomBattleCommandExecutor);
        getCommand("showMapSpawnPoints").setExecutor(showMapSpawnPointsCommandExecutor);
        getCommand("setGulag").setExecutor(setGulagCommandExecutor);
        getCommand("showLobbyInfo").setExecutor(showLobbyInfoCommandExecutor);
        getCommand("toggleH").setExecutor(toggleHealCommandExecutor);
        getCommand("toggleS").setExecutor(toggleSonarCommandExecutor);
        getCommand("toggleCampersGlowing").setExecutor(toggleCampersGlowingCommandExecutor);
        getCommand("stopBattle").setExecutor(stopBattleCommandExecutor);
        getCommand("changeSaveTech").setExecutor(changeSaveTechCommandExecutor);
    }

    @Override
    public void onDisable() {
        log.info("It's really don't working, dude!");

        //Saving lobbies list
        saveLobbies(SAVE_LOBBY_PATH);
    }

    public void saveLobbies(String path) {
        //Checking if lobbies list is empty
        if (lobbiesListWrapper.lobbies.isEmpty()) {
            log.info("Saving empty lobbies list");
        }

        //Choose extension of file to save lobbies
        switch (serializationTechnology) {
            case "default":
                path = path + ".txt";
                break;
            case "xml":
                path = path + ".xml";
                break;
            //case "json":
            //    path = path + ".json";
        }

        try {
            //Creating path
            Path lobbiesList = Paths.get(path);
            //Creating directory if it doesn't exist
            if (Files.notExists(lobbiesList.getParent())) {
                Files.createDirectory(lobbiesList.getParent());
            }
            //Creating file if it doesn't exist
            if (Files.notExists(lobbiesList)) {
                Files.createFile(lobbiesList);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        //Save lobbies list to file
        switch (serializationTechnology) {
            case "xml":
                try (FileWriter writer = new FileWriter(path)) {
                    JAXBContext context = JAXBContext.newInstance(LobbiesListWrapper.class);
                    Marshaller marshaller = context.createMarshaller();
                    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                    marshaller.marshal(lobbiesListWrapper, writer);
                } catch (IOException | JAXBException e) {
                    e.printStackTrace();
                }
                break;
                /*
            case JSON:
                try {

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
                 */
            case "default": {
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
                    oos.writeObject(lobbiesListWrapper);
                    oos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    //Load lobbies
    public LobbiesListWrapper loadLobbies(String path) {

        //Choose file to load lobbies
        switch (serializationTechnology) {
            case "default":
                path = path + ".txt";
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
                    return (LobbiesListWrapper) ois.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            case "xml":
                path = path + ".xml";
                try (FileReader reader = new FileReader(path)) {
                    JAXBContext context = JAXBContext.newInstance(LobbiesListWrapper.class);
                    Unmarshaller unmarshaller = context.createUnmarshaller();
                    return (LobbiesListWrapper) unmarshaller.unmarshal(reader);
                } catch (IOException | JAXBException e) {
                    e.printStackTrace();
                }
                break;
                /*
            case "json":
                path = path + ".json";
                 */
        }

        log.info("Unable to read file " + path + ". Lobbies list is empty");
        return new LobbiesListWrapper();
    }

}
