package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.ConfigHandler;
import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.handlers.TaskHandler;
import com.noahhusby.sledgehammer.projection.GeographicProjection;
import com.noahhusby.sledgehammer.projection.ModifiedAirocean;
import com.noahhusby.sledgehammer.projection.ScaleProjection;
import com.noahhusby.sledgehammer.tasks.data.IResponse;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import com.noahhusby.sledgehammer.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class LocationTask extends Task {

    public LocationTask(TransferPacket t, String[] data) {
        super(t, data);
    }

    @Override
    public String getCommandName() {
        return Constants.locationTask;
    }

    @Override
    public void execute() {
        Player player = Util.getPlayerFromName(getTransferPacket().sender);
        if(player == null) {
            throwNoSender();
            return;
        }

        String[] data = getData();

        String lat = data[0];
        String lon = data[1];

        if(ConfigHandler.tpllMode.toLowerCase().equals("cs")) {
            Bukkit.getServer().dispatchCommand(player,"cs tpll "+lat+" "+lon);
            return;
        } else if(ConfigHandler.tpllMode.toLowerCase().equals("tpll")) {
            Bukkit.getServer().dispatchCommand(player,"tpll "+lat+" "+lon);
            return;
        }

        GeographicProjection projection = new ModifiedAirocean();
        GeographicProjection uprightProj = GeographicProjection.orientProjection(projection, GeographicProjection.Orientation.upright);
        ScaleProjection scaleProj = new ScaleProjection(uprightProj, Constants.SCALE, Constants.SCALE);

        double proj[] = scaleProj.fromGeo(Double.parseDouble(lon), Double.parseDouble(lat));

        int x = (int) Math.floor(proj[0]);
        int y = 256;
        int z = (int) Math.floor(proj[1]);


        while (player.getWorld().getBlockAt(x, y, z).getType() != Material.AIR) {
            y++;
        }

        while (player.getWorld().getBlockAt(x, y, z).getType() == Material.AIR) {
            y--;
        }

        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),String.format("tp %s %s %s %s", getTransferPacket().sender, x, y+2, z));
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
