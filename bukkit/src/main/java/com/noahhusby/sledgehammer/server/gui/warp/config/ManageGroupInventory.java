/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - SetServerWarpInventory.java
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

import com.noahhusby.sledgehammer.common.warps.WarpGroupPayload;
import com.noahhusby.sledgehammer.server.Constants;
import com.noahhusby.sledgehammer.server.SledgehammerUtil;
import com.noahhusby.sledgehammer.server.data.warp.WarpConfigPayload;
import com.noahhusby.sledgehammer.server.gui.GUIChild;
import com.noahhusby.sledgehammer.server.gui.GUIController;
import com.noahhusby.sledgehammer.server.gui.GUIRegistry;
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
public class ManageGroupInventory extends GUIChild {
    private final int page;
    private final List<WarpGroupPayload> groups;

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

        inventory.setItem(4, SledgehammerUtil.getSkull(Constants.monitorHead, ChatColor.GREEN + "" + ChatColor.BOLD + "Groups"));

        boolean paged = false;
        if (page != 0) {
            ItemStack head = SledgehammerUtil.getSkull(Constants.arrowLeftHead, ChatColor.AQUA + "" + ChatColor.BOLD + "Previous Page");
            inventory.setItem(42, head);
            paged = true;
        }

        if (groups.size() > (page + 1) * Constants.warpsPerPage) {
            ItemStack head = SledgehammerUtil.getSkull(Constants.arrowRightHead, ChatColor.AQUA + "" + ChatColor.BOLD + "Next Page");
            inventory.setItem(44, head);
            paged = true;
        }

        if (paged) {
            setItem(43, SledgehammerUtil.setItemDisplayName(SkullUtil.itemFromNumber(page + 1), ChatColor.GREEN
                                                                                                + "" + ChatColor.BOLD + "Page " + (page + 1)));
        }

        int min = page * 27;
        int max = min + 27;

        if (max > groups.size()) {
            max = min + (groups.size() - (page * 27));
        }

        int current = 9;
        for (int x = min; x < max; x++) {
            WarpGroupPayload group = groups.get(x);

            String headId = group.getHeadId();
            if (headId.equals("")) {
                headId = Constants.cyanWoolHead;
            }
            ItemStack item = SledgehammerUtil.getSkull(headId, group.getName());

            ItemMeta meta = item.getItemMeta();

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.BLUE + "" + ChatColor.STRIKETHROUGH + "------------------");
            lore.add(ChatColor.DARK_GRAY + "Group: " + group.getName());
            lore.add(ChatColor.DARK_GRAY + "> " + ChatColor.GREEN + "Click to view.");
            lore.add(ChatColor.BLUE + "" + ChatColor.STRIKETHROUGH + "------------------");
            lore.add(ChatColor.GRAY + "ID: " + group.getId());
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

        ManageGroupInventoryController controller = (ManageGroupInventoryController) getController();

        if (e.getCurrentItem().getItemMeta().getDisplayName() == null) {
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

        if (e.getSlot() > 8 && e.getSlot() < 36) {
            ItemMeta meta = e.getCurrentItem().getItemMeta();
            String id = "";
            List<String> lore = meta.getLore();
            for (String s : lore) {
                if (s.contains("ID:")) {
                    id = ChatColor.stripColor(s).trim().replace("ID: ", "");
                }
            }

            GUIRegistry.register(new ManageGroupWarpInventory.ManageGroupWarpInventoryController(getController(), controller.getPayload(), id));
        }
    }

    public int getPage() {
        return page;
    }

    public static class ManageGroupInventoryController extends GUIController {
        private final List<ManageGroupInventory> warpInventories = new ArrayList<>();
        private final WarpConfigPayload payload;

        public ManageGroupInventoryController(Player p, WarpConfigPayload payload) {
            super(45, "Select a group to manage", p);
            this.payload = payload;
            init();
        }

        public ManageGroupInventoryController(GUIController controller, WarpConfigPayload payload) {
            super(controller);
            this.payload = payload;
            init();
        }

        @Override
        public void init() {
            List<WarpGroupPayload> groups = payload.getGroups();

            int total_pages = (int) Math.ceil(groups.size() / 27.0);
            if (total_pages == 0) {
                total_pages = 1;
            }
            for (int x = 0; x < total_pages; x++) {
                ManageGroupInventory w = new ManageGroupInventory(x, groups);
                w.initFromController(this, getPlayer(), getInventory());
                warpInventories.add(w);
            }

            openChild(getChildByPage(0));
        }

        public GUIChild getChildByPage(int page) {
            for (ManageGroupInventory w : warpInventories) {
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
