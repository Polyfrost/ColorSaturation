package org.polyfrost.colorsaturation;

import cc.polyfrost.oneconfig.events.EventManager;
import org.polyfrost.colorsaturation.command.SaturationCommand;
import org.polyfrost.colorsaturation.config.SaturationConfig;
import cc.polyfrost.oneconfig.events.event.InitializationEvent;
import net.minecraftforge.fml.common.Mod;
import cc.polyfrost.oneconfig.utils.commands.CommandManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

/**
 * The entrypoint of the Example Mod that initializes it.
 *
 * @see Mod
 * @see InitializationEvent
 */
@Mod(modid = ColorSaturation.MODID, name = ColorSaturation.NAME, version = ColorSaturation.VERSION)
public class ColorSaturation {

    // Sets the variables from `gradle.properties`. See the `blossom` config in `build.gradle.kts`.
    public static final String MODID = "@ID@";
    public static final String NAME = "@NAME@";
    public static final String VERSION = "@VER@";
    public static SaturationConfig config;

    // Register the config and commands.
    @Mod.EventHandler
    public void onInit(FMLInitializationEvent event) {
        config = new SaturationConfig();
        CommandManager.INSTANCE.registerCommand(new SaturationCommand());
        EventManager.INSTANCE.register(new Saturation());
    }
}
