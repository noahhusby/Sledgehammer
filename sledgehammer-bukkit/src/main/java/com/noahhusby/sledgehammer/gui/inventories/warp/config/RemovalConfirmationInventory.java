package com.noahhusby.sledgehammer.gui.inventories.warp.config;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.gui.inventories.general.GUIChild;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class RemovalConfirmationInventory extends GUIChild {
    private final String warp;
    public RemovalConfirmationInventory(String warp) {
        this.warp = warp;
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {

    }

    @Override
    public void init() {
        for(int x = 0; x < 27; x++) {
            ItemStack skull = SledgehammerUtil.getSkull(Constants.redCheckmarkHead, ChatColor.YELLOW + "" + ChatColor.BOLD + warp);

            ItemMeta meta = skull.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.RED + "Warp Created");
            meta.setLore(lore);

            skull.setItemMeta(meta);

            inventory.setItem(x, skull);
        }

    }
}
