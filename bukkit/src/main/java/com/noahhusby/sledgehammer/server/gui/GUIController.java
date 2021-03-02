/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - GUIController.java
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
