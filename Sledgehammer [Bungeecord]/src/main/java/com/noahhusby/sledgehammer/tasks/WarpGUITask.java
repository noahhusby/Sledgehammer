package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.commands.WarpCommand;
import com.noahhusby.sledgehammer.datasets.Point;
import com.noahhusby.sledgehammer.handlers.WarpHandler;
import com.noahhusby.sledgehammer.tasks.data.IResponse;
import com.noahhusby.sledgehammer.tasks.data.TaskPacket;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import com.noahhusby.sledgehammer.util.ProxyUtil;
import net.md_5.bungee.api.ProxyServer;
import org.json.simple.JSONObject;

public class WarpGUITask extends Task implements IResponse {

    public WarpGUITask(TransferPacket transfer) {
        super(transfer);
    }

    @Override
    public String getCommandName() {
        return Constants.warpGUITask;
    }

    @Override
    public TaskPacket build() {
        return buildPacket(WarpHandler.getInstance().generateGUIPayload());
    }

    @Override
    public IResponse getResponse() {
        return this;
    }

    @Override
    public String getResponseCommand() {
        return Constants.warpGUITask;
    }

    @Override
    public void respond(JSONObject d) {
        JSONObject data = (JSONObject) d.get("data");
        String action = (String) data.get("action");

        if(action.equals("webgui")) {
            new WarpCommand().execute(ProxyServer.getInstance().getPlayer(getSender()), new String[]{"map"});
        }
    }

    @Override
    public boolean validateResponse(TransferPacket transfer, JSONObject data) {
        return transfer.sender.equals(getSender());
    }
}
