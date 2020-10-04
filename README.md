# Sledgehammer
A set of tools for multi-server Build the Earth networks

[![sledgehammer-bukkit Actions Status](https://github.com/noahhusby/sledgehammer/workflows/sledgehammer-bukkit/badge.svg)](https://github.com/noahhusby/sledgehammer/actions)
[![sledgehammer-forge Actions Status](https://github.com/noahhusby/sledgehammer/workflows/sledgehammer-forge/badge.svg)](https://github.com/noahhusby/sledgehammer/actions)

## Features
* Region Assigning - /tpll will automatically teleport between servers based upon region configuration
* Global Warps - Set warps and teleport across the network
* Warp Map - Online map vizualizer with warps. Check out [Sledgehammer Map](https://github.com/noahhusby/Sledgehammer-Map/releases)

#### Future Plans
* Border Teleportation (WIP) - Teleport automatically between servers when passing through region borders
* Border Duplication - Duplicate buildings on border for a seamless teleportation experience

## Installation
Go to the [releases](https://github.com/noahhusby/Sledgehammer/releases) page and download the latest bungeecord plugin and bukkit plugin.

### Bungeecord
* Place Sledgehammer-x.x.x-bungeecord.jar in the `/plugins` folder of your Bungeecord installation.
* Run Bungeecord. The configuration files will automatically generate.

Note: Sledgehammer supports any fork of Bungeecord, including Waterfall.

### Bukkit
* Place Sledgehammer-bukkit-1.12.2-x.x-x.jar in the `/plugins` folder of your bukkit server.
* Run the server. The configuration files will automatically generate.

## Configuration

### Default Bungeecord Configuration:
```yaml
# Configuration file

##########################################################################################################
# general
#--------------------------------------------------------------------------------------------------------#
# General options for sledgehammer
##########################################################################################################

general {
    # Set this to false to disable global tpll [/tpll & /cs tpll] [default: true]
    B:"Global Tpll"=true

    # The prefix of messages broadcasted to players from the proxy [default: &9&lBTE &8&l> ]
    S:"Message Prefix"=&9&lBTE &8&l> 

    # Generate a new key using https://uuidgenerator.net/version4
    # All corresponding sledgehammer clients must have the same code
    # Don't share this key with anyone you don't trust as it will allow anybody to run any command on connected servers. [default: ]
    S:"Network Authentication Code"=

    # The command for network-wide warping. Leave blank to disable
    # Permissions: sledgehammer.warp for teleporting, and sledgehammer.warp.admin for setting warps. [default: nwarp]
    S:"Warp Command"=nwarp
}


##########################################################################################################
# geography
#--------------------------------------------------------------------------------------------------------#
# Options for OpenStreetMaps and Teleportation.
##########################################################################################################

geography {
    # Set to false to disable automatic border teleportation, or true to enable it. (Note: OSM Offline Mode must be set to true for this to be enabled. [default: false]
    B:"Auto Border Teleportation"=false

    # Set false for fetching the latest data from OSM (more up to date), or true for using a downloaded database.
    # Please follow the guide on https://github.com/noahhusby/sledgehammer about downloading and configuring the offline database. [default: false]
    B:"OSM Offline Mode"=false
}


##########################################################################################################
# map
#--------------------------------------------------------------------------------------------------------#
# Options for sledgehammer's map
##########################################################################################################

map {
    # Set this to true to enable sledgehammer's map [default: false]
    B:Enable=false

    # The websocket url/ip where sledgehammer map is running [default: 127.0.0.1]
    S:Host=127.0.0.1

    # The starting latitude when the map loads. [range: -90.0 ~ 90.0, default: 0.0]
    S:Latitude=0.0

    # The starting longitude when the map loads. [range: -90.0 ~ 90.0, default: 0.0]
    S:Longitude=0.0

    # The direct http link for the map. This is the link that players will interact with.
    # NOTE: You must put either http:// or https:// at the beginning [default: http://map.bte-network.net]
    S:"Map Link"=http://localhost:7000

    # How long (in minutes) a session will last before the player needs to invoke the map command again. [range: 5 ~ 60, default: 10]
    I:"Map Session Timeout"=10

    # The port that the map websocket is running.
    # The websocket port can be changed in the map's config file [default: 7000]
    S:Port=7000

    #  [default: IP: bte-network.net]
    S:Subtitle=IP: bte-network.net

    #  [default: A BTE Network]
    S:Title=A BTE Network

    # The starting zoom when the map loads [range: 5 ~ 18, default: 6]
    I:Zoom=6
}
```

### Default Bukkit Configuration:
```yaml
# Configuration file

general {
    # Raise this if sledgehammer's inter-server actions aren't executing. Default: 10000 (10 seconds) [range: 2000 ~ 30000, default: 10000]
    I:"Execution Timeout"=10000

    # Use the same authentication code as the bungeecord server you are connecting to [default: ]
    S:"Network Authentication Code"=

    # Use 'internal' for sledgehammer's internal interpreter. Use 'tpll' for terra121's interpreter, or 'cs' for BTE Tool's interpreter. [default: internal]
    S:"Teleportation Mode"=internal
}
```
#### Tpll Execution Mode
The Sledgehammer plugin will recognize both /tpll and /cs tpll, but you must state which one you want to use for execution on the server. Sledgehammer also includes its own internal teleporter which reduces lag.
Default: internal

### Bungeecord Region Config
Sledgehammer supports the following region types: City (Town), County, State, and Country

To setup regions, you must have the permission node `sledgehammer.admin`. Run `/sha setup` in-game and follow the prompts. **Note: Territories are classified as states in sledgehammer, and should be entered as such.**

## Usage
### Commands
* `/tpll [target player] <lat> <lon>` (or `/cs tpll [target player] <lat> <lon>`) - Teleport to latitude and longitude within the server. Sledgehammer will automatically decide which server to send players to based upon region configuration. This can be disabled in the bungeecord config file.
* `/nwarp <warp name>` - Teleport to a specific warp
* `/nwarp list` - Lists all network warps
* `/nwarp set <warp name>` - Sets warp
* `/nwarp remove <warp name>` - Removes a warp

### Permissions
* `sledgehammer.admin` - Gives access to all sledgehammer commands + admin privileges. **Be careful when assigning this permission as it can cause serious damage!**
* `sledgehammer.tpll.[Server Name | all]` - Allows player to /tpll to that specific server
* `sledgehammer.tpll.bypass.[Server Name | all]` - Allows player to /tpll on a server, even if that server has local restrictions
* `sledgehammer.tpll.admin` - Allows an admin to execute /tpll on behalf of another player
* `sledgehammer.warp` - Gives access to /nwarp teleportation
* `sledgehammer.warp.admin` - Permits the creation and removal of warps

## Building
### Bungeecord
* Clone this repo, or download as a zip
* Open the `Sledgehammer [Bungeecord]` in your preferred IDE
* Build using: Maven Package

### Bukkit
* Clone this repo, or download as a zip
* Open the `Sledgehammer [Bukkit]` folder in IntelliJ
* Import it as a gradle project
* Run `./gradlew build` to build
