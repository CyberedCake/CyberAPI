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
    
<details>
  <summary><b>INSTALL WITH GRADLE [RECOMMENDED]</b> (click to expand/shrink)</summary>

Step 1) Include the below code in your build.gradle "repositories" section.
```gradle
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
```

Step 2) Include the below code in your build.gradle "dependencies" and replace "VERSION" with the latest version that you see here: [![](https://jitpack.io/v/CyberedCake/CyberAPI.svg)](https://jitpack.io/#CyberedCake/CyberAPI)
```gradle
	dependencies {
	        ...
	        implementation 'com.github.CyberedCake.CyberAPI:spigot:VERSION'
	}
```

Step 3) Reload your gradle project and follow the usage instructions below.
</details>
    
<details>
  <summary><b>INSTALL WITH MAVEN</b> (click to expand/shrink)</summary>

Step 1) Include the below code in your pom.xml "repositories" section.
```maven
	<repositories>
	    ...
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```

Step 2) Include the below code in your pom.xml as a "dependency" and replace "VERSION" with the latest version that you see here: [![](https://jitpack.io/v/CyberedCake/CyberAPI.svg)](https://jitpack.io/#CyberedCake/CyberAPI)
```maven
    <dependencies>
    	<dependency>
            <groupId>com.github.CyberedCake.CyberAPI</groupId>
            <artifactId>spigot</artifactId>
            <version>VERSION</version>
	    </dependency>
    </dependencies>
```

Step 3) Reload your maven project and follow the usage instructions below.
</details>

## How to use - Spigot
To use CyberAPI, write this in your main onEnable method:

```java
import net.cybercake.cyberapi.CyberAPI;
import net.cybercake.cyberapi.settings.Settings;

public class MainClass extends CyberAPI { // you must extend CyberAPI instead of JavaPlugin

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
