/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - WarpInventory.java
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

import com.noahhusby.sledgehammer.common.warps.Warp;
import com.noahhusby.sledgehammer.common.warps.WarpGroup;
import com.noahhusby.sledgehammer.common.warps.WarpPayload;
import com.noahhusby.sledgehammer.server.Constants;
import com.noahhusby.sledgehammer.server.SledgehammerUtil;
import com.noahhusby.sledgehammer.server.gui.GUIController;
import com.noahhusby.sledgehammer.server.gui.GUIRegistry;
import com.noahhusby.sledgehammer.server.network.NetworkHandler;
import com.noahhusby.sledgehammer.server.network.S2P.S2PWarpConfigPacket;
import com.noahhusby.sledgehammer.server.network.S2P.S2PWarpPacket;
import com.noahhusby.sledgehammer.server.util.SkullUtil;
import com.noahhusby.sledgehammer.server.util.WarpGUIUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class PinnedWarpInventory extends AbstractWarpInventory {
    private final int page;
    private final List<Warp> warps;

    @Override
    public void init() {
        super.init();
        inventory.setItem(4, SledgehammerUtil.getSkull(Constants.lampHead, ChatColor.YELLOW + "" + ChatColor.BOLD + "Pinned Warps"));
        inventory.setItem(40, WarpGUIUtil.generateCompass());
        inventory.setItem(45, WarpGUIUtil.generateWarpSort());

        boolean paged = false;
        if (page != 0) {
            ItemStack head = SledgehammerUtil.getSkull(Constants.arrowLeftHead, ChatColor.AQUA + "" + ChatColor.BOLD + "Previous Page");
            inventory.setItem(51, head);
            paged = true;
        }

        if (warps.size() > (page + 1) * Constants.warpsPerPage) {
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

        if (max > warps.size()) {
            max = min + (warps.size() - (page * 27));
        }

        int current = 9;
        for (int x = min; x < max; x++) {
            Warp warp = warps.get(x);

            String headId = warp.getHeadID();
            if (headId.equals("")) {
                headId = Constants.yellowWoolHead;
            }
            ItemStack item = SledgehammerUtil.getSkull(headId, ((warp.getPinned() == Warp.PinnedMode.GLOBAL
                                                                 || warp.getPinned() == Warp.PinnedMode.LOCAL) ? ChatColor.GOLD : ChatColor.BLUE)
                                                               + "" + ChatColor.BOLD + warp.getName());

            ItemMeta meta = item.getItemMeta();

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.BLUE + "" + ChatColor.STRIKETHROUGH + "------------------");
            lore.add(ChatColor.DARK_GRAY + "Server: " + warp.getServer());
            lore.add(ChatColor.DARK_GRAY + "> " + ChatColor.GREEN + "Click to warp.");
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

        PinnedWarpInventoryController controller = (PinnedWarpInventoryController) getController();

        if (e.getCurrentItem().getItemMeta().getDisplayName() == null) {
            return;
        }

        if (e.getSlot() == 40) {
            GUIRegistry.register(new WarpMenuInventory.WarpMenuInventoryController(getController(), controller.getPayload()));
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

            NetworkHandler.getInstance().send(new S2PWarpPacket(player, controller.getPayload(), id));

            controller.close();
        }
    }

    public int getPage() {
        return page;
    }

    public static class PinnedWarpInventoryController extends AbstractWarpInventoryController<PinnedWarpInventory> {

        public PinnedWarpInventoryController(Player p, WarpPayload payload) {
            super("Warps", p, payload);
            init();
        }

        public PinnedWarpInventoryController(GUIController controller, WarpPayload payload) {
            super(controller, payload);
            init();
        }

        @Override
        public void init() {
            List<Warp> warps = new ArrayList<>();
            for (WarpGroup wg : payload.getGroups()) {
                for (Warp w : wg.getWarps()) {
                    if ((payload.isLocal() && w.getPinned() == Warp.PinnedMode.GLOBAL) ||
                        (!payload.isLocal() && (w.getPinned() == Warp.PinnedMode.GLOBAL || w.getPinned() == Warp.PinnedMode.LOCAL))) {
                        warps.add(w);
                    }
                }
            }

            int total_pages = (int) Math.ceil(warps.size() / 27.0);
            if (total_pages == 0) {
                total_pages = 1;
            }
            for (int x = 0; x < total_pages; x++) {
                addPage(new PinnedWarpInventory(x, warps));
            }
            openChild(getChildByPage(0));
        }
    }
}
