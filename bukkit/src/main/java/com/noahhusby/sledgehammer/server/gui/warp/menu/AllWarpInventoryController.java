/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - AllWarpInventoryController.java
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
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AllWarpInventoryController extends AbstractWarpInventoryController<AllWarpInventory> {

    public AllWarpInventoryController(Player player, WarpPayload payload) {
        super("Warps", player, payload);
    }

    public AllWarpInventoryController(GUIController controller, WarpPayload payload) {
        super(controller, payload);
    }

    @Override
    public void init() {
        List<Warp> warps = new ArrayList<>();
        for (WarpGroup wg : payload.getGroups()) {
            warps.addAll(wg.getWarps());
        }

        int total_pages = (int) Math.ceil(warps.size() / 27.0);
        if (total_pages == 0) {
            total_pages = 1;
        }
        for (int x = 0; x < total_pages; x++) {
            addPage(new AllWarpInventory(x, warps));
        }
        openChild(getChildByPage(0));
    }
}
