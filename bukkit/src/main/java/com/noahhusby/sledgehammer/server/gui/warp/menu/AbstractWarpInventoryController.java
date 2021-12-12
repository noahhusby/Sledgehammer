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

package com.noahhusby.sledgehammer.server.gui.warp.menu;

import com.google.common.collect.Lists;
import com.noahhusby.sledgehammer.common.warps.WarpPayload;
import com.noahhusby.sledgehammer.server.gui.GUIController;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Noah Husby
 */
public abstract class AbstractWarpInventoryController<T extends AbstractWarpInventory> extends GUIController {
    @Getter
    protected final WarpPayload payload;
    @Getter
    private final List<T> pages = Lists.newArrayList();

    public AbstractWarpInventoryController(String name, Player player, WarpPayload payload) {
        super(54, name, player);
        this.payload = payload;
    }

    public AbstractWarpInventoryController(GUIController controller, WarpPayload payload) {
        super(controller);
        this.payload = payload;
    }

    protected void addPage(T page) {
        page.initFromController(this, getPlayer(), getInventory());
        pages.add(page);
    }

    public T getChildByPage(int page) {
        for (T w : pages) {
            if (w.getPage() == page) {
                return w;
            }
        }
        return null;
    }
}
