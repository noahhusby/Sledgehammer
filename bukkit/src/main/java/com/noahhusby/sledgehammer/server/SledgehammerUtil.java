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
import com.noahhusby.sledgehammer.server.util.TerraConnector;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@UtilityClass
public class SledgehammerUtil extends CommonUtil {

    public static final JsonParser parser = new JsonParser();

    @Getter
    private static TerraConnector terraConnector = null;

    /**
     * Checks whether a player is online or not
     *
     * @param name Name of player
     * @return True if online, false if not
     */
    public static boolean isPlayerAvailable(String name) {
        return isPlayerAvailable(Bukkit.getPlayer(name));
    }

    /**
     * Checks whether a player is online or not
     *
     * @param p {@link Player}
     * @return True if online, false if not
     */
    public static boolean isPlayerAvailable(Player p) {
        if (p == null) {
            return false;
        }
        return p.isOnline();
    }

    /**
     * Gets a skull from a base64 texture
     *
     * @param base64 Base64 Texture
     * @param name   Display Name of Skull
     * @return {@link ItemStack}
     */
    public static ItemStack getSkull(String base64, String name) {
        try {
            return setItemDisplayName(SkullUtil.itemFromBase64(base64), name);
        } catch (StringIndexOutOfBoundsException | NullPointerException e) {
            return getSkull(Constants.steveHead, name);
        }
    }

    /**
     * Sets the display name of an item
     *
     * @param item {@link ItemStack}
     * @param name Display name of Item
     * @return {@link ItemStack}
     */
    public static ItemStack setItemDisplayName(@NonNull ItemStack item, @NonNull String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Gets whether TerraPlusPlus is installed
     *
     * @return True if T++ is installed, false if not
     */
    public static boolean hasTerraPlusPlus() {
        return terraConnector != null;
    }

    /**
     * Checks if TerraPlusPlus is installed
     */
    protected static void checkForTerra() {
        try {
            Class.forName("net.buildtheearth.terraplusplus.TerraMod");
            terraConnector = new TerraConnector();
            Sledgehammer.LOGGER.info("TerraPlusPlus is installed. Using terra height mode.");
        } catch (ClassNotFoundException ignored) {
            Sledgehammer.LOGGER.info("TerraPlusPlus is not installed. Using vanilla height mode.");
        }
    }
}
