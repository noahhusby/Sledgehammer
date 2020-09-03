package com.noahhusby.sledgehammer.handlers;

import com.google.common.collect.Maps;
import com.google.gson.annotations.Expose;
import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.tasks.SetWarpTask;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import com.noahhusby.sledgehammer.util.ChatHelper;
import com.noahhusby.sledgehammer.datasets.Point;
import com.noahhusby.sledgehammer.util.ProxyUtil;
import com.noahhusby.sledgehammer.util.TextElement;
import com.noahhusby.sledgehammer.util.Warp;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.Map;

public class WarpHandler {
    private static WarpHandler instance;

    public static WarpHandler getInstance() {
        return instance;
    }

    public static void setInstance(WarpHandler instance) {
        WarpHandler.instance = instance;
    }

    public WarpHandler() {}

    @Expose(serialize = true, deserialize = true)
    public Map<String, Warp> warps = Maps.newHashMap();

    public Map<String, String> requestedWarps = Maps.newHashMap();

    public Warp getWarp(String w) {
        return warps.get(w.toLowerCase());
    }

    public void requestNewWarp(String warp, CommandSender sender) {
        requestedWarps.put(sender.getName(), warp);

        TransferPacket t = new TransferPacket(ProxyUtil.getServerFromPlayerName(sender.getName()), sender.getName());
        TaskHandler.getInstance().execute(new SetWarpTask(t));
        //CommunicationHandler.executeRequest(ProxyServer.getInstance().getPlayer(sender.getName()).getServer().getInfo(),
        //        sender.getName(), "POS");
    }

    public void removeWarp(String w, CommandSender sender) {
        if(warps.containsKey(w.toLowerCase())) {
            warps.remove(w.toLowerCase());
            sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("Successfully removed ", ChatColor.GOLD),
                    new TextElement(ChatHelper.capitalize(w), ChatColor.RED)));
            ConfigHandler.getInstance().saveWarpDB();
            return;
        }

        sender.sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("Warp could not be removed", ChatColor.RED)));

    }

    public void incomingLocationResponse(String sender, Point p) {
        if(requestedWarps.containsKey(sender)) {
            addWarp(sender, requestedWarps.get(sender), p, ProxyServer.getInstance().getPlayer(sender).getServer().getInfo());
            requestedWarps.remove(sender);
        }
    }

    public String getWarpList() {
        String warpList = "";
        boolean first = true;
        for(String s : warps.keySet()) {
            if(first) {
                warpList = ChatHelper.capitalize(s);
                first = false;
            } else {
                warpList += ", "+ChatHelper.capitalize(s);
            }
        }
        return warpList;
    }

    private void addWarp(String sender, String w, Point p, ServerInfo s) {
        warps.remove(w.toLowerCase());

        warps.put(w.toLowerCase(), new Warp(p, s.getName()));

        ProxyServer.getInstance().getPlayer(sender).sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("Created warp ", ChatColor.GOLD),
                new TextElement(ChatHelper.capitalize(w), ChatColor.RED)));
        ConfigHandler.getInstance().saveWarpDB();
    }



}
