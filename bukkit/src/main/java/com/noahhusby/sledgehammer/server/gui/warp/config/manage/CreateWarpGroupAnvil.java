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

import com.google.gson.JsonObject;
import com.noahhusby.sledgehammer.common.warps.WarpGroupConfigPayload;
import com.noahhusby.sledgehammer.server.Sledgehammer;
import com.noahhusby.sledgehammer.server.gui.AnvilChild;
import com.noahhusby.sledgehammer.server.gui.AnvilController;
import com.noahhusby.sledgehammer.server.gui.GUIRegistry;
import com.noahhusby.sledgehammer.server.gui.warp.config.ManageWarpGroupViewInventory;
import com.noahhusby.sledgehammer.server.network.NetworkHandler;
import com.noahhusby.sledgehammer.server.network.s2p.S2PWarpGroupConfigPacket;
import lombok.RequiredArgsConstructor;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class CreateWarpGroupAnvil extends AnvilChild {

    private final WarpGroupConfigPayload payload;

    @Override
    public void build(AnvilGUI.Builder builder) {
        builder.text("Enter group name")
                .title("Create a new warp group")
                .plugin(Sledgehammer.getInstance());

        ItemStack warp = new ItemStack(Material.WOOL, 1, (byte) 4);
        builder.itemLeft(warp);
    }

    @Override
    public void onLeftItemClick() {
        GUIRegistry.register(new ManageWarpGroupViewInventory.ManageWarpGroupViewInventoryController(getController().getPlayer(), payload));
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
                GUIRegistry.register(new ManageWarpGroupViewInventory.ManageWarpGroupViewInventoryController(getController().getPlayer(), payload));
                break;
            case FINISH:
                if (getText().equals("")) {
                    GUIRegistry.register(new CreateWarpGroupAnvil.CreateWarpGroupAnvilController(getController().getPlayer(), payload));
                } else {
                    JsonObject json = new JsonObject();
                    json.addProperty("groupName", getText());
                    NetworkHandler.getInstance().send(new S2PWarpGroupConfigPacket(S2PWarpGroupConfigPacket.ProxyConfigAction.CREATE_GROUP, getController().getPlayer(), payload.getSalt(), json));
                }
                break;
        }
    }

    public static class CreateWarpGroupAnvilController extends AnvilController {
        private final WarpGroupConfigPayload payload;

        public CreateWarpGroupAnvilController(AnvilController controller, WarpGroupConfigPayload payload) {
            super(controller);
            this.payload = payload;
            init();
        }

        public CreateWarpGroupAnvilController(Player player, WarpGroupConfigPayload payload) {
            super(player);
            this.payload = payload;
            init();
        }

        @Override
        public void init() {
            openChild(new CreateWarpGroupAnvil(payload));
        }
    }
}
