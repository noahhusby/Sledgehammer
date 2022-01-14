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

package com.noahhusby.sledgehammer.server.gui.warp.config.manage;

import com.noahhusby.sledgehammer.common.warps.Warp;
import com.noahhusby.sledgehammer.common.warps.WarpConfigPayload;
import com.noahhusby.sledgehammer.server.Sledgehammer;
import com.noahhusby.sledgehammer.server.gui.AnvilChild;
import com.noahhusby.sledgehammer.server.gui.AnvilController;
import com.noahhusby.sledgehammer.server.gui.GUIRegistry;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ChangeWarpNameAnvil extends AnvilChild {

    private final WarpConfigPayload payload;
    private final Warp warp;

    public ChangeWarpNameAnvil(WarpConfigPayload payload, Warp warp) {
        this.payload = payload;
        this.warp = warp;
    }

    @Override
    public void build(AnvilGUI.Builder builder) {
        builder.text(warp.getName())
                .title("Rename Warp")
                .plugin(Sledgehammer.getInstance());

        ItemStack warp = new ItemStack(Material.WOOL, 1, (byte) 4);
        builder.itemLeft(warp);
    }

    @Override
    public void onLeftItemClick() {
        GUIRegistry.register(new ManageWarpInventory.ManageWarpInventoryController(getController().getPlayer(), payload, warp));
    }

    @Override
    public void onRightItemClick() {
    }

    @Override
    public void onFinish(CloseMode mode) {
        switch (mode) {
            case EXIT:
                break;
            case RIGHT:
            case LEFT:
                GUIRegistry.register(new ManageWarpInventory.ManageWarpInventoryController(getController().getPlayer(), payload, warp));
                break;
            case FINISH:
                if (getText().equals("")) {
                    GUIRegistry.register(new ChangeNameController(getController().getPlayer(), payload, warp));
                } else {
                    warp.setName(getText());
                    GUIRegistry.register(new ManageWarpInventory.ManageWarpInventoryController(getController().getPlayer(), payload, warp));
                }
                break;
        }
    }

    public static class ChangeNameController extends AnvilController {
        private final WarpConfigPayload payload;
        private final Warp warp;

        public ChangeNameController(AnvilController controller, WarpConfigPayload payload, Warp warp) {
            super(controller);
            this.payload = payload;
            this.warp = warp;
            init();
        }

        public ChangeNameController(Player player, WarpConfigPayload payload, Warp warp) {
            super(player);
            this.payload = payload;
            this.warp = warp;
            init();
        }

        @Override
        public void init() {
            openChild(new ChangeWarpNameAnvil(payload, warp));
        }
    }
}
