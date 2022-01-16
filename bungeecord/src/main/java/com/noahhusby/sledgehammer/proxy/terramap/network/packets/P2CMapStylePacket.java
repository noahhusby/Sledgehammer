/*
 * MIT License
 *
 * Copyright 2020-2022 noahhusby
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

package com.noahhusby.sledgehammer.proxy.terramap.network.packets;

import com.noahhusby.sledgehammer.proxy.terramap.MapStyleLibrary.MapStyle;
import com.noahhusby.sledgehammer.proxy.terramap.network.NetworkUtil;
import fr.thesmyler.bungee2forge.api.ForgePacket;
import io.netty.buffer.ByteBuf;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

import java.util.Map;

/**
 * Sent to players joining the network to give them access to this proxy's custom map styles.
 *
 * @author SmylerMC
 */
public class P2CMapStylePacket implements ForgePacket {

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
        // Needed so this class can be instanced by the channel in case someone sends us such a packet
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
        NetworkUtil.writeStringToBuf(this.id, buf);
        buf.writeLong(this.providerVersion);
        String singleUrl = this.backwardCompat ? this.urlPatterns[0]: "";
        NetworkUtil.writeStringToBuf(singleUrl, buf);
        buf.writeInt(this.names.size());
        for (String key : this.names.keySet()) {
            NetworkUtil.writeStringToBuf(key, buf);
            NetworkUtil.writeStringToBuf(this.names.get(key), buf);
        }
        buf.writeInt(this.copyrights.size());
        for (String key : this.copyrights.keySet()) {
            NetworkUtil.writeStringToBuf(key, buf);
            NetworkUtil.writeStringToBuf(this.copyrights.get(key), buf);
        }
        buf.writeInt(this.minZoom);
        buf.writeInt(this.maxZoom);
        buf.writeInt(this.displayPriority);
        buf.writeBoolean(this.isAllowedOnMinimap);
        NetworkUtil.writeStringToBuf(this.comment, buf);
        if (!this.backwardCompat) {
            buf.writeInt(this.maxConcurrentConnections);
            NetworkUtil.writeStringArrayToByteBuf(this.urlPatterns, buf);
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
