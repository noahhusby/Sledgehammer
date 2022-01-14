package com.noahhusby.sledgehammer.proxy.terramap.network;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.DefinedPacket;

public class NetworkUtil {

    private NetworkUtil() {}

    /**
     * Encodes a String to a byte buffer using the [size (varint) | string (utf-8)] format
     *
     * @param str - String to write
     * @param buf - Buffer to write to
     */
    public static void writeStringToBuf(String str, ByteBuf buf) {
        DefinedPacket.writeString(str, buf);
    }

    /**
     * Reads a String from a bte buffer using the [size (varint) | string (utf-8)] format
     *
     * @param buf
     * @return
     */
    public static String readStringFromBuf(ByteBuf buf) {
        return DefinedPacket.readString(buf);
    }

}
