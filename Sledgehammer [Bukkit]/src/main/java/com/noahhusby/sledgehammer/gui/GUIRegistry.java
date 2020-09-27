/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bukkit] - GUIRegistry.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.gui;

import com.google.common.collect.Maps;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class GUIRegistry {
    private final static List<GUIController> registeredControllers = new ArrayList<>();

    public static void register(GUIController c) {
        removeExpiredControllers(c);
        registeredControllers.add(c);
    }

    public static void onInventoryClick(InventoryClickEvent e) {

        for(GUIController i : registeredControllers) {
            if(i.getInventory().getName().equals(e.getInventory().getName())) i.onInventoryClick(e);
        }
    }

    public static void onInventoryClose(InventoryCloseEvent e) {

    }

    private static void removeExpiredControllers(GUIController newListener) {
        List<GUIController> expired = new ArrayList<>();
        for(GUIController i : registeredControllers) {
            if (i.getPlayer() != null) {
                if(i.getPlayer().getName().equalsIgnoreCase(newListener.getPlayer().getName())) expired.add(i);
            }
        }

        for(GUIController i : expired) {
            registeredControllers.remove(i);
        }
    }
}
