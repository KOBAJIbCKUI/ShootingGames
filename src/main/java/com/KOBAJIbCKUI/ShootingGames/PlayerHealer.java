package com.KOBAJIbCKUI.ShootingGames;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
        healingPlayer.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 1, 1));
    }
}
