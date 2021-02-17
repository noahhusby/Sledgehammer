package com.noahhusby.sledgehammer.server.gui.inventories.warp.config.manage;

import com.noahhusby.sledgehammer.common.warps.Warp;
import com.noahhusby.sledgehammer.server.Sledgehammer;
import com.noahhusby.sledgehammer.server.data.warp.WarpConfigPayload;
import com.noahhusby.sledgehammer.server.gui.inventories.anvil.AnvilChild;
import com.noahhusby.sledgehammer.server.gui.inventories.general.GUIRegistry;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ChangeNameAnvil extends AnvilChild {

    private final WarpConfigPayload payload;
    private final Warp warp;

    public ChangeNameAnvil(WarpConfigPayload payload, Warp warp) {
        this.payload = payload;
        this.warp = warp;
    }

    @Override
    public AnvilGUI.Builder build(AnvilGUI.Builder builder) {
        builder.text(warp.getName())
            .title("Rename Warp")
            .plugin(Sledgehammer.getInstance());

        ItemStack warp = new ItemStack(Material.WOOL, 1, (byte) 4);
        builder.itemLeft(warp);

        return builder;
    }

    @Override
    public void onLeftItemClick() {
        GUIRegistry.register(new ManageWarpInventoryController(getController().getPlayer(), payload, warp));
    }

    @Override
    public void onRightItemClick() {

    }

    @Override
    public void onFinish() {
        switch (getCloseMode())  {
            case EXIT:
                break;
            case RIGHT:
            case LEFT:
                GUIRegistry.register(new ManageWarpInventoryController(getController().getPlayer(), payload, warp));
                break;
            case FINISH:
                if(getText().equals("")) {
                    GUIRegistry.register(new ChangeNameController(getController().getPlayer(), payload, warp));
                } else {
                    warp.setName(getText());
                    GUIRegistry.register(new ManageWarpInventoryController(getController().getPlayer(), payload, warp));
                }
                break;
        }
    }
}
