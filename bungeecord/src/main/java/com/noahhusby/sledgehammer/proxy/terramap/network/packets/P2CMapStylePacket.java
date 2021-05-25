package com.noahhusby.sledgehammer.proxy.terramap.network.packets;

import java.util.Map;

import com.noahhusby.sledgehammer.proxy.terramap.MapStyleLibrary.MapStyle;
import com.noahhusby.sledgehammer.proxy.terramap.network.ForgeChannel;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

/**
 * Sent to players joining the network to give them access to this proxy's custom map styles.
 *
 * @author SmylerMC
 * @see MapStyleRegistry
 */
public class P2CMapStylePacket implements IForgePacket {

    private String id;
    private long providerVersion;
    private String[] urlPatterns;
    private Map<String, String> names;
    private Map<String, String> copyrights;
    private int minZoom;
    private int maxZoom;
    private int displayPriority;
    private boolean isAllowedOnMinimap;
    private String comment;
    private int maxConcurrentConnections;
    private boolean debug;
    private boolean backwardCompat;

    public P2CMapStylePacket() {
    }
    
    public P2CMapStylePacket(MapStyle style) {
        this(style, false); // We have no way to reliably know the client's TM version at the time we send the packet
    }

    public P2CMapStylePacket(MapStyle style, boolean backwardCompat) {
        this.id = style.id;
        this.providerVersion = style.version;
        this.urlPatterns = style.urls;
        this.names = style.name;
        this.copyrights = style.copyright;
        this.minZoom = style.minZoom;
        this.maxZoom = style.maxZoom;
        this.displayPriority = style.displayPriority;
        this.isAllowedOnMinimap = style.allowOnMinimap;
        this.comment = style.comment;
        this.maxConcurrentConnections = style.maxConcurrentRequests;
        this.debug = style.debug;
        this.backwardCompat = backwardCompat;
    }


    @Override
    public void decode(ByteBuf buf) {
        // We shouldn't receive this, so we don't need to decode it if we do
    }

    @Override
    public void encode(ByteBuf buf) {
        ForgeChannel.writeStringToBuf(this.id, buf);
        buf.writeLong(this.providerVersion);
        String singleUrl = this.backwardCompat ? this.urlPatterns[0]: "";
        ForgeChannel.writeStringToBuf(singleUrl, buf);
        buf.writeInt(this.names.size());
        for(String key: this.names.keySet()) {
            ForgeChannel.writeStringToBuf(key, buf);
            ForgeChannel.writeStringToBuf(this.names.get(key), buf);
        }
        buf.writeInt(this.copyrights.size());
        for(String key: this.copyrights.keySet()) {
            ForgeChannel.writeStringToBuf(key, buf);
            ForgeChannel.writeStringToBuf(this.copyrights.get(key), buf);
        }
        buf.writeInt(this.minZoom);
        buf.writeInt(this.maxZoom);
        buf.writeInt(this.displayPriority);
        buf.writeBoolean(this.isAllowedOnMinimap);
        ForgeChannel.writeStringToBuf(this.comment, buf);
        if(!this.backwardCompat) {
            buf.writeInt(this.maxConcurrentConnections);
            ForgeChannel.writeStringArrayToByteBuf(this.urlPatterns, buf);
            buf.writeBoolean(this.debug);
        }
    }

    @Override
    public boolean processFromServer(String channel, Server fromServer, ProxiedPlayer toPlayer) {
        return false;
    }

    @Override
    public boolean processFromClient(String channel, ProxiedPlayer fromPlayer, Server toServer) {
        return false;
    }

}
