/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bukkit] - P2SPacket.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.network;

import com.noahhusby.sledgehammer.Sledgehammer;

public abstract class P2SPacket implements IP2SPacket {
    public SledgehammerNetworkManager getManager() {
        return SledgehammerNetworkManager.getInstance();
    }

    protected void throwNoSender() {
        Sledgehammer.logger.warning("The task manager attempted to execute a task without an available sender.");
    }
}
