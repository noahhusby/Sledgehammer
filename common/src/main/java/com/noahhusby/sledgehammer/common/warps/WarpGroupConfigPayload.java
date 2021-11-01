package com.noahhusby.sledgehammer.common.warps;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class WarpGroupConfigPayload {
    private final String salt;
    private final Map<Integer, Warp> waypoints;
    private final Map<String, WarpGroup> groups;
    private final Map<String, List<Integer>> servers;

    @Setter
    private GroupConfigAction action;

    public enum GroupConfigAction {
        OPEN_CONFIG, REMOVE_SUCCESSFUL, REMOVE_FAILURE, ADD_SUCCESSFUL, ADD_FAILURE
    }
}
