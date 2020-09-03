package com.noahhusby.sledgehammer.commands.admin;

import com.noahhusby.sledgehammer.commands.data.IAdminCommand;
import com.noahhusby.sledgehammer.util.ChatHelper;
import com.noahhusby.sledgehammer.util.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PermissionListAdminCommand implements IAdminCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("Permissions:", ChatColor.RED)));
        ProxiedPlayer p;
        if(args.length == 0) {
            p = ProxyServer.getInstance().getPlayer(sender.getName());
        } else {
            p = ProxyServer.getInstance().getPlayer(args[0]);
        }

        if(p == null) {
            sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement(args[0], ChatColor.RED),
                    new TextElement(" could not be found on the network!", ChatColor.DARK_RED)));
            return;
        }

        for(String s : p.getPermissions()) {
            if(s.contains("sledgehammer")) sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement(s, ChatColor.GOLD)));
        }
    }

    @Override
    public String getName() {
        return "p";
    }

    @Override
    public String getPurpose() {
        return "List all sledgehammer permissions for a given player";
    }
}
