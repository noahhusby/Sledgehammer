package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.Reference;
import com.noahhusby.sledgehammer.data.location.Point;
import com.noahhusby.sledgehammer.handlers.TaskHandler;
import com.noahhusby.sledgehammer.tasks.data.IResponse;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.lwjgl.Sys;

import java.util.List;

public class SetWarpTask extends Task implements IResponse {

    private Point point;

    public SetWarpTask(TransferPacket t, String[] data) {
        super(t, data);
    }

    @Override
    public String getResponseCommand() {
        return Reference.setwarpTask;
    }

    @Override
    public String[] response() {
        return new String[]{point.x, point.y, point.z};
    }

    @Override
    public String getCommandName() {
        return Reference.setwarpTask;
    }

    @Override
    public void execute() {
        List<EntityPlayerMP> players = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers();
        if(players.isEmpty()) {
            return;
        }

        for(EntityPlayerMP p : players) {
            if(p.getName().equals(getTransferPacket().sender)) {
                point = new Point(String.valueOf(p.posX), String.valueOf(p.posY), String.valueOf(p.posZ));
            }
        }

        TaskHandler.getInstance().sendResponse(this);
    }

    @Override
    public IResponse getResponse() {
        return this;
    }

    @Override
    public void build(TransferPacket t, String[] data) {
        TaskHandler.getInstance().queueTask(new SetWarpTask(t, data));
    }
}
