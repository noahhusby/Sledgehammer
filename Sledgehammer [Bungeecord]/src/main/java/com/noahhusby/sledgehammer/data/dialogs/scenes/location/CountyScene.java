package com.noahhusby.sledgehammer.data.dialogs.scenes.location;

import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.types.Server;
import com.noahhusby.sledgehammer.data.dialogs.components.location.CountryComponent;
import com.noahhusby.sledgehammer.data.dialogs.components.location.CountyComponent;
import com.noahhusby.sledgehammer.data.dialogs.components.location.StateComponent;
import com.noahhusby.sledgehammer.data.dialogs.scenes.DialogScene;
import com.noahhusby.sledgehammer.data.dialogs.toolbars.ExitSkipToolbar;
import com.noahhusby.sledgehammer.data.dialogs.toolbars.IToolbar;
import com.noahhusby.sledgehammer.datasets.Location;
import com.noahhusby.sledgehammer.handlers.DialogHandler;
import net.md_5.bungee.api.config.ServerInfo;

public class CountyScene extends DialogScene {

    private ServerInfo server;
    private DialogScene scene;

    public CountyScene(ServerInfo server) {
        this(server, null);
    }

    public CountyScene(ServerInfo server, DialogScene scene) {
        this.server = server;
        this.scene = scene;
        registerComponent(new CountyComponent());
        registerComponent(new StateComponent());
        registerComponent(new CountryComponent());
    }

    @Override
    public void onFinish() {
        Location l = new Location(Location.detail.state, "", getValue("county"), getValue("state"), getValue("country"));

        Server s = ServerConfig.getInstance().getServer(server.getName());

        if(s == null) s = new Server(server.getName());

        s.locations.add(l);
        ServerConfig.getInstance().pushServer(s);
        if(scene != null) {
            DialogHandler.getInstance().discardDialog(this);
            DialogHandler.getInstance().startDialog(getCommandSender(), scene);
        }
    }

    @Override
    public IToolbar getToolbar() {
        return new ExitSkipToolbar();
    }

    @Override
    public void onToolbarAction(String m) {
        if(m.equals("exit")) {
            DialogHandler.getInstance().discardDialog(this);
            DialogHandler.getInstance().startDialog(getCommandSender(), new LocationSelectionScene(server));
        } else if(m.equals("@")) {
            progressDialog("");
        }
    }

    @Override
    public boolean isAdmin() {
        return true;
    }
}
