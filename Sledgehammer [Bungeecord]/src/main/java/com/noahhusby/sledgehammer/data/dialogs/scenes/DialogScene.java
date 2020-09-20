package com.noahhusby.sledgehammer.data.dialogs.scenes;

import com.google.common.collect.Maps;
import com.noahhusby.sledgehammer.data.dialogs.components.IDialogComponent;
import com.noahhusby.sledgehammer.data.dialogs.toolbars.DefaultToolbar;
import com.noahhusby.sledgehammer.data.dialogs.toolbars.IToolbar;
import com.noahhusby.sledgehammer.handlers.DialogHandler;
import com.noahhusby.sledgehammer.util.ChatHelper;
import com.noahhusby.sledgehammer.util.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

import java.util.Map;

public abstract class DialogScene implements IDialogScene {

    protected IDialogComponent currentComponent;
    protected CommandSender sender;

    protected final DialogHandler dialogHandler = DialogHandler.getInstance();
    private final Map<Integer, IDialogComponent> components = Maps.newHashMap();

    @Override
    public void init(CommandSender commandSender) {
        this.sender = commandSender;
        this.currentComponent = components.get(0);
        dialogHandler.progressDialog(this, false);
        onInitialization();
    }

    protected void registerComponent(IDialogComponent d) {
        components.put(components.size(), d);
    }

    @Override
    public IDialogComponent getCurrentComponent() {
        return currentComponent;
    }

    @Override
    public CommandSender getCommandSender() {
        return sender;
    }

    @Override
    public void onMessage(String m) {
        for(Map.Entry<String, String> x : getToolbar().getTools().entrySet()) {
            if(x.getKey().equals(m.toLowerCase().trim())) {
                onToolbarAction(m);
                return;
            }
        }

        progressDialog(m);
    }

    @Override
    public IToolbar getToolbar() {
        return new DefaultToolbar();
    }

    @Override
    public void onToolbarAction(String m) {
        if(m.equals("exit")) {
            dialogHandler.discardDialog(this);
            getCommandSender().sendMessage();
            getCommandSender().sendMessage(ChatHelper.getInstance().makeTextComponent(new TextElement("Exited the dialog!", ChatColor.RED)));
        }
    }

    @Override
    public boolean isAdmin() {
        return false;
    }

    @Override
    public TextElement[] getTitle() {
        return null;
    }

    @Override
    public void onComponentFinish() { }

    @Override
    public void onInitialization() { }

    public void progressDialog(String m) {
        progressDialog(m, false);
    }

    public void progressDialog(String m, boolean override) {
        if(currentComponent.validateResponse(m)) {
            currentComponent.setValue(m);
            if(currentComponent.isManual() && !override) {
                onComponentFinish();
                return;
            }
        } else {
            dialogHandler.progressDialog(this, true);
            return;
        }

        int x = components.size();
        for(Map.Entry<Integer, IDialogComponent> c : components.entrySet()) {
            if(c.getValue() == currentComponent) {
                if((c.getKey() + 1) >= x) {
                    dialogHandler.discardDialog(this);
                    onFinish();
                    return;
                }
                currentComponent = components.get(c.getKey()+1);
                dialogHandler.progressDialog(this, false);
                return;
            }
        }
        dialogHandler.progressDialog(this, false);
    }

    public String getValue(String key) {
        for(Map.Entry<Integer, IDialogComponent> s : components.entrySet()) {
            if(s.getValue().getKey().toLowerCase().equals(key.toLowerCase())) {
                return s.getValue().getValue();
            }
        }

        return "";
    }
}
