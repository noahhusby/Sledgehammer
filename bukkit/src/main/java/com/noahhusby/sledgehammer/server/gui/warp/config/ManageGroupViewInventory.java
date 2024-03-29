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

package com.noahhusby.sledgehammer.server.gui.warp.config;

import com.google.common.collect.Lists;
import com.noahhusby.sledgehammer.common.warps.Warp;
import com.noahhusby.sledgehammer.common.warps.WarpConfigPayload;
import com.noahhusby.sledgehammer.common.warps.WarpGroupPayload;
import com.noahhusby.sledgehammer.server.Constants;
import com.noahhusby.sledgehammer.server.SledgehammerUtil;
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
public class ManageGroupViewInventory extends GUIChild {
    private final int page;
    private final List<WarpGroupPayload> groups;

    @Override
    public void init() {
        fillInventory(createItem(Material.STAINED_GLASS_PANE, 1, (byte) 15, null));
        setItem(0, SledgehammerUtil.getSkull(Constants.Heads.redLeft, ChatColor.RED + "" + ChatColor.BOLD + "Go Back"));
        {
            ItemStack overview = SledgehammerUtil.getSkull(Constants.Heads.globe, ChatColor.GREEN + "" + ChatColor.BOLD + "Groups");
            ItemMeta meta = overview.getItemMeta();
            meta.setLore(Lists.newArrayList(ChatColor.GOLD + "Click to sort warps by server"));
            overview.setItemMeta(meta);
            setItem(4, overview);
        }
        boolean paged = false;
        if (page != 0) {
            ItemStack head = SledgehammerUtil.getSkull(Constants.Heads.arrowLeft, ChatColor.AQUA + "" + ChatColor.BOLD + "Previous Page");
            setItem(42, head);
            paged = true;
        }

        if (groups.size() > (page + 1) * Constants.warpsPerPage) {
            ItemStack head = SledgehammerUtil.getSkull(Constants.Heads.arrowRight, ChatColor.AQUA + "" + ChatColor.BOLD + "Next Page");
            setItem(44, head);
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
                headId = Constants.Heads.cyanWool;
            }
            ItemStack item = SledgehammerUtil.getSkull(headId, group.getName());

            ItemMeta meta = item.getItemMeta();

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.BLUE + "" + ChatColor.STRIKETHROUGH + "------------------");
            lore.add(ChatColor.DARK_GRAY + "Group: " + group.getName());
            lore.add(ChatColor.DARK_GRAY + "> " + ChatColor.GREEN + "Click to view.");
            lore.add(ChatColor.BLUE + "" + ChatColor.STRIKETHROUGH + "------------------");
            lore.add(ChatColor.GRAY + "Id: " + group.getId());
            meta.setLore(lore);
            item.setItemMeta(meta);

            setItem(current, item);
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

        if (e.getSlot() == 0) {
            controller.close();
            GUIRegistry.register(new ConfigMenu.ConfigMenuController(getPlayer(), controller.getPayload()));
            return;
        }

        if (e.getSlot() == 4) {
            GUIRegistry.register(new ManageServerViewInventory.ManageGroupInventoryController(getController(), controller.getPayload()));
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
                if (s.contains("Id:")) {
                    id = ChatColor.stripColor(s).trim().replace("Id: ", "");
                }
            }

            List<Integer> warpIds = controller.getPayload().getGroups().get(id).getWarps();
            List<Warp> warps = Lists.newArrayList();
            for (Integer warpId : warpIds) {
                warps.add(controller.getPayload().getWaypoints().get(warpId));
            }

            GUIRegistry.register(new ManageWarpViewInventory.ManageWarpViewInventoryController(getController(), controller.getPayload(), warps));
        }
    }

    public int getPage() {
        return page;
    }

    public static class ManageGroupInventoryController extends GUIController {
        private final List<ManageGroupViewInventory> warpInventories = new ArrayList<>();
        private final WarpConfigPayload payload;

        public ManageGroupInventoryController(Player p, WarpConfigPayload payload) {
            super(45, "Select a warp to edit", p);
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
            List<WarpGroupPayload> groups = Lists.newArrayList(payload.getGroups().values());

            int total_pages = (int) Math.ceil(groups.size() / 27.0);
            if (total_pages == 0) {
                total_pages = 1;
            }
            for (int x = 0; x < total_pages; x++) {
                ManageGroupViewInventory w = new ManageGroupViewInventory(x, groups);
                w.initFromController(this, getPlayer(), getInventory());
                warpInventories.add(w);
            }

            openChild(getChildByPage(0));
        }

        public GUIChild getChildByPage(int page) {
            for (ManageGroupViewInventory w : warpInventories) {
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
