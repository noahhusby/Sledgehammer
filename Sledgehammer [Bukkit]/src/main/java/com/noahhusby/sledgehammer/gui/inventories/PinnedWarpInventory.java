/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bukkit] - PinnedWarpInventory.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.gui.inventories;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.gui.GUIChild;
import com.noahhusby.sledgehammer.gui.GUIRegistry;
import com.noahhusby.sledgehammer.network.S2P.S2PWarpPacket;
import com.noahhusby.sledgehammer.network.S2P.S2PWebMapPacket;
import com.noahhusby.sledgehammer.network.SledgehammerNetworkManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PinnedWarpInventory extends GUIChild {
    private final int page;
    private final JSONArray warps;
    private final boolean web;

    private Inventory inventory;

    public PinnedWarpInventory(int page, JSONArray warps, boolean web) {
        this.page = page;
        this.warps = warps;
        this.web = web;
    }

    @Override
    public void init() {
        this.inventory = getInventory();
        int total_pages = (int) Math.ceil(warps.size() / 27.0);

        for(int x = 0; x < 54; x++) {
            ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);

            ItemMeta meta = glass.getItemMeta();
            meta.setDisplayName(ChatColor.RESET+"");
            meta.setDisplayName(null);
            glass.setItemMeta(meta);

            inventory.setItem(x, glass);
        }

        inventory.setItem(4, SledgehammerUtil.getSkull(Constants.lampHead, ChatColor.GRAY + "" + ChatColor.BOLD + "Pinned Warps"));
        inventory.setItem(48, SledgehammerUtil.getSkull(Constants.monitorHead, ChatColor.GOLD + "" + ChatColor.BOLD + "All Warps"));
        inventory.setItem(49, generateExit());
        inventory.setItem(50, generateCompass());

        if(web) {
            inventory.setItem(40, SledgehammerUtil.getSkull(Constants.globeHead, ChatColor.GREEN + "" + ChatColor.BOLD + "Open Web Map"));
        }

        if(page != 0) {
            ItemStack head = SledgehammerUtil.getSkull(Constants.arrowLeftHead, ChatColor.AQUA + "" + ChatColor.BOLD + "Previous Page");

            ItemMeta meta = head.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Page " + (page) + "/" + (int) Math.ceil(warps.size() / 27.0));
            meta.setLore(lore);
            head.setItemMeta(meta);

            inventory.setItem(45, head);
        }

        if(warps.size() > (page + 1) * Constants.warpsPerPage) {
            ItemStack head = SledgehammerUtil.getSkull(Constants.arrowRightHead, ChatColor.AQUA + "" + ChatColor.BOLD + "Next Page");

            ItemMeta meta = head.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GRAY + "" + ChatColor.BOLD + "Page " + (page+2) + "/" + (int) Math.ceil(warps.size() / 27.0));
            meta.setLore(lore);
            head.setItemMeta(meta);

            inventory.setItem(53, head);
        }

        int min = page * 27;
        int max = min + 27;

        if(max > warps.size()) {
            max = min + (warps.size() - (page * 27));
        }

        int current = 9;
        for(int x = min; x < max; x++) {
            JSONObject o = (JSONObject) warps.get(x);
            ItemStack warp = new ItemStack(Material.WOOL, 1, (byte) 4);
            ItemMeta meta = warp.getItemMeta();
            meta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + (String) o.get("name"));

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.BLUE + "" + ChatColor.STRIKETHROUGH + "------------------");
            lore.add(ChatColor.DARK_GRAY + "Server: " + (String) o.get("server"));
            lore.add(ChatColor.DARK_GRAY + "> " + ChatColor.GREEN + "Click to warp.");
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

        PinnedWarpInventoryController controller = (PinnedWarpInventoryController) getController();

        if(e.getCurrentItem().getItemMeta().getDisplayName() == null) return;

        if(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Previous Page")) {
            controller.openChild(controller.getChildByPage(page-1));
            return;
        }

        if(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Next Page")) {
            controller.openChild(controller.getChildByPage(page+1));
            return;
        }

        if(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Close GUI")) {
            controller.close();
            return;
        }

        if(e.getCurrentItem().getType() == Material.WOOL) {
            SledgehammerNetworkManager.getInstance().sendPacket(new S2PWarpPacket(player, ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())));
            controller.close();
            return;
        }

        if(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Open Web Map")) {
            SledgehammerNetworkManager.getInstance().sendPacket(new S2PWebMapPacket(player));
            controller.close();
            return;
        }

        if(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("All Warps")) {
            controller.switchToAll();
            return;
        }

        if(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Server Warps")) {
            controller.switchToServerList();
            return;
        }
    }

    private ItemStack generateExit() {
        ItemStack exit = new ItemStack(Material.BARRIER);
        ItemMeta m = exit.getItemMeta();
        m.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Close GUI");
        exit.setItemMeta(m);

        return exit;
    }

    private ItemStack generateCompass() {
        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta m = compass.getItemMeta();

        m.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Server Warps");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Sort warps by server");
        m.setLore(lore);

        compass.setItemMeta(m);

        return compass;
    }

    public int getPage() {
        return page;
    }
}
