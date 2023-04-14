package com.KOBAJIbCKUI.ShootingBattles.buffers;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerHealer implements Runnable {

    Player healingPlayer;
    public static Map<Player, Integer> players = new HashMap<>();

    public PlayerHealer(Player healingPlayer) {
        this.healingPlayer = healingPlayer;
    }

    @Override
    public void run() {
        double health = healingPlayer.getHealth();
        if (health >= 20) {
            return;
        }
        if (health <= 16) {
            healingPlayer.setHealth(health + 4);
        } else if (health > 16) {
            healingPlayer.setHealth(health + (20 - health));
        }
    }
}
