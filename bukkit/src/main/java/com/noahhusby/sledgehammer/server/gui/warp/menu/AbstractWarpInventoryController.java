package com.noahhusby.sledgehammer.server.gui.warp.menu;

import com.google.common.collect.Lists;
import com.noahhusby.sledgehammer.server.data.warp.WarpPayload;
import com.noahhusby.sledgehammer.server.gui.GUIController;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Noah Husby
 */
public abstract class AbstractWarpInventoryController<T extends AbstractWarpInventory> extends GUIController {
    @Getter protected final WarpPayload payload;
    @Getter private final List<T> pages = Lists.newArrayList();

    public AbstractWarpInventoryController(String name, Player player, WarpPayload payload) {
        super(54, name, player);
        this.payload = payload;
    }

    public AbstractWarpInventoryController(GUIController controller, WarpPayload payload) {
        super(controller);
        this.payload = payload;
    }

    protected void addPage(T page)  {
        page.initFromController(this, getPlayer(), getInventory());
        pages.add(page);
    }

    public T getChildByPage(int page) {
        for(T w : pages) {
            if(w.getPage() == page) {
                return w;
            }
        }
        return null;
    }
}
