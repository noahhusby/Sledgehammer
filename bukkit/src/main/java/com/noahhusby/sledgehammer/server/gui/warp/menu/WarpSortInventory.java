package com.noahhusby.sledgehammer.server.gui.warp.menu;

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.server.data.warp.WarpPayload;
import com.noahhusby.sledgehammer.server.gui.GUIChild;
import com.noahhusby.sledgehammer.server.gui.GUIRegistry;
import com.noahhusby.sledgehammer.server.network.NetworkHandler;
import com.noahhusby.sledgehammer.server.network.S2P.S2PWarpConfigPacket;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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

        byte pinColor = payload.getDefaultPage().equalsIgnoreCase("pinned") ? (byte) 14 : (byte) 0;
        byte allColor = payload.getDefaultPage().equalsIgnoreCase("all") ? (byte) 14 : (byte) 0;
        byte groupColor = payload.getDefaultPage().equalsIgnoreCase("group") ? (byte) 14 : (byte) 0;

        {
            ItemStack item = new ItemStack(Material.WOOL, 1, pinColor);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Pinned Warps");

            List<String> lore = new ArrayList<>();
            if (payload.getDefaultPage().equalsIgnoreCase("pinned")) {
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
            if (payload.getDefaultPage().equalsIgnoreCase("all")) {
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
            if (payload.getDefaultPage().equalsIgnoreCase("group")) {
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
            payload.setDefaultPage("pinned");
            data.addProperty("sort", "pinned");
        }

        if (e.getSlot() == 13) {
            payload.setDefaultPage("all");
            data.addProperty("sort", "all");
        }

        if (e.getSlot() == 15) {
            payload.setDefaultPage("group");
            data.addProperty("sort", "group");
        }

        NetworkHandler.getInstance().send(new S2PWarpConfigPacket(S2PWarpConfigPacket.ProxyConfigAction.UPDATE_PLAYER_DEFAULT,
                player, controller.getPayload().getSalt(), data));

        controller.close();
        GUIRegistry.register(new AllWarpInventoryController(getPlayer(), payload));
    }
}
