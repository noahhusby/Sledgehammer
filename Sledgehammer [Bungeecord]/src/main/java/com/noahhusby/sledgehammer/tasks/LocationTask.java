package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.tasks.data.IResponse;
import com.noahhusby.sledgehammer.tasks.data.TaskPacket;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import org.json.simple.JSONObject;

public class LocationTask extends Task {

    private final String lat;
    private final String lon;

    public LocationTask(TransferPacket transfer, String lat, String lon) {
        super(transfer);
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public String getCommandName() {
        return Constants.locationTask;
    }

    @Override
    public TaskPacket build() {
        JSONObject data = new JSONObject();
        data.put("lat", lat);
        data.put("lon", lon);
        return buildPacket(data);
    }

    @Override
    public IResponse getResponse() {
        return null;
    }
}
