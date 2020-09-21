package com.noahhusby.sledgehammer.tasks;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.datasets.Location;
import com.noahhusby.sledgehammer.datasets.OpenStreetMaps;
import com.noahhusby.sledgehammer.projection.GeographicProjection;
import com.noahhusby.sledgehammer.projection.ModifiedAirocean;
import com.noahhusby.sledgehammer.projection.ScaleProjection;
import com.noahhusby.sledgehammer.tasks.data.IResponse;
import com.noahhusby.sledgehammer.tasks.data.TaskPacket;
import com.noahhusby.sledgehammer.tasks.data.TransferPacket;
import com.noahhusby.sledgehammer.util.ChatHelper;
import com.noahhusby.sledgehammer.util.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import org.glassfish.grizzly.http.server.SuspendContext;
import org.json.simple.JSONObject;

public class TestLocationTask extends Task implements IResponse {
    private int zoom;
    public TestLocationTask(TransferPacket transfer, int zoom) {
        super(transfer);
        this.zoom = zoom;
        if(zoom == -1) this.zoom = ConfigHandler.zoom;
    }

    @Override
    public String getResponseCommand() {
        return Constants.testLocationTask;
    }

    @Override
    public void respond(JSONObject d) {
        JSONObject data = (JSONObject) d.get("data");
        JSONObject point = (JSONObject) data.get("point");

        GeographicProjection projection = new ModifiedAirocean();
        GeographicProjection uprightProj = GeographicProjection.orientProjection(projection, GeographicProjection.Orientation.upright);
        ScaleProjection scaleProj = new ScaleProjection(uprightProj, Constants.SCALE, Constants.SCALE);
        double proj[] = scaleProj.toGeo(Double.parseDouble((String) point.get("x")), Double.parseDouble((String) point.get("z")));

        Location online = OpenStreetMaps.getInstance().getLocation(proj[0], proj[1], zoom);
        if(online == null) {
            ProxyServer.getInstance().getPlayer(getSender()).sendMessage(ChatHelper.getInstance().makeAdminTextComponent(
                    new TextElement("This is not a valid location in the projection!", ChatColor.RED)));
            return;
        }

        CommandSender sender = ProxyServer.getInstance().getPlayer(getSender());
        ProxyServer.getInstance().getPlayer(getSender()).sendMessage(ChatHelper.getInstance().makeAdminTextComponent(
                new TextElement("Testing location at ", ChatColor.GRAY), new TextElement(proj[0]+", "+proj[1], ChatColor.BLUE),
                new TextElement(" (Zoom: "+zoom+")", ChatColor.GRAY)));
        ProxyServer.getInstance().getPlayer(getSender()).sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("Online: ", ChatColor.RED)));
        if(!online.city.equals("")) {
            sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("City - ", ChatColor.GRAY), new TextElement(online.city, ChatColor.BLUE)));
        }
        if(!online.county.equals("")) {
            sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("County - ", ChatColor.GRAY), new TextElement(online.county, ChatColor.BLUE)));
        }
        if(!online.state.equals("")) {
            sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("State - ", ChatColor.GRAY), new TextElement(online.state, ChatColor.BLUE)));
        }
        if(!online.country.equals("")) {
            sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("Country - ", ChatColor.GRAY), new TextElement(online.country, ChatColor.BLUE)));
        }
        if(!ConfigHandler.useOfflineMode) {
            ProxyServer.getInstance().getPlayer(getSender()).sendMessage(ChatHelper.getInstance().makeTextComponent(
                    new TextElement("Offline: ", ChatColor.RED), new TextElement("Disabled", ChatColor.DARK_RED)));
        } else {
            Location offline = OpenStreetMaps.getInstance().getOfflineLocation(proj[0], proj[1]);
            ProxyServer.getInstance().getPlayer(getSender()).sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("Offline: ", ChatColor.RED)));
            if(!offline.city.equals("")) {
                sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("City - ", ChatColor.GRAY), new TextElement(offline.city, ChatColor.BLUE)));
            }
            if(!offline.county.equals("")) {
                sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("County - ", ChatColor.GRAY), new TextElement(offline.county, ChatColor.BLUE)));
            }
            if(!offline.state.equals("")) {
                sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("State - ", ChatColor.GRAY), new TextElement(offline.state, ChatColor.BLUE)));
            }
            if(!offline.country.equals("")) {
                sender.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("Country - ", ChatColor.GRAY), new TextElement(offline.country, ChatColor.BLUE)));
            }
        }
    }

    @Override
    public boolean validateResponse(TransferPacket transfer, JSONObject data) {
        return transfer.sender.equals(getSender());
    }

    @Override
    public String getCommandName() {
        return Constants.testLocationTask;
    }

    @Override
    public TaskPacket build() {
        return buildPacket(new JSONObject());
    }

    @Override
    public IResponse getResponse() {
        return this;
    }
}
