package com.KOBAJIbCKUI.ShootingBattles.buffers;

import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class Sonar implements Runnable {
    private static final float ANGLE45 = (float) Math.PI / 4;
    private static final float ANGLE135 = (float) Math.PI / 2 + ANGLE45;
    private static final float ANGLE180 = (float) Math.PI;
    private static final float ANGLE360 = (float) Math.PI * 2;

    private static final String FRONT = "" + ChatColor.RED + ChatColor.BOLD + "%s - " + ChatColor.AQUA + "in front";
    private static final String RIGHT = "" + ChatColor.RED + ChatColor.BOLD + "%s - " + ChatColor.AQUA + "right";
    private static final String BEHIND = "" + ChatColor.RED + ChatColor.BOLD + "%s - " + ChatColor.AQUA + "behind";
    private static final String LEFT = "" + ChatColor.RED + ChatColor.BOLD + "%s - " + ChatColor.AQUA + "left";
    private static final String NOBODY_NEAR = "" + ChatColor.AQUA + ChatColor.BOLD + "Nobody near";

    public static Map<Player, Sonar> sonars = new HashMap<>();

    private final ShootingGames shootingGames;
    private final Player sonaringPlayer;
    private final int id;
    private final BossBar bar;

    public Sonar(Player sonaringPlayer) {
        this.shootingGames = ShootingGames.getPlugin();
        this.sonaringPlayer = sonaringPlayer;
        this.bar = Bukkit.createBossBar("", BarColor.WHITE, BarStyle.SOLID);

        this.bar.addPlayer(sonaringPlayer);

        this.id = Bukkit.getScheduler().scheduleSyncRepeatingTask(shootingGames, this, 0, 10);
    }

    @Override
    public void run() {
        Location sonaringPlayerLocation = sonaringPlayer.getEyeLocation();
        Location sonarablePlayerLocation;

        Vector sonaringPlayerDirection = sonaringPlayerLocation.getDirection();
        Vector toSonarableDirection = null;

        sonaringPlayerDirection.setY(0);
        sonaringPlayerDirection.normalize();

        StringBuilder resultString = new StringBuilder();

        float currentAngle;
        float sonaringDirAngle;
        float toSonarableDirAngle;

        for (Player player : Bukkit.getOnlinePlayers()) {

            if (player == sonaringPlayer || player.getGameMode() != GameMode.SURVIVAL) {
                continue;
            }

            sonarablePlayerLocation = player.getLocation();

            if (Math.abs(Math.abs(sonaringPlayerLocation.getX()) - Math.abs(sonarablePlayerLocation.getX())) <= 10 &&
                    Math.abs(Math.abs(sonaringPlayerLocation.getZ()) - Math.abs(sonarablePlayerLocation.getZ())) <= 10) {

                toSonarableDirection = new Vector(sonarablePlayerLocation.getX() - sonaringPlayerLocation.getX(), 0, sonarablePlayerLocation.getZ() - sonaringPlayerLocation.getZ()).normalize();
                sonaringDirAngle = (float) Math.atan2(sonaringPlayerDirection.getZ(), sonaringPlayerDirection.getX());
                toSonarableDirAngle = (float) Math.atan2(toSonarableDirection.getZ(), toSonarableDirection.getX());

                currentAngle = sonaringDirAngle - toSonarableDirAngle;
                if (currentAngle > Math.PI) {
                    currentAngle -= ANGLE360;
                } else if (currentAngle <= -Math.PI) {
                    currentAngle += ANGLE360;
                }

                if (currentAngle >= -ANGLE45 && currentAngle <= ANGLE45) {
                    resultString.append(String.format(FRONT, player.getName())).append(" ");
                    //Util.sendMessage(sonaringPlayer, String.format(FRONT, player));
                } else if (currentAngle < -ANGLE45 && currentAngle > -ANGLE135) {
                    resultString.append(String.format(RIGHT, player.getName())).append(" ");
                    //Util.sendMessage(sonaringPlayer, String.format(RIGHT, player));
                } else if ((currentAngle < -ANGLE135 && currentAngle >= -ANGLE180) || (currentAngle > ANGLE135 && currentAngle <= ANGLE180)) {
                    resultString.append(String.format(BEHIND, player.getName())).append(" ");
                    //Util.sendMessage(sonaringPlayer, String.format(BEHIND, player));
                } else if (currentAngle > ANGLE45 && currentAngle < ANGLE135) {
                    resultString.append(String.format(LEFT, player.getName())).append(" ");
                    //Util.sendMessage(sonaringPlayer, String.format(LEFT, player));
                }

                //sonaringPlayer.sendMessage("Angle " + Math.toDegrees(currentAngle));
            }
        }
        if (toSonarableDirection == null) {
            resultString.append(NOBODY_NEAR);
            //sonaringPlayer.sendMessage(NOBODY_NEAR);
        }
        this.bar.setTitle(resultString.toString());
    }

    public void stop() {
        this.bar.removeAll();
        Bukkit.getScheduler().cancelTask(id);
    }
}
