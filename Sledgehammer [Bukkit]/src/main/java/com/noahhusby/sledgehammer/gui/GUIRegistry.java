package com.noahhusby.sledgehammer.gui;

import com.google.common.collect.Maps;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GUIRegistry {
    private final static List<IGUIListener> registeredListeners = new ArrayList<>();

    public static void register(IGUIListener i) {
        registeredListeners.add(i);
    }

    public static void onInventoryClick(InventoryClickEvent e) {
        for(IGUIListener i : registeredListeners) {
            for(Inventory inv : i.getInventories()) {
                if(inv.getName().equals(e.getInventory().getName())) i.onInventoryClick(e);
            }
        }
    }

    public static void onInventoryClose(InventoryCloseEvent e) {
        for(IGUIListener i : registeredListeners) {
            for(Inventory inv : i.getInventories()) {
                if(inv.getName().equals(e.getInventory().getName())) i.onInventoryClose(e);
            }
        }
    }
}
