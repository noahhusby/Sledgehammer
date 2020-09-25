package com.noahhusby.sledgehammer.eventhandler;

import com.noahhusby.sledgehammer.gui.GUIRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class ServerEventHandler implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        GUIRegistry.onInventoryClick(e);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        GUIRegistry.onInventoryClose(e);
    }
}
