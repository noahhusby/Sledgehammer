# Sledgehammer
[![Build Status](https://jenkins.noahhusby.com/job/Sledgehammer/badge/icon)](https://jenkins.noahhusby.com/job/Sledgehammer/)
A set of tools for multi-server Build the Earth networks

### Documentation has been moved to the [wiki](https://github.com/noahhusby/Sledgehammer/wiki)

## Features

* Region Assigning - /tpll will automatically teleport between servers based upon region configuration
* Global Warps - Set warps and teleport across the network
* Warp Map - Online map vizualizer with warps. Check
  out [Sledgehammer Map](https://github.com/noahhusby/Sledgehammer-Map/releases)
* Border Teleportation (WIP) - Teleport automatically between servers when passing through region borders

#### Future Plans

* Border Duplication - Duplicate buildings on border for a seamless teleportation experience

## Installation

Go to the [releases](https://github.com/noahhusby/Sledgehammer/releases) page and download the latest bungeecord plugin
and bukkit plugin.

## Building

* Clone this repo, or download as a zip
* Open `sledgehammer` in your preferred IDE
* Build using: `./gradlew shadowJar`
* Individual Builds for Bungeecord and Bukkit will be found under `[module]/build`
