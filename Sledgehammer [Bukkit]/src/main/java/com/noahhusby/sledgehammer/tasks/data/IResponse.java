package com.noahhusby.sledgehammer.tasks.data;

import org.json.simple.JSONObject;

public interface IResponse {
    String getResponseCommand();

    JSONObject response();
}
