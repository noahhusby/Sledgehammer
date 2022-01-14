/*
 * MIT License
 *
 * Copyright 2020-2022 noahhusby
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
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
