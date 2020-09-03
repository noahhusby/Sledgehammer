package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.ConfigHandler;
import com.noahhusby.sledgehammer.Reference;
import com.noahhusby.sledgehammer.handlers.TaskHandler;
import com.noahhusby.sledgehammer.projection.GeographicProjection;
import com.noahhusby.sledgehammer.projection.ModifiedAirocean;
import com.noahhusby.sledgehammer.projection.ScaleProjection;
import com.noahhusby.sledgehammer.tasks.data.IResponse;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import com.noahhusby.sledgehammer.utils.Util;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class LocationTask extends Task {

    public LocationTask(TransferPacket t, String[] data) {
        super(t, data);
    }

    @Override
    public String getCommandName() {
        return Reference.locationTask;
    }

    @Override
    public void execute() {
        EntityPlayer player = Util.getPlayerFromName(getTransferPacket().sender);
        if(player == null) {
            throwNoSender();
            return;
        }

        String[] data = getData();

        String lat = data[0];
        String lon = data[1];

        if(ConfigHandler.tpllMode.toLowerCase().equals("cs")) {
            FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(player, "cs tpll "+lat+" "+lon);
            return;
        } else if(ConfigHandler.tpllMode.toLowerCase().equals("tpll")) {
            FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(player, "tpll "+lat+" "+lon);
            return;
        }

        GeographicProjection projection = new ModifiedAirocean();
        GeographicProjection uprightProj = GeographicProjection.orientProjection(projection, GeographicProjection.Orientation.upright);
        ScaleProjection scaleProj = new ScaleProjection(uprightProj, Reference.SCALE, Reference.SCALE);

        double proj[] = scaleProj.fromGeo(Double.parseDouble(lon), Double.parseDouble(lat));

        double x = Double.parseDouble(String.valueOf(proj[0]));
        double y = 256;
        double z = Double.parseDouble(String.valueOf(proj[1]));


        while (player.world.getBlockState(new BlockPos(x, y, z)).getMaterial() != Material.AIR) {
            y++;
        }

        while (player.world.getBlockState(new BlockPos(x, y, z)).getMaterial() == Material.AIR) {
            y--;
        }

        FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(FMLCommonHandler.instance().getMinecraftServerInstance(),
                String.format("tp %s %s %s %s", getTransferPacket().sender, x, y+2, z));
    }

    @Override
    public IResponse getResponse() {
        return null;
    }

    @Override
    public void build(TransferPacket t, String[] data) {
        TaskHandler.getInstance().queueTask(new LocationTask(t, data));
    }

}
