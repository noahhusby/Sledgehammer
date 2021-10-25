package com.noahhusby.sledgehammer.proxy.terramap.network.packets.mapsync;

/**
 * Indicates how a Terramap server or proxy supports player synchronization.
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