# Random Height Mod

A Minecraft Forge mod that randomly changes player height.

## Features

- Automatically randomizes player height at configurable intervals
- Customizable height scale range
- Countdown notifications before height changes
- Full command system for configuration
- Multi-language support (English & Chinese)

## Requirements

- Minecraft 1.20.1
- Forge 47.4.20+
- Pehkui 3.8.2+

## Commands

| Command | Description |
|---------|-------------|
| `/rh enable <true/false>` | Enable/disable the mod |
| `/rh interval <seconds>` | Set change interval (10-3600 seconds) |
| `/rh scale <min> <max>` | Set scale range (0.1-5.0) |
| `/rh countdown <true/false>` | Enable/disable countdown |
| `/rh countdown_time <seconds>` | Set countdown duration (1-60 seconds) |
| `/rh force` | Force immediate height change |
| `/rh config` | View current configuration |

## Installation

1. Install Forge 1.20.1
2. Install Pehkui mod (required dependency)
3. Place this mod JAR file in your `mods` folder

## Building from Source

```bash
./gradlew build
```

The compiled JAR will be located in `build/libs/`

## License

MIT License - See [LICENSE](LICENSE) file

## Author

Yifei