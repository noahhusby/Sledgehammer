# Sledgehammer
A set of tools for multi-server Build the Earth servers

## Features
* Region Assigning - /tpll will automatically teleport between servers based upon region configuration
* Global Warps - Set warps and teleport across the network

#### Future Plans
* Border Teleportation (WIP) - Teleport automatically between servers when passing through region borders
* Border Duplication - Duplicate buildings on border for a seamless teleportation experience

## Installation
Go to the [releases](https://github.com/noahhusby/Sledgehammer/releases) page and download the latest bungeecord plugin and forge mod.

### Bungeecord
* Place Sledgehammer-x.x-bungeecord.jar in the `/plugins` folder of your Bungeecord installation.
* Run Bungeecord. The configuration files will automatically generate.

Note: Sledgehammer supports any fork of Bungeecord, including Waterfall.

### Forge
* Place Sledgehammer-1.12.2-x.x.jar in the `/mods` folder of your forge server.
* Run the server. The configuration files will automatically generate.

## Configuration

### Default Bungeecord Configuration:
```yaml
# Generate a new key using https://uuidgenerator.net/version4
# All corresponding sledgehammer clients must have the same code
# Don't share this key with anyone you don't trust as it will allow anybody to run any command on connected servers.
authentication-key: ''

# The prefix of messages broadcasted to players from the proxy
message-prefix: '&9&lBTE &8&l> '

# Set this to false to disable global tpll [/tpll & /cs tpll]
global-tpll: true

# The command for network-wide warping. Leave blank to disable
# Permissions: sledgehammer.warp for teleporting, and sledgehammer.warp.admin for setting warps
warp-command: 'nwarp'

# Do not edit this!
config-version: 0.3
```

### Default Forge Configuration:
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

### Forge
* Clone this repo, or download as a zip
* Open the `Sledgehammer [Forge]` folder in IntelliJ
* Import it as a gradle project
* Run `./gradlew setupDecompWorkshop` to download the necessary libraries
* Run `./gradlew reofbShadowJar` to build
