package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.handlers.TaskHandler;
import com.noahhusby.sledgehammer.tasks.data.IResponse;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import com.noahhusby.sledgehammer.SledgehammerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

public class TeleportTask extends Task {

    public TeleportTask(TransferPacket t, JSONObject data) {
        super(t, data);
    }

    @Override
    public void execute() {
        JSONObject data = getData();
        Player player = SledgehammerUtil.getPlayerFromName(getTransferPacket().sender);
        if(player == null) {
            throwNoSender();
            return;
        }

        if(!player.isOnline()) {
            throwNoSender();
            return;
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("tp %s %s %s %s %s %s", getTransferPacket().sender, data.get("x"),
                data.get("y"), data.get("z"), data.get("yaw"), data.get("pitch")));
    }

    @Override
    public String getCommandName() {
        return "teleport";
    }

    @Override
    public IResponse getResponse() {
        return null;
    }

    @Override
    public void build(TransferPacket t, JSONObject data) {
        TaskHandler.getInstance().queueTask(new TeleportTask(t, data));
    }
}
