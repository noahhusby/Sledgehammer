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

package com.noahhusby.sledgehammer.proxy.commands;

import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.players.Permission;
import com.noahhusby.sledgehammer.proxy.players.PlayerHandler;
import com.noahhusby.sledgehammer.proxy.players.SledgehammerPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.concurrent.CompletableFuture;

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

        CompletableFuture<Permission> permissionFuture = SledgehammerPlayer.getPlayer(sender).getPermission("sledgehammer.border");
        permissionFuture.thenAccept(permission -> {
            if (permission.isLocal()) {
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
        });
    }
}
