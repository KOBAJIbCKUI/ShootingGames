package com.KOBAJIbCKUI.ShootingGames;

import java.util.ArrayList;
import java.util.List;

public class PlayerGlower implements Runnable {
    public static final List<Cube> cubesList = new ArrayList<>();

    @Override
    public void run() {
        for (Cube cube : cubesList) {
            if (cube.isInnerPlayerInCube()) {
                if (!cube.getPlayer().isGlowing()) {
                    cube.getPlayer().setGlowing(true);
                }
            } else  {
                cube.changeCubeCoords();
                if (cube.getPlayer().isGlowing()) {
                    cube.getPlayer().setGlowing(false);
                }
            }
        }
    }
}
