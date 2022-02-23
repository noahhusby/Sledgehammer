<p align="Center">
   <img src="https://raw.githubusercontent.com/noahhusby/noahhusby/master/assets/sledgehammer/icon.png" alt="Logo" width="250" height="278">
</p>
<h1 align="Center">Sledgehammer</h1>
<p align="center">
  <b>A multi-server utility for the BuildTheEarth project.</b>
  <br/>
  <a href="https://github.com/noahhusby/Sledgehammer/wiki"><strong>Read the docs »</strong></a>
  <br/><br/>
</p>
<p align="center">
    <img src="https://go.buildtheearth.net/community-shield">
    <a href="https://github.com/noahhusby/Sledgehammer/actions/workflows/build.yml"><img src="https://github.com/noahhusby/Sledgehammer/actions/workflows/build.yml/badge.svg"></a>
    <a href="https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE"><img src="https://img.shields.io/github/license/noahhusby/Sledgehammer"></a>
    <a href="https://github.com/noahhusby/Sledgehammer/releases"><img src="https://img.shields.io/github/v/release/noahhusby/sledgehammer?include_prereleases"></a>
    <a href="https://github.com/noahhusby/Sledgehammer"><img src="https://img.shields.io/tokei/lines/github/noahhusby/sledgehammer"></a>
    <a href="https://github.com/noahhusby/Sledgehammer"><img src="https://img.shields.io/github/repo-size/noahhusby/sledgehammer"></a>
    <a href="https://discord.com/invite/BGpmp3sfH5"><img src="https://img.shields.io/discord/706317564904472627?label=discord"></a>
</p>

## Features

* Region Assigning - /tpll will automatically teleport between servers based upon region configuration
* Global Warps - Set warps and teleport across the network
* Border Teleportation - Teleport automatically between servers when passing through region borders

## Installation

Go to the [releases](https://github.com/noahhusby/Sledgehammer/releases) page and download the latest bungeecord plugin
and bukkit plugin.

## Building

* Clone this repo, or download as a zip
* Open `sledgehammer` in your preferred IDE
* Build using: `./gradlew shadowJar`
* Individual Builds for Bungeecord and Bukkit will be found under `[module]/build`
