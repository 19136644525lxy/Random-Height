# Random Height Mod

A Minecraft Forge mod that randomly changes player height.

## Features

- Automatically switches player height at configurable intervals
- Configurable height scale range
- Countdown reminder before height change
- Complete command system for configuration
- Cloth Config GUI support
- Multi-language support (English/Chinese)
- Welcome messages on player join

## Dependencies

- Minecraft 1.20.1
- Forge 47.4.20+
- Pehkui 3.8.2+
- Cloth Config API 11.1.136+

## Commands

| Command | Description |
|---------|-------------|
| `/rh enable <true/false>` | Enable/disable random height feature |
| `/rh interval <seconds>` | Set change interval (10-3600 seconds) |
| `/rh scale <min> <max>` | Set scale range (0.1-5.0) |
| `/rh countdown <true/false>` | Enable/disable countdown reminder |
| `/rh countdown_time <seconds>` | Set countdown time (1-60 seconds) |
| `/rh force` | Force immediate height change |
| `/rh config` | View current configuration |

## Configuration

The mod can be configured via:
1. In-game commands
2. Cloth Config GUI (Mods menu -> Random Height -> Config)
3. Config file: `.minecraft/config/randomheight.toml`

## Installation

1. Install Forge 1.20.1
2. Install Pehkui mod
3. Install Cloth Config API
4. Place this mod in the `mods` folder

## License

MIT License - See [LICENSE](LICENSE) file

## Author

Yifei

## Project Link

https://github.com/19136644525lxy/Random-Height