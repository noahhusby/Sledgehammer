package com.noahhusby.sledgehammer.server.gui.warp.config.manage;

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.common.warps.Point;
import com.noahhusby.sledgehammer.common.warps.Warp;
import com.noahhusby.sledgehammer.common.warps.WarpGroup;
import com.noahhusby.sledgehammer.server.Constants;
import com.noahhusby.sledgehammer.server.SledgehammerUtil;
import com.noahhusby.sledgehammer.server.chat.ChatHandler;
import com.noahhusby.sledgehammer.server.data.warp.WarpConfigPayload;
import com.noahhusby.sledgehammer.server.gui.GUIChild;
import com.noahhusby.sledgehammer.server.gui.GUIController;
import com.noahhusby.sledgehammer.server.gui.GUIRegistry;
import com.noahhusby.sledgehammer.server.gui.warp.config.ManageGroupInventory;
import com.noahhusby.sledgehammer.server.gui.warp.config.confirmation.ConfirmationController;
import com.noahhusby.sledgehammer.server.network.NetworkHandler;
import com.noahhusby.sledgehammer.server.network.S2P.S2PWarpConfigPacket;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

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
            if (headId.equals("")) {
                headId = Constants.cyanWoolHead;
            }
            ItemStack item = SledgehammerUtil.getSkull(headId, ChatColor.BLUE
                                                               + "" + ChatColor.BOLD + cur.getName());
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.BLUE + "" + ChatColor.STRIKETHROUGH + "------------------");
            lore.add(ChatColor.DARK_GRAY + "Server: " + cur.getServer());
            lore.add(ChatColor.BLUE + "" + ChatColor.STRIKETHROUGH + "------------------");
            lore.add(ChatColor.GRAY + "ID: " + cur.getId());
            item.setLore(lore);
            setItem(4, item);
        }

        setItem(18, SledgehammerUtil.getSkull(Constants.arrowLeftHead, ChatColor.RED + ""
                                                                       + ChatColor.BOLD + "Back"));
        setItem(26, SledgehammerUtil.getSkull(Constants.limeCheckmarkHead, ChatColor.GREEN + "" + ChatColor.BOLD + "Save"));
        setItem(11, createItem(Material.NAME_TAG, 1, ChatColor.RED + "" + ChatColor.BOLD + "Change Name"));
        inventory.setItem(12, SledgehammerUtil.getSkull(Constants.pocketPortalHead, ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Move Location"));

        {
            byte pinnedColor = 7;
            String title = ChatColor.WHITE + "" + ChatColor.BOLD + "Not Pinned";

            if (payload.isLocal()) {
                switch (cur.getPinned()) {
                    case LOCAL:
                        pinnedColor = 4;
                        title = ChatColor.GOLD + "" + ChatColor.BOLD + "Pinned Locally";
                        break;
                    case GLOBAL:
                        pinnedColor = 14;
                        title = ChatColor.RED + "" + ChatColor.BOLD + "Pinned Globally";
                        break;
                }
            } else {
                if (cur.getPinned() == Warp.PinnedMode.LOCAL || cur.getPinned() == Warp.PinnedMode.GLOBAL) {
                    pinnedColor = 4;
                    title = ChatColor.GOLD + "" + ChatColor.BOLD + "Pinned";
                }
            }

            ItemStack item = createItem(Material.WOOL, 1, pinnedColor, title);
            List<String> lore = new ArrayList<>();
            if (payload.isLocal() && !payload.isAdmin() && cur.getPinned() == Warp.PinnedMode.GLOBAL) {
                lore.add(ChatColor.RED + "You don't have permission to change the mode!");
            } else {
                lore.add(ChatColor.GREEN + "Click to change!");
            }
            item.setLore(lore);
            setItem(13, item);
        }

        setItem(14, SledgehammerUtil.getSkull(Constants.steveHead, ChatColor.AQUA + "" + ChatColor.BOLD + "Change Head"));
        setItem(15, SledgehammerUtil.getSkull(Constants.redTrashCanHead, ChatColor.RED + "" + ChatColor.BOLD + "Delete Warp"));
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
            GUIRegistry.register(new ManageGroupInventory.ManageGroupInventoryController(getPlayer(), payload));
            return;
        }

        if (e.getSlot() == 11) {
            GUIRegistry.register(new ChangeNameAnvil.ChangeNameController(getPlayer(), payload, cur));
            return;
        }

        if (e.getSlot() == 12) {
            JsonObject data = new JsonObject();
            data.addProperty("warpId", cur.getId());
            Location loc = player.getLocation();
            Point point = new Point(loc.getX(), loc.getY(), loc.getZ(), loc.getY(), loc.getPitch()).limit();
            data.add("point", SledgehammerUtil.GSON.toJsonTree(point));

            NetworkHandler.getInstance().send(new S2PWarpConfigPacket(
                    S2PWarpConfigPacket.ProxyConfigAction.WARP_UPDATE_LOCATION,
                    getPlayer(), payload.getSalt(), data));
        }

        if (e.getSlot() == 13) {
            if (e.getCurrentItem().getDurability() == 7) {
                cur.setPinned(Warp.PinnedMode.LOCAL);
                GUIRegistry.register(new ManageWarpInventoryController(getController(), payload, cur));
            } else if (e.getCurrentItem().getDurability() == 4) {
                if (payload.isLocal() && payload.isAdmin()) {
                    cur.setPinned(Warp.PinnedMode.GLOBAL);
                    GUIRegistry.register(new ManageWarpInventoryController(getController(), payload, cur));
                } else if (!payload.isLocal()) {
                    cur.setPinned(Warp.PinnedMode.NONE);
                    GUIRegistry.register(new ManageWarpInventoryController(getController(), payload, cur));
                }
            } else if (e.getCurrentItem().getDurability() == 14) {
                if (payload.isAdmin()) {
                    cur.setPinned(Warp.PinnedMode.NONE);
                    GUIRegistry.register(new ManageWarpInventoryController(getController(), payload, cur));
                }
            }
        }

        if (e.getSlot() == 14) {
            getController().close();
            ChatHandler.getInstance().startEntry(getPlayer(), ChatColor.BLUE + "Enter the Base64 head code from " +
                                                              ChatColor.GOLD + "minecraft-heads.com", (success, text) -> {
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
                            ((ManageWarpInventoryController) controller).getPayload().getSalt(), cur.toJson()));
            GUIRegistry.register(new ConfirmationController(getPlayer(), payload, ConfirmationController.Type.HEAD_UPDATE));
        }
    }

    public static class ManageWarpInventoryController extends GUIController {

        private final WarpConfigPayload payload;
        private Warp warp;

        public ManageWarpInventoryController(Player p, WarpConfigPayload payload, int warpId) {
            super(27, "Edit Warp Settings", p);
            this.payload = payload;
            for (WarpGroup wg : payload.getGroups()) {
                for (Warp w : wg.getWarps()) {
                    if (w.getId() == warpId) {
                        this.warp = w;
                    }
                }
            }
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
