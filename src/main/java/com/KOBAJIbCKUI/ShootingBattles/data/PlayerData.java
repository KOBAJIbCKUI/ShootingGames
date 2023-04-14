package com.KOBAJIbCKUI.ShootingBattles.data;

import com.KOBAJIbCKUI.ShootingBattles.battle.ShootingBattle;
import com.KOBAJIbCKUI.ShootingBattles.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

public class PlayerData {

    private final ShootingBattle battle;
    private final double health;
    private final int food;
    private final float saturation;
    private final int expLevel;
    private final float expPoints;
    private final GameMode gamemode;
    private final UUID uuid;
    private final Scoreboard scoreboard;
    private final Location previousLocation;
    private ItemStack[] inventory;
    private ItemStack[] armour;
    private boolean online;

    public PlayerData(Player player, ShootingBattle battle) {

        this.battle = battle;
        this.uuid = player.getUniqueId();
        this.gamemode = player.getGameMode();
        this.food = player.getFoodLevel();
        this.saturation = player.getSaturation();
        this.expLevel = player.getLevel();
        this.expPoints = player.getExp();
        this.health = player.getHealth();
        this.scoreboard = player.getScoreboard();
        this.previousLocation = player.getLocation();
        this.previousLocation.setY(previousLocation.getY() + 1.0);

        player.setLevel(0);
        player.setExp(0);

        online = true;
    }

    public void restore() {
        Player player = getBukkitPlayer();
        if (player == null) {
            return;
        }
        player.setLevel(this.expLevel);
        player.setExp(this.expPoints);
        player.setFoodLevel(this.food);
        player.setSaturation(this.saturation);
        player.setGameMode(this.gamemode);
        player.setHealth(this.health);
        player.setScoreboard(this.scoreboard);

        player.setGlowing(false);
        player.setInvulnerable(true);

        Util.log(Bukkit.getPlayer(uuid).getName() + " restored parameters");
    }

    public void setStoredInventory(ItemStack[] inventory) {
        this.inventory = inventory;
    }

    public ItemStack[] getStoredInventory() {
        return this.inventory;
    }

    public void setStoredArmour(ItemStack[] armour) {
        this.armour = armour;
    }

    public ItemStack[] getStoredArmour() {
        return this.armour;
    }

    public void storeInventory(boolean remove) {
        Player player = Bukkit.getPlayer(this.uuid);
        this.inventory = cloneAndSave(player.getInventory().getStorageContents());
        this.armour = cloneAndSave(player.getInventory().getArmorContents());
        if (remove) {
            player.getInventory().clear();
        }
        Util.log("Armour storage size: " + armour.length);
        Util.log(Bukkit.getPlayer(uuid).getName() + " stored inventory");
    }

    public void restoreInventory(boolean isPlayer) {
        Player player = Bukkit.getPlayer(this.uuid);
        if (!isPlayer) {
            player.getInventory().setStorageContents(this.inventory);
        }
        player.getInventory().setArmorContents(this.armour);

        Util.log(Bukkit.getPlayer(uuid).getName() + " restored inventory");
    }

    public void restoreArmour() {
        Player player = Bukkit.getPlayer(uuid);
        player.getInventory().setArmorContents(this.armour);
        Util.log(Bukkit.getPlayer(uuid).getName() + " restored armour");
    }

    private ItemStack[] cloneAndSave(ItemStack[] original) {
        ItemStack[] copy = new ItemStack[original.length];
        for (int i = 0; i < original.length; i++) {
            if (original[i] != null) {
                copy[i] = original[i].clone();
            } else {
                copy[i] = original[i];
            }
        }
        return copy;
    }

    public ShootingBattle getBattle() {
        return battle;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Location getPreviousLocation() {
        return previousLocation;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public Player getBukkitPlayer() {
        return Bukkit.getPlayer(this.uuid);
    }
}
