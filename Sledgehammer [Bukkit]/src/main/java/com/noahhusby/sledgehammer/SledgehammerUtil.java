/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bukkit] - SledgehammerUtil.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.noahhusby.sledgehammer.ConfigHandler;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.json.simple.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public class SledgehammerUtil {
    public static Player getPlayerFromName(String name) {
        return Bukkit.getServer().getPlayer(name);
    }

    public static boolean compare(String a, String b) {
        if(a == null || b == null) return false;
        return a.trim().toLowerCase().equals(b.trim().toLowerCase());
    }

    public static ItemStack getSkull(String h, String n) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        mutateItemMeta(meta, h);
        meta.setDisplayName(n);
        head.setItemMeta(meta);
        return head;
    }

    private static void mutateItemMeta(SkullMeta meta, String b64) {
        Method metaSetProfileMethod = null;
        Field metaProfileField = null;
        try {
            if (metaSetProfileMethod == null) {
                metaSetProfileMethod = meta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
                metaSetProfileMethod.setAccessible(true);
            }
            metaSetProfileMethod.invoke(meta, makeProfile(b64));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ex) {
            // if in an older API where there is no setProfile method,
            // we set the profile field directly.
            try {
                if (metaProfileField == null) {
                    metaProfileField = meta.getClass().getDeclaredField("profile");
                    metaProfileField.setAccessible(true);
                }
                metaProfileField.set(meta, makeProfile(b64));

            } catch (NoSuchFieldException | IllegalAccessException ex2) {
                ex2.printStackTrace();
            }
        }
    }

    private static GameProfile makeProfile(String b64) {
        UUID id = new UUID(
                b64.substring(b64.length() - 20).hashCode(),
                b64.substring(b64.length() - 10).hashCode()
        );
        GameProfile profile = new GameProfile(id, "aaaaa");
        profile.getProperties().put("textures", new Property("textures", b64));
        return profile;
    }


    public static boolean isGenuineRequest(String u) {
        try {
            return u.equals(ConfigHandler.authenticationCode);
        } catch (Exception e) {
            Sledgehammer.logger.info("Error occurred while parsing incoming authentication command!");
            return false;
        }
    }

    public static boolean isPlayerAvailable(String p) {
        return isPlayerAvailable(getPlayerFromName(p));
    }

    public static boolean isPlayerAvailable(Player p) {
        if(p == null) return false;
        return p.isOnline();
    }
}
