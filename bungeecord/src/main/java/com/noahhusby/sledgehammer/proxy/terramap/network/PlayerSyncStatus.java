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

/**
 * Indicates how a Terramap server or proxy has support for player synchronization.
 *
 * @author SmylerMC
 */
public enum PlayerSyncStatus {

    ENABLED((byte) 0x01),
    DISABLED((byte) 0x00),
    UNKNOWN((byte) 0x02); //Either there was an error, or the server does not want us to know

    public final int VALUE;

    PlayerSyncStatus(byte value) {
        this.VALUE = value;
    }

    public static PlayerSyncStatus getFromNetworkCode(byte code) {
        for (PlayerSyncStatus s : PlayerSyncStatus.values()) {
            if (s.VALUE == code) {
                return s;
            }
        }
        return UNKNOWN;
    }

    public static PlayerSyncStatus getFromBoolean(boolean bool) {
        return bool ? ENABLED : DISABLED;
    }
}