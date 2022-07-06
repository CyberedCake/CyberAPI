# CyberAPI
[![](https://jitpack.io/v/CyberedCake/CyberAPI.svg?label=Latest+Version)](https://jitpack.io/p/CyberedCake/CyberAPI)
<br> <br>
The new and improved CyberAPI, version 3, is here! This is a library used for most of [CyberedCake's](https://github.com/CyberedCake) plugins and can be used by you for free! It adds additional features that Spigot and Bungeecord (bungee coming soon) do not have and makes existing stuff easier.
<br> <br>
You can view the to-do list and future plans [by clicking here](https://github.com/CyberedCake/CyberAPI/projects/1)

# How to install and use

<details>
  <summary><b>INSTALL FOR SPIGOT</b> (click to expand/shrink)</summary>

## Installation - Spigot
### (It is recommended that you use [PaperSpigot](https://papermc.io/downloads) instead of Spigot, but Spigot is still supported and PaperSpigot works on the 'spigot' portion of the library!)
    
<details>
  <summary><b>INSTALL WITH GRADLE [RECOMMENDED]</b> (click to expand/shrink)</summary>

--------------------------------------------------------------------------------------------------------------
Step 1) Include the below code in your build.gradle "repositories" section.
```gradle
	repositories {
		maven { url 'https://jitpack.io' }
	}
```

Step 2) Include the below code in your build.gradle "dependencies" and replace "LATEST" with the latest version that you see here: [![](https://jitpack.io/v/CyberedCake/CyberAPI.svg?label=Latest+Version)](https://jitpack.io/p/CyberedCake/CyberAPI) <br>
Step 2) <b>NOTE: You must keep the "v" at the beginning of the version number!</b>
```gradle
	dependencies {
	        implementation 'com.github.CyberedCake.CyberAPI:spigot:vLATEST'
	}
```

Step 3) Reload your gradle project and follow the usage instructions below.

--------------------------------------------------------------------------------------------------------------
</details>
    
<details>
  <summary><b>INSTALL WITH MAVEN</b> (click to expand/shrink)</summary>

--------------------------------------------------------------------------------------------------------------
Step 1) Include the below code in your pom.xml "repositories" section.
```xml
    <repositories>
    	<repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
	 </repository>
    </repositories>
```

Step 2) Include the below code in your pom.xml as a "dependency" and replace "LATEST" with the latest version that you see here: [![](https://jitpack.io/v/CyberedCake/CyberAPI.svg?label=Latest+Version)](https://jitpack.io/p/CyberedCake/CyberAPI) <br>
Step 2) <b>NOTE: You must keep the "v" at the beginning of the version number!</b>
```xml
    <dependencies>
    	<dependency>
            <groupId>com.github.CyberedCake.CyberAPI</groupId>
            <artifactId>spigot</artifactId>
            <version>vLATEST</version>
	 </dependency>
    </dependencies>
```

Step 3) Reload your maven project and follow the usage instructions below.

--------------------------------------------------------------------------------------------------------------
</details>

## How to use - Spigot
To use CyberAPI, write this in your main onEnable method:

```java
import net.cybercake.cyberapi.spigot.CyberAPI;
import net.cybercake.cyberapi.common.builders.settings.Settings;

public class MainClass extends CyberAPI { // you must extend CyberAPI instead of JavaPlugin

    @Override
    public void onEnable() {
        startCyberAPI( // this method will start CyberAPI and is **required** to be the first thing in your onEnable() method
                Settings.builder()
                        // put your settings here, usually in the form of .<setting>(<value>)
                        
                        .commandsPath("<your groupID>.commands")
                        // usually it's a good idea to define your commands package so that CyberAPI doesn't go searching through
                        // everything if you are using the 'better commands' feature
                        
                        .build() // build once you have changed the settings you want
        );
        
        // now you have access to everything CyberAPI!
        // view the docs here: https://docs.spigot.cybercake.net/
    }

}
```

</details>

<details>
  <summary><b>INSTALL FOR BUNGEECORD</b> (click to expand/shrink)</summary>

## Installation - Bungeecord
### (It is recommended that you use [Waterfall](https://papermc.io/downloads#Waterfall) instead of Bungeecord, but Bungeecord is still supported and Waterfall works on the 'bungee' portion of the library!)

<details>
  <summary><b>INSTALL WITH GRADLE [RECOMMENDED]</b> (click to expand/shrink)</summary>

--------------------------------------------------------------------------------------------------------------
Step 1) Include the below code in your build.gradle "repositories" section.
```gradle
	repositories {
		maven { url 'https://jitpack.io' }
	}
```

Step 2) Include the below code in your build.gradle "dependencies" and replace "LATEST" with the latest version that you see here: [![](https://jitpack.io/v/CyberedCake/CyberAPI.svg?label=Latest+Version)](https://jitpack.io/p/CyberedCake/CyberAPI) <br>
Step 2) <b>NOTE: You must keep the "v" at the beginning of the version number!</b>
```gradle
	dependencies {
	        implementation 'com.github.CyberedCake.CyberAPI:bungee:vLATEST'
	}
```

Step 3) Reload your gradle project and follow the usage instructions below.

--------------------------------------------------------------------------------------------------------------
</details>

<details>
  <summary><b>INSTALL WITH MAVEN</b> (click to expand/shrink)</summary>

--------------------------------------------------------------------------------------------------------------
Step 1) Include the below code in your pom.xml "repositories" section.
```xml
    <repositories>
    	<repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
	 </repository>
    </repositories>
```

Step 2) Include the below code in your pom.xml as a "dependency" and replace "LATEST" with the latest version that you see here: [![](https://jitpack.io/v/CyberedCake/CyberAPI.svg?label=Latest+Version)](https://jitpack.io/p/CyberedCake/CyberAPI) <br>
Step 2) <b>NOTE: You must keep the "v" at the beginning of the version number!</b>
```xml
    <dependencies>
    	<dependency>
            <groupId>com.github.CyberedCake.CyberAPI</groupId>
            <artifactId>bungee</artifactId>
            <version>vLATEST</version>
	 </dependency>
    </dependencies>
```

Step 3) Reload your maven project and follow the usage instructions below.

--------------------------------------------------------------------------------------------------------------
</details>

## How to use - Bungeecord
To use CyberAPI, write this in your main onEnable method:

```java
import net.cybercake.cyberapi.bungee.CyberAPI;
import net.cybercake.cyberapi.common.builders.settings.Settings;

public class MainClass extends CyberAPI { // you must extend CyberAPI instead of Plugin

    @Override
    public void onEnable() {
        startCyberAPI( // this method will start CyberAPI and is **required** to be the first thing in your onEnable() method
                Settings.builder()
                        // put your settings here, usually in the form of .<setting>(<value>)

                        .commandsPath("<your groupID>.commands")
                        // usually it's a good idea to define your commands package so that CyberAPI doesn't go searching through
                        // everything if you are using the 'better commands' feature

                        .build() // build once you have changed the settings you want
        );

        // now you have access to everything CyberAPI!
        // view the docs here: https://docs.bungee.cybercake.net/
    }

}
```

</details>
