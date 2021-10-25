/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - GUIController.java
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

package com.noahhusby.sledgehammer.server.gui;

import lombok.Getter;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.entity.Player;

public class AnvilController implements IController {
    @Getter
    private final Player player;

    public AnvilController(Player player) {
        this.player = player;
    }

    public AnvilController(AnvilController controller) {
        this.player = controller.player;
    }

    public void openChild(AnvilChild child) {
        if (child == null) {
            return;
        }
        child.setController(this);

        AnvilGUI.Builder builder = new AnvilGUI.Builder();
        child.build(builder);

        builder.onComplete((p, s) -> {
            child.setText(s);
            child.setCloseMode(AnvilChild.CloseMode.FINISH);
            return AnvilGUI.Response.close();
        });

        builder.onLeftInputClick((player) -> {
            child.onLeftItemClick();
            child.setCloseMode(AnvilChild.CloseMode.LEFT);
        });

        builder.onRightInputClick((player) -> {
            child.onRightItemClick();
            child.setCloseMode(AnvilChild.CloseMode.RIGHT);
        });

        builder.onClose((player) -> child.onFinish(child.getCloseMode()));
        builder.open(player);
    }

    public void close() {
        player.closeInventory();
    }

    public void init() {
    }
}
