package com.noahhusby.sledgehammer.network.S2P;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.SmartObject;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.datasets.Location;
import com.noahhusby.sledgehammer.datasets.OpenStreetMaps;
import com.noahhusby.sledgehammer.network.PacketInfo;
import com.noahhusby.sledgehammer.network.S2PPacket;
import com.noahhusby.sledgehammer.projection.GeographicProjection;
import com.noahhusby.sledgehammer.projection.ModifiedAirocean;
import com.noahhusby.sledgehammer.projection.ScaleProjection;
import com.noahhusby.sledgehammer.util.ChatHelper;
import com.noahhusby.sledgehammer.util.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import org.json.simple.JSONObject;

public class S2PTestLocationPacket extends S2PPacket {
    @Override
    public String getPacketID() {
        return Constants.testLocationID;
    }

    @Override
    public void onMessage(PacketInfo info, SmartObject data) {
        JSONObject point = (JSONObject) data.get("point");

        GeographicProjection projection = new ModifiedAirocean();
        GeographicProjection uprightProj = GeographicProjection.orientProjection(projection, GeographicProjection.Orientation.upright);
        ScaleProjection scaleProj = new ScaleProjection(uprightProj, Constants.SCALE, Constants.SCALE);
        double proj[] = scaleProj.toGeo(Double.parseDouble((String) point.get("x")), Double.parseDouble((String) point.get("z")));

        int zoom = data.getInteger("zoom");
        Location online = OpenStreetMaps.getInstance().getLocation(proj[0], proj[1], zoom);
        CommandSender player = ProxyServer.getInstance().getPlayer(info.getSender());

        if(online == null) {
            player.sendMessage(ChatHelper.getInstance().makeAdminTextComponent(
                    new TextElement("This is not a valid location in the projection!", ChatColor.RED)));
            return;
        }

        player.sendMessage(ChatHelper.getInstance().makeAdminTextComponent(
                new TextElement("Testing location at ", ChatColor.GRAY), new TextElement(proj[0]+", "+proj[1], ChatColor.BLUE),
                new TextElement(" (Zoom: "+zoom+")", ChatColor.GRAY)));
        player.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("Online: ", ChatColor.RED)));
        if(!online.city.equals("")) {
            player.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("City - ", ChatColor.GRAY), new TextElement(online.city, ChatColor.BLUE)));
        }
        if(!online.county.equals("")) {
            player.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("County - ", ChatColor.GRAY), new TextElement(online.county, ChatColor.BLUE)));
        }
        if(!online.state.equals("")) {
            player.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("State - ", ChatColor.GRAY), new TextElement(online.state, ChatColor.BLUE)));
        }
        if(!online.country.equals("")) {
            player.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("Country - ", ChatColor.GRAY), new TextElement(online.country, ChatColor.BLUE)));
        }
        if(!ConfigHandler.useOfflineMode) {
            player.sendMessage(ChatHelper.getInstance().makeTextComponent(
                    new TextElement("Offline: ", ChatColor.RED), new TextElement("Disabled", ChatColor.DARK_RED)));
        } else {
            Location offline = OpenStreetMaps.getInstance().getOfflineLocation(proj[0], proj[1]);
            player.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("Offline: ", ChatColor.RED)));
            if(!offline.city.equals("")) {
                player.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("City - ", ChatColor.GRAY), new TextElement(offline.city, ChatColor.BLUE)));
            }
            if(!offline.county.equals("")) {
                player.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("County - ", ChatColor.GRAY), new TextElement(offline.county, ChatColor.BLUE)));
            }
            if(!offline.state.equals("")) {
                player.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("State - ", ChatColor.GRAY), new TextElement(offline.state, ChatColor.BLUE)));
            }
            if(!offline.country.equals("")) {
                player.sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("Country - ", ChatColor.GRAY), new TextElement(offline.country, ChatColor.BLUE)));
            }
        }

    }
}
