/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - ServerWarpInventory.java
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

package com.noahhusby.sledgehammer.server.gui.warp.config;

import com.noahhusby.sledgehammer.common.warps.Warp;
import com.noahhusby.sledgehammer.common.warps.WarpConfigPayload;
import com.noahhusby.sledgehammer.common.warps.WarpGroupPayload;
import com.noahhusby.sledgehammer.server.Constants;
import com.noahhusby.sledgehammer.server.SledgehammerUtil;
import com.noahhusby.sledgehammer.server.gui.GUIChild;
import com.noahhusby.sledgehammer.server.gui.GUIController;
import com.noahhusby.sledgehammer.server.gui.GUIRegistry;
import com.noahhusby.sledgehammer.server.gui.warp.config.manage.ManageWarpInventory;
import com.noahhusby.sledgehammer.server.util.SkullUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ManageWarpViewInventory extends GUIChild {
    private final int page;
    private final List<Warp> warps;

    @Override
    public void init() {
        for (int x = 0; x < 45; x++) {
            ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);

            ItemMeta meta = glass.getItemMeta();
            meta.setDisplayName(ChatColor.RESET + "");
            meta.setDisplayName(null);
            glass.setItemMeta(meta);

            inventory.setItem(x, glass);
        }

        inventory.setItem(0, SledgehammerUtil.getSkull(Constants.redLeftHead, ChatColor.RED + "" + ChatColor.BOLD + "Go Back"));

        {
            inventory.setItem(4, SledgehammerUtil.getSkull(Constants.globeHead, ChatColor.GREEN + "" + ChatColor.BOLD + "Select a warp"));
        }

        boolean paged = false;
        if (page != 0) {
            ItemStack head = SledgehammerUtil.getSkull(Constants.arrowLeftHead, ChatColor.AQUA + "" + ChatColor.BOLD + "Previous Page");
            inventory.setItem(42, head);
            paged = true;
        }

        if (warps.size() > (page + 1) * Constants.warpsPerPage) {
            ItemStack head = SledgehammerUtil.getSkull(Constants.arrowRightHead, ChatColor.AQUA + "" + ChatColor.BOLD + "Next Page");
            inventory.setItem(44, head);
            paged = true;
        }

        if (paged) {
            setItem(43, SledgehammerUtil.setItemDisplayName(SkullUtil.itemFromNumber(page + 1), ChatColor.GREEN + "" + ChatColor.BOLD + "Page " + (page + 1)));
        }

        int min = page * 27;
        int max = min + 27;

        if (max > warps.size()) {
            max = min + (warps.size() - (page * 27));
        }

        int current = 9;
        for (int x = min; x < max; x++) {
            Warp warp = warps.get(x);

            ItemStack item = SledgehammerUtil.getSkull(Constants.cyanWoolHead, ChatColor.BLUE + "" + ChatColor.BOLD + warp.getName());

            ItemMeta meta = item.getItemMeta();

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.BLUE + "" + ChatColor.STRIKETHROUGH + "------------------");
            lore.add(ChatColor.DARK_GRAY + "Server: " + warp.getServer());
            lore.add(ChatColor.DARK_GRAY + "> " + ChatColor.GREEN + "Click to edit.");
            lore.add(ChatColor.BLUE + "" + ChatColor.STRIKETHROUGH + "------------------");
            lore.add(ChatColor.GRAY + "ID: " + warp.getId());
            meta.setLore(lore);
            item.setItemMeta(meta);

            inventory.setItem(current, item);
            current++;
        }
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

        ManageWarpViewInventoryController controller = (ManageWarpViewInventoryController) getController();

        if (e.getCurrentItem().getItemMeta().getDisplayName() == null) {
            return;
        }

        if(e.getSlot() == 0) {
            controller.close();
            GUIRegistry.register(new ConfigMenu.ConfigMenuController(getPlayer(), ((ManageWarpViewInventoryController) getController()).getPayload()));
            return;
        }

        if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Previous Page")) {
            controller.openChild(controller.getChildByPage(page - 1));
            return;
        }

        if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Next Page")) {
            controller.openChild(controller.getChildByPage(page + 1));
            return;
        }

        if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Close")) {
            controller.close();
            return;
        }

        if (e.getSlot() > 8 && e.getSlot() < 36) {
            ItemMeta meta = e.getCurrentItem().getItemMeta();
            int id = -1;
            List<String> lore = meta.getLore();
            for (String s : lore) {
                if (s.contains("ID:")) {
                    id = ((Long) Long.parseLong(ChatColor.stripColor(s).replaceAll("[^\\d.]", ""))).intValue();
                }
            }

            controller.close();
            GUIRegistry.register(new ManageWarpInventory.ManageWarpInventoryController(getPlayer(), controller.getPayload(), id));
        }
    }

    public int getPage() {
        return page;
    }

    public static class ManageWarpViewInventoryController extends GUIController {
        private final List<ManageWarpViewInventory> warpInventories = new ArrayList<>();
        private final WarpConfigPayload payload;
        private final List<Warp> warps;

        public ManageWarpViewInventoryController(Player p, WarpConfigPayload payload, List<Warp> warps) {
            super(45, "Select a warp to manage", p);
            this.payload = payload;
            this.warps = warps;
            init();
        }

        public ManageWarpViewInventoryController(GUIController controller, WarpConfigPayload payload, List<Warp> warps) {
            super(controller);
            this.payload = payload;
            this.warps = warps;
            init();
        }

        @Override
        public void init() {
            int total_pages = (int) Math.ceil(warps.size() / 27.0);
            if (total_pages == 0) {
                total_pages = 1;
            }
            for (int x = 0; x < total_pages; x++) {
                ManageWarpViewInventory w = new ManageWarpViewInventory(x, warps);
                w.initFromController(this, getPlayer(), getInventory());
                warpInventories.add(w);
            }

            openChild(getChildByPage(0));
        }

        public GUIChild getChildByPage(int page) {
            for (ManageWarpViewInventory w : warpInventories) {
                if (w.getPage() == page) {
                    return w;
                }
            }

            return null;
        }

        public WarpConfigPayload getPayload() {
            return payload;
        }
    }
}
