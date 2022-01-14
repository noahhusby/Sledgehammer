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

package com.noahhusby.sledgehammer.server.gui.warp.menu;

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.common.warps.Page;
import com.noahhusby.sledgehammer.common.warps.WarpPayload;
import com.noahhusby.sledgehammer.server.gui.GUIChild;
import com.noahhusby.sledgehammer.server.gui.GUIController;
import com.noahhusby.sledgehammer.server.gui.GUIRegistry;
import com.noahhusby.sledgehammer.server.network.NetworkHandler;
import com.noahhusby.sledgehammer.server.network.s2p.S2PWarpConfigPacket;
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
public class WarpSortInventory extends GUIChild {
    private final WarpPayload payload;

    @Override
    public void init() {
        fillInventory(createItem(Material.STAINED_GLASS_PANE, 1, (byte) 15, null));

        byte serverColor = (payload.getDefaultPage() == Page.SERVERS || payload.getDefaultPage() == Page.LOCAL_SERVER) ? (byte) 14 : (byte) 0;
        byte allColor = payload.getDefaultPage() == Page.ALL ? (byte) 14 : (byte) 0;
        byte groupColor = (payload.getDefaultPage() == Page.GROUPS || payload.getDefaultPage() == Page.LOCAL_GROUP) ? (byte) 14 : (byte) 0;

        {
            ItemStack item = new ItemStack(Material.WOOL, 1, serverColor);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Server Warps");

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.BLUE + "This will show warps sorted by server.");
            if (payload.getDefaultPage() == Page.SERVERS) {
                lore.add(ChatColor.RED + "This will be the default view when the menu opens.");
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
            inventory.setItem(11, item);
        }

        {
            ItemStack item = new ItemStack(Material.WOOL, 1, allColor);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "All Warps");

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.BLUE + "This will show all warps.");
            if (payload.getDefaultPage() == Page.ALL) {
                lore.add(ChatColor.RED + "This will be the default view when the menu opens.");
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
            inventory.setItem(13, item);
        }

        {
            ItemStack item = new ItemStack(Material.WOOL, 1, groupColor);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Group Warps");

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.BLUE + "This will show warps sorted by group.");
            if (payload.getDefaultPage() == Page.GROUPS) {
                lore.add(ChatColor.RED + "This will be the default view when the menu opens.");
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
            inventory.setItem(15, item);
        }

    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);

        WarpSortInventoryController controller = (WarpSortInventoryController) getController();

        if (e.getCurrentItem() == null) {
            return;
        }
        if (e.getCurrentItem().getItemMeta() == null) {
            return;
        }
        if (e.getCurrentItem().getItemMeta().getDisplayName() == null) {
            return;
        }

        JsonObject data = new JsonObject();

        if (e.getSlot() == 11) {
            payload.setDefaultPage(Page.SERVERS);
            data.addProperty("sort", "server");
        }

        if (e.getSlot() == 13) {
            payload.setDefaultPage(Page.ALL);
            data.addProperty("sort", "all");
        }

        if (e.getSlot() == 15) {
            payload.setDefaultPage(Page.GROUPS);
            data.addProperty("sort", "group");
        }

        NetworkHandler.getInstance().send(new S2PWarpConfigPacket(S2PWarpConfigPacket.ProxyConfigAction.UPDATE_PLAYER_DEFAULT,
                player, controller.getPayload().getSalt(), data));

        controller.close();

        switch (payload.getDefaultPage()) {
            case ALL:
                GUIRegistry.register(new AllWarpInventory.AllWarpInventoryController(getPlayer(), payload));
                break;
            case GROUPS:
                GUIRegistry.register(new WarpMenuInventory.WarpMenuInventoryController(getPlayer(), payload));
                break;
            case SERVERS:
                GUIRegistry.register(new ServerSortMenuInventory.ServerSortMenuInventoryController(getPlayer(), payload));
                break;
        }
    }

    public static class WarpSortInventoryController extends GUIController {
        private final WarpPayload payload;

        public WarpSortInventoryController(Player p, WarpPayload payload) {
            super(27, "Warp Sort", p);
            this.payload = payload;
            init();
        }

        @Override
        public void init() {
            GUIChild inventory = new WarpSortInventory(payload);
            inventory.initFromController(this, getPlayer(), getInventory());
            openChild(inventory);
        }

        public WarpPayload getPayload() {
            return payload;
        }
    }
}
