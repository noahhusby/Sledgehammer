package com.noahhusby.sledgehammer.server.gui.inventories.warp.config.confirmation;

import com.noahhusby.sledgehammer.server.Constants;
import com.noahhusby.sledgehammer.server.SledgehammerUtil;
import com.noahhusby.sledgehammer.server.data.warp.WarpConfigPayload;
import com.noahhusby.sledgehammer.server.gui.inventories.general.GUIChild;
import com.noahhusby.sledgehammer.server.gui.inventories.general.GUIRegistry;
import com.noahhusby.sledgehammer.server.gui.inventories.warp.config.ConfigMenuController;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class RemoveSuccessInventory extends GUIChild {
    private final WarpConfigPayload payload;
    public RemoveSuccessInventory(WarpConfigPayload payload) {
        this.payload = payload;
    }

    @Override
    public void init() {
        for(int x = 0; x < 27; x++) {
            ItemStack skull = SledgehammerUtil.getSkull(Constants.redCheckmarkHead, ChatColor.GREEN + "" + ChatColor.BOLD +
                    "Successfully removed warp!");

            ItemMeta meta = skull.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.BLUE + "Click to continue");
            meta.setLore(lore);

            skull.setItemMeta(meta);

            inventory.setItem(x, skull);
        }
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
        GUIRegistry.register(new ConfigMenuController(getPlayer(), payload));
    }
}
