package com.KOBAJIbCKUI.ShootingGames.lobby;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "lobbies_list")
public class LobbiesListWrapper implements Serializable {

    @XmlElement(name = "lobby")
    public List<Lobby> lobbies = new ArrayList<>();
}
