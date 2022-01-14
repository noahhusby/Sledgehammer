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

import com.noahhusby.sledgehammer.common.warps.WarpGroup;
import com.noahhusby.sledgehammer.common.warps.WarpGroupConfigPayload;
import com.noahhusby.sledgehammer.server.Sledgehammer;
import com.noahhusby.sledgehammer.server.gui.AnvilChild;
import com.noahhusby.sledgehammer.server.gui.AnvilController;
import com.noahhusby.sledgehammer.server.gui.GUIRegistry;
import lombok.RequiredArgsConstructor;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class ChangeWarpGroupNameAnvil extends AnvilChild {

    private final WarpGroupConfigPayload payload;
    private final WarpGroup group;

    @Override
    public void build(AnvilGUI.Builder builder) {
        builder.text(group.getName())
                .title("Rename Warp Group")
                .plugin(Sledgehammer.getInstance());

        ItemStack warp = new ItemStack(Material.WOOL, 1, (byte) 4);
        builder.itemLeft(warp);
    }

    @Override
    public void onLeftItemClick() {
        GUIRegistry.register(new ManageWarpGroupInventory.ManageWarpGroupInventoryController(getController().getPlayer(), payload, group));
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
                GUIRegistry.register(new ManageWarpGroupInventory.ManageWarpGroupInventoryController(getController().getPlayer(), payload, group));
                break;
            case FINISH:
                if (getText().equals("")) {
                    GUIRegistry.register(new ChangeWarpGroupNameAnvil.ChangeWarpGroupNameController(getController().getPlayer(), payload, group));
                } else {
                    group.setName(getText());
                    GUIRegistry.register(new ManageWarpGroupInventory.ManageWarpGroupInventoryController(getController().getPlayer(), payload, group));
                }
                break;
        }
    }

    public static class ChangeWarpGroupNameController extends AnvilController {
        private final WarpGroupConfigPayload payload;
        private final WarpGroup group;

        public ChangeWarpGroupNameController(AnvilController controller, WarpGroupConfigPayload payload, WarpGroup group) {
            super(controller);
            this.payload = payload;
            this.group = group;
            init();
        }

        public ChangeWarpGroupNameController(Player player, WarpGroupConfigPayload payload, WarpGroup group) {
            super(player);
            this.payload = payload;
            this.group = group;
            init();
        }

        @Override
        public void init() {
            openChild(new ChangeWarpGroupNameAnvil(payload, group));
        }
    }
}
