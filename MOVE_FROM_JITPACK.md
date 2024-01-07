# CyberAPI
This project has recently upgraded from a Jitpack repository to a Nexus repo. This change is still an experiment, but it is recommended that all users switch over to the nexus repo immediately. There will likely be a time in the near future where the Jitpack repository will stop working and thus any projects using CyberAPI under Jitpack may result in numerous errors.

# How to Change
The change is incredibly simple for end-developers. 
<br> <br> <strong>PLEASE NOTE: </strong> There is a chance that this URL will change in the next few days.

(1) Open the file where you implement CyberAPI, this is commonly `build.gradle` for Gradle-related projects and `pom.xml` for Maven-related projects.

(2) You will change one line in your Gradle or Maven build files.

<h3>Gradle</h3>
Change your Gradle `build.gradle` from whatever it currently is to:

```gradle
    repositories {
		    maven { url 'https://repo.prorickey.xyz/repository/maven-public/' } // may change to repo.cybercake.net in the future
	  }
```

<h3>Maven</h3>
Change your Maven `pom.xml` from whatever it currently is to:

```xml
    <repository>
        <url>https://repo.prorickey.xyz/repository/maven-public/</url> // may change to repo.cybercake.net in the future
    </repository>
```

(3) Reload your gradle! And now, you're done.
