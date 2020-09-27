/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bukkit] - GUIController.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GUIController {
    private IGUIChild currentChild = null;
    private final Inventory inventory;
    private final Player player;

    public GUIController(int size, String name, Player player) {
        inventory = Bukkit.createInventory(null, size, name);
        player.openInventory(inventory);
        this.player = player;
    }

    public void onInventoryClick(InventoryClickEvent e) {
        currentChild.onInventoryClick(e);
    }

    public void openChild(IGUIChild child) {
        if(child == null) return;
        this.currentChild = child;
        inventory.setContents(child.getInventory().getContents());
        player.updateInventory();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Player getPlayer() {
        return player;
    }

    public void close() {
        player.closeInventory();
    }
}
