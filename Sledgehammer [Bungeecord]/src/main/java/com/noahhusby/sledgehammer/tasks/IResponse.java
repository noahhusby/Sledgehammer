package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.tasks.data.TransferPacket;

public interface IResponse {
    String getResponseCommand();

    void respond(String[] data);

    boolean validateResponse(TransferPacket transfer, String[] data);
}
