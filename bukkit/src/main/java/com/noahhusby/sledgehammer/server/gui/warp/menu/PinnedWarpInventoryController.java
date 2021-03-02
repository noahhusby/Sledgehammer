/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - WarpInventoryController.java
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

public class PinnedWarpInventoryController extends AbstractWarpInventoryController<PinnedWarpInventory> {

    public PinnedWarpInventoryController(Player p, WarpPayload payload) {
        super("Warps", p, payload);
        init();
    }

    public PinnedWarpInventoryController(GUIController controller, WarpPayload payload) {
        super(controller, payload);
        init();
    }

    @Override
    public void init() {
        List<Warp> warps = new ArrayList<>();
        for (WarpGroup wg : payload.getGroups()) {
            for (Warp w : wg.getWarps()) {
                if ((payload.isLocal() && w.getPinned() == Warp.PinnedMode.GLOBAL) ||
                    (!payload.isLocal() && (w.getPinned() == Warp.PinnedMode.GLOBAL || w.getPinned() == Warp.PinnedMode.LOCAL))) {
                    warps.add(w);
                }
            }
        }

        int total_pages = (int) Math.ceil(warps.size() / 27.0);
        if (total_pages == 0) {
            total_pages = 1;
        }
        for (int x = 0; x < total_pages; x++) {
            addPage(new PinnedWarpInventory(x, warps));
        }
        openChild(getChildByPage(0));
    }
}
