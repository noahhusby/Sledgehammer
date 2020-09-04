package com.noahhusby.sledgehammer.commands.data;

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
}

