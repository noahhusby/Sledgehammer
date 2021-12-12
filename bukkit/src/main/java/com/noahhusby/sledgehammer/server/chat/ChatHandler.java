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

package com.noahhusby.sledgehammer.server.chat;

import com.google.common.collect.Maps;
import com.noahhusby.sledgehammer.server.Sledgehammer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.function.BiConsumer;

public class ChatHandler implements Listener {
    private static ChatHandler instance = null;

    public static ChatHandler getInstance() {
        return instance == null ? instance = new ChatHandler() : instance;
    }

    private final Map<Player, BiConsumer<Boolean, String>> entries = Maps.newHashMap();

    public void startEntry(Player player, String message, BiConsumer<Boolean, String> consumer) {
        entries.remove(player);

        entries.put(player, consumer);
        for (int i = 0; i < 18; i++) {
            player.sendMessage();
        }

        player.sendMessage(ChatColor.RED + "--------------------------------------------------");
        player.sendMessage(message);
        player.sendMessage(ChatColor.GRAY + "Send " + ChatColor.GOLD + "'cancel' " + ChatColor.GRAY + "to cancel.");
        player.sendMessage(ChatColor.RED + "--------------------------------------------------");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        boolean cancel = entries.containsKey(e.getPlayer());
        if (!cancel) {
            return;
        }
        e.setCancelled(true);

        Sledgehammer.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Sledgehammer.getInstance(), () -> {
            boolean cancelled = e.getMessage().equalsIgnoreCase("cancel");
            entries.get(e.getPlayer()).accept(!cancelled, e.getMessage());
            entries.remove(e.getPlayer());
        });
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent e) {
        entries.remove(e.getPlayer());
    }

}
