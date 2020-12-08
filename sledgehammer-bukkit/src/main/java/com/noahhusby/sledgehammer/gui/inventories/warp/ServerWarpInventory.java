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

package com.noahhusby.sledgehammer.gui.inventories.warp;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.gui.inventories.general.GUIChild;
import com.noahhusby.sledgehammer.gui.inventories.general.GUIHelper;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ServerWarpInventory extends GUIChild {
    private final int page;
    private final List<String> servers;

    private Inventory inventory;
    private JSONObject heads;

    public ServerWarpInventory(int page, List<String> servers, JSONObject heads) {
        this.page = page;
        this.servers = servers;
        this.heads = heads;
    }

    @Override
    public void init() {
        this.inventory = getInventory();
        int total_pages = (int) Math.ceil(servers.size() / 27.0);

        for(int x = 0; x < 54; x++) {
            ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);

            ItemMeta meta = glass.getItemMeta();
            meta.setDisplayName(ChatColor.RESET+"");
            meta.setDisplayName(null);
            glass.setItemMeta(meta);

            inventory.setItem(x, glass);
        }

        inventory.setItem(49, GUIHelper.generateExit());
        inventory.setItem(40, GUIHelper.generateCompass());

        if(page != 0) {
            ItemStack head = SledgehammerUtil.getSkull(Constants.arrowLeftHead, ChatColor.AQUA + "" + ChatColor.BOLD + "Previous Page");

            ItemMeta meta = head.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Page " + (page) + "/" + (int) Math.ceil(servers.size() / 27.0));
            meta.setLore(lore);
            head.setItemMeta(meta);

            inventory.setItem(45, head);
        }

        if(servers.size() > (page + 1) * Constants.warpsPerPage) {
            ItemStack head = SledgehammerUtil.getSkull(Constants.arrowRightHead, ChatColor.AQUA + "" + ChatColor.BOLD + "Next Page");

            ItemMeta meta = head.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Page " + (page+2) + "/" + (int) Math.ceil(servers.size() / 27.0));
            meta.setLore(lore);
            head.setItemMeta(meta);

            inventory.setItem(53, head);
        }

        int min = page * 27;
        int max = min + 27;

        if(max > servers.size()) {
            max = min + (servers.size() - (page * 27));
        }

        int current = 9;
        for(int x = min; x < max; x++) {
            String server = servers.get(x);
            String headID = heads.get(server).equals("*") ? Constants.goldenBlankHead : "";
            ItemStack warp = SledgehammerUtil.getSkull(headID,ChatColor.AQUA + "" + ChatColor.BOLD + server);
            ItemMeta meta = warp.getItemMeta();

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.BLUE + "" + ChatColor.STRIKETHROUGH + "------------------");
            lore.add(ChatColor.DARK_GRAY + "> " + ChatColor.GREEN + "Click to view warps.");
            lore.add(ChatColor.BLUE + "" + ChatColor.STRIKETHROUGH + "------------------");
            meta.setLore(lore);
            warp.setItemMeta(meta);
            inventory.setItem(current, warp);
            current++;
        }
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if(e.getCurrentItem() == null) return;

        ServerWarpInventoryController controller = (ServerWarpInventoryController) getController();

        if(e.getCurrentItem().getItemMeta().getDisplayName() == null) return;

        if(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Previous Page")) {
            controller.openChild(controller.getChildByPage(page-1));
            return;
        }

        if(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Next Page")) {
            controller.openChild(controller.getChildByPage(page+1));
            return;
        }

        if(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Close")) {
            controller.close();
            return;
        }

        if(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Return to Main Menu")) {
            controller.switchToAll();
            return;
        }

        controller.switchToServer(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));

    }

    public int getPage() {
        return page;
    }
}
