package com.noahhusby.sledgehammer.players;

import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.types.SledgehammerServer;
import com.noahhusby.sledgehammer.datasets.Point;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.score.Scoreboard;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class SledgehammerPlayer implements ProxiedPlayer {

    private ProxiedPlayer player;

    private boolean flagged = false;
    private Point location;
    private Point track;

    public SledgehammerPlayer(ProxiedPlayer player) {
        this.player = player;
    }

    public void update() {
        this.player = ProxyServer.getInstance().getPlayer(player.getName());
    }

    @Override
    public String getDisplayName() {
        return player.getDisplayName();
    }

    @Override
    public void setDisplayName(String s) {
        player.setDisplayName(s);
    }

    @Override
    public void sendMessage(ChatMessageType chatMessageType, BaseComponent... baseComponents) {
        player.sendMessage(chatMessageType, baseComponents);
    }

    @Override
    public void sendMessage(ChatMessageType chatMessageType, BaseComponent baseComponent) {
        player.sendMessage(chatMessageType, baseComponent);
    }

    @Override
    public void connect(ServerInfo serverInfo) {
        player.connect(serverInfo);
    }

    @Override
    public void connect(ServerInfo serverInfo, ServerConnectEvent.Reason reason) {
        player.connect(serverInfo, reason);
    }

    @Override
    public void connect(ServerInfo serverInfo, Callback<Boolean> callback) {
        player.connect(serverInfo, callback);
    }

    @Override
    public void connect(ServerInfo serverInfo, Callback<Boolean> callback, ServerConnectEvent.Reason reason) {
        player.connect(serverInfo, callback, reason);
    }

    @Override
    public void connect(ServerConnectRequest serverConnectRequest) {
        player.connect(serverConnectRequest);
    }

    @Override
    public Server getServer() {
        return player.getServer();
    }

    @Override
    public int getPing() {
        return player.getPing();
    }

    @Override
    public void sendData(String s, byte[] bytes) {
        player.sendData(s, bytes);
    }

    @Override
    public PendingConnection getPendingConnection() {
        return player.getPendingConnection();
    }

    @Override
    public void chat(String s) {
        player.chat(s);
    }

    @Override
    public ServerInfo getReconnectServer() {
        return player.getReconnectServer();
    }

    @Override
    public void setReconnectServer(ServerInfo serverInfo) {
        player.setReconnectServer(serverInfo);
    }

    @Override
    public String getUUID() {
        return player.getUUID();
    }

    @Override
    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    @Override
    public Locale getLocale() {
        return player.getLocale();
    }

    @Override
    public byte getViewDistance() {
        return player.getViewDistance();
    }

    @Override
    public ChatMode getChatMode() {
        return player.getChatMode();
    }

    @Override
    public boolean hasChatColors() {
        return player.hasChatColors();
    }

    @Override
    public SkinConfiguration getSkinParts() {
        return player.getSkinParts();
    }

    @Override
    public MainHand getMainHand() {
        return player.getMainHand();
    }

    @Override
    public void setTabHeader(BaseComponent baseComponent, BaseComponent baseComponent1) {
        player.setTabHeader(baseComponent, baseComponent1);
    }

    @Override
    public void setTabHeader(BaseComponent[] baseComponents, BaseComponent[] baseComponents1) {
        player.setTabHeader(baseComponents, baseComponents1);
    }

    @Override
    public void resetTabHeader() {
        player.resetTabHeader();
    }

    @Override
    public void sendTitle(Title title) {
        player.sendTitle(title);
    }

    @Override
    public boolean isForgeUser() {
        return player.isForgeUser();
    }

    @Override
    public Map<String, String> getModList() {
        return player.getModList();
    }

    @Override
    public Scoreboard getScoreboard() {
        return player.getScoreboard();
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public void sendMessage(String s) {
        player.sendMessage(s);
    }

    @Override
    public void sendMessages(String... strings) {
        player.sendMessages(strings);
    }

    @Override
    public void sendMessage(BaseComponent... baseComponents) {
        player.sendMessage(baseComponents);
    }

    @Override
    public void sendMessage(BaseComponent baseComponent) {
        player.sendMessage(baseComponent);
    }

    @Override
    public Collection<String> getGroups() {
        return player.getGroups();
    }

    @Override
    public void addGroups(String... strings) {
        player.addGroups(strings);
    }

    @Override
    public void removeGroups(String... strings) {
        player.removeGroups(strings);
    }

    @Override
    public boolean hasPermission(String s) {
        return player.hasPermission(s);
    }

    @Override
    public void setPermission(String s, boolean b) {
        player.setPermission(s, b);
    }

    @Override
    public Collection<String> getPermissions() {
        return player.getPermissions();
    }

    @Override
    public InetSocketAddress getAddress() {
        return player.getAddress();
    }

    @Override
    public SocketAddress getSocketAddress() {
        return player.getSocketAddress();
    }

    @Override
    public void disconnect(String s) {
        player.disconnect(s);
    }

    @Override
    public void disconnect(BaseComponent... baseComponents) {
        player.disconnect(baseComponents);
    }

    @Override
    public void disconnect(BaseComponent baseComponent) {
        player.disconnect(baseComponent);
    }

    @Override
    public boolean isConnected() {
        return player.isConnected();
    }

    @Override
    public Unsafe unsafe() {
        return player.unsafe();
    }

    public boolean onSledgehammer() {
        return ServerConfig.getInstance().getServer(getServer().getInfo().getName()) != null;
    }

    public boolean onEarthServer() {
        SledgehammerServer server = ServerConfig.getInstance().getServer(getServer().getInfo().getName());
        if(server == null) return false;
        return server.earthServer;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point p) {
        this.location = p;
    }

    public Point getTrackingPoint() {
        return track;
    }

    public void setTrackingPoint(Point p) {
        this.track = p;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public static SledgehammerPlayer getPlayer(String s) {
        return PlayerManager.getInstance().getPlayer(s);
    }

    public static SledgehammerPlayer getPlayer(CommandSender s) {
        return PlayerManager.getInstance().getPlayer(s);
    }
}
