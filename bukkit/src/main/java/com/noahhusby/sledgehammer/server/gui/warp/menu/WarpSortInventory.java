package com.noahhusby.sledgehammer.server.gui.warp.menu;

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.common.warps.WarpPayload;
import com.noahhusby.sledgehammer.server.gui.GUIChild;
import com.noahhusby.sledgehammer.server.gui.GUIController;
import com.noahhusby.sledgehammer.server.gui.GUIRegistry;
import com.noahhusby.sledgehammer.server.network.NetworkHandler;
import com.noahhusby.sledgehammer.server.network.S2P.S2PWarpConfigPacket;
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

        byte pinColor = payload.getDefaultPage() == WarpPayload.Page.PINNED ? (byte) 14 : (byte) 0;
        byte allColor = payload.getDefaultPage() == WarpPayload.Page.ALL ? (byte) 14 : (byte) 0;
        byte groupColor = payload.getDefaultPage() == WarpPayload.Page.GROUPS ? (byte) 14 : (byte) 0;

        {
            ItemStack item = new ItemStack(Material.WOOL, 1, pinColor);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Pinned Warps");

            List<String> lore = new ArrayList<>();
            if (payload.getDefaultPage() == WarpPayload.Page.PINNED) {
                lore.add(ChatColor.RED + "Current Default");
            }
            lore.add(ChatColor.BLUE + "This will show pinned warps when opened.");
            meta.setLore(lore);

            item.setItemMeta(meta);

            inventory.setItem(11, item);
        }

        {
            ItemStack item = new ItemStack(Material.WOOL, 1, allColor);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "All Warps");

            List<String> lore = new ArrayList<>();
            if (payload.getDefaultPage() == WarpPayload.Page.ALL) {
                lore.add(ChatColor.RED + "Current Default");
            }
            lore.add(ChatColor.BLUE + "This will show all warps when opened.");
            meta.setLore(lore);

            item.setItemMeta(meta);

            inventory.setItem(13, item);
        }

        {
            ItemStack item = new ItemStack(Material.WOOL, 1, groupColor);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Group Warps");

            List<String> lore = new ArrayList<>();
            if (payload.getDefaultPage() == WarpPayload.Page.GROUPS) {
                lore.add(ChatColor.RED + "Current Default");
            }
            lore.add(ChatColor.BLUE + "This will show your local group when opened.");
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
            payload.setDefaultPage(WarpPayload.Page.PINNED);
            data.addProperty("sort", "pinned");
        }

        if (e.getSlot() == 13) {
            payload.setDefaultPage(WarpPayload.Page.ALL);
            data.addProperty("sort", "all");
        }

        if (e.getSlot() == 15) {
            payload.setDefaultPage(WarpPayload.Page.GROUPS);
            data.addProperty("sort", "group");
        }

        NetworkHandler.getInstance().send(new S2PWarpConfigPacket(S2PWarpConfigPacket.ProxyConfigAction.UPDATE_PLAYER_DEFAULT,
                player, controller.getPayload().getSalt(), data));

        controller.close();
        GUIRegistry.register(new AllWarpInventory.AllWarpInventoryController(getPlayer(), payload));
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
