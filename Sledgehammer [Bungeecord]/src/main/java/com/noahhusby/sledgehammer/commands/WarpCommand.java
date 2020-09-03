package com.noahhusby.sledgehammer.commands;

import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.commands.data.Command;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.handlers.TaskHandler;
import com.noahhusby.sledgehammer.handlers.WarpHandler;
import com.noahhusby.sledgehammer.tasks.TeleportTask;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import com.noahhusby.sledgehammer.util.ChatHelper;
import com.noahhusby.sledgehammer.util.TextElement;
import com.noahhusby.sledgehammer.util.Warp;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;

public class WarpCommand extends Command {

    public WarpCommand() {
        super(ConfigHandler.getInstance().getConfiguration().getString("warp-command"), "sledgehammer.warp");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!hasGeneralPermission(sender)) {
            sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("You don't have access to that command!", ChatColor.RED)));
            return;
        }

        if(args.length < 1) {
            sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("Usage: /"+ConfigHandler.getInstance().getConfiguration().getString(
                    "warp-command")+" <warp>", ChatColor.RED)));
            sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("Usage: /"+ConfigHandler.getInstance().getConfiguration().getString(
                    "warp-command")+" list to see the available warps.", ChatColor.RED)));
            return;
        }

        if(args[0].equals("set")) {
            if(!hasPermissionAdmin(sender)) {
                sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("You don't have access to that command!", ChatColor.RED)));
                return;
            }

            if(args.length < 2) {
                sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("Usage: [/"+ConfigHandler.getInstance().getConfiguration().getString(
                        "warp-command")+" set <warp name>] to set a warp.", ChatColor.RED)));
                return;
            }
            WarpHandler.getInstance().requestNewWarp(args[1], sender);
        } else if(args[0].equals("delete") || args[0].equals("remove")) {
            if (!hasPermissionAdmin(sender)) {
                sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("You don't have access to that command!", ChatColor.RED)));
                return;
            }

            if (args.length < 2) {
                sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("Usage: [/" + ConfigHandler.getInstance().getConfiguration().getString(
                        "warp-command") + " remove <warp name>] to remove a warp.", ChatColor.RED)));
                return;
            }
            WarpHandler.getInstance().removeWarp(args[1], sender);
        } else if(args[0].equals("list")) {
            sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("Warps: ", ChatColor.GOLD),
                    new TextElement(WarpHandler.getInstance().getWarpList(), ChatColor.RED)));
        } else {
            Warp warp = WarpHandler.getInstance().getWarp(args[0]);
            if(warp == null) {
                sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("Error: Warp not found", ChatColor.RED)));
                return;
            }

            if(ProxyServer.getInstance().getPlayer(sender.getName()).getServer().getInfo() != ProxyServer.getInstance().getServerInfo(warp.server)) {
                ProxyServer.getInstance().getPlayer(sender.getName()).connect(ProxyServer.getInstance().getServerInfo(warp.server));
            }

            TransferPacket t = new TransferPacket(ProxyServer.getInstance().getServerInfo(warp.server), sender.getName());
            TaskHandler.getInstance().execute(new TeleportTask(t, warp.point));
        }
    }


}
