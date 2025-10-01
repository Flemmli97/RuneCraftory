# RuneCraftory 
[![](http://cf.way2muchnoise.eu/full_292745_CurseForge_%20.svg)![](http://cf.way2muchnoise.eu/versions/292745.svg)](https://www.curseforge.com/minecraft/mc-mods/runecraftory)  
[![](https://img.shields.io/modrinth/dt/m7U2nGaM?logo=modrinth&label=Modrinth)![](https://img.shields.io/modrinth/game-versions/m7U2nGaM?logo=modrinth&label=Latest%20for)](https://modrinth.com/mod/runecraftory)  
[![Discord](https://img.shields.io/discord/790631506313478155?color=0a48c4&label=discord)](https://discord.gg/8Cx26tfWNs)

Minecraft mod based on the Rune Factory series.

To use this mod as a dependency add the following snippet to your build.gradle:  
```groovy
repositories {
    maven {
        name = "Flemmli97"
        url "https://maven.blazing-coop.net/releases"
    }
}

dependencies {    
    //Fabric/Loom==========    
    modImplementation("io.github.flemmli97:runecraftory:${minecraft_version}-${mod_version}-${mod_loader}")
    
    //NeoForge==========    
    implementation("io.github.flemmli97:runecraftory:${minecraft_version}-${mod_version}-${mod_loader}")
}
```