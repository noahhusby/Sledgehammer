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

package com.noahhusby.sledgehammer.server.gui.warp.config.confirmation;

import com.google.common.collect.Lists;
import com.noahhusby.sledgehammer.server.Constants;
import com.noahhusby.sledgehammer.server.SledgehammerUtil;
import com.noahhusby.sledgehammer.server.gui.GUIChild;
import com.noahhusby.sledgehammer.server.gui.GUIRegistry;
import com.noahhusby.sledgehammer.server.gui.IController;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@RequiredArgsConstructor
public class CreationSuccessInventory extends GUIChild {
    private final IController c;
    private final String message;

    @Override
    public void init() {
        ItemStack skull = SledgehammerUtil.getSkull(Constants.Heads.limeCheckmark, ChatColor.GREEN + "" + ChatColor.BOLD + message);
        ItemMeta meta = skull.getItemMeta();
        meta.setLore(Lists.newArrayList(ChatColor.BLUE + "Click to continue"));
        skull.setItemMeta(meta);
        fillInventory(skull);
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if (c != null) {
            GUIRegistry.register(c);
        } else {
            controller.close();
        }
    }
}
