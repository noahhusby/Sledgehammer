package com.noahhusby.sledgehammer.commands;

import com.noahhusby.sledgehammer.commands.data.Command;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.handlers.TaskHandler;
import com.noahhusby.sledgehammer.handlers.WarpHandler;
import com.noahhusby.sledgehammer.maps.MapHandler;
import com.noahhusby.sledgehammer.tasks.TeleportTask;
import com.noahhusby.sledgehammer.tasks.WarpGUITask;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import com.noahhusby.sledgehammer.util.ChatHelper;
import com.noahhusby.sledgehammer.util.TextElement;
import com.noahhusby.sledgehammer.util.Warp;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;

public class WarpCommand extends Command {

    public WarpCommand() {
        super(ConfigHandler.warpCommand, "sledgehammer.warp");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!hasGeneralPermission(sender)) {
            sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("You don't have permission to run this command!", ChatColor.DARK_RED)));
            return;
        }

        if(args.length < 1) {
            sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("Usage: /"+ConfigHandler.warpCommand+" <warp>", ChatColor.RED)));
            sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("Use ", ChatColor.GRAY),
                    new TextElement("/"+ConfigHandler.warpCommand+" list", ChatColor.BLUE),new TextElement(" to see the available warps.", ChatColor.GRAY)));
            return;
        }

        if(args[0].equals("set")) {
            if(!hasPermissionAdmin(sender)) {
                sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("You don't have permission to run this command!", ChatColor.DARK_RED)));
                return;
            }

            if(args.length < 2) {
                sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("Use ", ChatColor.GRAY),
                                new TextElement("/"+ConfigHandler.warpCommand+" set <warp name>", ChatColor.BLUE), new TextElement(" to set a warp.", ChatColor.GRAY)));
                return;
            }
            WarpHandler.getInstance().requestNewWarp(args[1], sender);
        } else if(args[0].equals("delete") || args[0].equals("remove")) {
            if (!hasPermissionAdmin(sender)) {
                sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("You don't have permission to run this command!", ChatColor.DARK_RED)));
                return;
            }

            if (args.length < 2) {
                sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("Use ", ChatColor.GRAY),
                        new TextElement("/"+ConfigHandler.warpCommand+" remove <warp name>", ChatColor.BLUE), new TextElement(" to remove set a warp.", ChatColor.GRAY)));

                return;
            }
            WarpHandler.getInstance().removeWarp(args[1], sender);
        } else if(args[0].equals("list")) {
            sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("Warps: ", ChatColor.GRAY),
                    new TextElement(WarpHandler.getInstance().getWarpList(), ChatColor.RED)));
        } if(args[0].equals("map")) {
            MapHandler.getInstance().newMapCommand(sender);
        } else {
            Warp warp = WarpHandler.getInstance().getWarp(args[0]);
            if(warp == null) {
                sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("Error: Warp not found", ChatColor.RED)));
                return;
            }

            if(ProxyServer.getInstance().getPlayer(sender.getName()).getServer().getInfo() != ProxyServer.getInstance().getServerInfo(warp.server)) {
                ProxyServer.getInstance().getPlayer(sender.getName()).connect(ProxyServer.getInstance().getServerInfo(warp.server));
                sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("Sending you to ", ChatColor.GRAY), new TextElement(warp.server, ChatColor.RED)));
            }

            sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("Warping to ", ChatColor.GRAY), new TextElement(args[0], ChatColor.RED)));
            TransferPacket t = new TransferPacket(ProxyServer.getInstance().getServerInfo(warp.server), sender.getName());
            TaskHandler.getInstance().execute(new WarpGUITask(t));
            TaskHandler.getInstance().execute(new TeleportTask(t, warp.point));
        }
    }


}
