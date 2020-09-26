package com.noahhusby.sledgehammer.gui.inventories;

import com.noahhusby.sledgehammer.SmartObject;
import com.noahhusby.sledgehammer.gui.GUIController;
import com.noahhusby.sledgehammer.gui.GUIRegistry;
import com.noahhusby.sledgehammer.gui.IGUIChild;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PinnedWarpInventoryController extends GUIController {
    private final List<PinnedWarpInventory> warpInventories = new ArrayList<>();
    private final JSONObject warpData;

    public PinnedWarpInventoryController(Player p, SmartObject warpData) {
        this(p, warpData.toJSON());
    }

    public PinnedWarpInventoryController(Player p, JSONObject warpData) {
        super(54, "Pinned Warps", p);
        this.warpData = warpData;

        JSONArray rawWarps = (JSONArray) warpData.get("waypoints");
        JSONArray warps = new JSONArray();
        for(Object o : rawWarps) {
            JSONObject ob = (JSONObject) o;
            if((boolean) ob.get("pinned")) warps.add(o);
        }
        boolean web = (boolean) warpData.get("web");

        int total_pages = (int) Math.ceil(warps.size() / 27.0);
        if(total_pages == 0) total_pages = 1;
        for(int x = 0; x < total_pages; x++) {
            PinnedWarpInventory w = new PinnedWarpInventory(x, warps, web);
            w.initFromController(this, getPlayer(), getInventory());
            warpInventories.add(w);
        }

        openChild(getChildByPage(0));
    }

    public IGUIChild getChildByPage(int page) {
        for(PinnedWarpInventory w : warpInventories) {
            if(w.getPage() == page) return w;
        }

        return null;
    }

    public void switchToAll() {
        GUIRegistry.register(new WarpInventoryController(getPlayer(), warpData));
    }

    public void switchToServerList() {
        GUIRegistry.register(new ServerWarpInventoryController(getPlayer(), warpData));
    }
}
