# AFK Guard Mod

A client-side Fabric mod that protects you while you're AFK by automatically disconnecting if you take damage. AFK in peace.

## Features

- **AFK Protection**: Automatically disconnects the client if you take damage while the guard is active.
- **Auto-AFK**: Automatically enables the guard after a configurable period of inactivity (default: 30 seconds).
- **Configurable**: Customize the delay, status messages, and damage sources.
- **Ignore Player Damage**: Option to ignore damage from other players (useful for preventing combat logging, can be disabled).

## Usage

### Keybinds
- **Toggle AFK Guard**: Manually enable/disable the guard. (Check your keybinds menu to set).
- **Toggle Auto-AFK**: Enable/disable the automatic AFK timer.

### Commands
All commands start with `/afkguard`:

- `/afkguard config` - View the current configuration.
- `/afkguard delay <seconds>` - Set the inactivity time before Auto-AFK triggers.
- `/afkguard messages <true|false>` - Toggle status messages on enable/disable.
- `/afkguard ignoreplayerdamage <true|false>` - Choose whether damage from other players should trigger a disconnect.

## Configuration
The config file is located at `.minecraft/config/afkguard.json`. You can edit it manually or use the in-game commands. Cloth config is also supported. I recommend using mod menu with cloth config api to configure the mod in-game.
