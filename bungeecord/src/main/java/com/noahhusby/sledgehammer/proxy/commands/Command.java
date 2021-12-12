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

package com.noahhusby.sledgehammer.proxy.commands;

import com.noahhusby.sledgehammer.proxy.network.NetworkHandler;
import com.noahhusby.sledgehammer.proxy.players.PlayerHandler;
import com.noahhusby.sledgehammer.proxy.players.SledgehammerPlayer;
import net.md_5.bungee.api.CommandSender;

public abstract class Command extends net.md_5.bungee.api.plugin.Command {
    private final String permissionNode;

    public Command(String name, String node) {
        super(name);
        this.permissionNode = node;
    }

    public Command(String name, String node, String[] alias) {
        super(name, "", alias);
        this.permissionNode = node;
    }

    protected boolean isAllowed(CommandSender sender) {
        if (!sender.hasPermission("sledgehammer.requiresh")) {
            return true;
        }
        return SledgehammerPlayer.getPlayer(sender).onSledgehammer();
    }

    protected boolean hasPerms(CommandSender sender) {
        return hasPerms(sender, false);
    }

    protected boolean hasPerms(CommandSender sender, boolean exact) {
        if (PlayerHandler.getInstance().isAdmin(sender)) {
            return true;
        }
        if (permissionNode == null) {
            return false;
        }

        if (sender.hasPermission(permissionNode + ".admin")) {
            return true;
        }
        for (String s : sender.getPermissions()) {
            if ((exact && s.equalsIgnoreCase(permissionNode)) || (!exact && s.contains(permissionNode))) {
                return true;
            }
        }

        return false;
    }

    protected boolean hasPerms(CommandSender sender, String specificNode) {
        if (PlayerHandler.getInstance().isAdmin(sender)) {
            return true;
        }
        if (permissionNode == null) {
            return false;
        }

        for (String s : sender.getPermissions()) {
            if (s == null) {
                continue;
            }
            if (s.equals(permissionNode + "." + specificNode)) {
                return true;
            }
        }

        return false;
    }

    public boolean isAdmin(CommandSender sender) {
        return PlayerHandler.getInstance().isAdmin(sender);
    }

    protected NetworkHandler getNetworkManager() {
        return NetworkHandler.getInstance();
    }
}

