/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - DialogHandler.java
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
 *  You should have received a copy of the GNU General Public License
 *  along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.proxy.dialogs;

import com.google.common.collect.Maps;
import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.Sledgehammer;
import com.noahhusby.sledgehammer.proxy.dialogs.scenes.IDialogScene;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.Map;

public class DialogHandler implements Listener {
    private static DialogHandler instance = null;

    public static DialogHandler getInstance() {
        return instance == null ? instance = new DialogHandler() : instance;
    }

    private DialogHandler() {
        Sledgehammer.addListener(this);
    }

    private final Map<CommandSender, IDialogScene> activeScenes = Maps.newHashMap();

    /**
     * Start a dialog scene
     *
     * @param c The {@link CommandSender} that the dialog should be shown to
     * @param s The {@link com.noahhusby.sledgehammer.proxy.dialogs.scenes.DialogScene} that should be shown
     */
    public void startDialog(CommandSender c, IDialogScene s) {
        activeScenes.put(c, s);
        s.init(c);
    }

    /**
     * Stop the dialog from running
     *
     * @param s The {@link IDialogScene} to discard
     */
    public void discardDialog(IDialogScene s) {
        activeScenes.remove(s.getCommandSender(), s);
    }

    /**
     * Move the dialog to the next component
     *
     * @param s     The scene to progress
     * @param error True if the last entry was incorrect, false if correct
     */
    public void progressDialog(IDialogScene s, boolean error) {
        for (int x = 0; x < 20; x++) {
            s.getCommandSender().sendMessage();
        }

        if (error) {
            s.getCommandSender().sendMessage(ChatUtil.combine(ChatColor.RED, "Invalid entry!"));
            s.getCommandSender().sendMessage();
        }

        if (s.getTitle() != null) {
            s.getCommandSender().sendMessage(ChatUtil.combine(s.getTitle()));
        }

        if (s.getToolbar() != null) {
            Map<String, String> tools = s.getToolbar().getTools();
            for (Map.Entry<String, String> x : tools.entrySet()) {
                s.getCommandSender().sendMessage(ChatUtil.combine(ChatColor.GRAY, "Use ", ChatColor.BLUE, x.getKey(),
                        ChatColor.GRAY, " to ", x.getValue()));
            }
            s.getCommandSender().sendMessage();
        }

        if (s.isAdmin()) {
            s.getCommandSender().sendMessage(ChatUtil.adminAndCombine(ChatColor.YELLOW, s.getCurrentComponent().getPrompt()));
        } else {
            s.getCommandSender().sendMessage(ChatUtil.titleAndCombine(ChatColor.YELLOW, s.getCurrentComponent().getPrompt()));
        }
        if (s.getCurrentComponent().getExplanation() != null) {
            s.getCommandSender().sendMessage(ChatUtil.combine(s.getCurrentComponent().getExplanation()));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatEvent(ChatEvent e) {
        for (Map.Entry<CommandSender, IDialogScene> l : activeScenes.entrySet()) {
            ProxiedPlayer p = ProxyServer.getInstance().getPlayer(l.getKey().getName());
            if (p != null) {
                if (p.equals(e.getSender())) {
                    e.setCancelled(true);
                    l.getValue().onMessage(e.getMessage());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLeave(PlayerDisconnectEvent e) {
        for (CommandSender c : activeScenes.keySet()) {
            if (c.getName().equals(e.getPlayer().getName())) {
                activeScenes.remove(c);
                return;
            }
        }
    }

}
