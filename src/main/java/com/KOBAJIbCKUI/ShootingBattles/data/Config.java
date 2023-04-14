package com.KOBAJIbCKUI.ShootingBattles.data;

import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {
    private final ShootingGames shootingGames;
    private FileConfiguration config;

    public static int rewardForWin;
    public static int rewardForLoose;
    public static int rewardForKill;
    public static int bonus;
    public static int killstreakLength;

    public Config(ShootingGames shootingGames) {
        this.shootingGames = shootingGames;
        loadConfig();
    }

    public void loadConfig() {

        config = shootingGames.getConfig();

        //Rewards from config
        rewardForWin = config.getInt("rewards.win_reward",8);
        rewardForLoose = config.getInt("rewards.loose_reward", 3);
        rewardForKill = config.getInt("rewards.kill_reward", 5);
        bonus = config.getInt("rewards.bonus", 5);
        killstreakLength = config.getInt("killstreak_length", 3);

        saveConfig();
    }

    public void saveConfig() {

        //Saving rewards values to config
        config.set("rewards.win_reward", rewardForWin);
        config.set("rewards.loose_reward", rewardForLoose);
        config.set("rewards.kill_reward", rewardForKill);
        config.set("rewards.bonus", bonus);
        config.set("killstreak_length", killstreakLength);

        shootingGames.saveConfig();
    }
}
