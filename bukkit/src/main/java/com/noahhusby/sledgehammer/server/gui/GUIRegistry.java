/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - GUIRegistry.java
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

import com.google.common.collect.Maps;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Map;
import java.util.UUID;

public class GUIRegistry implements Listener {
    private final static Map<UUID, IController> controllers = Maps.newHashMap();

    public static void register(IController c) {
        controllers.put(c.getPlayer().getUniqueId(), c);
    }

    public static void onInventoryClick(InventoryClickEvent e) {
        IController controller = controllers.get(e.getWhoClicked().getUniqueId());
        if(controller instanceof AnvilController) return;
        ((GUIController) controller).onInventoryClick(e);
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        onInventoryClick(e);
    }
}
