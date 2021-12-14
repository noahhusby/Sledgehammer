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

package com.noahhusby.sledgehammer.proxy.warp;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.noahhusby.lib.data.storage.StorageHashMap;
import com.noahhusby.sledgehammer.common.warps.Page;
import com.noahhusby.sledgehammer.common.warps.Point;
import com.noahhusby.sledgehammer.common.warps.Warp;
import com.noahhusby.sledgehammer.common.warps.WarpConfigPayload;
import com.noahhusby.sledgehammer.common.warps.WarpGroup;
import com.noahhusby.sledgehammer.common.warps.WarpGroupConfigPayload;
import com.noahhusby.sledgehammer.common.warps.WarpGroupPayload;
import com.noahhusby.sledgehammer.common.warps.WarpPayload;
import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.Constants;
import com.noahhusby.sledgehammer.proxy.SledgehammerUtil;
import com.noahhusby.sledgehammer.proxy.config.SledgehammerConfig;
import com.noahhusby.sledgehammer.proxy.network.NetworkHandler;
import com.noahhusby.sledgehammer.proxy.network.p2s.P2SSetwarpPacket;
import com.noahhusby.sledgehammer.proxy.players.SledgehammerPlayer;
import com.noahhusby.sledgehammer.proxy.servers.SledgehammerServer;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.function.Consumer;

public class WarpHandler {
    private static WarpHandler instance;

    public static WarpHandler getInstance() {
        return instance == null ? instance = new WarpHandler() : instance;
    }

    @Getter
    private final StorageHashMap<Integer, Warp> warps = new StorageHashMap<>(Integer.class, Warp.class);

    @Getter
    private final StorageHashMap<String, WarpGroup> warpGroups = new StorageHashMap<>(String.class, WarpGroup.class);

    @Getter
    private TreeMap<String, WarpGroup> warpGroupByServer = Maps.newTreeMap(String.CASE_INSENSITIVE_ORDER);
    private final Map<UUID, Warp> warpRequests = Maps.newHashMap();
    private final Map<Warp, Consumer<Warp>> warpConsumers = Maps.newHashMap();

    private WarpHandler() {
        warpGroups.onLoadEvent(this::refreshWarpGroupCache);
        warpGroups.onSaveEvent(this::refreshWarpGroupCache);
    }

    /**
     * Gets a warp by name
     *
     * @param name Name of warp
     * @return Warp if found, null if not
     */
    public List<Warp> getWarps(String name) {
        List<Warp> lw = Lists.newArrayList();
        for (Warp w : warps.values()) {
            if (w.getName().equalsIgnoreCase(name)) {
                lw.add(w);
            }
        }

        return lw;
    }

    /**
     * Gets a warp by it's ID
     *
     * @param id Warp ID
     * @return {@link Warp} if exists, null if not.
     */
    public Warp getWarp(int id) {
        return warps.get(id);
    }

    /**
     * Requests that a new warp should be created
     *
     * @param warpName The name of the warp
     * @param sender   The command sender
     */
    public void requestNewWarp(String warpName, CommandSender sender) {
        requestNewWarp(warpName, sender, null);
    }

    /**
     * Requests a new warp that should be created
     *
     * @param warpName The name of the warp
     * @param sender   The command sender
     * @param consumer The warp response
     */
    public void requestNewWarp(String warpName, CommandSender sender, Consumer<Warp> consumer) {
        Warp warp = new Warp();
        warp.setName(warpName);
        UUID uuid = ((ProxiedPlayer) sender).getUniqueId();
        warpRequests.put(uuid, warp);
        if (consumer != null) {
            warpConsumers.put(warp, consumer);
        }
        NetworkHandler.getInstance().send(new P2SSetwarpPacket(sender.getName(), SledgehammerUtil.getServerFromSender(sender)));
    }

    public void removeWarp(int warpID, CommandSender sender) {
        Warp warp = warps.remove(warpID);
        if (warp == null) {
            return;
        }
        sender.sendMessage(ChatUtil.titleAndCombine(ChatColor.GRAY, "Successfully removed ",
                ChatColor.RED, warp.getName(), ChatColor.GRAY, " from ", ChatColor.BLUE, warp.getServer()));
        warps.saveAsync();
    }

    /**
     * Called by incoming location packet to register a warp
     *
     * @param sender Name of sender
     * @param point  Location of warp
     */
    public void incomingLocationResponse(String sender, Point point) {
        synchronized (warpRequests) {
            SledgehammerPlayer player = SledgehammerPlayer.getPlayer(sender);
            Warp warp = warpRequests.remove(player.getUniqueId());

            if (warp == null) {
                return;
            }

            warp.setId(generateWarpID());
            warp.setPoint(point);
            warp.setServer(player.getServer().getInfo().getName());

            warps.put(warp.getId(), warp);
            warps.saveAsync();

            if (warpConsumers.containsKey(warp)) {
                warpConsumers.remove(warp).accept(warp);
            } else {
                player.sendMessage(ChatUtil.titleAndCombine(ChatColor.GRAY, "Created warp ", ChatColor.RED, warp.getName(), " on ",
                        ChatColor.RED, warp.getServer()));
            }
        }
    }

