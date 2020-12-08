package com.noahhusby.sledgehammer.gui.inventories.warp.config;

import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.gui.inventories.anvil.AnvilChild;
import com.noahhusby.sledgehammer.gui.inventories.general.GUIRegistry;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

public class WarpNameEntryAnvil extends AnvilChild {
    @Override
    public AnvilGUI.Builder build(AnvilGUI.Builder builder) {
        builder.text("Enter Warp Name")
            .title("Create a warp")
            .plugin(Sledgehammer.sledgehammer);

        ItemStack warp = new ItemStack(Material.WOOL, 1, (byte) 4);
        builder.itemLeft(warp);

        return builder;
    }

    @Override
    public void onLeftItemClick() {
        GUIRegistry.register(new WarpConfigController(getController().getPlayer()));
    }

    @Override
    public void onRightItemClick() {

    }

    @Override
    public void onFinish() {
        switch (getCloseMode())  {
            case EXIT:
                break;
            case LEFT:
                GUIRegistry.register(new WarpConfigController(getController().getPlayer()));
                break;
            case RIGHT:
                break;
            case FINISH:
                if(getText().equals("")) {
                    GUIRegistry.register(new WarpNameEntryController(getController().getPlayer()));
                } else {
                    GUIRegistry.register(new ConfirmationController(getController().getPlayer(), getText(), false));
                }
                break;
        }
    }
}
