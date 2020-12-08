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

package com.noahhusby.sledgehammer.gui.inventories.warp.config;

import com.noahhusby.sledgehammer.gui.inventories.general.GUIController;
import org.bukkit.entity.Player;

public class WarpConfigController extends GUIController {

    public WarpConfigController(Player p) {
        super(27, "Warp Config", p);
        init();
    }

    public WarpConfigController(GUIController controller) {
        super(controller);
        init();
    }

    @Override
    public void init() {
        WarpConfig warpConfig = new WarpConfig();
        warpConfig.initFromController(this, getPlayer(), getInventory());
        openChild(warpConfig);
    }

}
