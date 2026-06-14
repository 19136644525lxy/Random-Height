# Random Height Mod

A Minecraft Forge mod that randomly changes player height.

## Features

- Automatically randomize player height at regular intervals
- Configurable interval time
- Configurable height scale range
- Countdown reminder before height change
- Complete command system for configuration

## Dependencies

- Minecraft 1.20.1
- Forge 47.4.20+
- Pehkui 3.8.2+
- Cloth Config API 11.1.136+

## Commands

| Command | Description |
|---------|-------------|
| `/rh enable <true/false>` | Enable/disable random height feature |
| `/rh interval <seconds>` | Set interval time (10-3600 seconds) |
| `/rh scale <min> <max>` | Set scale range (0.1-5.0) |
| `/rh scale countdown <true/false>` | Enable/disable countdown reminder |
| `/rh scalecountdown_time <seconds>` | Set countdown time (1-60 seconds) |
| `/rh scale force` | Force immediate height change |
| `/rh scale config` | View current configuration |

## Installation

1. Install Forge 1.20.1
2. Install Pehkui mod
3. Place this mod in the `mods` folder

## License

MIT License - See [LICENSE](LICENSE) file

## Author

Yifei