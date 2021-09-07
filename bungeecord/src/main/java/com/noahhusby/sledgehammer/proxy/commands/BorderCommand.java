/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - BorderCommands.java
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

package com.noahhusby.sledgehammer.proxy.commands;

import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.players.Permission;
import com.noahhusby.sledgehammer.proxy.players.PlayerHandler;
import com.noahhusby.sledgehammer.proxy.players.SledgehammerPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BorderCommand extends Command {
    public BorderCommand() {
        super("border", "sledgehammer.border");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(ChatUtil.getPlayerOnly());
            return;
        }

        if (!isAllowed(sender)) {
            sender.sendMessage(ChatUtil.getNotAvailable());
            return;
        }

        Permission permission = SledgehammerPlayer.getPlayer(sender).getPermission("sledgehammer.border");
        if(permission.isLocal()) {
            SledgehammerPlayer p = PlayerHandler.getInstance().getPlayer(sender);
            if (args.length == 0) {
                if (p.checkAttribute("BORDER_MODE", false)) {
                    sender.sendMessage(ChatUtil.titleAndCombine(ChatColor.GRAY, "Border teleportation is currently set to ", ChatColor.RED, "off!"));
                    sender.sendMessage(ChatUtil.combine(ChatColor.GRAY, "Use ", ChatColor.YELLOW, "/border on", ChatColor.GRAY, " to turn it on!"));
                    return;
                }

                sender.sendMessage(ChatUtil.titleAndCombine(ChatColor.GRAY, "Border teleportation is currently set to ", ChatColor.GREEN, "on!"));
                sender.sendMessage(ChatUtil.combine(ChatColor.GRAY, "Use ", ChatColor.YELLOW, "/border off", ChatColor.GRAY, " to turn it off!"));
                return;
            }

            String command = args[0];

            if (command.equalsIgnoreCase("on")) {
                sender.sendMessage(ChatUtil.combine(ChatColor.GRAY, "Border teleportation has been set to ", ChatColor.GREEN, "on!"));
                p.getAttributes().put("BORDER_MODE", true);

                return;
            }

            if (command.equalsIgnoreCase("off")) {
                sender.sendMessage(ChatUtil.combine(ChatColor.GRAY, "Border teleportation has been set to ", ChatColor.RED, "off!"));
                p.getAttributes().put("BORDER_MODE", false);
                return;
            }

            sender.sendMessage(ChatUtil.combine(ChatColor.RED, "Usage: /border [on/off]"));
            return;
        }
        sender.sendMessage(ChatUtil.getNoPermission());
    }
}
