package com.noahhusby.sledgehammer.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.List;

public interface IGUIListener {
    void onInventoryClick(InventoryClickEvent e);
    void onInventoryClose(InventoryCloseEvent e);
    List<Inventory> getInventories();
}
