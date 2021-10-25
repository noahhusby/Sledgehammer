package com.noahhusby.sledgehammer.common.warps;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class WarpConfigPayload {
    private final boolean local;
    private final boolean admin;
    private final String group;
    private final String salt;
    private final Map<Integer, Warp> waypoints;
    private final Map<String, WarpGroupPayload> groups;
    private final Map<String, List<Integer>> servers;

    @Setter
    private ServerConfigAction action;

    public enum ServerConfigAction {
        OPEN_CONFIG, REMOVE_SUCCESSFUL, REMOVE_FAILURE, ADD_SUCCESSFUL, ADD_FAILURE, LOCATION_UPDATE
    }
}
