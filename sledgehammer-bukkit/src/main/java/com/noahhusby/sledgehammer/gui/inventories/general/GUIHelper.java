package com.noahhusby.sledgehammer.gui.inventories.general;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GUIHelper {
    public static ItemStack generateExit() {
        ItemStack exit = new ItemStack(Material.BARRIER);
        ItemMeta m = exit.getItemMeta();
        m.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Close");
        exit.setItemMeta(m);

        return exit;
    }

    public static ItemStack generateCompass() {
        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta m = compass.getItemMeta();

        m.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Server Warps");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Sort warps by server");
        m.setLore(lore);

        compass.setItemMeta(m);

        return compass;
    }
}
