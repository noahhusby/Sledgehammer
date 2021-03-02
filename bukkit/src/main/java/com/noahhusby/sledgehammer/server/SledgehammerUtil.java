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

package com.noahhusby.sledgehammer.server;

import com.google.gson.JsonParser;
import com.noahhusby.sledgehammer.common.CommonUtil;
import com.noahhusby.sledgehammer.server.util.SkullUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import net.buildtheearth.terraplusplus.generator.EarthGeneratorSettings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@UtilityClass
public class SledgehammerUtil extends CommonUtil {

    public static final JsonParser parser = new JsonParser();
    private static final EarthGeneratorSettings bteGeneratorSettings = EarthGeneratorSettings.parse(EarthGeneratorSettings.BTE_DEFAULT_SETTINGS);

    public static boolean hasTerraPlusPlus = false;

    public static EarthGeneratorSettings getBTEDefaultSettings() {
        return bteGeneratorSettings;
    }

    public static Player getPlayerFromName(String name) {
        return Bukkit.getServer().getPlayer(name);
    }

    public static ItemStack getSkull(String base64, String name) {
        try {
            return setItemDisplayName(SkullUtil.itemFromBase64(base64), name);
        } catch (StringIndexOutOfBoundsException | NullPointerException e){
            return getSkull(Constants.steveHead, name);
        }
    }

    public static ItemStack setItemDisplayName(@NonNull ItemStack item, @NonNull String name) {
        item.getItemMeta().setDisplayName(name);
        return item;
    }

    public static boolean isPlayerAvailable(String p) {
        return isPlayerAvailable(getPlayerFromName(p));
    }

    public static boolean isPlayerAvailable(Player p) {
        if(p == null) return false;
        return p.isOnline();
    }

    protected static void checkForTerra() {
        try {
            Class.forName("net.buildtheearth.terraplusplus.TerraMod");
            hasTerraPlusPlus = true;
            Sledgehammer.LOGGER.info("TerraPlusPlus is installed. Using terra height mode.");
        } catch(ClassNotFoundException ignored) {
            Sledgehammer.LOGGER.info("TerraPlusPlus is not installed. Using vanilla height mode.");
        }
    }
}
