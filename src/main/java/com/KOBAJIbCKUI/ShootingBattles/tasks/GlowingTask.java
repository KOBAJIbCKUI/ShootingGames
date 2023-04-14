package com.KOBAJIbCKUI.ShootingBattles.tasks;

import com.KOBAJIbCKUI.ShootingBattles.ShootingGames;
import com.KOBAJIbCKUI.ShootingBattles.battle.ShootingBattle;
import com.KOBAJIbCKUI.ShootingBattles.util.Cube;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class GlowingTask implements Runnable {
    private List<Cube> cubesList = new ArrayList<>();

    private final ShootingBattle shootingBattle;
    private final int id;
    private final int glowPeriod = 30;

    public GlowingTask(ShootingBattle shootingBattle) {
        this.shootingBattle = shootingBattle;
        for (UUID uuid : shootingBattle.getBattlePlayerData().getPlayers()) {
            cubesList.add(new Cube(Bukkit.getPlayer(uuid)));
        }
        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(ShootingGames.getPlugin(), this, glowPeriod * 20L, glowPeriod * 20L);
    }

    public void removeCube(Player player) {
        removeCube(player.getUniqueId());
    }

    public void removeCube(UUID uuid) {
        Cube cube;
        for (Iterator<Cube> it = cubesList.iterator(); it.hasNext();) {
            cube = it.next();
            if (cube.getPlayer().getUniqueId().equals(uuid)) {
                setGlowing(cube.getPlayer(), false);
                it.remove();
                break;
            }
        }
    }

    public void clearCubes() {
        cubesList.forEach(c -> setGlowing(c.getPlayer(), false));
        cubesList.clear();
    }

    private void setGlowing(Player player, boolean glowing) {
        player.setGlowing(glowing);
    }

    @Override
    public void run() {
            for (Cube cube : cubesList) {
                setGlowing(cube.getPlayer(), cube.isInnerPlayerInCube());
                cube.changeCubeCoords();
            }
    }

    public void stop() {
        clearCubes();
        Bukkit.getScheduler().cancelTask(id);
    }
}
