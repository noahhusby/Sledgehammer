package com.noahhusby.sledgehammer.handlers;

import com.google.common.collect.Maps;
import com.google.gson.annotations.Expose;
import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.util.ChatHelper;
import com.noahhusby.sledgehammer.util.Point;
import com.noahhusby.sledgehammer.util.TextElement;
import com.noahhusby.sledgehammer.util.Warp;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import org.apache.commons.lang3.StringUtils;

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
        CommunicationHandler.executeRequest(ProxyServer.getInstance().getPlayer(sender.getName()).getServer().getInfo(),
                sender.getName(), "WARP_LOC");

    }

    public void incomingLocationResponse(String sender, Point p) {
        if(requestedWarps.containsKey(sender)) {
            addWarp(sender, requestedWarps.get(sender), p, ProxyServer.getInstance().getPlayer(sender).getServer().getInfo());
            requestedWarps.remove(sender);
        }
    }

    private void addWarp(String sender, String w, Point p, ServerInfo s) {
        //Sledgehammer.loadWarpDB();
        warps.remove(w.toLowerCase());

        warps.put(w.toLowerCase(), new Warp(p, s.getName()));

        ProxyServer.getInstance().getPlayer(sender).sendMessage(ChatHelper.getInstance().makeTitleTextComponent(new TextElement("Created warp ", ChatColor.GOLD),
                new TextElement(StringUtils.capitalize(w), ChatColor.RED)));
        Sledgehammer.saveWarpDB();
    }


    private void deleteWarp(String w) {

    }
}
