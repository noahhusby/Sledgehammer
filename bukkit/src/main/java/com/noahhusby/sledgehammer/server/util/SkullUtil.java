package com.noahhusby.sledgehammer.server.util;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

/**
 * A library for the Bukkit API to create player skulls
 * from names, base64 strings, and texture URLs.
 * <p>
 * Does not use any NMS code, and should work across all versions.
 *
 * @author Dean B on 12/28/2016.
 */
@UtilityClass
public class SkullUtil {

    private static Method metaSetProfileMethod;
    private static Field metaProfileField;

    private static final Map<Integer, ItemStack> numberHeads = Maps.newHashMap();

    static {
        try {
            Material.class.getDeclaredField("PLAYER_HEAD");
            Material.valueOf("SKULL");
        } catch (NoSuchFieldException | IllegalArgumentException ignored) {
        }

        numberHeads.put(0, itemFromTextureId("3f09018f46f349e553446946a38649fcfcf9fdfd62916aec33ebca96bb21b5"));
        numberHeads.put(1, itemFromTextureId("ca516fbae16058f251aef9a68d3078549f48f6d5b683f19cf5a1745217d72cc"));
        numberHeads.put(2, itemFromTextureId("4698add39cf9e4ea92d42fadefdec3be8a7dafa11fb359de752e9f54aecedc9a"));
        numberHeads.put(3, itemFromTextureId("fd9e4cd5e1b9f3c8d6ca5a1bf45d86edd1d51e535dbf855fe9d2f5d4cffcd2"));
        numberHeads.put(4, itemFromTextureId("f2a3d53898141c58d5acbcfc87469a87d48c5c1fc82fb4e72f7015a3648058"));
        numberHeads.put(5, itemFromTextureId("d1fe36c4104247c87ebfd358ae6ca7809b61affd6245fa984069275d1cba763"));
        numberHeads.put(6, itemFromTextureId("3ab4da2358b7b0e8980d03bdb64399efb4418763aaf89afb0434535637f0a1"));
        numberHeads.put(7, itemFromTextureId("297712ba32496c9e82b20cc7d16e168b035b6f89f3df014324e4d7c365db3fb"));
        numberHeads.put(8, itemFromTextureId("abc0fda9fa1d9847a3b146454ad6737ad1be48bdaa94324426eca0918512d"));
        numberHeads.put(9, itemFromTextureId("d6abc61dcaefbd52d9689c0697c24c7ec4bc1afb56b8b3755e6154b24a5d8ba"));
        numberHeads.put(10, itemFromTextureId("7af3fd473a648b847ccda1d2074479bb7672771dc435223468ed9ff7b76cb3"));
        numberHeads.put(11, itemFromTextureId("48cab53b02098e681a46d1d7f5ff691746adf4e1fb3afe3516dd2af944569"));
        numberHeads.put(12, itemFromTextureId("bfd83b5baae4cb85694a14d6d13341ef71aa3d92d37de07bea77b2c9dc53e"));
        numberHeads.put(13, itemFromTextureId("b1e5985be4886f9f16e2447c3f461053b451342d4fb0166fb2f88df7422136b4"));
        numberHeads.put(14, itemFromTextureId("1681456438ae9b2d4d2bfab9cf3ffa9354eebdb3f02ce2957929348e5b85ff95"));
        numberHeads.put(15, itemFromTextureId("719c4db7365b1b88b129e704184213fe078d88bc3d4ae3d52290f61d955d51"));
        numberHeads.put(16, itemFromTextureId("9f5dd079b98fdac43a19a795ba46fd97f23ea7757d92ad0a69adc973289e5a"));
        numberHeads.put(17, itemFromTextureId("fe51bfe8ebfaa585a787e1cb772c7fd7d9a9286d95efa54d66fa8274f188f"));
        numberHeads.put(18, itemFromTextureId("b6de9a5a2d8a23707019b9ef61d1662860e0b1653df6c27616be2c76fcd1875"));
        numberHeads.put(19, itemFromTextureId("dbd45904d34b636b2f64261b3d8bced25828c2b8c4823b7e183ee8a6f1a284d"));
        numberHeads.put(20, itemFromTextureId("74b93c721519e149649b7e4afb6d76cfc814608ae9c95e7c3db4bf4bdaacf31e"));
    }

