package com.noahhusby.sledgehammer.utils;

import com.noahhusby.sledgehammer.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

public class Util {
    public static Player getPlayerFromName(String name) {
        return Bukkit.getServer().getPlayer(name);
    }


}
