package com.noahhusby.sledgehammer.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class Util {
    public static Player getPlayerFromName(String name) {
        return Bukkit.getServer().getPlayer(name);
    }
}
