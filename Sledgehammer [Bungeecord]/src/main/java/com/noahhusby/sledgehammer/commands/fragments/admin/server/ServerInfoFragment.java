package com.noahhusby.sledgehammer.commands.fragments.admin.server;

import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.types.Server;
import com.noahhusby.sledgehammer.util.ChatHelper;
import com.noahhusby.sledgehammer.util.TextElement;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.config.ServerInfo;

public class ServerInfoFragment implements ICommandFragment {
    @Override
    public void execute(CommandSender sender, String[] args) {
        Server server = ServerConfig.getInstance().getServer(args[0]);
        ServerInfo info = ProxyServer.getInstance().getServerInfo(args[0]);
        sender.sendMessage();
        sender.sendMessage(ChatHelper.getInstance().makeAdminTextComponent(new TextElement("Server Info - ", ChatColor.GRAY),
                new TextElement(info.getName(), ChatColor.BLUE)));
        sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("Name: ", ChatColor.GRAY),
                new TextElement(info.getName(), ChatColor.BLUE)));
        if(server == null) {
            sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("Status: ", ChatColor.GRAY),
                    new TextElement("Unconfigured", ChatColor.RED)));
            return;
        } else if (!server.isInitialized()){
            sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("Status: ", ChatColor.GRAY),
                    new TextElement("Configured", ChatColor.GREEN), new TextElement(" (Not Initialized)", ChatColor.RED)));
            sender.sendMessage();
            if(server.earthServer) {
                sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("Earth: ", ChatColor.GRAY),
                        new TextElement("Yes", ChatColor.GREEN)));
                sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("Permission Mode: ", ChatColor.GRAY),
                        new TextElement(ChatHelper.capitalize(server.permission_type), ChatColor.RED)));
            } else {
                sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("Earth: ", ChatColor.GRAY),
                        new TextElement("No", ChatColor.RED)));
            }
        } else {
            sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("Status: ", ChatColor.GRAY),
                    new TextElement("Initialized", ChatColor.GREEN)));
            sender.sendMessage();
            if(server.earthServer) {
                sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("Earth: ", ChatColor.GRAY),
                        new TextElement("Yes", ChatColor.GREEN)));
                sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("Permission Mode: ", ChatColor.GRAY),
                        new TextElement(ChatHelper.capitalize(server.permission_type), ChatColor.RED)));
            } else {
                sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("Earth: ", ChatColor.GRAY),
                        new TextElement("No", ChatColor.RED)));
            }
            sender.sendMessage();
            sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("SH Version: ", ChatColor.GRAY),
                    new TextElement(server.getSledgehammerVersion(), ChatColor.BLUE)));
            sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("Tpll Mode: ", ChatColor.GRAY),
                    new TextElement(server.getTpllMode(), ChatColor.BLUE)));
        }
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getPurpose() {
        return "List info about server";
    }

    @Override
    public String[] getArguments() {
        return null;
    }
}
