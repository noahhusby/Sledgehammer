package com.noahhusby.sledgehammer;

import com.noahhusby.sledgehammer.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

public class SledgehammerUtil {
    public static Player getPlayerFromName(String name) {
        return Bukkit.getServer().getPlayer(name);
    }

    public static boolean compare(String a, String b) {
        if(a == null || b == null) return false;
        return a.trim().toLowerCase().equals(b.trim().toLowerCase());
    }

}
