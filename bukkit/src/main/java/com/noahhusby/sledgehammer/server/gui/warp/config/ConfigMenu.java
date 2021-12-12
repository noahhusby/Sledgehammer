/*
 * MIT License
 *
 * Copyright 2020-2021 noahhusby
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

package com.noahhusby.sledgehammer.server.gui.warp.config;

import com.noahhusby.sledgehammer.common.warps.WarpConfigPayload;
import com.noahhusby.sledgehammer.server.Constants;
import com.noahhusby.sledgehammer.server.SledgehammerUtil;
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
            GUIRegistry.register(new ManageServerViewInventory.ManageGroupInventoryController(getPlayer(), ((ConfigMenuController) controller).getPayload()));
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
