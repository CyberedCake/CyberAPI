# CyberAPI
[![](https://jitpack.io/v/CyberedCake/CyberAPI.svg)](https://jitpack.io/#CyberedCake/CyberAPI)
<br> <br>
The new and improved CyberAPI, version 3, is here! This is a library used for most of [CyberedCake's](https://github.com/CyberedCake) plugins and can be used by you for free! It adds additional features that Spigot and Bungeecord (bungee coming soon) do not have and makes existing stuff easier.

## As of now, only \*\*SPIGOT\*\* and Spigot forks are supported! Bungeecord support coming soon!

# How to install and use

<details>
  <summary><b>INSTALL FOR SPIGOT</b> (click to expand/shrink)</summary>

## Installation - Spigot
### (It is recommended that you use [PaperSpigot](https://papermc.io/) instead of Spigot, but Spigot is still supported and PaperSpigot works on the 'spigot' portion of the library!)
    
You can install the maven package and gradle package by going **_[here (jitpack.io)](https://jitpack.io/#CyberedCake/CyberAPI/)_** and clicking the platform you wish to use CyberAPI on.
    
## How to use - Spigot
To use CyberAPI, write this in your main onEnable method:

```java
import net.cybercake.cyberapi.CyberAPI;
import net.cybercake.cyberapi.settings.Settings;

public class MainClass extends CyberAPI { // you must extend CyberAPI

    @Override
    public void onEnable() {
        startCyberAPI( // this method will start CyberAPI and is **required** to be the first thing in your onEnable() method
                new Settings()
                        // put your settings here, usually in the form of .<setting>(<value>)
                        .build() // build once you have changed the settings you want
        );
        
        // now you have access to everything CyberAPI!
        // the official website for the javadocs will be coming soon, but it's not here yet!
        // have fun!
    }

}
```

</details>

<details>
  <summary><b>INSTALL FOR BUNGEECORD</b> (click to expand/shrink)</summary>

## Coming soon!
</details>
