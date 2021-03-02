/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - GUIChild.java
 *
 * Sledgehammer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sledgehammer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.server.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class GUIChild {
    protected Inventory inventory;
    protected GUIController controller;
    protected Player player;

    public void initFromController(GUIController controller, Player player, Inventory inventory) {
        this.inventory = Bukkit.createInventory(null, inventory.getSize());
        this.controller = controller;
        this.player = player;
        init();
    }

    public Inventory getInventory() {
        return inventory;
    }

    protected Player getPlayer() {
        return player;
    }

    protected GUIController getController() {
        return controller;
    }

    public abstract void onInventoryClick(InventoryClickEvent e);

    public abstract void init();

    protected ItemStack createItem(Material material, int amount, String name) {
        return createItem(material, amount, (byte) 0, name);
    }

    protected ItemStack createItem(Material material, int amount, byte meta, String name) {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE, amount, meta);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        item.setItemMeta(itemMeta);
        return item;
    }

    protected void setItem(int index, ItemStack item) {
        inventory.setItem(index, item);
    }

    protected void fillInventory(ItemStack item) {
        for (int x = 0; x < inventory.getSize(); x++) {
            setItem(x, item);
        }
    }
}
