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

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.common.warps.WarpGroup;
import com.noahhusby.sledgehammer.common.warps.WarpGroupConfigPayload;
import com.noahhusby.sledgehammer.server.Constants;
import com.noahhusby.sledgehammer.server.SledgehammerUtil;
import com.noahhusby.sledgehammer.server.chat.ChatHandler;
import com.noahhusby.sledgehammer.server.gui.GUIChild;
import com.noahhusby.sledgehammer.server.gui.GUIController;
import com.noahhusby.sledgehammer.server.gui.GUIRegistry;
import com.noahhusby.sledgehammer.server.gui.warp.config.ManageWarpGroupViewInventory;
import com.noahhusby.sledgehammer.server.gui.warp.config.confirmation.ConfirmationController;
import com.noahhusby.sledgehammer.server.network.NetworkHandler;
import com.noahhusby.sledgehammer.server.network.s2p.S2PWarpGroupConfigPacket;
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
public class ManageWarpGroupInventory extends GUIChild {

    private final WarpGroupConfigPayload payload;
    private final WarpGroup cur;

    @Override
    public void init() {
        fillInventory(createItem(Material.STAINED_GLASS_PANE, 1, (byte) 15, null));
        {
            String headId = cur.getHeadId();
            if (headId == null || headId.equals("")) {
                headId = Constants.Heads.cyanWool;
            }
            ItemStack item = SledgehammerUtil.getSkull(headId, ChatColor.BLUE
                                                               + "" + ChatColor.BOLD + cur.getName());
            ItemMeta meta = item.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.BLUE + "" + ChatColor.STRIKETHROUGH + "------------------");
            lore.add(ChatColor.GRAY + "ID: " + cur.getId());
            lore.add(ChatColor.BLUE + "" + ChatColor.STRIKETHROUGH + "------------------");
            meta.setLore(lore);
            item.setItemMeta(meta);
            setItem(4, item);
        }
        setItem(11, createItem(Material.NAME_TAG, 1, ChatColor.RED + "" + ChatColor.BOLD + "Change Name"));
        setItem(12, SledgehammerUtil.getSkull(Constants.Heads.steve, ChatColor.AQUA + "" + ChatColor.BOLD + "Change Head"));
        if (payload.isAdmin()) {
            setItem(13, SledgehammerUtil.getSkull(Constants.Heads.cyanWool, ChatColor.AQUA + "" + ChatColor.BOLD + "Edit Warps"));
            setItem(14, SledgehammerUtil.getSkull(Constants.Heads.yellowWool, ChatColor.GOLD + "" + ChatColor.BOLD + "Edit Servers"));
            setItem(15, SledgehammerUtil.getSkull(Constants.Heads.redTrashCan, ChatColor.RED + "" + ChatColor.BOLD + "Delete Group"));
        }
        setItem(18, SledgehammerUtil.getSkull(Constants.Heads.arrowLeft, ChatColor.RED + "" + ChatColor.BOLD + "Back"));
        setItem(26, SledgehammerUtil.getSkull(Constants.Heads.limeCheckmark, ChatColor.GREEN + "" + ChatColor.BOLD + "Save"));
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

        if (e.getSlot() == 11) {
            GUIRegistry.register(new ChangeWarpGroupNameAnvil.ChangeWarpGroupNameController(getPlayer(), payload, cur));
            return;
        }

        if (e.getSlot() == 12) {
            getController().close();
            ChatHandler.getInstance().startEntry(getPlayer(), ChatColor.BLUE + "Enter the Minecraft-URL value from " + ChatColor.GRAY + "minecraft-heads.com", (success, text) -> {
                if (success) {
                    cur.setHeadId(text);
                }
                GUIRegistry.register(new ManageWarpGroupInventoryController(player, payload, cur));
            });
        }

        if (e.getSlot() == 13) {
            GUIRegistry.register(new EditWarpGroupWarpsInventory.EditWarpGroupWarpsInventoryController(getPlayer(), payload, cur));
            return;
        }

        if (e.getSlot() == 14) {
            GUIRegistry.register(new EditWarpGroupServersInventory.EditWarpGroupServersInventoryController(getPlayer(), payload, cur));
            return;
        }

        if (e.getSlot() == 15) {
            JsonObject data = new JsonObject();
            data.addProperty("groupId", cur.getId());
            NetworkHandler.getInstance().send(new S2PWarpGroupConfigPacket(S2PWarpGroupConfigPacket.ProxyConfigAction.REMOVE_GROUP, getPlayer(), payload.getSalt(), data));
        }

        if (e.getSlot() == 18) {
            GUIRegistry.register(new ManageWarpGroupViewInventory.ManageWarpGroupViewInventoryController(getPlayer(), payload));
            return;
        }

        if (e.getSlot() == 26) {
            GUIRegistry.register(new ConfirmationController(getPlayer(), null, ConfirmationController.Type.HEAD_UPDATE, "Successfully updated warp group!"));
            NetworkHandler.getInstance().send(new S2PWarpGroupConfigPacket(S2PWarpGroupConfigPacket.ProxyConfigAction.UPDATE_GROUP, getPlayer(), ((ManageWarpGroupInventoryController) controller).getPayload().getSalt(), SledgehammerUtil.GSON.toJsonTree(cur).getAsJsonObject()));
        }
    }

    public static class ManageWarpGroupInventoryController extends GUIController {

        private final WarpGroupConfigPayload payload;
        private final WarpGroup group;

        public ManageWarpGroupInventoryController(Player p, WarpGroupConfigPayload payload, String warpGroupId) {
            super(27, "Edit Warp Settings", p);
            this.payload = payload;
            this.group = payload.getGroups().get(warpGroupId);
            init();
        }

        public ManageWarpGroupInventoryController(Player p, WarpGroupConfigPayload payload, WarpGroup group) {
            super(27, "Edit Warp Settings", p);
            this.payload = payload;
            this.group = group;
            init();
        }

        public ManageWarpGroupInventoryController(GUIController controller, WarpGroupConfigPayload payload, WarpGroup group) {
            super(controller);
            this.payload = payload;
            this.group = group;
            init();
        }


        @Override
        public void init() {
            ManageWarpGroupInventory warpConfig = new ManageWarpGroupInventory(getPayload(), group);
            warpConfig.initFromController(this, getPlayer(), getInventory());
            openChild(warpConfig);
        }

        public WarpGroupConfigPayload getPayload() {
            return payload;
        }
    }
}
