/*
 * MIT License
 *
 * Copyright 2020-2022 noahhusby
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

import com.google.common.collect.Maps;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Map;
import java.util.UUID;

public class GUIRegistry implements Listener {
    private final static Map<UUID, IController> controllers = Maps.newHashMap();

    public static void register(IController c) {
        controllers.put(c.getPlayer().getUniqueId(), c);
    }

    public static void onInventoryClick(InventoryClickEvent e) {
        IController controller = controllers.get(e.getWhoClicked().getUniqueId());
        if (controller == null || controller instanceof AnvilController) {
            return;
        }
        ((GUIController) controller).onInventoryClick(e);
    }

    public static void onInventoryClose(InventoryCloseEvent e) {
        controllers.remove(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onInventoryCloseEvent(InventoryCloseEvent e) {
        onInventoryClose(e);
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent e) {
        onInventoryClick(e);
    }
}
