package com.noahhusby.sledgehammer.data.dialogs;

import com.google.common.collect.Maps;
import com.noahhusby.sledgehammer.data.dialogs.components.IDialogComponent;
import com.noahhusby.sledgehammer.handlers.DialogHandler;
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
        if(currentComponent.validateResponse(m)) {
            currentComponent.setValue(m);
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
