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
package com.noahhusby.sledgehammer.proxy.terramap.network;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.protocol.DefinedPacket;

public class NetworkUtil {

    private NetworkUtil() {}

    /**
     * Encodes a String to a byte buffer using the [size (varint) | string (utf-8)] format
     *
     * @param str a String to write
     * @param buf a buffer to write to
     */
    public static void writeStringToBuf(String str, ByteBuf buf) {
        DefinedPacket.writeString(str, buf);
    }

    /**
     * Reads a String from a byte buffer using the [size (varint) | string (utf-8)] format
     *
     * @param buf a buffer to read from
     * @return a String read from the buffer
     */
    public static String readStringFromBuf(ByteBuf buf) {
        return DefinedPacket.readString(buf);
    }

    /**
     * Writes a String array to a buffer by writing the size of the array as a varint followed by the strings themselves
     *
     * @param strings an array of Strings to write to the buffer
     * @param buf a buffer to write to
     */
    public static void writeStringArrayToByteBuf(String[] strings, ByteBuf buf) {
        DefinedPacket.writeVarInt(strings.length, buf);
        for(String str: strings) DefinedPacket.writeString(str, buf);
    }

}
