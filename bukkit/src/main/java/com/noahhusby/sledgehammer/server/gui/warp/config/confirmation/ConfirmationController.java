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

import com.noahhusby.sledgehammer.server.gui.GUIChild;
import com.noahhusby.sledgehammer.server.gui.GUIController;
import org.bukkit.entity.Player;

public class ConfirmationController extends GUIController {
    private final GUIController c;
    private final Type type;
    private final String message;

    public ConfirmationController(Player player, GUIController c, Type type, String message) {
        super(27, "Warp Confirmation", player);
        this.c = c;
        this.type = type;
        this.message = message;
        init();
    }

    @Override
    public void init() {
        GUIChild child = null;
        switch (type) {
            case ADD_SUCCESSFUL:
                child = new CreationSuccessInventory(c, message);
                break;
            case ADD_FAILURE:
                child = new CreationFailureInventory(c, message);
                break;
            case REMOVE_FAILURE:
                child = new RemoveFailureInventory(c, message);
                break;
            case REMOVE_SUCCESSFUL:
                child = new RemoveSuccessInventory(c, message);
                break;
            case HEAD_UPDATE:
                child = new UpdateSuccessInventory(c, message);
                break;
            case LOCATION_UPDATE:
                child = new LocationSuccessInventory(c);
                break;
        }

        child.initFromController(this, getPlayer(), getInventory());
        openChild(child);
    }

    public enum Type {
        REMOVE_SUCCESSFUL, REMOVE_FAILURE, ADD_SUCCESSFUL, ADD_FAILURE, HEAD_UPDATE, LOCATION_UPDATE
    }
}
