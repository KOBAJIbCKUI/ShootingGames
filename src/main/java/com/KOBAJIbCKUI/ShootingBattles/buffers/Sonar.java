package com.KOBAJIbCKUI.ShootingBattles.buffers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class Sonar implements Runnable {
    private final Player sonaringPlayer;
    private static final float ANGLE45 = (float) Math.PI / 4;
    private static final float ANGLE135 = (float) Math.PI / 2 + ANGLE45;
    private static final float ANGLE225 = (float) Math.PI / 2 + ANGLE135;
    private static final float ANGLE315 = (float) Math.PI / 2 + ANGLE225;
    private static final float ANGLE360 = (float) Math.PI * 2;

    private static final String FRONT = "" + ChatColor.RED + ChatColor.BOLD + "%s - " + ChatColor.AQUA + "in front";
    private static final String RIGHT = "" + ChatColor.RED + ChatColor.BOLD + "%s - " + ChatColor.AQUA + "right";
    private static final String BEHIND = "" + ChatColor.RED + ChatColor.BOLD + "%s - " + ChatColor.AQUA + "behind";
    private static final String LEFT = "" + ChatColor.RED + ChatColor.BOLD + "%s - " + ChatColor.AQUA + "left";
    private static final String NOBODY_NEAR = "" + ChatColor.AQUA + ChatColor.BOLD + "Nobody near";

    public static Map<Player, Integer> players = new HashMap<>();

    public Sonar(Player sonaringPlayer) {
        this.sonaringPlayer = sonaringPlayer;
    }

    @Override
    public void run() {
        Location sonarablePlayerLocation, sonaringPlayerLocation = sonaringPlayer.getLocation();
        sonaringPlayerLocation.setY(0.0);

        Vector sonaringPlayerDirection = sonaringPlayerLocation.getDirection(), toSonarableDirection = null;

        float currentAngle;

        for (Player player : Bukkit.getOnlinePlayers()) {

            if (player == sonaringPlayer || player.getGameMode() != GameMode.SURVIVAL) {
                continue;
            }

            sonarablePlayerLocation = player.getLocation();

            if (Math.abs(sonaringPlayerLocation.getX()) - Math.abs(sonarablePlayerLocation.getX()) <= 10 &&
                    Math.abs(sonaringPlayerLocation.getZ()) - Math.abs(sonarablePlayerLocation.getZ()) <= 10) {

                sonarablePlayerLocation.setY(0.0);
                toSonarableDirection = sonarablePlayerLocation.toVector();

                if ((sonaringPlayerDirection.getX() < 0 && toSonarableDirection.getX() > 0) || (sonaringPlayerDirection.getX() > 0 && toSonarableDirection.getX() < 0)) {
                    currentAngle = ANGLE360 - sonaringPlayerDirection.angle(toSonarableDirection);
                } else {
                    currentAngle = sonaringPlayerDirection.angle(toSonarableDirection);
                }

                if (currentAngle > ANGLE315 || currentAngle <= ANGLE45) {
                    sonaringPlayer.sendMessage(String.format(FRONT, player.getDisplayName()));
                } else if (currentAngle > ANGLE45 && currentAngle <= ANGLE135) {
                    sonaringPlayer.sendMessage(String.format(RIGHT, player.getDisplayName()));
                } else if (currentAngle > ANGLE135 && currentAngle <= ANGLE225) {
                    sonaringPlayer.sendMessage(String.format(BEHIND, player.getDisplayName()));
                } else if (currentAngle > ANGLE225) {
                    sonaringPlayer.sendMessage(String.format(LEFT, player.getDisplayName()));
                }
            }
        }
        if (toSonarableDirection == null) {
            sonaringPlayer.sendMessage(NOBODY_NEAR);
        }
    }
}