    public WarpStatus getWarpStatus(String warpName, String server) {
        WarpGroup serverWarpGroup = warpGroupByServer.get(server);
        for (Warp w : warps.values()) {
            if (w.getName().equalsIgnoreCase(warpName) && (!SledgehammerConfig.warps.localWarp || (serverWarpGroup != null && serverWarpGroup.getServers().contains(w.getServer())))) {
                return getWarpStatus(w.getId(), server);
            }
        }

        return WarpStatus.AVAILABLE;
    }

    public WarpStatus getWarpStatus(int warpId, String server) {
        boolean local = SledgehammerConfig.warps.localWarp;
        Warp warp = warps.get(warpId);
        if (warp == null) {
            return WarpStatus.AVAILABLE;
        }

        WarpGroup serverWarpGroup = warpGroupByServer.get(server);
        if (!local && warp.isGlobal()) {
            return WarpStatus.RESERVED;
        } else if (!local && serverWarpGroup != null && serverWarpGroup.getServers().contains(warp.getServer())) {
            return WarpStatus.EXISTS;
        }

        return WarpStatus.AVAILABLE;
    }

    /**
     * Gets list of warps as a String
     *
     * @return List of warps as String
     */
    public TextComponent getWarpList(String server) {
        TextComponent list = ChatUtil.titleAndCombine(ChatColor.RED, "Warps: ");
        boolean first = true;
        for (Warp w : warps.values()) {
            if (!(w.getServer().equalsIgnoreCase(server) || w.isGlobal() ||
                  !SledgehammerConfig.warps.localWarp)) {
                continue;
            }
            TextComponent t = new TextComponent(w.getName());
            t.setColor(ChatColor.BLUE);
            t.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/%s %s", SledgehammerConfig.warps.warpCommand, w.getName())));
            t.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                    new Text("Click to warp to " + w.getName())));
            if (first) {
                first = false;
            } else {
                list.addExtra(ChatUtil.combine(ChatColor.GRAY, ", "));
            }
            list.addExtra(t);
        }

        return list;
    }

    /**
     * Generates the payload for the warp GUI
     *
     * @param player     {@link SledgehammerPlayer}
     * @param editAccess True if player has permission to edit warps, false if not
     * @return GUI Payload
     */
    public WarpPayload generateGUIPayload(SledgehammerPlayer player, boolean editAccess) {
        SledgehammerServer s = player.getSledgehammerServer();
        if (s == null) {
            return null;
        }

        boolean local = SledgehammerConfig.warps.localWarp;
        Page page = Page.ALL;
        String localGroup = null;
        if (player.getAttributes().containsKey("WARP_SORT")) {
            if (player.checkAttribute("WARP_SORT", Page.GROUPS.name())) {
                page = Page.LOCAL_GROUP;
            } else if (player.checkAttribute("WARP_SORT", Page.SERVERS.name())) {
                page = Page.SERVERS;
            }
        } else {
            if (SledgehammerConfig.warps.warpMenuPage.equalsIgnoreCase("local")) {
                page = Page.LOCAL_GROUP;
            } else if (SledgehammerConfig.warps.warpMenuPage.equalsIgnoreCase("servers")) {
                page = Page.SERVERS;
            } else if (SledgehammerConfig.warps.warpMenuPage.equalsIgnoreCase("groups")) {
                page = Page.GROUPS;
            }
        }

        if (page == Page.LOCAL_GROUP) {
            WarpGroup warpGroup = warpGroupByServer.get(s.getName());
            if (warpGroup == null) {
                for (Warp warp : warps.values()) {
                    if (warp.getServer().equalsIgnoreCase(s.getName())) {
                        localGroup = s.getName();
                        page = Page.LOCAL_SERVER;
                        break;
                    }
                }
                if (page != Page.LOCAL_SERVER) {
                    page = Page.GROUPS;
                }
            } else {
                localGroup = warpGroup.getId();
            }
        }

        Map<String, WarpGroupPayload> groups = Maps.newHashMap();
        for (Map.Entry<String, WarpGroup> group : warpGroups.entrySet()) {
            groups.put(group.getKey(), toPayload(group.getValue()));
        }

        Map<Integer, Warp> warps = Maps.newHashMap();
        Map<String, List<Integer>> servers = Maps.newHashMap();
        for (Warp warp : this.warps.values()) {
            warps.put(warp.getId(), warp.toWaypoint());
            if (!servers.containsKey(warp.getServer())) {
                servers.put(warp.getServer(), Lists.newArrayList(warp.getId()));
            } else {
                servers.get(warp.getServer()).add(warp.getId());
            }
        }
        return new WarpPayload(page, editAccess, local, localGroup, player.trackAction(), warps, groups, servers);
    }

    /**
     * Generates payload for the warp configuration GUI
     *
     * @param player {@link SledgehammerPlayer}
     * @param admin  True if they are able to edit all groups
     * @return Config Payload
     */
    public WarpConfigPayload generateConfigPayload(SledgehammerPlayer player, boolean admin) {
        SledgehammerServer s = player.getSledgehammerServer();
        if (s == null) {
            return null;
        }
        String requestGroup = null;
        WarpGroup group = warpGroupByServer.get(s.getServerInfo().getName());
        if (group != null) {
            requestGroup = group.getId();
        }
        Map<String, List<Integer>> servers = Maps.newHashMap();
        Map<Integer, Warp> waypoints = Maps.newHashMap();
        for (Warp warp : warps.values()) {
            if (!admin && !((group != null && group.getServers().contains(warp.getServer())) || s.getName().equalsIgnoreCase(warp.getServer()))) {
                continue;
            }
            if (!servers.containsKey(warp.getServer())) {
                servers.put(warp.getServer(), Lists.newArrayList(warp.getId()));
            } else {
                servers.get(warp.getServer()).add(warp.getId());
            }
            waypoints.put(warp.getId(), warp.toWaypoint());
        }
        Map<String, WarpGroupPayload> groupPayload = Maps.newHashMap();
        if (admin) {
            Map<String, WarpGroupPayload> temp = Maps.newHashMap();
            for (Map.Entry<String, WarpGroup> g : warpGroups.entrySet()) {
                temp.put(g.getKey(), toPayload(g.getValue()));
            }
            groupPayload = temp;
        } else if (group != null) {
            groupPayload.put(group.getId(), toPayload(group));
        }
        return new WarpConfigPayload(SledgehammerConfig.warps.localWarp, admin, requestGroup, player.trackAction(), waypoints, groupPayload, servers);
    }

    /**
     * Generates payload for the warp group configuration GUI
     *
     * @param player {@link SledgehammerPlayer}
     * @param admin  True if they are able to edit all groups
     * @return Config Payload
     */
    public WarpGroupConfigPayload generateGroupConfigPayload(SledgehammerPlayer player, boolean admin) {
        SledgehammerServer s = player.getSledgehammerServer();
        if (s == null) {
            return null;
        }
        Map<String, List<Integer>> servers = Maps.newHashMap();
        Map<Integer, Warp> waypoints = Maps.newHashMap();
        for (Warp warp : warps.values()) {
            if (!servers.containsKey(warp.getServer())) {
                servers.put(warp.getServer(), Lists.newArrayList(warp.getId()));
            } else {
                servers.get(warp.getServer()).add(warp.getId());
            }
            waypoints.put(warp.getId(), warp.toWaypoint());
        }
        Map<String, WarpGroup> groups = Maps.newHashMap();
        for (Map.Entry<String, WarpGroup> g : warpGroups.entrySet()) {
            if (admin) {
                groups.put(g.getKey(), g.getValue());
            } else if (g.getValue().getServers().contains(player.getServer().getInfo().getName())) {
                groups.put(g.getKey(), g.getValue());
            }
        }
        return new WarpGroupConfigPayload(player.trackAction(), waypoints, groups, servers, admin);
    }

    public WarpGroupPayload toPayload(WarpGroup group) {
        List<Integer> payloadWarps = Lists.newArrayList();
        for (Integer warpId : group.getWarps()) {
            Warp warp = WarpHandler.getInstance().getWarp(warpId);
            if (warp != null) {
                payloadWarps.add(warpId);
            }
        }
        for (Warp warp : WarpHandler.getInstance().getWarps().values()) {
            if (group.getServers().contains(warp.getServer()) && !payloadWarps.contains(warp.getId())) {
                payloadWarps.add(warp.getId());
            }
        }
        return new WarpGroupPayload(group.getId(), group.getName(), group.getHeadId(), payloadWarps);
    }

    /**
     * Generates an ID for warps
     *
     * @return New ID
     */
    private int generateWarpID() {
        if (SledgehammerConfig.general.proxyTotal != -1) {
            return generateMultiWarpID();
        }
        int x = -1;
        for (Warp w : warps.values()) {
            if (w.getId() > x) {
                x = w.getId();
            }
        }

        return x < 0 ? 0 : x + 1;
    }

    /**
     * Generates a warp ID for multiserver networks
     *
     * @return New ID
     */
    private int generateMultiWarpID() {
        int i = SledgehammerConfig.general.proxyId;
        int min = i * Constants.warpIdBuffer;
        int max = (min + Constants.warpIdBuffer) - 1;
        while (true) {
            for (int x = min; x < max; x++) {
                if (getWarp(x) == null) {
                    return x;
                }
            }

            i += (SledgehammerConfig.general.proxyTotal + 1);
            min = i * Constants.warpIdBuffer;
            max = (min + Constants.warpIdBuffer) - 1;
        }
    }

    private void refreshWarpGroupCache() {
        TreeMap<String, WarpGroup> temp = Maps.newTreeMap(String.CASE_INSENSITIVE_ORDER);
        for (WarpGroup group : warpGroups.values()) {
            for (String server : group.getServers()) {
                temp.put(server, group);
            }
        }
        warpGroupByServer = temp;
    }

    public enum WarpStatus {
        EXISTS, RESERVED, AVAILABLE
    }
}
