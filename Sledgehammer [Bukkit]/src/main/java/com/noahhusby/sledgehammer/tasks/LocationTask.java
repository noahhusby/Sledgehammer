package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.ConfigHandler;
import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.handlers.TaskHandler;
import com.noahhusby.sledgehammer.projection.GeographicProjection;
import com.noahhusby.sledgehammer.projection.ModifiedAirocean;
import com.noahhusby.sledgehammer.projection.ScaleProjection;
import com.noahhusby.sledgehammer.tasks.data.IResponse;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import com.noahhusby.sledgehammer.SledgehammerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

public class LocationTask extends Task {

    public LocationTask(TransferPacket t, JSONObject data) {
        super(t, data);
    }

    @Override
    public String getCommandName() {
        return Constants.locationTask;
    }

    @Override
    public void execute() {
        Player player = SledgehammerUtil.getPlayerFromName(getTransferPacket().sender);
        if(player == null) {
            throwNoSender();
            return;
        }

        JSONObject data = getData();

        String lat = (String) data.get("lat");
        String lon = (String) data.get("lon");

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
        int z = (int) Math.floor(proj[1]);


        int y = Constants.scanHeight;

        while(player.getWorld().getBlockAt(x, y, z).getType() != Material.AIR) {
            y += Constants.scanHeight;
        }

        while(player.getWorld().getBlockAt(x, y, z).getType() == Material.AIR) {
            y -= 1;
        }

        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),String.format("tp %s %s %s %s", getTransferPacket().sender, x, y+1, z));
    }

    @Override
    public IResponse getResponse() {
        return null;
    }

    @Override
    public void build(TransferPacket t, JSONObject data) {
        TaskHandler.getInstance().queueTask(new LocationTask(t, data));
    }

}
