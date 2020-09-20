package com.noahhusby.sledgehammer.data.dialogs.toolbars;

public class ExitSkipToolbar extends Toolbar {
    public ExitSkipToolbar() {
        addTool("exit", "exit the dialog");
        addTool("@", "skip this prompt");
    }
}
