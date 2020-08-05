# Sledgehammer
A set of tools for multi-server Build the Earth servers

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
message-prefix: '[BTE] '

# Set this to false to disable global tpll [/tpll & /cs tpll]
global-tpll: true

# The command for network-wide warping. Leave blank to disable
# Permissions: sledgehammer.warp for teleporting, and sledgehammer.warp.admin for setting warps
warp-command: 'nwarp'

# Enter your Bungeecord (Case Sensitive) servers below with the corresponding states.
# This is used to determine where to teleport when using /tpll
servers:
  - Michigan:
      - State, Michigan, United States of America
      - City, Toledo, Ohio
  - Ohio:
      - State, Ohio, United States of America
```
#### Bungeecord Region Config
Sledgehammer supports the following region types: City (Town), County, State, and Country

Examples:
* **City** - City, Toledo, Ohio
* **County** - County, Wayne, Michigan
* **State** - State, California, United States of America
* **Country** - Country, United States of America

### Default Forge Configuration:
```yaml
# Configuration file

general {
    # Use the same authentication code as the bungeecord server you are connecting to [default: ]
    S:"Network Authentication Code"=

    # Use [tpll] for /tpll, or [cs] for /cs tpll [default: tpll]
    S:"Teleportation Mode"=tpll
}
```
#### Tpll Execution Mode
The Sledgehammer plugin will recognize both /tpll and /cs tpll, but you must state which one you want to use for execution on the server. Default: tpll

## Usage

## Building
