# Hammer Core
Hammer Core is a powerful API that provides a lot of utility code that a normal mod maker usually types.
It also provides extra utility code that has a wide usage

# How to add to your development environment
Include following code into your build.gradle:

>repositories {
>       maven {
>               name = "CurseForge"
>               url = "https://minecraft.curseforge.com/api/maven/"
>       }
>}
>dependencies {
>       deobfCompile "hammer-lib:HammerCore-MCVERSION-HCVERSION:deobf"
>}

Where "MCVERSION" is Minecraft Version and "HCVERSION" is Hammer Core Version starting from 1.9.2.