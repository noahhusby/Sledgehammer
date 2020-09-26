package com.noahhusby.sledgehammer.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public interface IGUIChild {
    Inventory getInventory();
    void onInventoryClick(InventoryClickEvent e);
    void init();
}
