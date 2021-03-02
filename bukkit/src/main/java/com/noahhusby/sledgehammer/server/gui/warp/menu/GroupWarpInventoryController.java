/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - ServerWarpInventoryController.java
 *
 * Sledgehammer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sledgehammer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.server.gui.warp.menu;

import com.noahhusby.sledgehammer.common.warps.Warp;
import com.noahhusby.sledgehammer.common.warps.WarpGroup;
import com.noahhusby.sledgehammer.server.data.warp.WarpPayload;
import com.noahhusby.sledgehammer.server.gui.GUIController;
import com.noahhusby.sledgehammer.server.gui.GUIRegistry;
import org.bukkit.entity.Player;

import java.util.List;

public class GroupWarpInventoryController extends AbstractWarpInventoryController<GroupWarpInventory> {
    private final String groupId;

    public GroupWarpInventoryController(Player player, WarpPayload payload, String groupId) {
        super("Warps", player, payload);
        this.groupId = groupId;
        init();
    }

    public GroupWarpInventoryController(GUIController controller, WarpPayload payload, String groupId) {
        super(controller, payload);
        this.groupId = groupId;
        init();
    }

    @Override
    public void init() {
        WarpGroup group = null;
        for (WarpGroup g : payload.getGroups()) {
            if (g.getId().equals(groupId)) {
                group = g;
            }
        }

        if (group == null) {
            GUIRegistry.register(new AllWarpInventoryController(getPlayer(), payload));
            return;
        }

        List<Warp> warps = group.getWarps();

        int total_pages = (int) Math.ceil(warps.size() / 27.0);
        if (total_pages == 0) {
            total_pages = 1;
        }
        for (int x = 0; x < total_pages; x++) {
            addPage(new GroupWarpInventory(x, warps, group));
        }
        openChild(getChildByPage(0));
    }
}
