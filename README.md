# WooLifeIndicator

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://opensource.org/licenses/MIT)
[![GitHub issues](https://img.shields.io/github/issues/AlphaOmega-IT/WooLifeIndicator.svg)](https://github.com/AlphaOmega-IT/WooLifeIndicator/issues)
[![GitHub stars](https://img.shields.io/github/stars/AlphaOmega-IT/WooLifeIndicator.svg)](https://github.com/AlphaOmega-IT/WooLifeIndicator/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/AlphaOmega-IT/WooLifeIndicator.svg)](https://github.com/AlphaOmega-IT/WooLifeIndicator/network)
[![GitHub pull requests](https://img.shields.io/github/issues-pr/AlphaOmega-IT/WooLifeIndicator.svg)](https://github.com/AlphaOmega-IT/WooLifeIndicator/pulls)

WooLifeIndicator is a Minecraft Plugin for several server platforms including Spigot and Paper, it allows you to enable health bars of entities including all types
of mobs and players. You can restrict worlds and mob types by their default name. Everything happens without PACKETS, so you do not need any further installations to run
this plugin. You can even design your health bar depending on the entity's health.

## Features

- **Simple Integration:** Easily integrate WooLifeIndicator into your server with minimal setup.
- **Pluralization Support:** Handle pluralization effortlessly for different languages. //COMING SOON WITH FUTURE UPDATES
- **Dynamic Loading:** Dynamically load translations based on user preferences or other criteria. //COMING SOON WITH FUTURE UPDATES
- **Customizable:** Customize and extend WooLifeIndicator to suit your specific localization needs. //COMING SOON WITH FUTURE UPDATES
- **NO TOUCH OF CONFIGS EVER:** Customize everything in WooLifeIndicator using a simple inventory layout. //WILL BE RELEASED AFTER THE FIRST 100 DOWNLOADS OF THE PLUGIN: ;)

## Getting Started

### STEP 1: Download the actual plugin
To integrate WooLifeIndicator into your server, download this plugin on the SpigotMC page or our website.
You can also download it from the GitHub itself: https://github.com/AlphaOmega-IT/WooLifeIndicator/releases/tag/PreRelease

### STEP 2: Start your server
Start your server, every file will be generated after the first start, edit your files freely after.

### STEP 3: Edit the config files
Now you are able to edit the config files. The plugin itself is constructed so that if a event get canceled no health bar will be shown. 

```yaml

life-indicator-config.yml - Here is everything about the command itself, which does not include any functionality yet since it's the first version
and is not really needed. It's more likely for the future including more updates! You find here as well the permissions you need to set up.

# This file contains the configuration settings for the life indicator of entities.
# It is loaded by the 'woolifeindicator' plugin.
# For more information, see: https://wiki.atwoo.eu/life-indicator/ //TODO
# For more information, see: https://github.com/AlphaOmega-IT/LifeIndicator

# Commands
commands:
  life-indicator:
    name: 'life-indicator'
    aliases:
      - 'li'
      - 'lifeindicator'
      - 'health-bar'
      - 'healthbar'
      - 'hb'
    description: 'Settings for the life indicator of entities'
    usage: '/life-indicator'
    argumentUsages:
      1$: 'lut["prefix"] & "<gray>/" & alias & ""'
    errorMessages:
      playerNotOnline$: 'lut["prefix"] & "&7The player &c" & value & " &7is &cnot online"'
      internalError$: 'lut["prefix"] & "&4An internal error occurred"'

# Permissions for the commands
permissions:
  missingMessage$: 'lut["prefix"] & "You\sre lacking the permission: " & permission'
  nodes:
    lifeIndicatorAdmin: 'woolifeindicator.lifeindicator.admin'
    seeOthersLifeIndicator: 'woolifeindicator.lifeindicator.visible.players'

# Prefix for the messages
lut:
  prefix: '<gray>[<gold>LifeIndicator<gray>] '
```

```yaml

damage-indicator-config.yml - Here is everything for the design and functionality of the plugin itself, adjust everything as you want.
You can even change the design from "Simple" to "Complex" if you like to (caution: it will only appear if the damageIndicators: key is empty or set as before)
Simple will be always displayed in the number format for players and other entities. 

# This file contains the configuration settings for the life indicator of entities.
# It is loaded by the 'woolifeindicator' plugin.
# For more information, see: https://wiki.atwoo.eu/life-indicator/ //TODO
# For more information, see: https://github.com/AlphaOmega-IT/LifeIndicator

damage-indicator:
  
  # World blacklist, where the damage indicator will not be shown
  blockedWorlds:
    - 'world_nether'

  # Entity blacklist, where the damage indicator will not be shown
  blockedEntities:
    - 'enderman'

  # How long the damage indicator should be visible
  fadeOutTime: 5
  
  # Show the damage indicator of players underneath their nametags
  showLifeIndicatorOfPlayers: true
  
  # Damage indicator design
  # Possible values: Simple, Complex (Experimental)
  damageIndicatorDesign: 'Simple'
  
  # Damage indicators for different damages (if left empty, the damage indicator will show the default design)
  # Simple cause the damage to be a number between 0 and max_health
  # Complex cause the damage to be shown as hearts
  damageIndicators:
    - 0:  '<dark_red>❥</dark_red>'
    - 1:  '<dark_red>❤</dark_red>'
    - 2:  '<dark_red>❤❥</dark_red>'
    - 3:  '<dark_red>❤❤</dark_red>'
    - 4:  '<dark_red>❤❤❥</dark_red>'
    - 5:  '<red>❤❤❤</red>'
    - 6:  '<red>❤❤❤❥</red>'
    - 7:  '<red>❤❤❤❤</red>'
    - 8:  '<red>❤❤❤❤❥</red>'
    - 9:  '<gold>❤❤❤❤❤</gold>'
    - 10: '<gold>❤❤❤❤❤❥</gold>'
    - 11: '<gold>❤❤❤❤❤❤</gold>'
    - 12: '<gold>❤❤❤❤❤❤❥</gold>'
    - 13: '<yellow>❤❤❤❤❤❤❤</yellow>'
    - 14: '<yellow>❤❤❤❤❤❤❤❥</yellow>'
    - 15: '<yellow>❤❤❤❤❤❤❤❤</yellow>'
    - 16: '<yellow>❤❤❤❤❤❤❤❤❥</yellow>'
    - 17: '<green>❤❤❤❤❤❤❤❤❤</green>'
    - 18: '<green>❤❤❤❤❤❤❤❤❤❥</green>'
    - 19: '<green>❤❤❤❤❤❤❤❤❤❤</green>'

```

# Contributing to WooLifeIndicator

We welcome contributions to WooLifeIndicator! If you would like to contribute, please follow these guidelines:

1. Fork the repository and clone it locally.
2. Create a new branch for your feature or bug fix.
3. Make your changes and ensure they are well-tested.
4. Submit a pull request with a clear description of your changes.

Thank you for contributing to WooLifeIndicator!

# Support

If you have any questions, or issues, or need assistance, feel free to reach out:

- Open an [issue](https://github.com/AlphaOmega-IT/WooLifeIndicator/issues)
- Join our [community forums](https://discord.gg/Jq5CAUEDWB)
- Email us at services@alphaomega-it.de

We appreciate your feedback and will do our best to assist you!
