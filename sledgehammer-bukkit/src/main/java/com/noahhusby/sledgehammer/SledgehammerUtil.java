/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - SledgehammerUtil.java
 *
 * Sledgehammer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sledgehammer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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

    public static boolean isPlayerAvailable(String p) {
        return isPlayerAvailable(getPlayerFromName(p));
    }

    public static boolean isPlayerAvailable(Player p) {
        if(p == null) return false;
        return p.isOnline();
    }

    public static class JsonUtils {
        public static JSONObject toObject(String s) {
            try {
                return (JSONObject) new JSONParser().parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static JSONArray toArray(Object o) {
            return toArray((String) o);
        }

        public static JSONArray toArray(String s) {
            try {
                return (JSONArray) new JSONParser().parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static int fromBoolean(boolean b) {
            return b ? 1 : 0;
        }

        public static boolean fromBooleanValue(long l) {
            return new Long(l).intValue() != 0;
        }

        public static int toInt(Object val) {
            int x = 0;
            if(val instanceof Long) {
                x = ((Long) val).intValue();
            } else {
                x = (int) val;
            }
            return x;
        }
    }
}
