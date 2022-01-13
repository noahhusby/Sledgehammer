/*
 * MIT License
 *
 * Copyright 2020-2022 noahhusby
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
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
     * @param urlId Base64 Texture
     * @param name  Display Name of Skull
     * @return {@link ItemStack}
     */
    public static ItemStack getSkull(String urlId, String name) {
        try {
            return setItemDisplayName(SkullUtil.itemWithUrlId(SkullUtil.createSkull(), urlId), name);
        } catch (StringIndexOutOfBoundsException | NullPointerException e) {
            return getSkull(Constants.Heads.steve, name);
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
    public static void checkForTerra() {
        try {
            Class.forName("net.buildtheearth.terraplusplus.TerraMod");
            terraConnector = new TerraConnector();
            Sledgehammer.LOGGER.info("TerraPlusPlus is installed. Using terra height mode.");
        } catch (ClassNotFoundException ignored) {
            Sledgehammer.LOGGER.info("TerraPlusPlus is not installed. Using vanilla height mode.");
        }
    }
}
