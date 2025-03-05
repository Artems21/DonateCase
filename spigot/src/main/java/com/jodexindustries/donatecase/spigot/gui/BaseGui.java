package com.jodexindustries.donatecase.spigot.gui;

import com.jodexindustries.donatecase.spigot.BukkitBackend;
import org.bukkit.entity.Player;

public abstract class BaseGui implements GuiExecute {
    private final BukkitBackend backend;
    private  Player player;

    public BaseGui(BukkitBackend backend, Player player) {
        this.backend = backend;
        this.player = player;
    }



}
