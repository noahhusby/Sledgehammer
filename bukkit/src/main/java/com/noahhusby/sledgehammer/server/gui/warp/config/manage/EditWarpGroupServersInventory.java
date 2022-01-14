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

package com.noahhusby.sledgehammer.server.gui.warp.config.manage;

import com.google.common.collect.Lists;
import com.noahhusby.sledgehammer.common.warps.WarpGroup;
import com.noahhusby.sledgehammer.common.warps.WarpGroupConfigPayload;
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
public class EditWarpGroupServersInventory extends GUIChild {
    private final int page;
    private final List<String> servers;
    private final WarpGroup group;

    @Override
    public void init() {
        fillInventory(createItem(Material.STAINED_GLASS_PANE, 1, (byte) 15, null));
        setItem(4, SledgehammerUtil.getSkull(((group.getHeadId() == null || group.getHeadId().equals("")) ? Constants.Heads.blackBook : group.getHeadId()), ChatColor.GREEN + "" + ChatColor.BOLD + group.getName()));
        setItem(40, SledgehammerUtil.getSkull(Constants.Heads.limeCheckmark, ChatColor.GREEN + "" + ChatColor.BOLD + "Done"));

        boolean paged = false;
        if (page != 0) {
            ItemStack head = SledgehammerUtil.getSkull(Constants.Heads.arrowLeft, ChatColor.AQUA + "" + ChatColor.BOLD + "Previous Page");
            setItem(42, head);
            paged = true;
        }
        if (servers.size() > (page + 1) * Constants.warpsPerPage) {
            ItemStack head = SledgehammerUtil.getSkull(Constants.Heads.arrowRight, ChatColor.AQUA + "" + ChatColor.BOLD + "Next Page");
            setItem(44, head);
            paged = true;
        }
        if (paged) {
            setItem(43, SledgehammerUtil.setItemDisplayName(SkullUtil.itemFromNumber(page + 1), ChatColor.GREEN + "" + ChatColor.BOLD + "Page " + (page + 1)));
        }

        int min = page * 27;
        int max = min + 27;

        if (max > servers.size()) {
            max = min + (servers.size() - (page * 27));
        }

        int current = 9;
        for (int x = min; x < max; x++) {
            String server = servers.get(x);
            boolean enabled = group.getServers().contains(server);
            String head = enabled ? Constants.Heads.limeWool : Constants.Heads.redWool;
            ItemStack item = SledgehammerUtil.getSkull(head, ChatColor.GOLD + "" + ChatColor.BOLD + server);

            ItemMeta meta = item.getItemMeta();

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.BLUE + "" + ChatColor.STRIKETHROUGH + "------------------");
            lore.add(ChatColor.DARK_GRAY + "Server: " + server);
            lore.add(ChatColor.DARK_GRAY + "> " + (enabled ? ChatColor.RED + "Click to remove" : ChatColor.GREEN + "Click to add"));
            lore.add(ChatColor.BLUE + "" + ChatColor.STRIKETHROUGH + "------------------");
            lore.add(ChatColor.GRAY + "ID: " + server);
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

        EditWarpGroupServersInventoryController controller = (EditWarpGroupServersInventoryController) getController();

        if (e.getCurrentItem().getItemMeta().getDisplayName() == null) {
            return;
        }

        if (e.getSlot() == 40) {
            GUIRegistry.register(new ManageWarpGroupInventory.ManageWarpGroupInventoryController(getPlayer(), controller.getPayload(), group));
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
            String server = ChatColor.stripColor(meta.getDisplayName());
            if (server == null || server.equals("")) {
                return;
            }
            if (group.getServers().contains(server)) {
                group.getServers().remove(server);
            } else {
                group.getServers().add(server);
            }
            GUIRegistry.register(new EditWarpGroupServersInventoryController(getController(), controller.getPayload(), group, this.page));
        }

    }

    public int getPage() {
        return page;
    }

    public static class EditWarpGroupServersInventoryController extends GUIController {
        private final List<EditWarpGroupServersInventory> inventories = new ArrayList<>();
        private final WarpGroupConfigPayload payload;
        private final WarpGroup group;
        private int page = 0;

        public EditWarpGroupServersInventoryController(Player p, WarpGroupConfigPayload payload, WarpGroup group) {
            super(45, "Edit servers", p);
            this.payload = payload;
            this.group = group;
            init();
        }

        public EditWarpGroupServersInventoryController(GUIController controller, WarpGroupConfigPayload payload, WarpGroup group) {
            super(controller);
            this.payload = payload;
            this.group = group;
            init();
        }

        public EditWarpGroupServersInventoryController(GUIController controller, WarpGroupConfigPayload payload, WarpGroup group, int page) {
            super(controller);
            this.payload = payload;
            this.group = group;
            this.page = page;
            init();
        }

        @Override
        public void init() {
            List<String> servers = Lists.newArrayList(payload.getServers().keySet());
            int total_pages = (int) Math.ceil(servers.size() / 27.0);
            if (total_pages == 0) {
                total_pages = 1;
            }
            for (int x = 0; x < total_pages; x++) {
                EditWarpGroupServersInventory w = new EditWarpGroupServersInventory(x, servers, group);
                w.initFromController(this, getPlayer(), getInventory());
                inventories.add(w);
            }

            openChild(getChildByPage(page));
        }

        public GUIChild getChildByPage(int page) {
            for (EditWarpGroupServersInventory w : inventories) {
                if (w.getPage() == page) {
                    return w;
                }
            }
            return null;
        }

        public WarpGroupConfigPayload getPayload() {
            return payload;
        }
    }
}