    /**
     * Creates a player skull, should work in both legacy and new Bukkit APIs.
     */
    public static ItemStack createSkull() {
        try {
            return new ItemStack(Material.valueOf("PLAYER_HEAD"));
        } catch (IllegalArgumentException e) {
            return new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (byte) 3);
        }
    }

    /**
     * Creates a player skull item with the skin based on a player's UUID.
     *
     * @param id The Player's UUID.
     * @return The head of the Player.
     */
    public static ItemStack itemFromUuid(UUID id) {
        return itemWithUuid(createSkull(), id);
    }

    /**
     * Creates a player skull item with the skin at a Mojang URL.
     *
     * @param url The Mojang URL.
     * @return The head of the Player.
     */
    public static ItemStack itemFromUrl(String url) {
        return itemWithUrl(createSkull(), url);
    }

    /**
     * Creates a player skull item with the skin based on a base64 string.
     *
     * @param base64 The Mojang URL.
     * @return The head of the Player.
     */
    public static ItemStack itemFromBase64(String base64) {
        return itemWithBase64(createSkull(), base64);
    }

    /**
     * Creates a player skull item based on the number index
     *
     * @param index Number Head 0 - 20
     * @return {@link ItemStack}
     */
    public static ItemStack itemFromNumber(int index) {
        return numberHeads.get(index);
    }

    /**
     * Modifies a skull to use the skin of the player with a given UUID.
     *
     * @param item The item to apply the name to. Must be a player skull.
     * @param id   The Player's UUID.
     * @return The head of the Player.
     */
    public static ItemStack itemWithUuid(@NonNull ItemStack item, @NonNull UUID id) {
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(id));
        item.setItemMeta(meta);

        return item;
    }

    /**
     * Modifies a skull to use the skin at the given Mojang URL.
     *
     * @param item The item to apply the skin to. Must be a player skull.
     * @param url  The URL of the Mojang skin.
     * @return The head associated with the URL.
     */
    public static ItemStack itemWithUrl(@NonNull ItemStack item, @NonNull String url) {
        return itemWithBase64(item, urlToBase64(url));
    }

    /**
     * Modifies a skull to use the skin based on the given base64 string.
     *
     * @param item   The ItemStack to put the base64 onto. Must be a player skull.
     * @param base64 The base64 string containing the texture.
     * @return The head with a custom texture.
     */
    public static ItemStack itemWithBase64(@NonNull ItemStack item, @NonNull String base64) {
        if (!(item.getItemMeta() instanceof SkullMeta)) {
            return null;
        }
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        mutateItemMeta(meta, base64);
        item.setItemMeta(meta);

        return item;
    }

    private static String urlToBase64(String url) {

        URI actualUrl;
        try {
            actualUrl = new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        String toEncode = "{\"textures\":{\"SKIN\":{\"url\":\"" + actualUrl.toString() + "\"}}}";
        return Base64.getEncoder().encodeToString(toEncode.getBytes());
    }

    private static GameProfile makeProfile(String b64) {
        // random uuid based on the b64 string
        UUID id = new UUID(
                b64.substring(b64.length() - 20).hashCode(),
                b64.substring(b64.length() - 10).hashCode()
        );
        GameProfile profile = new GameProfile(id, "aaaaa");
        profile.getProperties().put("textures", new Property("textures", b64));
        return profile;
    }

    private static void mutateItemMeta(SkullMeta meta, String b64) {
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

    private static ItemStack itemFromTextureId(String textureId) {
        return itemFromUrl("http://textures.minecraft.net/texture/" + textureId);
    }
}
