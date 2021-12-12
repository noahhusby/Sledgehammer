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

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public abstract class GUIController implements IController {
    @Getter
    private final Inventory inventory;
    @Getter
    private final Player player;

    private GUIChild currentChild = null;

    public GUIController(int size, String name, Player player) {
        inventory = Bukkit.createInventory(null, size, name);
        player.openInventory(inventory);
        this.player = player;
    }

    public GUIController(GUIController controller) {
        this.player = controller.player;
        this.inventory = controller.inventory;
    }

    public void onInventoryClick(InventoryClickEvent e) {
        currentChild.onInventoryClick(e);
    }

    public void openChild(GUIChild child) {
        if (child == null) {
            return;
        }
        this.currentChild = child;
        inventory.setContents(child.getInventory().getContents());
        player.updateInventory();
    }

    public void close() {
        player.closeInventory();
    }

    public void init() {
    }
}
