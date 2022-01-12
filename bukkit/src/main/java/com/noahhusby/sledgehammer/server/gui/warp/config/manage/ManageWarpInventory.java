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

package com.noahhusby.sledgehammer.server.gui.warp.config.manage;

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.common.warps.Point;
import com.noahhusby.sledgehammer.common.warps.Warp;
import com.noahhusby.sledgehammer.common.warps.WarpConfigPayload;
import com.noahhusby.sledgehammer.server.Constants;
import com.noahhusby.sledgehammer.server.SledgehammerUtil;
import com.noahhusby.sledgehammer.server.chat.ChatHandler;
import com.noahhusby.sledgehammer.server.gui.GUIChild;
import com.noahhusby.sledgehammer.server.gui.GUIController;
import com.noahhusby.sledgehammer.server.gui.GUIRegistry;
import com.noahhusby.sledgehammer.server.gui.warp.config.ConfigMenu;
import com.noahhusby.sledgehammer.server.gui.warp.config.ManageServerViewInventory;
import com.noahhusby.sledgehammer.server.gui.warp.config.confirmation.ConfirmationController;
import com.noahhusby.sledgehammer.server.network.NetworkHandler;
import com.noahhusby.sledgehammer.server.network.s2p.S2PWarpConfigPacket;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ManageWarpInventory extends GUIChild {

    private final WarpConfigPayload payload;
    private final Warp cur;

    @Override
    public void init() {
        fillInventory(createItem(Material.STAINED_GLASS_PANE, 1, (byte) 15, null));
        {
            String headId = cur.getHeadID();
            if (headId == null || headId.equals("")) {
                headId = Constants.Heads.cyanWool;
            }
            ItemStack item = SledgehammerUtil.getSkull(headId, ChatColor.BLUE
                                                               + "" + ChatColor.BOLD + cur.getName());
            ItemMeta meta  = item.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.BLUE + "" + ChatColor.STRIKETHROUGH + "------------------");
            lore.add(ChatColor.DARK_GRAY + "Server: " + cur.getServer());
            lore.add(ChatColor.BLUE + "" + ChatColor.STRIKETHROUGH + "------------------");
            lore.add(ChatColor.GRAY + "ID: " + cur.getId());
            meta.setLore(lore);
            item.setItemMeta(meta);
            setItem(4, item);
        }
        setItem(18, SledgehammerUtil.getSkull(Constants.Heads.arrowLeft, ChatColor.RED + "" + ChatColor.BOLD + "Back"));
        setItem(26, SledgehammerUtil.getSkull(Constants.Heads.limeCheckmark, ChatColor.GREEN + "" + ChatColor.BOLD + "Save"));
        setItem(11, createItem(Material.NAME_TAG, 1, ChatColor.RED + "" + ChatColor.BOLD + "Change Name"));
        setItem(12, SledgehammerUtil.getSkull(Constants.Heads.pocketPortal, ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Move Location"));
        setItem(14, SledgehammerUtil.getSkull(Constants.Heads.steve, ChatColor.AQUA + "" + ChatColor.BOLD + "Change Head"));
        setItem(15, SledgehammerUtil.getSkull(Constants.Heads.redTrashCan, ChatColor.RED + "" + ChatColor.BOLD + "Delete Warp"));
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
        if (e.getSlot() == 18) {
            GUIRegistry.register(new ManageServerViewInventory.ManageGroupInventoryController(getPlayer(), payload));
            return;
        }

        if (e.getSlot() == 11) {
            GUIRegistry.register(new ChangeWarpNameAnvil.ChangeNameController(getPlayer(), payload, cur));
            return;
        }

        if (e.getSlot() == 12) {
            JsonObject data = new JsonObject();
            data.addProperty("warpId", cur.getId());
            Location loc = player.getLocation();
            Point point = new Point(loc.getX(), loc.getY(), loc.getZ(), loc.getY(), loc.getPitch()).limit();
            data.add("point", SledgehammerUtil.GSON.toJsonTree(point));

            NetworkHandler.getInstance().send(new S2PWarpConfigPacket(S2PWarpConfigPacket.ProxyConfigAction.WARP_UPDATE_LOCATION, getPlayer(), payload.getSalt(), data));
        }

        if (e.getSlot() == 14) {
            getController().close();
            ChatHandler.getInstance().startEntry(getPlayer(), ChatColor.BLUE + "Enter the Minecraft-URL value from " +
                                                              ChatColor.GRAY + "minecraft-heads.com", (success, text) -> {
                if (success) {
                    cur.setHeadID(text);
                }
                GUIRegistry.register(new ManageWarpInventoryController(player, payload, cur));
            });
        }

        if (e.getSlot() == 15) {
            JsonObject data = new JsonObject();
            data.addProperty("warpId", cur.getId());

            NetworkHandler.getInstance().send(new S2PWarpConfigPacket(
                    S2PWarpConfigPacket.ProxyConfigAction.REMOVE_WARP,
                    getPlayer(), payload.getSalt(), data));
        }

        if (e.getSlot() == 26) {
            NetworkHandler.getInstance().send(
                    new S2PWarpConfigPacket(S2PWarpConfigPacket.ProxyConfigAction.UPDATE_WARP, getPlayer(),
                            ((ManageWarpInventoryController) controller).getPayload().getSalt(), SledgehammerUtil.GSON.toJsonTree(cur).getAsJsonObject()));
            GUIRegistry.register(new ConfirmationController(getPlayer(), new ConfigMenu.ConfigMenuController(getPlayer(), payload), ConfirmationController.Type.HEAD_UPDATE, "Successfully updated warp!"));
        }
    }

    public static class ManageWarpInventoryController extends GUIController {

        private final WarpConfigPayload payload;
        private final Warp warp;

        public ManageWarpInventoryController(Player p, WarpConfigPayload payload, int warpId) {
            super(27, "Edit Warp Settings", p);
            this.payload = payload;
            this.warp = payload.getWaypoints().get(warpId);
            init();
        }

        public ManageWarpInventoryController(Player p, WarpConfigPayload payload, Warp warp) {
            super(27, "Edit Warp Settings", p);
            this.payload = payload;
            this.warp = warp;
            init();
        }

        public ManageWarpInventoryController(GUIController controller, WarpConfigPayload payload, Warp warp) {
            super(controller);
            this.payload = payload;
            this.warp = warp;
            init();
        }


        @Override
        public void init() {
            ManageWarpInventory warpConfig = new ManageWarpInventory(getPayload(), warp);
            warpConfig.initFromController(this, getPlayer(), getInventory());
            openChild(warpConfig);
        }

        public WarpConfigPayload getPayload() {
            return payload;
        }
    }
}
