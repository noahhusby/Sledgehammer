/*
 * MIT License
 *
 * Copyright 2020-2022 noahhusby
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.noahhusby.sledgehammer.proxy.servers;

import com.google.gson.annotations.Expose;
import com.noahhusby.lib.data.storage.Key;
import com.noahhusby.sledgehammer.common.SledgehammerVersion;
import com.noahhusby.sledgehammer.common.TpllMode;
import com.noahhusby.sledgehammer.proxy.datasets.Location;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.ArrayList;
import java.util.List;

@Key("name")
@Getter
@Setter
public class SledgehammerServer {
    @Expose
    private final String name;
    @Expose
    private String nick;
    @Expose
    private boolean earthServer;
    @Expose
    private List<Location> locations = new ArrayList<>();
    @Expose
    private int xOffset;
    @Expose
    private int zOffset;
    @Expose
    private boolean stealthMode;
    @Expose
    private TpllMode tpllMode = TpllMode.NORMAL;
    private SledgehammerVersion sledgehammerVersion = null;

    public SledgehammerServer(String name) {
        this.name = name;
        this.nick = name;
    }

    /**
     * Gets whether the server is initialized
     *
     * @return True if initialized, false if not
     */
    public boolean isInitialized() {
        return sledgehammerVersion != null;
    }

    /**
     * Gets {@link ServerInfo}
     *
     * @return {@link ServerInfo}
     */
    public ServerInfo getInfo() {
        return ProxyServer.getInstance().getServerInfo(name);
    }
}
