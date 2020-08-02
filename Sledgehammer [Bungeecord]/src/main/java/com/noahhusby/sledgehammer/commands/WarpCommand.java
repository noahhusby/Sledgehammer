package com.noahhusby.sledgehammer.commands;

import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.handlers.CommunicationHandler;
import com.noahhusby.sledgehammer.handlers.WarpHandler;
import com.noahhusby.sledgehammer.util.ChatHelper;
import com.noahhusby.sledgehammer.util.TextElement;
import com.noahhusby.sledgehammer.util.Warp;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

public class WarpCommand extends Command {

    public WarpCommand() {
        super(Sledgehammer.configuration.getString("warp-command"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission("sledgehammer.warp")) {
            sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("You don't have access to that command!", ChatColor.RED)));
            return;
        }

        if(args.length < 1) {
            sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("Usage: /"+Sledgehammer.configuration.getString(
                    "warp-command")+" <warp>", ChatColor.RED)));
            sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("Usage: /"+Sledgehammer.configuration.getString(
                    "warp-command")+" list to see the available warps.", ChatColor.RED)));
            return;
        }

        if(args[0].equals("set")) {
            if(!sender.hasPermission("sledgehammer.warp.admin")) {
                sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("You don't have access to that command!", ChatColor.RED)));
                return;
            }

            if(args.length < 2) {
                sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("Usage: [/"+Sledgehammer.configuration.getString(
                        "warp-command")+" set <warp name>] to set a warp.", ChatColor.RED)));
                return;
            }
            WarpHandler.getInstance().requestNewWarp(args[1], sender);
        } else if(args[0].equals("list")) {

        } else {
            Warp warp = WarpHandler.getInstance().getWarp(args[0]);
            if(warp == null) {
                sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("Error: Warp not found", ChatColor.RED)));
                return;
            }

            if(ProxyServer.getInstance().getPlayer(sender.getName()).getServer().getInfo() != ProxyServer.getInstance().getServerInfo(warp.server)) {
                ProxyServer.getInstance().getPlayer(sender.getName()).connect(ProxyServer.getInstance().getServerInfo(warp.server));
            }
            CommunicationHandler.executeCommand(ProxyServer.getInstance().getServerInfo(warp.server), "teleport", sender.getName(), warp.point.x,
                    warp.point.y, warp.point.z);
        }
    }


}
