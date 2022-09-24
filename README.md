<h1><p align="center">CyberAPI</p></h1>

<p align="center">
<a href="https://jitpack.io/p/CyberedCake/CyberAPI"><img src="https://jitpack.io/v/CyberedCake/CyberAPI.svg?label=Latest+Build" alt="JitPack Build Status"></a>
<a href="https://github.com/CyberedCake/CyberAPI/releases/latest"><img src="https://github.com/CyberedCake/CyberAPI/actions/workflows/actions.yml/badge.svg" alt="GitHub Actions Build Status"></a>
</p>
The new and improved CyberAPI, version 3, is here! This is a library used for most of [CyberedCake's](https://github.com/CyberedCake) plugins and can be used by you for free! It adds additional features that Spigot and Bungeecord do not have and makes existing stuff easier.
<br> <br>
You can view the to-do list and future plans [by clicking here](https://github.com/CyberedCake/CyberAPI/projects/1)

<h1><p align="center">How to install and use</p></h1>

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

Step 2) Include the below code in your build.gradle "dependencies" and replace "LATEST BUILD" with the latest build that you see here: [![](https://jitpack.io/v/CyberedCake/CyberAPI.svg?label=Latest+Build)](https://jitpack.io/p/CyberedCake/CyberAPI) <br>
```gradle
	dependencies {
	        implementation 'com.github.CyberedCake.CyberAPI:spigot:LATEST BUILD'
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

Step 2) Include the below code in your build.gradle "dependencies" and replace "LATEST BUILD" with the latest build that you see here: [![](https://jitpack.io/v/CyberedCake/CyberAPI.svg?label=Latest+Build)](https://jitpack.io/p/CyberedCake/CyberAPI) <br>
```xml
    <dependencies>
    	<dependency>
            <groupId>com.github.CyberedCake.CyberAPI</groupId>
            <artifactId>spigot</artifactId>
            <version>LATEST BUILD</version>
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

Step 2) Include the below code in your build.gradle "dependencies" and replace "LATEST BUILD" with the latest build that you see here: [![](https://jitpack.io/v/CyberedCake/CyberAPI.svg?label=Latest+Build)](https://jitpack.io/p/CyberedCake/CyberAPI) <br>
```gradle
	dependencies {
	        implementation 'com.github.CyberedCake.CyberAPI:bungee:LATEST BUILD'
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

Step 2) Include the below code in your build.gradle "dependencies" and replace "LATEST BUILD" with the latest build that you see here: [![](https://jitpack.io/v/CyberedCake/CyberAPI.svg?label=Latest+Build)](https://jitpack.io/p/CyberedCake/CyberAPI) <br>
```xml
    <dependencies>
    	<dependency>
            <groupId>com.github.CyberedCake.CyberAPI</groupId>
            <artifactId>bungee</artifactId>
            <version>LATEST BUILD</version>
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
