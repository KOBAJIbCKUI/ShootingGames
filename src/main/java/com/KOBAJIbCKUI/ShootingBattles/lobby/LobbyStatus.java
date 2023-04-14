package com.KOBAJIbCKUI.ShootingBattles.lobby;

import com.KOBAJIbCKUI.ShootingBattles.data.Messages;

public enum LobbyStatus {
    READY,
    NOTREADY,
    COUNTDOWN,
    RUNNING,
    GULAG;

    public String getName() {
        switch (this) {
            case READY:
                return Messages.LOBBY_STATUS_READY;
            case NOTREADY:
                return Messages.LOBBY_STATUS_NOTREADY;
            case COUNTDOWN:
                return Messages.LOBBY_STATUS_COUNTDOWN;
            case RUNNING:
                return Messages.LOBBY_STATUS_RUNNING;
            case GULAG:
                return Messages.LOBBY_STATUS_GULAG;
            default:
                return "ERROR!";
        }
    }
}
