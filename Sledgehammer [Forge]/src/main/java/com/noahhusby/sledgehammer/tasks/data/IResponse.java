package com.noahhusby.sledgehammer.tasks.data;

import com.noahhusby.sledgehammer.tasks.data.TransferPacket;

public interface IResponse {
    String getResponseCommand();

    String[] response();
}
