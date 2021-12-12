/*
 * MIT License
 *
 * Copyright 2020-2021 noahhusby
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
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
