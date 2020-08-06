package com.noahhusby.sledgehammer.tasks;

import com.google.gson.internal.$Gson$Preconditions;
import com.noahhusby.sledgehammer.ConfigHandler;
import com.noahhusby.sledgehammer.Reference;
import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.handlers.TaskQueueManager;
import com.noahhusby.sledgehammer.projection.GeographicProjection;
import com.noahhusby.sledgehammer.projection.ModifiedAirocean;
import com.noahhusby.sledgehammer.projection.ScaleProjection;
import com.noahhusby.sledgehammer.utils.Util;
import com.sun.org.apache.regexp.internal.RE;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.lwjgl.util.vector.Vector;
import org.lwjgl.util.vector.Vector3f;

import javax.vecmath.Tuple2d;
import javax.vecmath.Vector3d;

public class TpllTask extends Task {

    private final String lat;
    private final String lon;

    public TpllTask(String sender, long executionTime, int time, String lat, String lon) {
        super(sender, executionTime, time);
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public void execute() {
        EntityPlayer player = Util.getPlayerFromName(sender);
        if(player == null) {
            throwNoSender();
            return;
        }

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
        double y = player.getPosition().getY();
        double z = Double.parseDouble(String.valueOf(proj[1]));


        while (player.world.getBlockState(new BlockPos(x, y, z)).getMaterial() != Material.AIR) {
            y++;
        }

        while (player.world.getBlockState(new BlockPos(x, y, z)).getMaterial() == Material.AIR) {
            y--;
        }

        FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(player, String.format("tp %s %s %s", x, y, z));
    }

}
