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

package com.noahhusby.sledgehammer.proxy.terramap.network.packets;

import com.noahhusby.sledgehammer.proxy.terramap.network.ForgeChannel;
import io.netty.buffer.ByteBuf;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

/**
 * Represents a Forge plugin message that's being sent or received via a {@link ForgeChannel}.
 *
 * @author SmylerMC
 */
public interface IForgePacket {

    /**
     * Encode the packet's content in the buffer, excluding discriminator
     *
     * @param buf
     */
    void encode(ByteBuf buf);

    /**
     * Decode the packet's content from the buffer, excluding discriminator
     *
     * @param buf
     */
    void decode(ByteBuf buf);

    /**
     * Process a packet sent from a server to a client
     *
     * @param channel
     * @param fromServer
     * @param toPlayer
     * @return true if the packet should be stopped from reaching the client
     */
    boolean processFromServer(String channel, Server fromServer, ProxiedPlayer toPlayer);

    /**
     * Process a packet sent from a client to a server
     *
     * @param channel
     * @param fromPlayer
     * @param toServer
     * @return true if the packet should be stopped from reaching the server
     */
    boolean processFromClient(String channel, ProxiedPlayer fromPlayer, Server toServer);

}
