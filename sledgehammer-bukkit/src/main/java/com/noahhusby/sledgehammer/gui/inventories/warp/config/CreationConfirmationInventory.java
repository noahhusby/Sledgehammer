package com.noahhusby.sledgehammer.gui.inventories.warp.config;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.gui.inventories.general.GUIChild;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CreationConfirmationInventory extends GUIChild {
    private final String warp;
    public CreationConfirmationInventory(String warp) {
        this.warp = warp;
    }

    @Override
    public void init() {
        for(int x = 0; x < 27; x++) {
            ItemStack skull = SledgehammerUtil.getSkull(Constants.limeCheckmarkHead, ChatColor.YELLOW + "" + ChatColor.BOLD + warp);

            ItemMeta meta = skull.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GREEN + "Warp Created");
            lore.add(ChatColor.BLUE + "Click to continue");
            meta.setLore(lore);

            skull.setItemMeta(meta);

            inventory.setItem(x, skull);
        }
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
        getPlayer().closeInventory();
    }
}
