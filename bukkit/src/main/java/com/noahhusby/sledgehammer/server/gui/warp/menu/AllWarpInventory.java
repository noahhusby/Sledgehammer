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

package com.noahhusby.sledgehammer.server.gui.warp.menu;

import com.google.common.collect.Lists;
import com.noahhusby.sledgehammer.common.warps.Warp;
import com.noahhusby.sledgehammer.common.warps.WarpPayload;
import com.noahhusby.sledgehammer.server.Constants;
import com.noahhusby.sledgehammer.server.SledgehammerUtil;
import com.noahhusby.sledgehammer.server.gui.GUIController;
import com.noahhusby.sledgehammer.server.gui.GUIRegistry;
import com.noahhusby.sledgehammer.server.network.NetworkHandler;
import com.noahhusby.sledgehammer.server.network.s2p.S2PWarpConfigPacket;
import com.noahhusby.sledgehammer.server.network.s2p.S2PWarpPacket;
import com.noahhusby.sledgehammer.server.util.SkullUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class AllWarpInventory extends AbstractWarpInventory {
    private final int page;
    private final List<Warp> warps;

    @Override
    public void init() {
        super.init();
        setItem(4, SledgehammerUtil.getSkull(Constants.Heads.globe, ChatColor.GREEN + "" + ChatColor.BOLD + "All Warps"));

        boolean paged = false;
        if (page != 0) {
            ItemStack head = SledgehammerUtil.getSkull(Constants.Heads.arrowLeft, ChatColor.AQUA + "" + ChatColor.BOLD + "Previous Page");
            setItem(51, head);
            paged = true;
        }

        if (warps.size() > (page + 1) * Constants.warpsPerPage) {
            ItemStack head = SledgehammerUtil.getSkull(Constants.Heads.arrowRight, ChatColor.AQUA + "" + ChatColor.BOLD + "Next Page");
            setItem(53, head);
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

            String headId = warp.getHeadId();
            if (headId == null || headId.equals("")) {
                headId = Constants.Heads.yellowWool;
            }
            ItemStack item = SledgehammerUtil.getSkull(headId, (ChatColor.BLUE)
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

        AllWarpInventoryController controller = (AllWarpInventoryController) getController();

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

    @Override
    public int getPage() {
        return page;
    }

    public static class AllWarpInventoryController extends AbstractWarpInventoryController<AllWarpInventory> {
        public AllWarpInventoryController(Player player, WarpPayload payload) {
            super("Warps", player, payload);
            init();
        }

        public AllWarpInventoryController(GUIController controller, WarpPayload payload) {
            super(controller, payload);
            init();
        }

        @Override
        public void init() {
            List<Warp> warps = Lists.newArrayList(payload.getWaypoints().values());
            int total_pages = (int) Math.ceil(warps.size() / 27.0);
            if (total_pages == 0) {
                total_pages = 1;
            }
            for (int x = 0; x < total_pages; x++) {
                addPage(new AllWarpInventory(x, warps));
            }
            openChild(getChildByPage(0));
        }
    }
}
