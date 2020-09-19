package com.noahhusby.sledgehammer.tasks.data;

import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import org.json.simple.JSONObject;

public interface IResponse {
    String getResponseCommand();

    void respond(JSONObject data);

    boolean validateResponse(TransferPacket transfer, JSONObject data);
}
