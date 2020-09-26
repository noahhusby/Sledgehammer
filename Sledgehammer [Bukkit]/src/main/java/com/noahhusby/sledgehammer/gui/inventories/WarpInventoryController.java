package com.noahhusby.sledgehammer.gui.inventories;

import com.noahhusby.sledgehammer.SmartObject;
import com.noahhusby.sledgehammer.gui.GUIController;
import com.noahhusby.sledgehammer.gui.IGUIChild;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WarpInventoryController extends GUIController {
    private final List<WarpInventory> warpInventories = new ArrayList<>();

    public WarpInventoryController(Player p, SmartObject warpData) {
        this(p, warpData.toJSON());
    }

    public WarpInventoryController(Player p, JSONObject warpData) {
        super(54, "Warps", p);

        JSONArray warps = (JSONArray) warpData.get("waypoints");
        boolean web = (boolean) warpData.get("web");

        int total_pages = (int) Math.ceil(warps.size() / 27.0);
        for(int x = 0; x < total_pages; x++) {
            WarpInventory w = new WarpInventory(x, warps, web);
            w.initFromController(this, getPlayer(), getInventory());
            warpInventories.add(w);
        }

        openChild(getChildByPage(0));
    }

    public IGUIChild getChildByPage(int page) {
        for(WarpInventory w : warpInventories) {
            if(w.getPage() == page) return w;
        }

        return null;
    }
}
