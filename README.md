# RuneCraftory 
[![](http://cf.way2muchnoise.eu/full_292745_Forge_%20.svg)![](http://cf.way2muchnoise.eu/versions/292745.svg)](https://www.curseforge.com/minecraft/mc-mods/runecraftory)  
[![](http://cf.way2muchnoise.eu/full_616229_Fabric_%20.svg)![](http://cf.way2muchnoise.eu/versions/616229.svg)](https://www.curseforge.com/minecraft/mc-mods/runecraftory-fabric)  
[![](https://img.shields.io/modrinth/dt/m7U2nGaM?logo=modrinth&label=Modrinth)![](https://img.shields.io/modrinth/game-versions/m7U2nGaM?logo=modrinth&label=Latest%20for)](https://modrinth.com/mod/runecraftory)  
[![Discord](https://img.shields.io/discord/790631506313478155?color=0a48c4&label=discord)](https://discord.gg/8Cx26tfWNs)

Minecraft mod based on the Rune Factory series.

To use this mod as a dependency add the following snippet to your build.gradle:  
```groovy
repositories {
    maven {
        name = "Flemmli97"
        url "https://gitlab.com/api/v4/projects/21830712/packages/maven"
    }
}

dependencies {    
    //Fabric/Loom==========    
    modImplementation("io.github.flemmli97:runecraftory:${minecraft_version}-${mod_version}-${mod_loader}")
    
    //Forge==========    
    compile fg.deobf("io.github.flemmli97:runecraftory:${minecraft_version}-${mod_version}-${mod_loader}")
}
```