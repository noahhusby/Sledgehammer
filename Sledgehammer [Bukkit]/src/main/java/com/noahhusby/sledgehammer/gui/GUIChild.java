package com.noahhusby.sledgehammer.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public abstract class GUIChild implements IGUIChild {
    protected Inventory inventory;
    protected GUIController controller;
    protected Player player;

    public void initFromController(GUIController controller, Player player, Inventory inventory) {
        this.inventory = Bukkit.createInventory(null, inventory.getSize());
        this.controller = controller;
        this.player = player;
        init();
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    protected Player getPlayer() {
        return player;
    }

    protected GUIController getController() {
        return controller;
    }
}
