# ColorSaturation

[![Modrinth downloads](https://img.shields.io/modrinth/dt/fLNnSWgM?style=flat&logo=modrinth&label=Modrinth&color=1bd96a)](https://modrinth.com/mod/colorsaturation)
[![CurseForge downloads](https://img.shields.io/curseforge/dt/1148008?style=flat&logo=curseforge&label=CurseForge&color=f16436)](https://www.curseforge.com/minecraft/mc-mods/colorsaturation)

ColorSaturation recolors the world with a shader. Four sliders set the saturation, contrast, brightness and hue.

The shader is off after you install the mod. Turn on "Enabled" in OneConfig to see it.

## Features

- Set the saturation strength between -1 and 5. 1 leaves the colors unchanged and 0 is grayscale. Values below 0 swap each color for its opposite.
- Set the contrast between 0 and 2. 1 leaves it unchanged.
- Set the brightness between 0 and 2. 1 leaves it unchanged.
- Shift the hue by up to 180 degrees in either direction.
- The shader covers the world and your hand. The HUD and menus keep their normal colors.

![The same landscape before and after raising the saturation](images/before-and-after.png)

## Supported versions

- Minecraft 1.8.9 on Ornithe.
- Minecraft 1.21.1, 1.21.4, 1.21.5, 1.21.8, 1.21.10, 1.21.11, 26.1, 26.2 and 26.3 on Fabric.

ColorSaturation needs [OneConfig](https://modrinth.com/mod/oneconfig) and Fabric Language Kotlin.

## Support

- Report bugs and ask questions on the [Polyfrost Discord](https://polyfrost.org/discord).
- Check [Polyfrost Status](https://status.polyfrost.org/) if a Polyfrost service is down.

## Building

```sh
./gradlew ":26.3:build"
```

Replace `26.3` with any supported Minecraft version. The build needs JDK 21 and JDK 25 installed.
