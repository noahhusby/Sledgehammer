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

@Key("Name")
@Getter
public class SledgehammerServer {
    @Expose
    private String name;
    @Expose
    @Setter
    private String nick;
    @Expose
    @Setter
    private boolean earthServer;
    @Expose
    @Setter
    private List<Location> locations = new ArrayList<>();
    @Expose
    @Setter
    private int xOffset;
    @Expose
    @Setter
    private int zOffset;
    @Expose
    @Setter
    private boolean stealthMode;
    @Expose
    @Setter
    private TpllMode tpllMode = TpllMode.NORMAL;
    private SledgehammerVersion sledgehammerVersion = null;

    public SledgehammerServer() {
    }

    public SledgehammerServer(String name) {
        this.name = name;
        this.nick = name;
    }

    /**
     * Initializes server from init packet
     *
     * @param version Sledgehammer Version
     */
    public void initialize(SledgehammerVersion version) {
        this.sledgehammerVersion = version;
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
    public ServerInfo getServerInfo() {
        return ProxyServer.getInstance().getServerInfo(name);
    }
}
