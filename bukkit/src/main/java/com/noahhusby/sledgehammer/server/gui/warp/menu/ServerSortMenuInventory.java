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

package com.noahhusby.sledgehammer.server.gui.warp.menu;

import com.google.common.collect.Lists;
import com.noahhusby.sledgehammer.common.warps.WarpGroupPayload;
import com.noahhusby.sledgehammer.common.warps.WarpPayload;
import com.noahhusby.sledgehammer.server.Constants;
import com.noahhusby.sledgehammer.server.SledgehammerUtil;
import com.noahhusby.sledgehammer.server.gui.GUIController;
import com.noahhusby.sledgehammer.server.gui.GUIRegistry;
import com.noahhusby.sledgehammer.server.network.NetworkHandler;
import com.noahhusby.sledgehammer.server.network.S2P.S2PWarpConfigPacket;
import com.noahhusby.sledgehammer.server.util.SkullUtil;
import com.noahhusby.sledgehammer.server.util.WarpGUIUtil;
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
public class ServerSortMenuInventory extends AbstractWarpInventory {
    private final int page;
    private final List<String> servers;

    @Override
    public void init() {
        super.init();
        inventory.setItem(4, SledgehammerUtil.getSkull(Constants.monitorHead, ChatColor.GREEN + "" + ChatColor.BOLD + "Servers"));
        inventory.setItem(45, WarpGUIUtil.generateWarpSort());
        inventory.setItem(49, WarpGUIUtil.generateExit());

        boolean paged = false;
        if (page != 0) {
            ItemStack head = SledgehammerUtil.getSkull(Constants.arrowLeftHead, ChatColor.AQUA + "" + ChatColor.BOLD + "Previous Page");
            inventory.setItem(51, head);
            paged = true;
        }

        if (servers.size() > (page + 1) * Constants.warpsPerPage) {
            ItemStack head = SledgehammerUtil.getSkull(Constants.arrowRightHead, ChatColor.AQUA + "" + ChatColor.BOLD + "Next Page");
            inventory.setItem(53, head);
            paged = true;
        }

        if (paged) {
            setItem(52, SledgehammerUtil.setItemDisplayName(SkullUtil.itemFromNumber(page + 1), ChatColor.GREEN
                                                                                                + "" + ChatColor.BOLD + "Page " + (page + 1)));
        }

        int min = page * 27;
        int max = min + 27;

        if (max > servers.size()) {
            max = min + (servers.size() - (page * 27));
        }

        int current = 9;
        for (int x = min; x < max; x++) {
            ItemStack item = SledgehammerUtil.getSkull(Constants.yellowWoolHead, ChatColor.YELLOW + "" + ChatColor.BOLD + servers.get(x));
            ItemMeta meta = item.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.BLUE + "" + ChatColor.STRIKETHROUGH + "------------------");
            lore.add(ChatColor.DARK_GRAY + "Server: " + servers.get(x));
            lore.add(ChatColor.DARK_GRAY + "> " + ChatColor.GREEN + "Click to view.");
            lore.add(ChatColor.BLUE + "" + ChatColor.STRIKETHROUGH + "------------------");
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

        ServerSortMenuInventoryController controller = (ServerSortMenuInventoryController) getController();

        if (e.getCurrentItem().getItemMeta().getDisplayName() == null) {
            return;
        }

        if (e.getSlot() == 45) {
            controller.close();
            GUIRegistry.register(new WarpSortInventory.WarpSortInventoryController(getPlayer(), controller.getPayload()));
            return;
        }

        if (e.getSlot() == 46 && controller.getPayload().isEditAccess()) {
            controller.close();
            NetworkHandler.getInstance().send(new S2PWarpConfigPacket(S2PWarpConfigPacket.ProxyConfigAction.OPEN_CONFIG,
                    getPlayer(), controller.getPayload().getSalt()));
            return;
        }

        if (e.getSlot() == 49) {
            controller.close();
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
                if (s.contains("Server:")) {
                    id = ChatColor.stripColor(s).trim().replace("Server: ", "");
                }
            }

            GUIRegistry.register(new ServerWarpInventory.ServerWarpInventoryController(getController(), controller.getPayload(), id));
        }
    }

    public int getPage() {
        return page;
    }

    public static class ServerSortMenuInventoryController extends AbstractWarpInventoryController<ServerSortMenuInventory> {

        public ServerSortMenuInventoryController(Player player, WarpPayload payload) {
            super("Warps", player, payload);
            init();
        }

        public ServerSortMenuInventoryController(GUIController controller, WarpPayload payload) {
            super(controller, payload);
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
                addPage(new ServerSortMenuInventory(x, servers));
            }

            openChild(getChildByPage(0));
        }
    }
}
