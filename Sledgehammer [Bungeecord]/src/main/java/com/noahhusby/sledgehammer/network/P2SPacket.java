/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - P2SPacket.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.network;

public abstract class P2SPacket implements IP2SPacket {
    public SledgehammerNetworkManager getManager() {
        return SledgehammerNetworkManager.getInstance();
    }
}
