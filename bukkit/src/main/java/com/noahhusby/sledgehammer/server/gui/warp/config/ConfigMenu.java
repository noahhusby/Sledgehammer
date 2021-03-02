package com.noahhusby.sledgehammer.server.gui.warp.config;

import com.noahhusby.sledgehammer.server.Constants;
import com.noahhusby.sledgehammer.server.SledgehammerUtil;
import com.noahhusby.sledgehammer.server.data.warp.WarpConfigPayload;
import com.noahhusby.sledgehammer.server.gui.GUIChild;
import com.noahhusby.sledgehammer.server.gui.GUIController;
import com.noahhusby.sledgehammer.server.gui.GUIRegistry;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ConfigMenu extends GUIChild {

    public ConfigMenu() {
    }

    @Override
    public void init() {
        fillInventory(createItem(Material.STAINED_GLASS_PANE, 1, (byte) 15, null));
        inventory.setItem(13, SledgehammerUtil.getSkull(Constants.limePlusHead, ChatColor.GREEN + "" + ChatColor.BOLD + "Create new warp"));
        inventory.setItem(15, SledgehammerUtil.getSkull(Constants.goldenExclamationHead, ChatColor.GOLD + "" + ChatColor.BOLD + "Manage Warps"));
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if (e.getCurrentItem() == null) {
            return;
        }
        if (e.getCurrentItem().getItemMeta() == null) {
            return;
        }
        if (e.getCurrentItem().getItemMeta().getDisplayName() == null) {
            return;
        }
        if (e.getSlot() == 13) {
            GUIRegistry.register(new WarpNameEntryAnvil.WarpNameEntryController(getPlayer(), ((ConfigMenuController) controller).getPayload()));
            return;
        }
        if (e.getSlot() == 15) {
            GUIRegistry.register(new ManageGroupInventory.ManageGroupInventoryController(getPlayer(), ((ConfigMenuController) controller).getPayload()));
            return;
        }
    }

    public static class ConfigMenuController extends GUIController {

        private final WarpConfigPayload payload;

        public ConfigMenuController(Player p, WarpConfigPayload payload) {
            super(27, "Warp Config", p);
            this.payload = payload;
            init();
        }

        @Override
        public void init() {
            ConfigMenu warpConfig = new ConfigMenu();
            warpConfig.initFromController(this, getPlayer(), getInventory());
            openChild(warpConfig);
        }

        public WarpConfigPayload getPayload() {
            return payload;
        }
    }
}
