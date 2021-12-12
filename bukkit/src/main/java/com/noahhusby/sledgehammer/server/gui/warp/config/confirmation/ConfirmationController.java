/*
 * MIT License
 *
 * Copyright 2020-2021 noahhusby
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

package com.noahhusby.sledgehammer.server.gui.warp.config.confirmation;

import com.noahhusby.sledgehammer.common.warps.Warp;
import com.noahhusby.sledgehammer.common.warps.WarpConfigPayload;
import com.noahhusby.sledgehammer.server.gui.GUIChild;
import com.noahhusby.sledgehammer.server.gui.GUIController;
import org.bukkit.entity.Player;

public class ConfirmationController extends GUIController {
    private final WarpConfigPayload payload;
    private final Type type;
    private Warp warp;

    public ConfirmationController(Player player, WarpConfigPayload payload, Type type, Warp warp) {
        super(27, "Warp Confirmation", player);
        this.payload = payload;
        this.type = type;
        this.warp = warp;
        init();
    }

    public ConfirmationController(Player player, WarpConfigPayload payload, Type type) {
        super(27, "Warp Confirmation", player);
        this.payload = payload;
        this.type = type;
        init();
    }

    public ConfirmationController(GUIController controller, WarpConfigPayload payload, Type type) {
        super(controller);
        this.payload = payload;
        this.type = type;
        init();
    }

    @Override
    public void init() {
        GUIChild child = null;
        switch (type) {
            case ADD_SUCCESSFUL:
                child = new CreationSuccessInventory(payload);
                break;
            case ADD_FAILURE:
                child = new CreationFailureInventory(payload);
                break;
            case REMOVE_FAILURE:
                child = new RemoveFailureInventory(payload);
                break;
            case REMOVE_SUCCESSFUL:
                child = new RemoveSuccessInventory(payload);
                break;
            case HEAD_UPDATE:
                child = new HeadUpdateSuccessInventory(payload);
                break;
            case LOCATION_UPDATE:
                child = new LocationSuccessInventory(payload, warp);
                break;
        }

        child.initFromController(this, getPlayer(), getInventory());
        openChild(child);
    }

    public enum Type {
        REMOVE_SUCCESSFUL, REMOVE_FAILURE, ADD_SUCCESSFUL, ADD_FAILURE, HEAD_UPDATE, LOCATION_UPDATE
    }
}
