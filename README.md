# AutomataCraft

![logo](https://github.com/beothorn/automataCraft/blob/master/docs/logo.png)

Create one or more 3x3x3 block pattern inside the game itself to be replaced by another 3x3x3 pattern.

[Watch the videos to check how to use it](https://youtu.be/LbdNy-vU9z4) .

## Requirements
- JDK 21
- Gradle Wrapper (included)
- Minecraft 1.21.10 client or dedicated server
- [NeoForge 21.10.55-beta](https://neoforged.net/) installed in your Minecraft profile

## Building from source
1. Clone this repository.
2. Run `./gradlew --version` once to verify Java is detected.
3. Run `./gradlew clean build` to compile and produce `build/libs/automata-1.21.10-9.0.0.0.jar`.

## Installing the mod
1. Install NeoForge 21.10.55-beta for Minecraft 1.21.10.
2. Copy the built JAR from `build/libs` into your Minecraft `mods` folder (client or server).
3. Launch Minecraft/NeoForge and verify the AutomataCraft mod is listed.

## Debugging with IntelliJ IDEA
1. Open the project folder in IntelliJ and allow it to import the Gradle build.
2. Run `./gradlew genIntellijRuns` if IntelliJ does not automatically create run configurations.
3. Use the generated `runClient` or `runServer` configuration to start a debug session. Place breakpoints as needed.
4. To regenerate data assets, run the `runData` configuration or execute `./gradlew runData` from the terminal.

## Additional information
- Building and running uses the included Gradle wrapper; you do not need a global Gradle installation.
- If you update dependencies, delete the `.gradle` cache folder to avoid conflicts between old Forge artifacts and NeoForge.
