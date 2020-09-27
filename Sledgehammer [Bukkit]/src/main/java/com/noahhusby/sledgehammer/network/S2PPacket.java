/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bukkit] - S2PPacket.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.network;

public abstract class S2PPacket implements IS2PPacket {
    public SledgehammerNetworkManager getManager() {
        return SledgehammerNetworkManager.getInstance();
    }
}
